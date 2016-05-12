package com.voyageone.web2.cms.openapi.control;

import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.web2.cms.openapi.service.CmsImageFileService;
//import com.voyageone.service.model.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.GetImageResultBean;
import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(
        value = "/rest/product/image"
)
public class CmsImageFileController extends OpenAipBaseController {
    private static final String CREATE_USER = "SystemCreateImage";

    @Autowired
    CmsImageFileService service;

    @RequestMapping(value = "get_own")
    public GetImageResultBean get(HttpServletRequest request, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam boolean isUploadUSCDN, @RequestParam String vparam) throws Exception {
        $info("CmsImageFileController:get start cId:=[%s],templateId=[%s],file=[%s],vparam=[%s]", cId, templateId, file, vparam);
        return service.getImage(cId, templateId, file, isUploadUSCDN, vparam, CREATE_USER);
    }

    //source=name[icon],url[%s]&source=name[s],url[%s]&scale=height[1100],width[700]&blank=color[white],height[1200],name[bcc],width[1200]&select=name[bcc]&composite=compose[Over],image[s],x[200],y[100]&composite=compose[Over],image[icon],x[100],y[32]&annotate=fill[red],font[VeraSans-Bold],pointsize[18],text[%s],x[923],y[832]&sink
    ///http://localhost:8081/rest/product/image/get?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered
    //http://localhost:8081/rest/product/image/get?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=["file:bcbg/bcbg-sku.png","file:bcbg/bcbgtupian.jpg","Text String to be rendered"]
    @RequestMapping(value = "get")
    public GetImageResultBean get(HttpServletRequest request, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam) throws Exception {
        $info("CmsImageFileController:get start cId:=[%s],templateId=[%s],file=[%s],vparam=[%s]", cId, templateId, file, vparam);
        return service.getImage(cId, templateId, file, false, vparam, CREATE_USER);
    }

    @RequestMapping(value = "addList", method = RequestMethod.POST)
    public AddListResultBean addList(@RequestBody AddListParameter parameter) {
        return service.addListWithTrans(parameter);
    }
}