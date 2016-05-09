package com.voyageone.task2.cms.service.platform.common;

import com.google.common.collect.Lists;
import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsMtPlatformDictDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.impl.cms.sx.sku_field.SkuFieldBuilderService;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.TmallGjSkuFieldBuilderImpl1;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.TmallGjSkuFieldBuilderImpl2;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.TmallGjSkuFieldBuilderImpl3;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.ims.ImsBtProductModel;
import com.voyageone.task2.cms.bean.SkuTemplateSchema;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.TmallUploadRunState;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.dao.PlatformSkuInfoDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.model.PlatformSkuInfoModel;
//import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import com.voyageone.task2.cms.service.putaway.SkuFieldBuilderFactory;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_0;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_1;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_2;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.text.DateFormat.*;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by dell on 2016/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SxGetProductInfoTest {

    @Autowired
    private SkuFieldBuilderFactory skuFieldBuilderFactory;

    @Autowired
    private ProductService productService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private SkuFieldBuilderService skuFieldBuilderService;

    @Autowired
    private SxGetProductInfo sxGetProductInfo;

    @Autowired
    private ImsBtProductDao imsBtProductDao;

    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    protected PlatformSkuInfoDao platformSkuInfoDao;

    @Autowired
    private CmsMtPlatformDictDao cmsMtPlatformDictDao;

    @Test
    public void testFuc() throws Exception {
        System.out.println();
        System.out.println("start:"+ DateTimeUtil.getNow());
        System.out.println(getInstance().format(new Date()));

        // sku属性设置 start
        String skuPro;
        {
            skuPro = "<itemRule>\n" +
                    "\t<field id=\"sku\" name=\"SKU\" type=\"multiComplex\">\n" +
                    "\t\t<rules>\n" +
                    "\t\t<rule name=\"1026894095_1\" value=\"SKU与类目销售属性必须匹配\"/>\n" +
                    "\t\t<rule name=\"1830467373_1\" value=\"如果销售属性存在套餐且存在官方标配，官方标配必选\"/>\n" +
                    "\t\t</rules>\n" +
                    "\t\t<fields>\n" +
                    "\t\t\t<field id=\"prop_20509\" name=\"尺码\" type=\"singleCheck\">\n" +
                    "\t\t\t\t<options>\n" +
                    "\t\t\t\t<option displayName=\"S（54-56cm）\" value=\"12430609\"/>\n" +
                    "\t\t\t\t<option displayName=\"M（56-58cm）\" value=\"12430610\"/>\n" +
                    "\t\t\t\t<option displayName=\"L（58-60cm）\" value=\"12430611\"/>\n" +
                    "\t\t\t\t<option displayName=\"XL（60cm以上）\" value=\"12430612\"/>\n" +
                    "\t\t\t\t<option displayName=\"可调节\" value=\"4054751\"/>\n" +
                    "\t\t\t\t</options>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"prop_1627207\" name=\"颜色分类\" type=\"singleCheck\">\n" +
                    "\t\t\t\t<options>\n" +
                    "\t\t\t\t<option displayName=\"军绿色\" value=\"3232483\"/>\n" +
                    "\t\t\t\t<option displayName=\"天蓝色\" value=\"3232484\"/>\n" +
                    "\t\t\t\t<option displayName=\"巧克力色\" value=\"3232481\"/>\n" +
                    "\t\t\t\t<option displayName=\"桔色\" value=\"90554\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅灰色\" value=\"28332\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅绿色\" value=\"30156\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅黄色\" value=\"60092\"/>\n" +
                    "\t\t\t\t<option displayName=\"深卡其布色\" value=\"3232482\"/>\n" +
                    "\t\t\t\t<option displayName=\"深灰色\" value=\"3232478\"/>\n" +
                    "\t\t\t\t<option displayName=\"深紫色\" value=\"3232479\"/>\n" +
                    "\t\t\t\t<option displayName=\"深蓝色\" value=\"28340\"/>\n" +
                    "\t\t\t\t<option displayName=\"白色\" value=\"28320\"/>\n" +
                    "\t\t\t\t<option displayName=\"粉红色\" value=\"3232480\"/>\n" +
                    "\t\t\t\t<option displayName=\"紫罗兰\" value=\"80882\"/>\n" +
                    "\t\t\t\t<option displayName=\"紫色\" value=\"28329\"/>\n" +
                    "\t\t\t\t<option displayName=\"红色\" value=\"28326\"/>\n" +
                    "\t\t\t\t<option displayName=\"绿色\" value=\"28335\"/>\n" +
                    "\t\t\t\t<option displayName=\"花色\" value=\"130164\"/>\n" +
                    "\t\t\t\t<option displayName=\"蓝色\" value=\"28338\"/>\n" +
                    "\t\t\t\t<option displayName=\"褐色\" value=\"132069\"/>\n" +
                    "\t\t\t\t<option displayName=\"透明\" value=\"107121\"/>\n" +
                    "\t\t\t\t<option displayName=\"酒红色\" value=\"28327\"/>\n" +
                    "\t\t\t\t<option displayName=\"黄色\" value=\"28324\"/>\n" +
                    "\t\t\t\t<option displayName=\"黑色\" value=\"28341\"/>\n" +
                    "\t\t\t\t</options>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"sku_price\" name=\"价格\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"decimal\"/>\n" +
                    "\t\t\t\t<rule name=\"minValueRule\" value=\"0.00\" exProperty=\"not include\"/>\n" +
                    "\t\t\t\t<rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\"/>\n" +
                    "\t\t\t\t<rule name=\"1370739466_1\" value=\"SKU价格受类目限制\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"sku_quantity\" name=\"库存\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
                    "\t\t\t\t<rule name=\"minValueRule\" value=\"0\" exProperty=\"include\"/>\n" +
                    "\t\t\t\t<rule name=\"maxValueRule\" value=\"1000000\" exProperty=\"not include\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"sku_outerId\" name=\"商家编码\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
                    "\t\t\t\t<rule name=\"maxLengthRule\" value=\"64\" exProperty=\"include\" unit=\"byte\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"sku_barcode\" name=\"条形码\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
                    "\t\t\t\t<rule name=\"527827168_1\" value=\"条形码必须符合EAN和UPC编码规则\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"hscode\" name=\"HS海关代码\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
                    "\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t</fields>\n" +
                    "\t</field>\n" +
                    "\t<field id=\"prop_extend_20509\" name=\"尺码扩展\" type=\"multiComplex\">\n" +
                    "\t\t<fields>\n" +
                    "\t\t\t<field id=\"prop_20509\" name=\"尺码\" type=\"singleCheck\">\n" +
                    "\t\t\t\t<options>\n" +
                    "\t\t\t\t<option displayName=\"S（54-56cm）\" value=\"12430609\"/>\n" +
                    "\t\t\t\t<option displayName=\"M（56-58cm）\" value=\"12430610\"/>\n" +
                    "\t\t\t\t<option displayName=\"L（58-60cm）\" value=\"12430611\"/>\n" +
                    "\t\t\t\t<option displayName=\"XL（60cm以上）\" value=\"12430612\"/>\n" +
                    "\t\t\t\t<option displayName=\"可调节\" value=\"4054751\"/>\n" +
                    "\t\t\t\t</options>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"alias_name\" name=\"别名\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
                    "\t\t\t\t<rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"byte\"/>\n" +
                    "\t\t\t\t<rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t</fields>\n" +
                    "\t</field>\n" +
                    "\t<field id=\"prop_extend_1627207\" name=\"颜色分类扩展\" type=\"multiComplex\">\n" +
                    "\t\t<fields>\n" +
                    "\t\t\t<field id=\"prop_1627207\" name=\"颜色分类\" type=\"singleCheck\">\n" +
                    "\t\t\t\t<options>\n" +
                    "\t\t\t\t<option displayName=\"军绿色\" value=\"3232483\"/>\n" +
                    "\t\t\t\t<option displayName=\"天蓝色\" value=\"3232484\"/>\n" +
                    "\t\t\t\t<option displayName=\"巧克力色\" value=\"3232481\"/>\n" +
                    "\t\t\t\t<option displayName=\"桔色\" value=\"90554\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅灰色\" value=\"28332\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅绿色\" value=\"30156\"/>\n" +
                    "\t\t\t\t<option displayName=\"浅黄色\" value=\"60092\"/>\n" +
                    "\t\t\t\t<option displayName=\"深卡其布色\" value=\"3232482\"/>\n" +
                    "\t\t\t\t<option displayName=\"深灰色\" value=\"3232478\"/>\n" +
                    "\t\t\t\t<option displayName=\"深紫色\" value=\"3232479\"/>\n" +
                    "\t\t\t\t<option displayName=\"深蓝色\" value=\"28340\"/>\n" +
                    "\t\t\t\t<option displayName=\"白色\" value=\"28320\"/>\n" +
                    "\t\t\t\t<option displayName=\"粉红色\" value=\"3232480\"/>\n" +
                    "\t\t\t\t<option displayName=\"紫罗兰\" value=\"80882\"/>\n" +
                    "\t\t\t\t<option displayName=\"紫色\" value=\"28329\"/>\n" +
                    "\t\t\t\t<option displayName=\"红色\" value=\"28326\"/>\n" +
                    "\t\t\t\t<option displayName=\"绿色\" value=\"28335\"/>\n" +
                    "\t\t\t\t<option displayName=\"花色\" value=\"130164\"/>\n" +
                    "\t\t\t\t<option displayName=\"蓝色\" value=\"28338\"/>\n" +
                    "\t\t\t\t<option displayName=\"褐色\" value=\"132069\"/>\n" +
                    "\t\t\t\t<option displayName=\"透明\" value=\"107121\"/>\n" +
                    "\t\t\t\t<option displayName=\"酒红色\" value=\"28327\"/>\n" +
                    "\t\t\t\t<option displayName=\"黄色\" value=\"28324\"/>\n" +
                    "\t\t\t\t<option displayName=\"黑色\" value=\"28341\"/>\n" +
                    "\t\t\t\t</options>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"alias_name\" name=\"别名\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
                    "\t\t\t\t<rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"byte\"/>\n" +
                    "\t\t\t\t<rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t\t<field id=\"prop_image\" name=\"属性图片\" type=\"input\">\n" +
                    "\t\t\t\t<rules>\n" +
                    "\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
                    "\t\t\t\t</rules>\n" +
                    "\t\t\t</field>\n" +
                    "\t\t</fields>\n" +
                    "\t</field>\n" +
                    "</itemRule>";
        }

//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid003"); // 模板1
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid002"); // 模板2
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid004"); // 模板3

        Map<String, Integer> skuInventoryMap = new HashMap<>();
        skuInventoryMap.put("c001-1", 1100);
        skuInventoryMap.put("c001-2", 1200);
        skuInventoryMap.put("c001-4", 1400);
        skuInventoryMap.put("c004-1", 4100);

        // sku属性设置 new Src start
        List<Field> skuInfoFields;
        {
            List<Field> fields = SchemaReader.readXmlForList(skuPro);
            List<Field> allSkuFields = new ArrayList<>();
            recursiveGetFields(fields, allSkuFields);
            AbstractSkuFieldBuilder skuFieldService = skuFieldBuilderService.getSkuFieldBuilder(23, allSkuFields);

            CmsMtPlatformDictModel cmsMtPlatformDictModel = cmsMtPlatformDictDao.selectOne(new HashMap<String, Object>(){{
                put("orderChannelId", "066");
                put("cartId", 23);
                put("name", "属性图片模板");
            }});
            skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());

            if (skuFieldService == null) {
                System.out.println("null");
            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl1) {
                skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());
                System.out.println("TmallGjSkuFieldBuilderImpl1");
            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl2) {
                System.out.println("TmallGjSkuFieldBuilderImpl2");
            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl3) {
                skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());
                System.out.println("TmallGjSkuFieldBuilderImpl3");
