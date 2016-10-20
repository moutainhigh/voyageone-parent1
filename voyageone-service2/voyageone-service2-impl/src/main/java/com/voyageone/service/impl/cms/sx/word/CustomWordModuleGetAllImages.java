package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetAllImages extends CustomWordModule {

    public final static String moduleName = "GetAllImages";

    public CustomWordModuleGetAllImages() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetAllImages customModuleUserParamGetAllImages = ((CustomWordValueGetAllImages) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamGetAllImages.getImageTemplate();
        RuleExpression htmlTemplateExpression = customModuleUserParamGetAllImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetAllImages.getImageType();
        RuleExpression useOriUrlExpression = customModuleUserParamGetAllImages.getUseOriUrl();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, shopBean, user, extParameter);

        // added by morse.lu 2016/07/18 start
        RuleExpression useCmsBtImageTemplateExpression = customModuleUserParamGetAllImages.getUseCmsBtImageTemplate();
        String useCmsBtImageTemplate = expressionParser.parse(useCmsBtImageTemplateExpression, shopBean, user, extParameter);
        // added by morse.lu 2016/07/18 end

        //system param
        List<CmsBtProductModel> sxProducts = sxData.getProductList();

        String parseResult = "";
        Set<String> imageSet = new HashSet<>();

        for (CmsBtProductModel sxProduct : sxProducts) {
            // modified by morse.lu 2016/06/02 start
//            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProduct.getFields().getImages(imageType);
            // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductService.getProductImages(sxProduct, imageType);
            // modified by morse.lu 2016/06/02 end

            // added by morse.lu 2016/06/13 start
            int index = 0;
            // added by morse.lu 2016/06/13 end
            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                // added by morse.lu 2016/06/13 start
                // target店，第一张图不要，从第二张开始到第六张
                index++;
                if (sxData.getChannelId().equals(ChannelConfigEnums.Channel.TARGET.getId())
                        && CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE == imageType
                        && (index < 2 || index > 6)) {
                    continue;
                }
                // added by morse.lu 2016/06/13 end
                // 20160512 tom 有可能为空 add START
                if (StringUtils.isEmpty(cmsBtProductModelFieldImage.getName())) {
                    continue;
                }
                // 20160512 tom 有可能为空 add END
                // 20160513 tom 图片服务器切换 START
//                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
                String completeImageUrl;
                // added by morse.lu 2016/07/18 start
                if (Boolean.parseBoolean(useCmsBtImageTemplate)) {
                    // 用图片管理模板
                    // added by morse.lu 2016/09/19 start
                    RuleExpression viewTypeExpression = customModuleUserParamGetAllImages.getViewType();
                    String viewType = expressionParser.parse(viewTypeExpression, shopBean, user, extParameter);
                    if (StringUtils.isEmpty(viewType)) {
                        viewType = "1"; // 默认 PC端
                    }
                    // added by morse.lu 2016/09/19 end
                    completeImageUrl = sxProductService.getImageTemplate(
                                                    sxData.getChannelId(),
//                                                    sxData.getMainProduct().getOrgChannelId(),
                                                    sxData.getCartId(),
                                                    3, // 3：详情细节模版
//                                                    1, // PC端
                                                    Integer.valueOf(viewType),
                                                    sxData.getMainProduct().getCommon().getFields().getBrand(),
                                                    sxData.getMainProduct().getCommon().getFields().getProductType(),
                                                    sxData.getMainProduct().getCommon().getFields().getSizeType(),
                                                    cmsBtProductModelFieldImage.getName());
                    if (StringUtils.isEmpty(completeImageUrl)) {
                        $warn("商品详情图url未在图片管理模板表里设定!" +
                                ",BrandName= " + sxData.getMainProduct().getCommon().getFields().getBrand() +
                                ",ProductType= " + sxData.getMainProduct().getCommon().getFields().getProductType() +
                                ",SizeType=" + sxData.getMainProduct().getCommon().getFields().getSizeType());
                        return "";
                    }
                } else
                // added by morse.lu 2016/07/18 end
                if ("1".equals(useOriUrlStr)) {
                    // 使用原图
                    // start
                    try {
                        // 20160717 tom 换一种方法 START
//                        String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", cmsBtProductModelFieldImage.getName());
//                        $info("[CustomWordModuleGetAllImages]取得图片大小url:" + url);
//                        String result = HttpUtils.get(url, null);
//                        result = result.substring(result.indexOf("image"));
//                        String[] args = result.split("image\\.");
//                        Map<String, String> responseMap = new HashMap<>();
//                        for (String param : args) {
//                            if (param.indexOf("=") > 0) {
//                                String[] keyVal = param.split("=");
//                                if (keyVal.length > 1) {
//                                    responseMap.put(keyVal[0], keyVal[1]);
//                                } else {
//                                    responseMap.put(keyVal[0], "");
//                                }
//                            }
//                        }
//                        completeImageUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", cmsBtProductModelFieldImage.getName(), responseMap.get("width"), responseMap.get("height"));
                        completeImageUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&qlt=100", cmsBtProductModelFieldImage.getName());
                        // 20160717 tom 换一种方法 END
                        $info("[CustomWordModuleGetAllImages]取得原始图片url:" + completeImageUrl);
                    } catch (Exception e) {
                        throw new BusinessException("[CustomWordModuleGetAllImages]取得原始图片url失败!");
                    }
                    // end
                } else {
//                    completeImageUrl = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, cmsBtProductModelFieldImage.getName());
                    completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
                }
                // 20160513 tom 图片服务器切换 END
//                completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, completeImageUrl);
                }
                else {
                    parseResult += completeImageUrl;
                }
                imageSet.add(completeImageUrl);
            }
        }

        Map<String, String> map = null;
        if (!imageSet.isEmpty() && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        } else if (!imageSet.isEmpty() && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!StringUtils.isEmpty(entry.getValue())) {
                    parseResult = parseResult.replace(entry.getKey(), entry.getValue());
                } else { // add by desmond 2016/07/13 start
                    // 如果未能取得平台url(源图片去的失败或者上传到平台失败)时，删除原图片url
                    if (htmlTemplate != null) {
                        parseResult = parseResult.replace(String.format(htmlTemplate, entry.getKey()), "");
                    } else {
                        parseResult = parseResult.replace(entry.getKey(), "");
                    }
                }
                // add by desmond 2016/07/13 end
            }
        }

        return parseResult;
    }
}
