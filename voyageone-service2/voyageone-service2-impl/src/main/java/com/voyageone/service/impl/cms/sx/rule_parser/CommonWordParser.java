package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.CommonWord;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapping type是Common类型的设值,与MASTER类型比，除了取值地方不同，逻辑完全一致
 *
 * @author morse.lu 2016/06/27
 * @since 2.1.0
 */
public class CommonWordParser extends VOAbsLoggable {

    private CmsBtProductModel cmsBtProductModel;

    private List<Map<String, Object>> evaluationContextStack;

    public CommonWordParser(CmsBtProductModel cmsBtProductModel) {
        evaluationContextStack = new ArrayList<>();
        this.cmsBtProductModel = cmsBtProductModel;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.COMMON.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            CommonWord commonWord = (CommonWord) ruleWord;
            String propName = commonWord.getValue();
            Map<String, String> extra = commonWord.getExtra();
            Object plainPropValueObj = null;
            if (evaluationContextStack.isEmpty()) {
                plainPropValueObj = getPropValue(cmsBtProductModel.getCommon().getFields(), propName);
            } else {
                for (int i = evaluationContextStack.size(); i>0; i--) {
                    Map<String, Object> evaluationContext = evaluationContextStack.get(i-1);
                    plainPropValueObj = getPropValue(evaluationContext, propName);
                    if (plainPropValueObj != null) {
                        break;
                    }
                }
                //如果evaluationContext存在，但其中的某属性为空，那么从全局取
                if (plainPropValueObj == null) {
                    plainPropValueObj = getPropValue(cmsBtProductModel.getCommon().getFields(), propName);
                }
            }

            if (plainPropValueObj == null) {
                return null;
            }
            if (extra == null || extra.size() == 0) {
                if (plainPropValueObj instanceof List) {
                    if (((List) plainPropValueObj).isEmpty()) {
                        // 检查一下, 如果没有值的话, 后面的也不用做了
                        return null;
                    }
                    return ExpressionParser.encodeStringArray((List<String>) plainPropValueObj); // 用"~~"分隔
                } else {
                    return String.valueOf(plainPropValueObj);
                }
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof List) {
                    // 20160120 tom ims1->ims2升级导致的问题, 增加检查 START
                    if (((List) plainPropValueObj).isEmpty()) {
                        // 检查一下, 如果没有值的话, 后面的也不用做了
                        return null;
                    }
                    // 20160120 tom ims1->ims2升级导致的问题, 增加检查 END
                    List<String> plainPropValues = (List<String>) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return ExpressionParser.encodeStringArray(mappedPropValues);
                } else {
                    $error("Master value must be String or String[]");
                    return null;
                }
            }
        }
    }

    private Object getPropValue(Map<String, Object> evaluationContext, String propName) {
        char separator = '.';
        if (evaluationContext == null) {
            return null;
        }
        int separatorPos = propName.indexOf(separator);
        if (separatorPos == -1) {
            return evaluationContext.get(propName);
        }
        String firstPropName = propName.substring(0, separatorPos);
        String leftPropName = propName.substring(separatorPos + 1);
        return getPropValue((Map<String, Object>) evaluationContext.get(firstPropName), leftPropName);
    }

    public Map<String, Object> popEvaluationContext() {
        Map<String, Object> evaluationContext = evaluationContextStack.get(evaluationContextStack.size()-1);
        evaluationContextStack.remove(evaluationContext);
        return evaluationContext;
    }

    public void pushEvaluationContext(Map<String, Object> evaluationContext) {
        evaluationContextStack.add(evaluationContext);
    }

    public CmsBtProductModel getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }
}
