package com.voyageone.service.impl.cms.product.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalBySmartMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piao on 2017/3/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsAdvSearchProductApprovalServiceTest {

    @Autowired
    CmsAdvSearchProductApprovalService cmsAdvSearchProductApprovalService;

    @Test
    public void approval() throws Exception {

        String msg = "{\n" +
                "\t\"channelId\": \"010\",\n" +
                "\t\"cartList\": [23],\n" +
                "\t\"productCodes\": [\"51A0HC13E1-00LCNB0\"],\n" +
                "\t\"params\": {\n" +
                "\t\t\"cartId\": 23,\n" +
                "\t\t\"_option\": \"approval\",\n" +
                "\t\t\"productIds\": [\"51A0HC13E1-00LCNB0\"],\n" +
                "\t\t\"isSelAll\": 0,\n" +
                "\t\t\"notChkPrice\": 1\n" +
                "\t},\n" +
                "\t\"cmsSessionParams\": {\n" +
                "\t\t\"platformType\": {\n" +
                "\t\t\t\"cTypeId\": \"MT\",\n" +
                "\t\t\t\"cartId\": 0\n" +
                "\t\t},\n" +
                "\t\t\"extraInfo\": {\n" +
                "\t\t\t\"_adv_search_props_custAttsQueryList\": [{\n" +
                "\t\t\t\t\"configValue1\": \"产品品牌\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.brand\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"产品材质\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MetalStamp\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"产品宝石\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Stone\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"产品尺寸\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Description Measure\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"宝石性质\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.GemCreationMethod\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"宝石总量\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.GemstoneTotalWeight\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"产地\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.CountryOfOrigin\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"戒指手寸\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Ringsize\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configValue1\": \"受众人群\",\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Age Group\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MSRP\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ManufacturerCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Gender\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneLength\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.LabCreated\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneWidth\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.isClearance\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.LaunchProduct\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.AgeGroup\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Style\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ClaspType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.NominalScaleWeight\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ItemCreationDate\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.TotalMetalWeight\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MetalType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ItemClassification\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MerchantCategory\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Popularity\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MerchClassification\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.VisibilityName\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ColorCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.FinishCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.TotalQuantity\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice1\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice0\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Active\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BulletPoint2\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice3\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BulletPoint3\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice2\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PieceID\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BulletPoint1\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MetalColor\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice9\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ChainType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice8\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ActionURL\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Visibility\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice5\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ChainLength\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice4\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice7\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ManufacturingPolicy\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.MagicPrice6\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BulletPoint4\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Manufacturermodel\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ITEMTHUMBURL\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneColor\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneSetting\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Keywords\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.SettingTypeDesc\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Supplier\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ShapeCut\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PriceLabel\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneGroupCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BulletPoint5\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.CTTW\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PrimaryStoneWeight\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Clarity\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.GIAColor\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.SettingTypeCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.Collection\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BraceletLength\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlMinColor\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.GemCreationManual\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.DescriptionMeasure\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlMinimumColor\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.TreatmentMethod1\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.StoneColorCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ClaspCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlUniformity\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlStringingMethod\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlLuster\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlShape\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.TreatmentMethod2\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.TreatmentMethod3\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.PearlCount\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ChainItemNo\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.BackFinding\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ShapeCode\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.DiamondCertificate\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"configCode\": \"feed.cnAtts.ChainSKU\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"客户建议零售价\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.clientMsrpPrice\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"客户成本价\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.clientNetPrice\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"品牌方商品地址\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.clientProductUrl\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-1\",\n" +
                "\t\t\t\t\"configValue1\": \"原始主商品\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.isMasterMain\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"产品名称英语\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.productNameEn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"产品名称中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.originalTitleCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"款号\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.model\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"商品特质英文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.codeDiff\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"商品特质中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.color\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"产品分类\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.productType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"适用人群\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.sizeType\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"适用人群(中文)\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.sizeTypeCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"产品分类(中文)\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.productTypeCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"税号集货\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.hsCodeCrop\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list-2\",\n" +
                "\t\t\t\t\"configValue1\": \"税号个人\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.hsCodePrivate\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"材质英语\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.materialEn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"材质中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.materialCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"产地\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.origin\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"简短描述英语\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.shortDesEn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"简短描述中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.shortDesCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"详情描述英语\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.longDesEn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"详情描述中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.longDesCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"使用说明英语\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.usageEn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"使用说明中文\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.usageCn\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list\",\n" +
                "\t\t\t\t\"configValue1\": \"商品翻译状态\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.translateStatus\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"翻译者\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.translator\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"翻译时间\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.translateTime\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list\",\n" +
                "\t\t\t\t\"configValue1\": \"主类目设置状态\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.categoryStatus\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"主类目设置者\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.categorySetter\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"主类目设置时间\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.categorySetTime\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"list\",\n" +
                "\t\t\t\t\"configValue1\": \"税号设置状态\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.hsCodeStatus\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"税号设置者\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.hsCodeSetter\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"税号设置时间\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.hsCodeSetTime\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"产品注释\",\n" +
                "\t\t\t\t\"configCode\": \"common.comment\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"string\",\n" +
                "\t\t\t\t\"configValue1\": \"商品创建时间\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.created\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"valType\": \"number\",\n" +
                "\t\t\t\t\"configValue1\": \"SKU数\",\n" +
                "\t\t\t\t\"configCode\": \"common.fields.skuCnt\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"_adv_search_customProps\": [],\n" +
                "\t\t\t\"_adv_search_commonProps\": [],\n" +
                "\t\t\t\"_adv_search_props_searchItems\": \"\",\n" +
                "\t\t\t\"_adv_search_params\": {\n" +
                "\t\t\t\t\"groupPageNum\": 0,\n" +
                "\t\t\t\t\"groupPageSize\": 0,\n" +
                "\t\t\t\t\"productPageNum\": 1,\n" +
                "\t\t\t\t\"productPageSize\": 10,\n" +
                "\t\t\t\t\"brandSelType\": 1,\n" +
                "\t\t\t\t\"freeTagType\": 1,\n" +
                "\t\t\t\t\"supplierType\": 1,\n" +
                "\t\t\t\t\"productSelType\": \"1\",\n" +
                "\t\t\t\t\"sizeSelType\": \"1\",\n" +
                "\t\t\t\t\"cartId\": 0,\n" +
                "\t\t\t\t\"productStatus\": [],\n" +
                "\t\t\t\t\"platformStatus\": [],\n" +
                "\t\t\t\t\"pRealStatus\": [],\n" +
                "\t\t\t\t\"pCatStatus\": 0,\n" +
                "\t\t\t\t\"promotionTagType\": 1,\n" +
                "\t\t\t\t\"cidValue\": [],\n" +
                "\t\t\t\t\"shopCatStatus\": 0,\n" +
                "\t\t\t\t\"priceChgFlg\": \"\",\n" +
                "\t\t\t\t\"priceDiffFlg\": \"\",\n" +
                "\t\t\t\t\"hasErrorFlg\": 0,\n" +
                "\t\t\t\t\"salesType\": \"All\",\n" +
                "\t\t\t\t\"custAttrMap\": [{\n" +
                "\t\t\t\t\t\"inputVal\": \"\",\n" +
                "\t\t\t\t\t\"inputOpts\": \"\",\n" +
                "\t\t\t\t\t\"inputOptsKey\": \"\"\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"fileType\": 0,\n" +
                "\t\t\t\t\"custGroupType\": \"1\",\n" +
                "\t\t\t\t\"pcatStatus\": 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"_adv_search_selSalesType\": [],\n" +
                "\t\t\t\"_adv_search_productListTotal\": 4590\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"consumerRetryTimes\": 0,\n" +
                "\t\"mqId\": 0,\n" +
                "\t\"delaySecond\": 0,\n" +
                "\t\"sender\": \"edward\"\n" +
                "}";
        AdvSearchProductApprovalMQMessageBody advSearchExportMQMessageBody = JacksonUtil.json2Bean(msg, AdvSearchProductApprovalMQMessageBody.class);

        cmsAdvSearchProductApprovalService.approval(advSearchExportMQMessageBody);

    }

    @Test
    public void intelligent() throws Exception {

        AdvSearchProductApprovalBySmartMQMessageBody messageBody = new AdvSearchProductApprovalBySmartMQMessageBody();
        messageBody.setCartId(27);
        messageBody.setChannelId("010");
        List<String> productCodes = new ArrayList<>();
        productCodes.add("CHBR043MB");
        productCodes.add("BF00003YGK");
        productCodes.add("01411YAA");
        messageBody.setProductCodes(productCodes);
        messageBody.setSender("edward.lin");
        List<CmsBtOperationLogModel_Msg> errorList = cmsAdvSearchProductApprovalService.intelligent(messageBody);
        errorList.forEach(msg -> {
            System.out.println(msg.getSkuCode() + ":" + msg.getMsg());
        });
    }
}
