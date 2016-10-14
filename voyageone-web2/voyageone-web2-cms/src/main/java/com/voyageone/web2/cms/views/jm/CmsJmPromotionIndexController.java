package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotion3Service;
import com.voyageone.service.impl.cms.jumei2.JmBtDealImportService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = CmsUrlConstants.JMPROMOTION.LIST.INDEX.ROOT, method = RequestMethod.POST)
public class CmsJmPromotionIndexController extends CmsController {
    private final CmsBtJmPromotionService service;
    private final CmsBtJmPromotion3Service service3;
    private final JmBtDealImportService serviceJmBtDealImport;

    @Autowired
    public CmsJmPromotionIndexController(JmBtDealImportService serviceJmBtDealImport, CmsBtJmPromotionService service,
                                         CmsBtJmPromotion3Service service3) {
        this.serviceJmBtDealImport = serviceJmBtDealImport;
        this.service = service;
        this.service3 = service3;
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(service.init());
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GET_LIST_BY_WHERE)
    public AjaxResponse getListByWhere(@RequestBody Map<String, Object> params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(service.getListByWhere(params));
    }

    /**
     * 编辑聚美专场活动时，取得活动信息
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GetEditModel)
    public AjaxResponse getEditModel(@RequestBody int id) {
        CmsBtJmPromotionSaveBean parameter = new CmsBtJmPromotionSaveBean();
        CmsBtJmPromotionModel model = new CmsBtJmPromotionModel();
        model.setId(id);
        parameter.setModel(model);
        return success(service.getEditModel(parameter));
    }

    /**
     * 暂存和保存活动详细信息时，取得活动信息(包含扩展信息)
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GetEditModelExt)
    public AjaxResponse getEditModelExt(@RequestBody CmsBtJmPromotionSaveBean parameter) {
        return success(service.getEditModel(parameter));
    }

    /**
     * 保存活动信息
     * 包括新建和编辑聚美专场活动, 暂存和保存活动详细信息
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.SaveModel)
    public AjaxResponse saveModel(@RequestBody CmsBtJmPromotionSaveBean parameter) {
        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();
        parameter.getModel().setChannelId(channelId);
        parameter.getModel().getActivityEnd().setSeconds(59);//聚美专场结束时间都以59秒结尾。
        return success(service.saveModel(parameter, userName, channelId));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GET)
    public Object get(@RequestBody int id) {//@RequestParam("id")
        return success(service.select(id));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.DELETE)
    public Object delete(@RequestBody int id) {
        service.delete(id);
        return success(0);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GetTagListByPromotionId)
    public AjaxResponse getTagListByPromotionId(@RequestBody int promotionId) {
        return success(service3.getTagListByPromotionId(promotionId));
    }

    /**
     * 查询聚美活动一览
     * 聚美专场新增，不使用原有的"CmsPromotionIndexController.getPage (/cms/promotion/index/getPage)"
     */
    @RequestMapping("getJmPromList")
    public AjaxResponse getJmPromotionList(@RequestBody Map<String, Object> params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(service.getJmPromotionList(params));
    }

    //获取数量
    @RequestMapping("getJmPromCount")
    public AjaxResponse getCount(@RequestBody Map<String, Object> params) {
        params.put("channelId", getUser().getSelChannelId());
        return success(service.getJmPromotionCount(params));
    }

    //"/cms/jmpromotion/index/importJM"
    @RequestMapping(value = CmsUrlConstants.JMPROMOTION.LIST.INDEX.ImportJM, method = RequestMethod.GET)
    public Object importChannel(@RequestParam("channelId") String channelId) {
        // return success(service3.getTagListByPromotionId(promotionId));
        return serviceJmBtDealImport.importJM(channelId);
        // return "true";
    }
}
