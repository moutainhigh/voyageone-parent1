package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductService;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductPriceLogGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsCountGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.ProductPriceLogGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsCountGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/product",
        method = RequestMethod.POST
)
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    /**
     * selectOne
     *
     * @return ProductGetResponse
     */
    @RequestMapping("selectOne")
    public ProductGetResponse selectOne(@RequestBody ProductGetRequest request) {
        return productService.selectOne(request);
    }

    /**
     * selectList
     *
     * @return ProductsGetResponse
     */
    @RequestMapping("selectList")
    public ProductsGetResponse selectList(@RequestBody ProductsGetRequest request) {
        return productService.selectList(request);
    }

    /**
     * selectList
     *
     * @return ProductsCountGetResponse
     */
    @RequestMapping("selectCount")
    public ProductsCountGetResponse selectCount(@RequestBody ProductsCountGetRequest request) {
        return productService.selectCount(request);
    }

    /**
     * getPriceLog
     *
     * @return ProductPriceLogGetResponse
     */
    @RequestMapping("priceLog/get")
    public ProductPriceLogGetResponse getPriceLog(@RequestBody ProductPriceLogGetRequest request) {
        return productService.getPriceLog(request);
    }
}