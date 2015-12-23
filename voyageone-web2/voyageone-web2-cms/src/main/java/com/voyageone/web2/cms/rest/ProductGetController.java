package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.CmsRestController;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/puroduct",
        method = RequestMethod.POST
)
public class ProductGetController extends CmsRestController {

    @Autowired
    private ProductGetService productService;

    /**
     * selectOne
     * @return CmsBtProductModel
     */
    @RequestMapping("selectOne")
    public ProductGetResponse selectOne(@RequestBody ProductGetRequest response) {
        return productService.selectOne(response);
    }

    /**
     * selectList
     * @return List<CmsBtProductModel>
     */
    @RequestMapping("selectList")
    public ProductsGetResponse selectList(@RequestBody ProductsGetRequest response) {
        return productService.selectList(response);
    }
}
