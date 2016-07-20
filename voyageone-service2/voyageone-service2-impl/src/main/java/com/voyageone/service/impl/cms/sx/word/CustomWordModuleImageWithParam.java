package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleImageWithParam extends CustomWordModule {

    public final static String moduleName = "ImageWithParam";

    public CustomWordModuleImageWithParam() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamImageWithParam customModuleUserParamImageWithParam= ((CustomWordValueImageWithParam) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamImageWithParam.getImageTemplate();
        List<RuleExpression> imageParamExpressions = customModuleUserParamImageWithParam.getImageParams();
        // added by morse.lu 2016/07/13 start
        RuleExpression useCmsBtImageTemplateExpression = customModuleUserParamImageWithParam.getUseCmsBtImageTemplate();
        String useCmsBtImageTemplate = expressionParser.parse(useCmsBtImageTemplateExpression, shopBean, user, extParameter);
        // added by morse.lu 2016/07/13 end

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        List<String> imageParams = new ArrayList<>();
        for (RuleExpression imageParamExpression : imageParamExpressions) {
            String imageParam = expressionParser.parse(imageParamExpression, shopBean, user, extParameter);
            if (imageParam == null) {
                // added by morse.lu 2016/06/07 start
                // bug吧，应该要加进list，不然参数总数不对了
                imageParams.add(imageParam);
                // added by morse.lu 2016/06/07 end
                continue;
            }
            try {
                imageParam = URLEncoder.encode(imageParam, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.fillInStackTrace();
                $error(e.getMessage(), e);
            }
            imageParams.add(imageParam);
        }

        int deleteItemsCount =0;
        for(Iterator<String> iterator=imageParams.iterator();iterator.hasNext();){
            String param = iterator.next();
            if(param == null || "".equals(param.trim())){

                iterator.remove();

                deleteItemsCount++;
            }

        }

        for (int i=0;i<deleteItemsCount;i++){
            imageParams.add("");
        }

        // 20160513 tom 图片服务器切换 START
        // modified by morse.lu 2016/07/13 start
//        String parseResult = String.format(imageTemplate, imageParams.toArray());
        String parseResult;
        if (Boolean.parseBoolean(useCmsBtImageTemplate)) {
            // 用图片管理模板
            parseResult = sxProductService.getImageTemplate(sxData.getChannelId(),
                                                            sxData.getCartId(),
                                                            4, // 4：参数模版
                                                            1, // PC端
                                                            sxData.getMainProduct().getCommon().getFields().getBrand(),
                                                            sxData.getMainProduct().getCommon().getFields().getProductType(),
                                                            sxData.getMainProduct().getCommon().getFields().getSizeType(),
                                                            imageParams.toArray(new String[imageParams.size()]));
            if (StringUtils.isEmpty(parseResult)) {
                $warn("参数图url未在图片管理模板表里设定!" +
                        ",BrandName= " + sxData.getMainProduct().getCommon().getFields().getBrand() +
                        ",ProductType= " + sxData.getMainProduct().getCommon().getFields().getProductType() +
                        ",SizeType=" + sxData.getMainProduct().getCommon().getFields().getSizeType());
                return "";
            }
        } else {
            parseResult = String.format(imageTemplate, imageParams.toArray());
        }
        // modified by morse.lu 2016/07/13 end
//        String parseResult = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, imageParams.get(0));
//        String parseResult = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, imageParams.toArray(new String[imageParams.size()]));
        // 20160513 tom 图片服务器切换 END

//        parseResult = sxProductService.encodeImageUrl(parseResult);
        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            Set<String> url = new HashSet<>();
            url.add(parseResult);
            Map<String, String> map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
            if (map != null && map.containsKey(parseResult)) {
                parseResult = map.get(parseResult);
            }
        } else if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
            Set<String> url = new HashSet<>();
            url.add(parseResult);
            Map<String, String> map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
            if (map != null && map.containsKey(parseResult)) {
                parseResult = map.get(parseResult);
            }
        }
//        if (imageSet != null) {
//            imageSet.add(parseResult);
//        }

        return parseResult;
    }
}
