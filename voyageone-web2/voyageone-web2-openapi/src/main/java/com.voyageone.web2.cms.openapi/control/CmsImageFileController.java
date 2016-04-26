package com.voyageone.web2.cms.openapi.control;

import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateFileService;
import com.voyageone.service.model.openapi.image.AddListParameter;
import com.voyageone.service.model.openapi.image.AddListRespone;
import com.voyageone.service.model.openapi.image.GetImageRespone;
import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(
        value  = "/rest",
        method = RequestMethod.GET
)
public class CmsImageFileController extends OpenAipBaseController {
    @Autowired
    CmsMtImageCreateFileService service;
    ///http://localhost:8081/rest/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered
    @RequestMapping(value = "/product/image/getImage", method = RequestMethod.GET)
    public GetImageRespone get(HttpServletRequest request, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam) throws Exception {
        String queryString = request.getQueryString();
        return service.getImage(cId, templateId, file, vparam, queryString, "SystemCreateImage");
    }
    @RequestMapping(value = "/product/image/addList", method = RequestMethod.GET)
    public AddListRespone addList(@RequestParam AddListParameter parameter) {
        return  service.addList(parameter);

    }
}
