package com.voyageone.web2.cms.views.search;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSearchInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.SEARCH.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsSearchIndexController extends CmsController {

    @Autowired
    private CmsSearchIndexService searchIndexService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.INDEX.INIT)
    public AjaxResponse init() {
        return success(searchIndexService.getMasterData(getUser(), getLang()));
    }

    /**
     * 检索出group和product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.INDEX.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索group数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.INDEX.GET_GROUP_LIST)
    public AjaxResponse getGroupList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.INDEX.GET_PRODUCT_LIST)
    public AjaxResponse getProductList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 返回用户信息
        return success(resultBean);
    }

}