//            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl_3) {
//                System.out.println("TmallGjSkuFieldBuilderImpl_3");
            } else {
                System.out.println("not exists");
            }

            SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
            ExpressionParser exp = new ExpressionParser(sxProductService, sxData);

            ShopBean shopBean = new ShopBean();
//        shopBean.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());
            shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

            skuInfoFields = skuFieldService.buildSkuInfoField(allSkuFields, exp, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, "morse");
        }
        // sku属性设置 new Src end

        // sku属性设置 old Src start
//        List<com.taobao.top.schema.field.Field> skuInfoFields;
//        {
//            List<com.taobao.top.schema.field.Field> fields = com.taobao.top.schema.factory.SchemaReader.readXmlForList(skuPro);
//            List<com.taobao.top.schema.field.Field> allSkuFields = new ArrayList<>();
//            recursiveGetTaobaoFields(fields, allSkuFields);
//            com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder skuFieldBuilder = null;
//            try {
//                skuFieldBuilder = skuFieldBuilderFactory.getSkuFieldBuilder(23, allSkuFields);
//            } catch (TaskSignal taskSignal) {
//                taskSignal.printStackTrace();
//            }
//            if (skuFieldBuilder == null) {
//                System.out.println("null");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_0) {
//                CmsMtPlatformDictModel cmsMtPlatformDictModel = cmsMtPlatformDictDao.selectOne(new HashMap<String, Object>(){{
//                    put("orderChannelId", "066");
//                    put("cartId", 23);
//                    put("name", "属性图片模板");
//                }});
//                skuFieldBuilder.setCodeImageTemplete(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl_0");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_1) {
//                System.out.println("TmallGjSkuFieldBuilderImpl_1");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_2) {
//                System.out.println("TmallGjSkuFieldBuilderImpl_2");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_3) {
//                System.out.println("TmallGjSkuFieldBuilderImpl_3");
//            } else {
//                System.out.println("not exists");
//            }
//
//            List<CmsBtProductModel> cmsBtProductModels = productService.getProductByGroupId("066", 333L, false);
//            List<SxProductBean> sxProductBeans = new ArrayList<>();
//            CmsBtProductModel mainProductModel = null;
//            CmsBtProductGroupModel mainProductPlatform = null;
//            SxProductBean mainSxProduct = null;
//
//            for (CmsBtProductModel cmsBtProductModel : cmsBtProductModels) {
//                CmsBtProductGroupModel productPlatform = cmsBtProductModel.getGroups();
//                String prodCode = cmsBtProductModel.getFields().getCode();
//                // tom 获取feed info的数据 START
//                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode("066", prodCode);
//                // tom 获取feed info的数据 END
//                SxProductBean sxProductBean = new SxProductBean(cmsBtProductModel, productPlatform, feedInfo);
//                if (filtProductsByPlatform(sxProductBean)) {
//                    sxProductBeans.add(sxProductBean);
//                    if (prodCode.equals(productPlatform.getMainProductCode())) {
//                        mainProductModel = cmsBtProductModel;
//                        mainProductPlatform = productPlatform;
//                        mainSxProduct = sxProductBean;
//                    }
//                }
//            }
//
//            Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap = new HashMap<>();
//            for (SxProductBean sxProductBean : sxProductBeans) {
//                for (CmsBtProductModel_Sku sku : sxProductBean.getCmsBtProductModel().getSkus()) {
//                    skuProductMap.put(sku, sxProductBean);
//                }
//            }
//
//            TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = new TmallUploadRunState(null).new TmallContextBuildCustomFields();
//
//            skuFieldBuilder.setExpressionParser(new com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser("066", 23, mainSxProduct, sxProductBeans));
//
//            try {
//                skuInfoFields = skuFieldBuilder.buildSkuInfoField(23, null, allSkuFields, sxProductBeans, skuProductMap, cmsMtPlatformMappingModel, skuInventoryMap, tmallContextBuildCustomFields, new HashSet<>());
//            } catch (TaskSignal taskSignal) {
//                return;
//            }
//        }
        // sku属性设置 old Src end
        // sku属性设置 end

