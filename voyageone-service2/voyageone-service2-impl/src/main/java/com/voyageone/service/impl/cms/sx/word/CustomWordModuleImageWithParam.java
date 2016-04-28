package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleImageWithParam extends CustomWordModule {

    @Autowired
    private SxProductService sxProductService;

    public final static String moduleName = "ImageWithParam";

    public CustomWordModuleImageWithParam() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamImageWithParam customModuleUserParamImageWithParam= ((CustomWordValueImageWithParam) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamImageWithParam.getImageTemplate();
        List<RuleExpression> imageParamExpressions = customModuleUserParamImageWithParam.getImageParams();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        List<String> imageParams = new ArrayList<>();
        for (RuleExpression imageParamExpression : imageParamExpressions) {
            String imageParam = expressionParser.parse(imageParamExpression, shopBean, user, extParameter);
            if (imageParam == null) {
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

        String parseResult = String.format(imageTemplate, imageParams.toArray());
//        parseResult = sxProductService.encodeImageUrl(parseResult);
        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            Set<String> url = new HashSet<>();
            url.add(parseResult);
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
        }
//        if (imageSet != null) {
//            imageSet.add(parseResult);
//        }

        return parseResult;
    }
}