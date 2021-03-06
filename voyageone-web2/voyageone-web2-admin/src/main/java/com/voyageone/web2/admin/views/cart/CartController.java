package com.voyageone.web2.admin.views.cart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.com.cart.CartService;
import com.voyageone.service.model.com.CtCartModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.cart.CartFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Cart.Self.ROOT, method = RequestMethod.POST)
public class CartController extends AdminController {
	
	@Resource(name = "AdminCartService")
	private CartService cartService;
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Cart.Self.GET_ALL_CART)
	public AjaxResponse getAllCart(@RequestBody(required = false) String channelId) {
		List<CtCartModel> carts = cartService.getAllCart(channelId);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		carts.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("cartId", item.getCartId());
			put("name", item.getName());
			put("shortName", item.getShortName());
		}}));
		return success(result);
	}
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Cart.Self.GET_CART_BY_IDS)
	public AjaxResponse getCartByIds(@RequestBody Map<String, String> params) {
		String strCartIds = params.get("cartIds");
		Preconditions.checkArgument(StringUtils.isNotBlank(strCartIds));
		// 取得CartId
		String[] cartIds = StringUtils.split(strCartIds, ",");
		// 检索Cart信息
		List<CtCartModel> carts = cartService.getCartByIds(Arrays.asList(cartIds));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		carts.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("cartId", item.getCartId());
			put("name", item.getName());
			put("shortName", item.getShortName());
		}}));
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.SEARCH_CART_BY_PAGE)
	public AjaxResponse searchCartByPage(@RequestBody CartFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索Cart信息
		PaginationResultBean<CtCartModel> cartPage = cartService.searchCartByPage(form.getCartId(), form.getCartName(),
				form.getCartType(), form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(cartPage);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.ADD_CART)
	public AjaxResponse addCart(@RequestBody CartFormBean form) {
		return addOrUpdateCart(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.UPDATE_CART)
	public AjaxResponse updateCart(@RequestBody CartFormBean form) {
		return addOrUpdateCart(form, false);
	}
	
	private AjaxResponse addOrUpdateCart(CartFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkNotNull(form.getPlatformId());
		Preconditions.checkNotNull(form.getCartId());
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getName()));
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getShortName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCartType()));
		Preconditions.checkNotNull(form.getActive());
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getDescription()));

		// 保存Cart信息
		CtCartModel model = new CtCartModel();
		BeanUtils.copyProperties(form, model);
		cartService.addOrUpdateCart(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.DELETE_CART)
	public AjaxResponse deleteCart(@RequestBody Integer[] cartIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(cartIds));
		// 删除Cart信息
		cartService.deleteCart(Arrays.asList(cartIds), getUser().getUserName());

		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.GET_ALL_PLATFORM)
	public AjaxResponse getAllPlatform() {
		return success(cartService.getAllPlatform());
	}

}
