package com.voyageone.service.impl.cms.jumei.enumjm;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMProductImportColumn   {
    ProductCode("product_code","cms_bt_jm_product","产品编号",EnumExcelColumnType.ColumnType_String,false),//Acms_bt_jm_product
    AppId("app_id","cms_bt_jm_promotion_product","APP端模块ID"),//APP端模块ID
    PcId("pc_id","cms_bt_jm_promotion_product","PC端模块ID"),//Acms_bt_jm_product
    ForeignLanguageName("foreign_language_name","cms_bt_jm_product","商品英文名称",EnumExcelColumnType.ColumnType_String,false),//B
    ProductNameCn("product_name_cn","cms_bt_jm_product","商品中文名称"),//C
    ProductLongName("product_long_name","cms_bt_jm_product","长标题"),//D
    ProductMediumName("product_medium_name","cms_bt_jm_product","中标题"),//E
    ProductShortName("product_short_name","cms_bt_jm_product","短标题"),//E
    VoCategoryName("vo_category_name","cms_bt_jm_product","主数据类目",EnumExcelColumnType.ColumnType_String,false),//G
    CategoryLv1Name("category_lv1_name","cms_bt_jm_product","聚美一级类目"),//H
    CategoryLv2Name("category_lv2_name","cms_bt_jm_product","聚美一级类目"),//I
    CategoryLv3Name("category_lv3_name","cms_bt_jm_product","聚美一级类目"),//J
    CategoryLv4Name("category_lv4_name","cms_bt_jm_product","聚美一级类目"),//K
    CategoryLv4Id("category_lv4_id","cms_bt_jm_product","聚美四级类目ID"),//L
    VOBrandName("vo_brand_name","cms_bt_jm_product","主数据品牌子",EnumExcelColumnType.ColumnType_String,false),//N
    BrandName("brand_name","cms_bt_jm_product","聚美品牌名称",EnumExcelColumnType.ColumnType_String,false),//N
    BrandId("brand_id","cms_bt_jm_product","聚美品牌"),//O
    ProductType("product_type","cms_bt_jm_product","商品类别",EnumExcelColumnType.ColumnType_String,false),//P
    SizeType("size_type","cms_bt_jm_product","尺码类别",EnumExcelColumnType.ColumnType_String,false),//Q
    ColorEn("color_en","cms_bt_jm_product","英文颜色"),//R
    Attribute("attribute","cms_bt_jm_product","中文颜色"),//S
    Origin("origin","cms_bt_jm_product","英文产地"),//T
    AddressOfProduce("address_of_produce","cms_bt_jm_product","中文产地"),//U
    MaterialEn("material_en","cms_bt_jm_product","英文材质"),//V
    MaterialCn("material_cn","cms_bt_jm_product","使用方法_产品介绍"),//W
    ProductDesEn("product_des_en","cms_bt_jm_product","使用方法_产品介绍"),//X
    ProductDesCn("product_des_cn","cms_bt_jm_product","使用方法_产品介绍"),//Y
    AvailablePeriod("available_period","cms_bt_jm_product","保质期"),//Z
    ApplicableCrowd("applicable_crowd","cms_bt_jm_product","适合人群"),//AA
    SearchMetaTextCustom("search_meta_text_custom","cms_bt_jm_product","自定义搜索词"),//AB
    SpecialNote("special_note","cms_mt_master_info","特别说明 用于聚美上新"),//AC
    Msrp("msrp","cms_bt_jm_product","海外官网价格",EnumExcelColumnType.ColumnType_String,false),//AD                                                           cms_bt_jm_product
    Limit("limit","cms_bt_jm_promotion_product","Deal每人限购"),//jm_bt_promotion_product
    HsCode("hs_code","cms_bt_jm_product","海关报关编号"),
    HsName("hs_name","cms_bt_jm_product","海关报关类目名称"),
    HsUnit("hs_unit","cms_bt_jm_product","海关报关单位");

    private ExcelColumn<Map> excelColumn;

    public ExcelColumn<Map> getExcelColumn() {
        return excelColumn;
    }

    public void setExcelColumn(ExcelColumn<Map> excelColumn) {
        this.excelColumn = excelColumn;
    }

    private EnumJMProductImportColumn(String columnName, String tableName, String text) {
        
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text);
    }
    private EnumJMProductImportColumn(String columnName, String tableName, String text,EnumExcelColumnType columnType,boolean isNull)
    {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
    }

    public  static  List<EnumJMProductImportColumn> getList()
    {
        List<EnumJMProductImportColumn> listEnumJMProductImportColumn = Arrays.asList(EnumJMProductImportColumn.values());
        listEnumJMProductImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumJMProductImportColumn;
    }
    public static  List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMProductImportColumn[] array = EnumJMProductImportColumn.values();
        for (EnumJMProductImportColumn column : array) {
                list.add(column.getExcelColumn());
        }
        return list;
    }
}