//        List<PlatformSkuInfoModel> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfoByCartId(23);
//        tmallSkuInfos.forEach(model->{
//            System.out.print(model.getSeq());
//            System.out.print("\t");
//            System.out.print(model.getCart_id());
//            System.out.print("\t");
//            System.out.print(model.getProp_id());
//            System.out.print("\t");
//            System.out.print(SkuTemplateSchema.decodeFieldTypes(model.getSku_type()));
//            System.out.print("\t");
//            System.out.print(Long.toBinaryString(model.getSku_type()));
//            System.out.println();
//        });


        // constructMappingPlatformProps start
//        String schema = "<itemRule>\n" +
//                "<field id=\"infos\" name=\"品牌信息\" type=\"input\">\n" +
//                "\t<rules>\n" +
//                "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t</rules>\n" +
//                "</field>\n" +
//                "<field id=\"title\" name=\"牌\" type=\"singleCheck\">\n" +
//                "\t<rules>\n" +
//                "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t</rules>\n" +
//                "</field>\n" +
//                "<field id=\"product_s\" name=\"商品\" type=\"multiComplex\">\n" +
//                "<fields>\n" +
//                "\t<field id=\"code_f\" name=\"Code\" type=\"input\">\n" +
//                "\t</field>\n" +
//                "\t<field id=\"orgin_f\" name=\"产地\" type=\"input\">\n" +
//                "\t</field>\n" +
//                "\t<field id=\"sell_p_s\" name=\"卖点\" type=\"complex\">\n" +
//                "\t\t<fields>\n" +
//                "\t\t\t<field id=\"sell_p_0\" name=\"卖点1\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t\t<field id=\"sell_p_1\" name=\"卖点2\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t\t<field id=\"sell_p_2\" name=\"卖点3\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t</fields>\n" +
//                "\t</field>\n" +
//                "\t<field id=\"col\" name=\"颜色\" type=\"multiCheck\">\n" +
//                "\t\t<rules>\n" +
//                "\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t\t</rules>\n" +
//                "\t\t<options>\n" +
//                "\t\t<option displayName=\"蓝\" value=\"b\"/>\n" +
//                "\t\t<option displayName=\"红\" value=\"r\"/>\n" +
//                "\t\t</options>\n" +
//                "\t</field>\n" +
//                "</fields>\n" +
//                "</field>\n" +
//                "</itemRule>";
//        List<Field> fields = SchemaReader.readXmlForList(schema);
////        Map<String, Field> fieldMap = schemaToIdPropMap(schema);
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid001");
//
//        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
//        ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//
//        ShopBean shopBean = new ShopBean();
////        shopBean.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());
//        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());
//
//        Map<String, Field> res = sxProductService.constructMappingPlatformProps(fields, cmsMtPlatformMappingModel, shopBean, exp, "morse");
//        res.forEach((key, val) -> System.out.println(key + "=" + val.getValue()));
//        String res = sxProductService.resolveDict("无线商品图片-1", exp, shopBean, "morse", null);
//        System.out.println(res);
        // constructMappingPlatformProps end


