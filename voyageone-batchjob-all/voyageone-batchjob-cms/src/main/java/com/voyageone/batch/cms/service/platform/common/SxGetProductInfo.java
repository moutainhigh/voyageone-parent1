package com.voyageone.batch.cms.service.platform.common;

import com.voyageone.batch.cms.bean.platform.SxData;
import com.voyageone.batch.cms.dao.SkuInventoryDao;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class SxGetProductInfo {
	@Autowired
	CmsProductService cmsProductService;
	@Autowired
	SkuInventoryDao skuInventoryDao;

	/**
	 * 获取group的所有商品的信息
	 */
	public SxData getProductInfoByGroupId(String channelId, Long groupId) {
		SxData sxData = new SxData();

		// 获取group的所有商品的主数据信息 (取出一堆的product)
		List<CmsBtProductModel> cmsBtProductModelList = getProductInfo(channelId, groupId);
		sxData.setProductList(cmsBtProductModelList);

		// 分析出需要的platform (只取出当前的group的platform信息, 存放的是多个product的当前platform)
		List<CmsBtProductModel_Group_Platform> platformList = new ArrayList<>();
		for (CmsBtProductModel productModel : cmsBtProductModelList) {
			platformList.add(productModel.getGroups().getPlatformByGroupId(groupId));
		}
		sxData.setPlatformList(platformList);

		// 设置一些基本信息
		sxData.setChannelId(channelId);
		sxData.setCartId(platformList.get(0).getCartId());
		sxData.setGroupId(groupId);
		// 平台上的productId
		String platformProductId = "";
		for (CmsBtProductModel_Group_Platform platform : platformList) {
			if (!StringUtils.isEmpty(platform.getProductId())) {
				platformProductId = platform.getProductId();
				break;
			}
		}
		sxData.setPlatformProductId(platformProductId);
		// 平台上的numIId
		String platformNumIId = "";
		for (CmsBtProductModel_Group_Platform platform : platformList) {
			if (!StringUtils.isEmpty(platform.getNumIId())) {
				platformNumIId = platform.getNumIId();
				break;
			}
		}
		sxData.setPlatformNumIId(platformNumIId);

		// 获取group的所有商品的sku信息 (剔除不需要在当前cart中上新的sku, 内容是所有product在当前cart中需要上新的sku)
		List<CmsBtProductModel_Sku> cmsBtProductModelSkuList = getSkuInfo(sxData.getCartId(), sxData.getSkuList());
		sxData.setSkuList(cmsBtProductModelSkuList);

		// 获取库存信息
		List<String> skus = new ArrayList<>();
		for (CmsBtProductModel_Sku sku : cmsBtProductModelSkuList) {
			skus.add(sku.getSkuCode());
		}
		Map<String, Integer> qtyList = skuInventoryDao.getSkuInventory(channelId, skus);
		// 如果找不到的话, 库存直接设置为0
		for (CmsBtProductModel_Sku sku : cmsBtProductModelSkuList) {
			if (!qtyList.containsKey(sku.getSkuCode())) {
				qtyList.put(sku.getSkuCode(), 0);
			}
		}
		sxData.setQtyList(qtyList);

		return sxData;
	}

	/**
	 * 获取group的所有商品的主数据信息
	 */
	private List<CmsBtProductModel> getProductInfo(String channelId, Long groupId) {
		return cmsProductService.getProductByGroupId(channelId, groupId);
	}

	/**
	 * 获取group的所有商品的sku信息 (剔除不需要在当前cart中上新的sku)
	 */
	private List<CmsBtProductModel_Sku> getSkuInfo(int cartId, List<CmsBtProductModel_Sku> skuSourceList) {
		List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

		for (CmsBtProductModel_Sku sku : skuSourceList) {
			if (sku.getSkuCarts() != null) {
				if (sku.getSkuCarts().contains(cartId)) {
					skuList.add(sku);
				}
			}
		}

		// 排序一下
		skuList = sortSkuInfo(skuList);

		return skuList;
	}

	/**
	 * 将sku排序一下, 用于显示在平台上面好看一点
	 * TODO: 排序的代码先临时这样写一下, 等最后有空了再改好看一点 tom
	 * TODO: 另外, 排序的算法这样写其实速度也是很一般, 如果以后数据量很大的话, 这里可以优化一下的 tom
	 * @param skuSourceList 等待排序的sku列表
	 * @return 排序之后的sku列表
	 */
	private List<CmsBtProductModel_Sku> sortSkuInfo(List<CmsBtProductModel_Sku> skuSourceList) {

		// 准备排序用的顺序
		List<String> lstSortRuleList = new ArrayList<>();
		// 纯数字系列
		for (int i = 0; i < 100; i++) {
			lstSortRuleList.add(String.valueOf(i));
			lstSortRuleList.add(String.valueOf(i) + ".5");
		}
		// 纯数字系列(cm)
		for (int i = 0; i < 100; i++) {
			lstSortRuleList.add(String.valueOf(i) + "cm");
			lstSortRuleList.add(String.valueOf(i) + ".5cm");
		}
		// SM系列
		lstSortRuleList.add("XXX");
		lstSortRuleList.add("XXS");
		lstSortRuleList.add("XS");
		lstSortRuleList.add("XS/S");
		lstSortRuleList.add("XSS");
		lstSortRuleList.add("S");
		lstSortRuleList.add("S/M");
		lstSortRuleList.add("M");
		lstSortRuleList.add("M/L");
		lstSortRuleList.add("L");
		lstSortRuleList.add("XL");
		lstSortRuleList.add("XXL");

		// 包的尺码不参与排序(放最后)
		lstSortRuleList.add("N/S");
		// OneSize尺码不参与排序(放最后)
		lstSortRuleList.add("O/S");
		lstSortRuleList.add("OneSize");

		// 排序
		List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

		for (String strValue : lstSortRuleList) {
			for (CmsBtProductModel_Sku value : skuSourceList) {
				String valueCheck = value.getSize();

				if (strValue.equals(valueCheck)) {
					skuList.add(value);
				}
			}
		}

		// 把剩下不在排序表里的例外的情况,放到最后面
		for (CmsBtProductModel_Sku value : skuSourceList) {
			String valueCheck = value.getSize();

			if (!lstSortRuleList.contains(valueCheck)) {
				skuList.add(value);
			}
		}

		return skuList;
	}

}
