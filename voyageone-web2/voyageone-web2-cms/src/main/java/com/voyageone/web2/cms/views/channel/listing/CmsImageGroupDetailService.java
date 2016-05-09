package com.voyageone.web2.cms.views.channel.listing;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.SFtpUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtImageGroupBean;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.BaseAppService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class CmsImageGroupDetailService extends BaseAppService {

    @Autowired
    private ImageGroupService imageGroupService;

    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init (Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String)param.get("channelId"), (String)param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String)param.get("channelId"), (String)param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String)param.get("channelId"), (String)param.get("lang")));

        String imageGroupId = (String)param.get("imageGroupId");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getImageGroupModel(imageGroupId);

        result.put("imageGroupInfo", imageGroupInfo);
        return result;
    }

    /**
     * 检索图片
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public List<CmsBtImageGroupModel_Image> search(Map<String, Object> param) {
        String imageGroupId = (String)param.get("imageGroupId");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getImageGroupModel(imageGroupId);
        editImageModel(imageGroupInfo.getImage(), (String)param.get("lang"));
        return imageGroupInfo.getImage();
    }

    /**
     * 检索结果转换
     *
     * @param images 图片列表
     * @param lang 语言
     */
    private void editImageModel(List<CmsBtImageGroupModel_Image> images, String lang) {
        if (images != null) {
            for (CmsBtImageGroupModel_Image image : images) {
                if ("cn".equals(lang)) {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("等待上传");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("上传成功");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("上传失败");
                    }
                } else {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("Waiting Upload");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("Upload Success");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("Upload Fail");
                    }
                }
            }
        }
    }

    /**
     * 编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        String imageGroupId = (String)param.get("imageGroupId");
        int cartId = Integer.parseInt((String)param.get("platform"));
        String imageGroupName = (String)param.get("imageGroupName");
        int imageType = Integer.parseInt((String)param.get("imageType"));
        int viewType = Integer.parseInt((String)param.get("viewType"));
        List<String> brandNameList = (List<String>)param.get("brandName");
        List<String> productTypeList = (List<String>)param.get("productType");
        List<String> sizeTypeList = (List<String>)param.get("sizeType");
        imageGroupService.update(imageGroupId, cartId, imageGroupName, imageType, viewType,
                brandNameList, productTypeList, sizeTypeList);
    }

    /**
     * 保存ImageGroup信息
     *
     * @param param 客户端参数
     * @param file 导入文件
     */
    public void saveImage(Map<String, Object> param, MultipartFile file) {

        if (file == null) {
            FtpBean ftpBean = formatFtpBean();
            ftpBean.setUpload_filename("test11.jpg");
            ftpBean.setUpload_path("/size/");
            URL url = null;
            try {
                url = new URL("http://www.sinaimg.cn/dy/slidenews/2_img/2016_18/789_1781785_209090.jpg");
                InputStream inputStream = url.openStream();
                ftpBean.setUpload_input(inputStream);
                //File uploadFile = new File("d:/snusa-detail_20.png");
                //ftpBean.setUpload_input(new FileInputStream(uploadFile));

                SFtpUtil ftpUtil = new SFtpUtil();
                //建立连接
                ChannelSftp ftpClient = ftpUtil.linkFtp(ftpBean);
                boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
                if (!isSuccess) {
                    throw new Exception("upload error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FtpBean ftpBean = formatFtpBean();
            ftpBean.setUpload_filename("test22.jpg");
            ftpBean.setUpload_path("/size/");
            try {
                ftpBean.setUpload_input(file.getInputStream());
                //File uploadFile = new File("d:/snusa-detail_20.png");
                //ftpBean.setUpload_input(new FileInputStream(uploadFile));

                SFtpUtil ftpUtil = new SFtpUtil();
                //建立连接
                ChannelSftp ftpClient = ftpUtil.linkFtp(ftpBean);
                boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
                if (!isSuccess) {
                    throw new Exception("upload error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void delete(Map<String, Object> param) {
        String imageGroupId = (String)param.get("imageGroupId");
        String originUrl = (String)param.get("originUrl");
        imageGroupService.logicDeleteImage(imageGroupId, originUrl);
    }

    private FtpBean formatFtpBean(){

        String url = "image.voyageone.com.cn";
        // ftp连接port
        String port = "22";
        // ftp连接usernmae
        String username = "voyageone-cms-sftp";
        // ftp连接password
        String password = "Li48I-22aBz";

        FtpBean ftpBean = new FtpBean();
        ftpBean.setPort(port);
        ftpBean.setUrl(url);
        ftpBean.setUsername(username);
        ftpBean.setPassword(password);
        ftpBean.setFile_coding("iso-8859-1");
        return ftpBean;
    }

}