//        productService.bathUpdateWithSXResult("066",23,Long.valueOf("333"),Arrays.asList("c001", "c002", "c101"),"num001", "pro001","2016-04-25 11:12:13","2016-04-25 12:13:14","2016-04-25 13:14:15", CmsConstants.PlatformStatus.Instock);

//        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));

////        System.out.println(sxProductService.changeSize(2, "5.5"));
//        Set<String> imageUrlSet = new HashSet<String>();
//        imageUrlSet.add("Ori4.jsp");
//        imageUrlSet.add("Ori5.jsp");
//        sxProductService.uploadImage("066", 23, "333", null, imageUrlSet, "morse");

//        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
//        model.setSeq(2323);
//        model.setCartId(8);
//        sxProductService.updateSxWorkload(model, 9, "morse");

//        productService.getLogicQty("066", Arrays.asList("skuL1", "skuL2", "skuL3"));

//        ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode("005",20, "74-721");

//        SxData sxData = new SxData();
//        sxData.setChannelId("066");
//        sxData.setCartId(23);
//        sxData.setGroupId(Long.valueOf(99));
//
//        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
//        platform.setNumIId("num099-3");
//        sxData.setPlatform(platform);
//
//        List<CmsBtProductModel> listProduct = new ArrayList<>();
//        for (int index = 0; index < 3; index++) {
//            CmsBtProductModel model = new CmsBtProductModel();
//            CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
//            fields.setCode("C" + String.valueOf(index));
//            model.setFields(fields);
//            listProduct.add(model);
//        }
//        sxData.setProductList(listProduct);
//
//        sxProductService.updateImsBtProduct(sxData, "p", "ljj");


        System.out.println("end");

