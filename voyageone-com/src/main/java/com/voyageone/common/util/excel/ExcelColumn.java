package com.voyageone.common.util.excel;

import com.voyageone.common.util.CamelUtil;
import org.springframework.util.StringUtils;

/**
 * Created by admin on 2015/10/9.
 */
public class ExcelColumn<T> {
    String text;
    String tableName;
    String columnName;
    String camelColumnName;
    EnumExcelColumnType columnType;
    boolean isNull = true;
    int orderIndex;
    FunctionFormatter<Object, T, Integer, Object> formatter;
    double columnWidth;
    short colorIndex;//HSSFColor.LIGHT_YELLOW.index  颜色索引值

    public ExcelColumn() {

    }

    public ExcelColumn(String columnName, String tableName, String text) {
        this.columnName = columnName;
        this.camelColumnName = CamelUtil.underlineToCamel(this.columnName);
        this.tableName = tableName;
        this.text = text;
    }

    public ExcelColumn(String columnName, String tableName, String text, boolean isNull) {
        this(columnName, tableName, text);
        this.isNull = isNull;
    }

    public ExcelColumn(String columnName, int orderIndex, String tableName, String text) {
        this.columnName = columnName;
        this.camelColumnName = CamelUtil.underlineToCamel(this.columnName);
        this.orderIndex = orderIndex;
        this.tableName = tableName;
        this.text = text;
    }

    public ExcelColumn(String columnName, int orderIndex, String tableName, String text, boolean isNull) {
        this(columnName, orderIndex, tableName, text);
        this.isNull = isNull;
    }

    public ExcelColumn(String columnName, int orderIndex, String tableName, String text, EnumExcelColumnType columnType, boolean isNull) {
        this(columnName, orderIndex, tableName, text);
        this.columnType = columnType;
        this.isNull = isNull;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCamelColumnName() {
        if (StringUtils.isEmpty(camelColumnName)) {
            return this.getColumnName();
        }
        return camelColumnName;
    }

    public void setCamelColumnName(String camelColumnName) {
        this.camelColumnName = camelColumnName;
    }

    public EnumExcelColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(EnumExcelColumnType columnType) {
        this.columnType = columnType;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public FunctionFormatter<Object, T, Integer, Object> getFormatter() {
        return formatter;
    }

    public void setFormatter(FunctionFormatter<Object, T, Integer, Object> formatter) {
        this.formatter = formatter;
    }

    public double getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }

    public short getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(short colorIndex) {
        this.colorIndex = colorIndex;
    }
}