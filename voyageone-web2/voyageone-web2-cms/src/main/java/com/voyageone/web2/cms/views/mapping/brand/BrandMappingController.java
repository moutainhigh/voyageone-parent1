package com.voyageone.web2.cms.views.mapping.brand;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.BrandMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;

/**
 * 品牌映射访问控制
 * @author Wangtd 2016/07/25
 * @since 2.3.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.MAPPING.BRAND.ROOT, method = RequestMethod.POST)
public class BrandMappingController extends CmsController {
	
	@Autowired
	private BrandMappingService brandMappingService;
	
	/**
	 * 初始化页面数据
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.INIT)
	public AjaxResponse init() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cartList", TypeChannels.getTypeListSkuCarts(getUser().getSelChannelId(),
				Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang()));
		return success(result);
	}
	
	/**
	 * 搜索品牌匹配数据
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.SEARCH_BRANDS)
	public AjaxResponse searchBrands(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		// 检索品牌映射关系的数量
		result.put("brandCount", brandMappingService.searchBrandsCount(brandMapping));
		// 检索品牌映射关系的数据
		result.put("brandList", brandMappingService.searchBrandsByPage(brandMapping));
		
		return success(result);
	}
	
	/**
	 * 检索商户品牌数据 
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.SEARCH_CUST_BRANDS)
	public AjaxResponse searchCustBrands(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		result.put("custBrandList", brandMappingService.searchCustBrands(brandMapping));
		
		return success(result);
	}
	
	/**
	 * 检索已匹配的品牌数据 
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.SEARCH_MATCHED_BRANDS)
	public AjaxResponse searchMatchedBrands(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		Preconditions.checkState(StringUtils.isNotBlank(brandMapping.getBrandId()));
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		result.put("matchedBrandList", brandMappingService.searchMatchedBrands(brandMapping));
		
		return success(result);
	}
	
	/**
	 * 更新匹配的品牌数据 
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.ADD_NEW_BRAND_MAPPING)
	public AjaxResponse addNewBrandMapping(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		Preconditions.checkState(StringUtils.isNotBlank(brandMapping.getBrandId()));
		Preconditions.checkState(StringUtils.isNotBlank(brandMapping.getCmsBrand()));
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		result.put("success", brandMappingService.addNewBrandMapping(brandMapping, userSession));
		
		return success(result);
	}
	
	/**
	 * 添加或更新匹配的品牌数据 
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.ADD_OR_UPDATE_BRAND_MAPPING)
	public AjaxResponse addOrUpdateBrandMapping(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		Preconditions.checkState(StringUtils.isNotBlank(brandMapping.getBrandId()));
		Preconditions.checkState(StringUtils.isNotBlank(brandMapping.getCmsBrand()));
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		result.put("success", brandMappingService.addOrUpdateBrandMapping(brandMapping, userSession));
		
		return success(result);
	}
	
	/**
	 * 同步平台品牌数据
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.SYNCHRONIZE_PLATFORM_BRANDS)
	public AjaxResponse synchronizePlatformBrands(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		try {
			brandMappingService.synchronizePlatformBrands(brandMapping, userSession);
			result.put("success", true);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			result.put("message", e.getMessage());
		}
		
		return success(result);
	}
	
	/**
	 * 取得平台品牌数据同步时间
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.GET_SYNCHRONIZE_TIME)
	public AjaxResponse getSynchronizedTime(@RequestBody BrandMappingBean brandMapping) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		
		result.put("synchTime", brandMappingService.getSynchronizedTime(brandMapping));
		
		return success(result);
	}

}
