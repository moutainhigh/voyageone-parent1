package com.voyageone.web2.cms.views.system.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.redis.CacheTemplateFactory;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aooer 2016/3/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.SYSTEM.CACHE.ROOT, method ={RequestMethod.POST, RequestMethod.GET})
public class CacheManagerController extends CmsController{

    @RequestMapping(CmsUrlConstants.SYSTEM.CACHE.INIT)
    public AjaxResponse init(HttpServletRequest request) throws Exception {
        if(!StringUtils.isEmpty(request.getParameter("cacheKey"))) {
            CacheTemplateFactory.getCacheTemplate().delete(request.getParameter("cacheKey"));
            return redirectTo("/modules/cms/app.html#/system/cache/index");
        }
        return success(cacheKeySet());
    }

    private Set<String> cacheKeySet(){
        return CacheTemplateFactory.getCacheTemplate().keys("ConfigData_*");
    }

}