//        List<CmsBtProductModel_Sku> skuSourceList = new ArrayList<>();
//
//        CmsBtProductModel_Sku sku13 = new CmsBtProductModel_Sku();
//        sku13.setSize("O/S");
//        sku13.setSkuCode("sku-O/S");
//        skuSourceList.add(sku13);
//
//        CmsBtProductModel_Sku sku11 = new CmsBtProductModel_Sku();
//        sku11.setSize("21.5cm");
//        sku11.setSkuCode("sku-21.5cm");
//        skuSourceList.add(sku11);
//
//        CmsBtProductModel_Sku sku15 = new CmsBtProductModel_Sku();
//        sku15.setSize("26");
//        sku15.setSkuCode("sku-26");
//        skuSourceList.add(sku15);
//
//        CmsBtProductModel_Sku sku10 = new CmsBtProductModel_Sku();
//        sku10.setSize("25.5");
//        sku10.setSkuCode("sku-25.5");
//        skuSourceList.add(sku10);
//
//        CmsBtProductModel_Sku sku1 = new CmsBtProductModel_Sku();
//        sku1.setSize("L");
//        sku1.setSkuCode("sku-l");
//        skuSourceList.add(sku1);
//
//        CmsBtProductModel_Sku sku12 = new CmsBtProductModel_Sku();
//        sku12.setSize("88.5cm1");
//        sku12.setSkuCode("sku-88.5cm1");
//        skuSourceList.add(sku12);
//
//        CmsBtProductModel_Sku sku8 = new CmsBtProductModel_Sku();
//        sku8.setSize("35");
//        sku8.setSkuCode("sku-35");
//        skuSourceList.add(sku8);
//
//        CmsBtProductModel_Sku sku4 = new CmsBtProductModel_Sku();
//        sku4.setSize("aaa");
//        sku4.setSkuCode("sku-aaa");
//        skuSourceList.add(sku4);
//
//        CmsBtProductModel_Sku sku5 = new CmsBtProductModel_Sku();
//        sku5.setSize("15");
//        sku5.setSkuCode("sku-15");
//        skuSourceList.add(sku5);
//
//        CmsBtProductModel_Sku sku9 = new CmsBtProductModel_Sku();
//        sku9.setSize("22cm");
//        sku9.setSkuCode("sku-22cm");
//        skuSourceList.add(sku9);
//
//        CmsBtProductModel_Sku sku2 = new CmsBtProductModel_Sku();
//        sku2.setSize("XXX");
//        sku2.setSkuCode("sku-xxx");
//        skuSourceList.add(sku2);
//
//        CmsBtProductModel_Sku sku7 = new CmsBtProductModel_Sku();
//        sku7.setSize("25");
//        sku7.setSkuCode("sku-25");
//        skuSourceList.add(sku7);
//
//        CmsBtProductModel_Sku sku3 = new CmsBtProductModel_Sku();
//        sku3.setSize("M");
//        sku3.setSkuCode("sku-m");
//        skuSourceList.add(sku3);
//
//        CmsBtProductModel_Sku sku6 = new CmsBtProductModel_Sku();
//        sku6.setSize("20cm");
//        sku6.setSkuCode("sku-20cm");
//        skuSourceList.add(sku6);
//
//        CmsBtProductModel_Sku sku14 = new CmsBtProductModel_Sku();
//        sku14.setSize("XS/S");
//        sku14.setSkuCode("sku-XS/S");
//        skuSourceList.add(sku14);

