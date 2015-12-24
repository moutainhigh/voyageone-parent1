package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.util.StringUtil;
import com.voyageone.common.masterdate.schema.util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public class MultiInputField extends com.voyageone.common.masterdate.schema.field.Field {
    protected List<Value> values = new ArrayList<>();

    public MultiInputField() {
        super.type = FieldTypeEnum.MULTIINPUT;
    }

    public void addValue(Value value) {
        if(value != null) {
            this.values.add(value);
        }
    }

    public void addValue(String value) {
        if(value != null) {
            Value v = new Value();
            v.setValue(value);
            this.addValue(v);
        }
    }

    public List<Value> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        if(values != null) {
            this.values.clear();
            for (String value : values) {
                Value v = new Value();
                v.setValue(value);
                this.values.add(v);
            }
        }
    }

    public List<String> getStringValues() {
        List<String> list = new ArrayList<>();

        for (Value v : this.values) {
            list.add(v.getValue());
        }

        return list;
    }

    public void addDefaultValue(String value) {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        MultiInputField defaultField = (MultiInputField)super.defaultValueField;
        defaultField.addValue(value);
    }

    public void setDefaultValues(List<String> values) {
        if(values != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            MultiInputField defaultField = (MultiInputField)super.defaultValueField;
            if(defaultField.getValues() != null && !defaultField.getValues().isEmpty()) {
                defaultField.getValues().clear();
            }

            for (String v : values) {
                defaultField.addValue(v);
            }

        }
    }

    public List<String> getDefaultValues() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        List<String> result = new ArrayList<>();
        MultiInputField defaultField = (MultiInputField)super.defaultValueField;

        for (Value aVList : defaultField.getValues()) {
            result.add(aVList.getValue());
        }

        return result;
    }

    public Element toParamElement() throws TopSchemaException {
        Element fieldNode = XmlUtils.createRootElement("field");
        if(StringUtil.isEmpty(this.id)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, (String)null);
        } else if(this.type != null && !StringUtil.isEmpty(this.type.value())) {
            FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(this.type.value());
            if(fieldEnum == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, this.id);
            } else {
                fieldNode.addAttribute("id", this.id);
                fieldNode.addAttribute("name", this.name);
                fieldNode.addAttribute("type", this.type.value());
                Element valuesNode = XmlUtils.appendElement(fieldNode, "values");

                for (Value value : this.values) {
                    Element valueNode = XmlUtils.appendElement(valuesNode, "value");
                    if (!StringUtil.isEmpty(value.getValue())) {
                        valueNode.setText(value.getValue());
                    }
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        MultiInputField defaultField = (MultiInputField)super.defaultValueField;
        List<Value> defaultValues = defaultField.getValues();
        if(defaultValues != null && !defaultValues.isEmpty()) {
            Element valuesNode = XmlUtils.createRootElement("default-values");

            for (Value defaultValue : defaultValues) {
                Element valueNode = XmlUtils.appendElement(valuesNode, "default-value");
                if (!StringUtil.isEmpty(defaultValue.getValue())) {
                    valueNode.setText(defaultValue.getValue());
                }
            }

            return valuesNode;
        } else {
            return null;
        }
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.MULTIINPUT);
    }

    @Override
    public List<Value> getValue() {
        return this.values;
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueFields) {
        Object valueObj = valueFields.get(id);
        if (valueObj != null && valueObj instanceof List) {
            List<String> values = new ArrayList<>();
            List valuesTmp = (List)valueObj;
            for (Object value : valuesTmp) {
                if (value != null) {
                    values.add(value.toString());
                }
            }
            for (String value : values) {
                addValue(value);
            }
        }
    }
}
