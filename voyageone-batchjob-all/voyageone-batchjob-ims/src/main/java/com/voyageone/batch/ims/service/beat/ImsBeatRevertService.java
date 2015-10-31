package com.voyageone.batch.ims.service.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsPicDao;
import com.voyageone.batch.ims.enums.BeatFlg;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.batch.ims.modelbean.ImsPic;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * 价格披露图片还原处理
 * <p>
 * Created by Jonas on 7/30/15
 * modify by sky on 20150814
 */
@Service
public class ImsBeatRevertService extends ImsBeatBaseService {

    private final static String NO_IMAGE_FLG = "NO IMAGE";

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private ImsPicDao imsPicDao;

    public void revert(List<TaskControlBean> taskControlList, BeatPicBean beatPicBean) {

        // 获取目标店铺
        ShopBean shopBean = ShopConfigs.getShop(beatPicBean.getChannel_id(), beatPicBean.getCart_id());

        // 获取店铺专用主图目录
        String cate_tid = getCategory(shopBean, ImsPicCategoryType.Main);

        $info("价格披露还原：获取图片目录 [ %s ] [ %s ]", beatPicBean.getNum_iid(), cate_tid);

        if (StringUtils.isEmpty(cate_tid)) {
            beatPicBean.setComment("价格披露还原：获取图片目录失败");
            return;
        }

        // 获取主图地址
        // 逻辑同 update(上传) 一样, 内部会通过 flg 判断如何生成图片的标识名称
        // 实际主图的下载上传不同于 update. 已处理过的商品主图会存储在相应的数据库中, 可重用.
        Map<Integer, String> tbImageUrlMap = getTbImageUrl(shopBean, beatPicBean, cate_tid, taskControlList);

        if (tbImageUrlMap == null) {
            // 这里之前的方法会记录为何获取不到 url 的原因。所以此处失败不输出 log
            beatPicBean.setBeat_flg(getResultFlg(beatPicBean, false));
            return;
        }

        Boolean flg = updateSchema(shopBean, beatPicBean, tbImageUrlMap);

        if (flg == null) return;

        beatPicBean.setBeat_flg(getResultFlg(beatPicBean, flg));

        $info("价格披露还原：完成 [ %s ] [ %s ]", beatPicBean.getNum_iid(), beatPicBean.getCode());
    }

    private Map<Integer, String> getTbImageUrl(ShopBean shopBean, BeatPicBean beatPicBean, String category_tid, List<TaskControlBean> taskControlList) {
        // 现根据位置获取 CMS 的图片信息
        List<ImsBeatImageInfo> imageInfoList = getTbImageUrl(beatPicBean);

        if (imageInfoList == null) return null;

        Map<Integer, String> tbImageUrlMap = new HashMap<>();

        // 为顺延的图片设定 NO IMAGE
        appendExtended(imageInfoList, beatPicBean);

        for (ImsBeatImageInfo imageInfo: imageInfoList) {

            // 补全信息
            imageInfo.setBeatInfo(beatPicBean);
            imageInfo.setCategoryTid(category_tid);
            imageInfo.setShop(shopBean);

            String tbImageUrl = getTbImageUrl(imageInfo, taskControlList);

            if (StringUtils.isEmpty(tbImageUrl)) return null;
            else if (tbImageUrl.equals(NO_IMAGE_FLG))
                // 转换 FLG 为 null, 后续接口调用内部会继续进一步判断
                tbImageUrlMap.put(imageInfo.getImage_id(), null);
            else
                tbImageUrlMap.put(imageInfo.getImage_id(), tbImageUrl);
        }

        return tbImageUrlMap;
    }

    private void appendExtended(List<ImsBeatImageInfo> imageInfoList, BeatPicBean beatPicBean) {

        String targets = beatPicBean.getTargets();

        ImsBeatImageInfo first = imageInfoList.get(0);

        int size = imageInfoList.size();

        for (String target: targets.split(",")) {

            int index = Integer.valueOf(target);

            // 为顺延产生的多余图片,设定 NO IMAGE
            if (index > size) {
                ImsBeatImageInfo imageInfo = copyInfo(first, index);
                imageInfo.setNoImage(true);
                imageInfoList.add(imageInfo);
            }
        }
    }