//        System.out.println("before sort");
//        for (CmsBtProductModel_Sku sku : skuSourceList) {
//            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize());
//        }

        // 大数据量
//        int max = 5000;
//        for (SkuSort skuSort : SkuSort.values()) {
//            if (skuSort == SkuSort.DIGIT) {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(String.valueOf(index));
//                    sku.setSkuCode("sku-" + index);
//                    skuSourceList.add(sku);
//                }
//            } else if (skuSort == SkuSort.DIGIT_UNITS) {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(String.valueOf(index) + "cm");
//                    sku.setSkuCode("sku-" + String.valueOf(index) + "cm");
//                    skuSourceList.add(sku);
//                }
//            } else {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(skuSort.getSize());
//                    sku.setSkuCode("sku-" + skuSort.name() + "-" + String.valueOf(index));
//                    skuSourceList.add(sku);
//                }
//            }
//        }

//        Map<String, Integer> mapSort = new HashMap<>();
//        for (SkuSort s : SkuSort.values()) {
//            mapSort.put(s.getSize(), Integer.valueOf(s.getSort()));
//        }
//
//        System.out.println("start:" + DateTimeUtil.getNowTimeStamp() + "  listSize=" + skuSourceList.size());
//        sortSkuInfo(skuSourceList);
//
//        System.out.println("");
//        System.out.println("after sort");
//        for (CmsBtProductModel_Sku sku : skuSourceList) {
////            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize() + " ,  sort=" + getSizeSort(sku.getSize()));
//            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize() + " ,  sort=" + getSizeSort(sku.getSize(), mapSort));
//        }
//
//        System.out.println("end:" + DateTimeUtil.getNowTimeStamp());