    private String getTbImageUrl(ImsBeatImageInfo imageInfo, List<TaskControlBean> taskControlList) {

        // 通过特殊标记处理无图位置
        if (imageInfo.isNoImage()) return NO_IMAGE_FLG;

        String title = imageInfo.getTitle();

        String cate_id = imageInfo.getCategoryTid();

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        ShopBean shopBean = imageInfo.getShop();

        ImsPic pic = imsPicDao.selectByTitle(title, cate_id);

        if (pic != null)
            return pic.getPic_url();

        Picture picture;

        try {
            PictureGetResponse res = tbPictureService.getPictures(shopBean, title, Long.valueOf(cate_id));

            List<Picture> pictures = res.getPictures();

            if (res.isSuccess() && pictures != null && pictures.size() > 0) {

                picture = pictures.get(0);

            } else {

                $info("价格披露还原：没有找到图片，准备从新上传 [ %s ] [ %s ] [ %s ] [ %s ]",
                        res.getSubMsg(), title, cate_id, shopBean.getShop_name());

                picture = upload(imageInfo, taskControlList);
            }

        } catch (ApiException e) {

            String message = format("价格披露还原：调用淘宝图片获取接口异常 [ %s ] [ %s ] [ %s ] [ %s ]",
                    e.getLocalizedMessage(), title, cate_id, shopBean.getShop_name());
            $info(message);
            beatPicBean.setComment(message);

            return null;
        }

        if (picture == null)
            return null;

        pic = new ImsPic();
        pic.setTitle(title);
        pic.setCategory_tid(cate_id);
        pic.setModifier(getTaskName());
        pic.setCreater(getTaskName());
        pic.setPic_tid(String.valueOf(picture.getPictureId()));
        pic.setPic_url(picture.getPicturePath());

        int count = imsPicDao.insert(pic);

        $info("价格披露还原：插入 pic 数据。反应行数 [ %s ]", count);

        return pic.getPic_url();
    }

    private Picture upload(ImsBeatImageInfo imageInfo, List<TaskControlBean> taskControlList) {

        String cate_id = imageInfo.getCategoryTid();

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        ShopBean shopBean = imageInfo.getShop();

        String errorMsg;
        // 获取模板地址
        // 此处先不把 “template_url” 加入到 Name 枚举中。等待后续做主图任务时，顺带修改这里获取模板的方式
        // 所以暂时使用字符串
        TaskControlBean templateUrl = taskControlList.stream()
                .filter(c -> c.getCfg_name().equals("template_url") && c.getCfg_val1().equals(shopBean.getCart_id() + shopBean.getOrder_channel_id()))
                .findFirst()
                .orElse(null);

        // 模版配置为空
        if (templateUrl == null || StringUtils.isEmpty(templateUrl.getCfg_val2())) {
            errorMsg = format("价格披露还原：没有找到主图模板地址，当前 Cart 为：[ %s ] [ %s ]", shopBean.getCart_id(),
                    shopBean.getShop_name());
            $info(errorMsg);
            beatPicBean.setComment(errorMsg);
            return null;
        }

        // 检查模版配置是否正确
        if (!templateUrl.getCfg_val2().contains("{key}")) {
            errorMsg = format("价格披露还原：主图模板地址没有找到关键字“{key}” [ %s ]", templateUrl.getCfg_val2());
            $info(errorMsg);
            beatPicBean.setComment(errorMsg);
            return null;
        }

        String imageName = imageInfo.getImage_name();

        if (StringUtils.isEmpty(imageName)) return null;

        byte[] image;
        String image_url = templateUrl.getCfg_val2().replace("{key}", imageName);

        $info("价格披露还原：尝试下载图片：[ %s ]", image_url);

        // 尝试下载
        try (InputStream inputStream = HttpUtils.getInputStream(image_url, null)) {

            image = IOUtils.toByteArray(inputStream);

            $info("价格披露还原：已下载，长度：[ %s ]", image.length);

        } catch (IOException e) {
            errorMsg = format("价格披露还原：线程内下载图片出现异常。异常信息：%s", e.getMessage());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return null;
        }

        // 尝试上传
        try {
            PictureUploadResponse res = tbPictureService.uploadPicture(shopBean, image, imageInfo.getTitle(),
                    cate_id);

            if (res.getPicture() != null) return res.getPicture();
            errorMsg = "线程内上传图片失败：" + format("[ %s ] [ %s ]", res.getSubCode(), res.getSubMsg());
            beatPicBean.setComment(errorMsg);
            $info("价格披露还原：线程内上传图片失败 [ %s ] [ %s ]", res.getSubCode(), res.getSubMsg());
            return null;

        } catch (ApiException e) {
            errorMsg = format("价格披露还原：线程内上传图片出现异常。异常信息：%s", e.getMessage());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return null;
        }
    }

    private BeatFlg getResultFlg(BeatPicBean beatPicBean, Boolean result) {
        switch (beatPicBean.getBeat_flg()) {
            case Passed:
                return result ? BeatFlg.Revert : BeatFlg.RevertFail;
            case Cancel:
                return result ? BeatFlg.Canceled : BeatFlg.CancelFail;
        }
        return null;
    }

    @Override
    public String getTaskName() {
        return "ImsBeatPicJob-revert";
    }
}