//        sxGetProductInfo.sortSkuInfo(skuSourceList);
//        System.out.println("end:" + DateTimeUtil.getNowTimeStamp());
    }

    public Map<String, Field> schemaToIdPropMap(String schema) throws TopSchemaException {
        Map<String, Field> fieldsMap = new HashMap<>();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        for (Field field : fields)
        {
            fieldsMap.put(field.getId(), field);
        }
        return fieldsMap;
    }

    private void recursiveGetTaobaoFields(List<com.taobao.top.schema.field.Field> fields, List<com.taobao.top.schema.field.Field> resultFields) {
        for (com.taobao.top.schema.field.Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetTaobaoFields(((com.taobao.top.schema.field.ComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetTaobaoFields(((com.taobao.top.schema.field.MultiComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }

    private void recursiveGetFields(List<Field> fields, List<Field> resultFields) {
        for (Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetFields(((ComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetFields(((MultiComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }

    /**
     * 如果sxProductBean中含有要在该平台中上新的sku, 返回true
     * 如果没有sku要上新，那么返回false
     */
    private boolean filtProductsByPlatform(SxProductBean sxProductBean) {
        CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();
        CmsBtProductGroupModel cmsBtProductModelGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
        List<CmsBtProductModel_Sku> cmsBtProductModelSkus = cmsBtProductModel.getSkus();
        int cartId = cmsBtProductModelGroupPlatform.getCartId();

        if (cmsBtProductModelSkus == null) {
            return false;
        }

        for (Iterator<CmsBtProductModel_Sku> productSkuIterator = cmsBtProductModelSkus.iterator(); productSkuIterator.hasNext();) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = productSkuIterator.next();
            if (!cmsBtProductModel_sku.isIncludeCart(cartId)) {
                productSkuIterator.remove();
            }
        }
        return !cmsBtProductModelSkus.isEmpty();
    }


    @Test
    public void testDict() throws Exception {
        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        ShopBean shopBean = new ShopBean();
        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        String[] extParameter = {"c001"};
        String val = sxProductService.resolveDict("京东产品图片-1", expressionParser, shopBean, "tom", extParameter);
        System.out.println(val);

        extParameter[0] = "c004";
        val = sxProductService.resolveDict("京东产品图片-1", expressionParser, shopBean, "tom", extParameter);
        System.out.println(val);
    }

	/**
     * 只是用来测试, 真实逻辑不是这样的
     */
    @Test
    public void testShopCustomCategory() throws Exception {
        String Separtor_Semicolon = ";";
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id("010");
        shop.setCart_id("23");

        // 多个条件表达式用分号分隔用
        StringBuilder builder = new StringBuilder();
        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids"加cartId
        String platformPropId = "seller_cids_" + shop.getCart_id();

        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 优先使用条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                // ===================expressionParser会被共通函数替换掉================================
                String propValue = expressionParser.parse(conditionExpression, shop, "tom", null);  // TODO No.8 调用共通函数
//                String propValue = "";
                // 多个表达式(2392231-4345291格式)用分号分隔
                if (propValue != null) {
                    builder.append(propValue);
                    builder.append(Separtor_Semicolon);   // 用分号(";")分隔
                }
            }
        }
        // 移除最后的分号
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        // 店铺种类
        System.out.println(builder.toString());

    }

}