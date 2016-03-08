package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/02/29.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.STOCK.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskStockController extends CmsController {

//    @Autowired
//    private CmsTaskStockService cmsTaskStockService;

    /**
     * @api {post} /cms/promotion/task_stock/initNewTaskFromPromotion 从活动一览新建库存隔离任务前初始化数据取得（取得隔离平台和共享平台）
     * @apiName initNewTaskFromPromotion
     * @apiDescription 从活动一览新建库存隔离任务前初始化数据取得（取得隔离平台和共享平台）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {List} selPromotionList 选择的Promotion列表
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} taskName 任务名
     * @apiSuccess (应用级返回字段) {Boolean} onlySku 只导入Sku和库存
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "showType":"1",
     *   "taskName":"",
     *   "onlySku":false,
     *   "platformList": [ {"carId":"23", "cartName":"天猫国际", "value":"", "type":"1", "restoreTime":"", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"", "type":"1", "restoreTime":"", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "type":"2", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     * }
     * 说明：先列出隔离平台，后列出共享平台，共享平台合并成一条数据，并且共享平台的"cart_id":"-1"。
     * showType：显示类型（1：从活动一览新建，2：从任务一览新建）； taskName：任务名；onlySku：是否只导入Sku和库存
     * carId：平台id；cartName：平台名；value：隔离比例；type：类型（1：隔离，2：共享）；restoreTime：还原时间；addPriority：增优先顺；subtractPriority：减优先顺
     *
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "选择的Promotion不能库存隔离（已经隔离中）", "displayType":null, "redirectTo":null, "data":null
     * }     *
     * @apiExample  业务说明
     *  1.某个渠道下，根据选择的Promotion，如果选择Promotion对应的平台已经在隔离状态中（cms_bt_tasks.status = '1:活动中'），那么提示该Promotion已经隔离的错误信息。
     *  2.根据选择的Promotion，从cms_bt_promotion表取得 任务的隔离平台信息。
     *  3.从tm_channel_shop取得该渠道对应的所有平台信息，除去1取得的隔离平台，剩余的平台作为共享平台。
     * @apiExample 使用表
     *  cms_bt_promotion
     *  tm_channel_shop
     *  cms_bt_tasks
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.INIT_NEW_TASK_FROM_PROMOTION)
    public AjaxResponse initNewTaskFromPromotion(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/initNewTaskFromTask 从任务一栏新建库存隔离任务前初始化数据取得（取得这个渠道对应的所有平台）
     * @apiName initNewTaskFromTask
     * @apiDescription 从任务一栏新建库存隔离任务前初始化数据取得（取得这个渠道对应的所有平台）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {List} selPromotionList 选择的Promotion列表
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} taskName 任务名
     * @apiSuccess (应用级返回字段) {Boolean} onlySku 只导入Sku和库存
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "taskName":"",
     *   "showType":"2",
     *   "platformList": [ {"carId":"23", "cartName":"天猫国际", "value":"", "type":"2", "promotionStartTime":"", "restoreTime":"", "addPriority":"", "subtractPriority":"},
     *                     {"cartId":"24", "cartName":"京东", "value":"", "type":"2", "promotionStartTime":"", "restoreTime":"", "addPriority":"", "subtractPriority":""},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"", "type":"2", "promotionStartTime":"", "restoreTime":"", "addPriority":"", "subtractPriority":""}...],
     *  }
     * }
     * 说明：列出这个渠道下面的所有平台，默认所有平台的类型为共享（"type":"2"）。
     * showType：显示类型（1：从活动一览新建，2：从任务一览新建）；taskName：任务名
     * carId：平台id；cartName：平台名；value：隔离比例；type：类型（1：隔离，2：共享）；promotionStartTime：活动开始时间；restoreTime：还原时间；addPriority：增优先顺；subtractPriority：减优先顺
     *
     * @apiExample  业务说明
     *  1.在任务一览下新建一个隔离任务时，根据渠道从tm_channel_shop表中取得所有的平台信息，供选择共享平台或者是隔离平台。
     *  默认所有平台都是共享平台。
     * @apiExample 使用表
     *  tm_channel_shop
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.INIT_NEW_TASK_FROM_TASK)
    public AjaxResponse initNewTaskFromTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/saveTask 新建/修改库存隔离任务
     * @apiName saveTask
     * @apiDescription 新建/修改库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {Object} taskInfo（json格式） 选择的Promotion列表
     * @apiParamExample  taskInfo参数示例1（从活动一览新建）
     *  "taskInfo":{
     *   "showType":"1",
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "onlySku":false,
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"30%", "type":"2", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "type":"2", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     *  @apiParamExample  taskInfo参数示例2（从任务一览新建）
     *  "taskInfo":{
     *   "showType":"2",
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "promotionStartTime":"2016/11/1 00:00:00", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"24", "cartName":"京东", "value":"20%", "type":"2", "promotionStartTime":"2016/11/1 03:00:00", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"},
     *                     {"cartId":"26", "cartName":"京东国际", "value":"", "type":"1", "promotionStartTime":"", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}，
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"", "type":"1", "promotionStartTime":"", "restoreTime":", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     * @apiParamExample  taskInfo参数示例3（修改）
     *  "taskInfo":{
     *   "taskId":1，
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"30%", "type":"2", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "type":"2", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     *  说明： showType：显示类型（1：从活动一览新建，2：从任务一览新建）；
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "隔离比例不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。隔离平台的隔离比例和所有平台的优先顺必须设定而且为大于0的整数。优先顺必须是1开始的连续整数。 隔离结束时间必须是时间格式。
     *                   如果从任务一览新建，那么共享平台的优先顺必须一致。
     *                   如果从任务一览新建活动时，隔离结束时间可以不输入，不输入的情况下，指定为活动结束的时间 加上 指定的分钟数（在com_mt_value表中定义id=426）。
     *                                             隔离结束时间输入时，必须大于指定为活动结束的时间 加上 指定的分钟数。
     *  2.将隔离信息（对应平台隔离比例，活动开始时间，还原时间，优先顺等）反应到cms_bt_tasks表和cms_bt_stock_separate_platform_info表。(新建的场合，任务状态 0:Ready；修改的场合，隔离比例不能变更)
     *  新建的场合，继续下面的步骤
     *  3.如果从活动一览新建活动时，抽出隔离平台下面的所有Sku后，取得商品基本情报，计算出可用库存数和各隔离平台的隔离数。
     *     3.0.从mangoDB的商品表，取得商品基本情报(每个channel需要取得的字段是不一样的，需要读取com_mt_value_channel的配置type_id=61)
     *    如果参数stockTaskInfo.onlySku = false,将隔离平台的信息插入到cms_bt_stock_separate_item表（只有基本信息和可用库存，各平台的隔离库存为0，状态为"0:未进行"）
     *    如果参数stockTaskInfo.onlySku = true,则按设定的隔离比例计算出各个平台的隔离库存更新到cms_bt_stock_separate_item表。（状态为"0:未进行"）
     *    3.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    3.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    3.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    3.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    3.5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *    3.6.隔离平台隔离库存 = 可用库存数 * 设定的百分比
     * @apiExample 使用表
     *  cms_bt_promotion
     *  cms_bt_tasks
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_item
     *  com_mt_value_channel
     *  cms_bt_product_cxxx
     *
     */    /**
     * @api {post} /cms/promotion/task_stock/saveTask 新建/修改库存隔离任务
     * @apiName saveTask
     * @apiDescription 新建/修改库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {Object} taskInfo（json格式） 选择的Promotion列表
     * @apiParamExample  taskInfo参数示例1（从活动一览新建）
     *  "taskInfo":{
     *   "showType":"1",
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "onlySku":false,
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"30%", "type":"2", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "type":"2", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     *  @apiParamExample  taskInfo参数示例2（从任务一览新建）
     *  "taskInfo":{
     *   "showType":"2",
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "promotionStartTime":"2016/11/1 00:00:00", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"24", "cartName":"京东", "value":"20%", "type":"2", "promotionStartTime":"2016/11/1 03:00:00", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"},
     *                     {"cartId":"26", "cartName":"京东国际", "value":"", "type":"1", "promotionStartTime":"", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}，
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"", "type":"1", "promotionStartTime":"", "restoreTime":", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     * @apiParamExample  taskInfo参数示例3（修改）
     *  "taskInfo":{
     *   "taskId":1，
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "type":"2", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"30%", "type":"2", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "type":"2", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     *  说明： showType：显示类型（1：从活动一览新建，2：从任务一览新建）；
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "隔离比例不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。隔离平台的隔离比例和所有平台的优先顺必须设定而且为大于0的整数。优先顺必须是1开始的连续整数。 隔离结束时间必须是时间格式。
     *                   如果从任务一览新建，那么共享平台的优先顺必须一致。
     *                   如果从任务一览新建活动时，隔离结束时间可以不输入，不输入的情况下，指定为活动结束的时间 加上 指定的分钟数（在com_mt_value表中定义id=426）。
     *                                             隔离结束时间输入时，必须大于指定为活动结束的时间 加上 指定的分钟数。
     *  2.将隔离信息（对应平台隔离比例，活动开始时间，还原时间，优先顺等）反应到cms_bt_tasks表和cms_bt_stock_separate_platform_info表。(新建的场合，任务状态 0:Ready；修改的场合，隔离比例不能变更)
     *  新建的场合，继续下面的步骤
     *  3.如果从活动一览新建活动时，抽出隔离平台下面的所有Sku后，取得商品基本情报，计算出可用库存数和各隔离平台的隔离数。
     *     3.0.从mangoDB的商品表，取得商品基本情报(每个channel需要取得的字段是不一样的，需要读取com_mt_value_channel的配置type_id=61)
     *    如果参数stockTaskInfo.onlySku = false,将隔离平台的信息插入到cms_bt_stock_separate_item表（只有基本信息和可用库存，各平台的隔离库存为0，状态为"0:未进行"）
     *    如果参数stockTaskInfo.onlySku = true,则按设定的隔离比例计算出各个平台的隔离库存更新到cms_bt_stock_separate_item表。（状态为"0:未进行"）
     *    3.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    3.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    3.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    3.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    3.5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *    3.6.隔离平台隔离库存 = 可用库存数 * 设定的百分比
     * @apiExample 使用表
     *  cms_bt_promotion
     *  cms_bt_tasks
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_item
     *  com_mt_value_channel
     *  cms_bt_product_cxxx
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_TASK)
    public AjaxResponse saveTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/delTask 删除库存隔离任务
     * @apiName delTask
     * @apiDescription 删除库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "已经开始隔离，删除失败", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check是否可以删除。如果这个任务在cms_bt_stock_separate_item表中存在状态<>0:未进行的隔离数据，则不允许删除。。
     *  2.删除cms_bt_stock_separate_platform_info表，cms_bt_stock_separate_item表，cms_bt_tasks表中对应的数据。
     * @apiExample 使用表
     *  cms_bt_tasks
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.DEL_TASK)
    public AjaxResponse delTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/search 检索
     * @apiName search
     * @apiDescription 检索
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} property1 属性1（品牌）
     * @apiParam (应用级参数) {String} property2 属性2（英文短描述）
     * @apiParam (应用级参数) {String} property3 属性3（性别）
     * @apiParam (应用级参数) {String} property4 属性4（SIZE）
     * @apiParam (应用级参数) {String} start1 库存隔离明细页检索开始Index
     * @apiParam (应用级参数) {String} length1 库存隔离明细页检索件数
     * @apiParam (应用级参数) {String} start2 实时库存状态页检索开始Index
     * @apiParam (应用级参数) {String} length2 实时库存状态页检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} codeNum 商品Code数
     * @apiSuccess (应用级返回字段) {String} skuNum Sku数
     * @apiSuccess (应用级返回字段) {String} readyNum 未进行数
     * @apiSuccess (应用级返回字段) {String} waitSeparationNum 等待隔离数
     * @apiSuccess (应用级返回字段) {String} separationOKNum 隔离成功数
     * @apiSuccess (应用级返回字段) {String} separationFailNum 隔离失败数
     * @apiSuccess (应用级返回字段) {String} waitRestoreNum 等待还原数
     * @apiSuccess (应用级返回字段) {String} restoreOKNum 还原成功数
     * @apiSuccess (应用级返回字段) {String} restoreFailNum 还原失败数
     * @apiSuccess (应用级返回字段) {String} changedNum 再修正数
     * @apiSuccess (应用级返回字段) {Object} propertyList 属性列表（json数组）
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台列表（json数组）
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccess (应用级返回字段) {Object} realStockList 实时库存状态（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "codeNum":5000,
     *   "skuNum":10000,
     *   "readyNum":1000,
     *   "waitSeparationNum":5000,
     *   "separationOKNum":1000,
     *   "separationFailNum":0,
     *   "waitRestoreNum":5000,
     *   "restoreOKNum":1000,
     *   "restoreFailNum":0,
     *   "changedNum":0,
     *   "propertyList": [ {"name":"品牌", "show":false},
     *                     {"name":"英文短描述", "show":false},
     *                     {"name":"性别", "show":false},]，
     *                     {"name":"Size", "show":false}
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际"},
     *                     {"cartId":"27", "cartName":"聚美优品"} ]，
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":30, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]，
     *   "realStockList": [ {"code":"35265465", "Sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":-1, },
     *                                                                          {"cart_id":"27", "separationQty":-1, }]}
     *                    ...]，
     *  }
     * }
     * 说明："qty":-1为动态。"separationQty":-1为动态。
     * propertyList是通过在com_mt_value_channel中通过配置来取得。（配置这个channel显示几个属性，每个属性在画面上的显示名称是什么）
     * @apiExample  业务说明
     *  1.根据任务ID，Code，Sku，状态和各个属性从cms_bt_stock_separate_item表检索库存隔离明细。（按一个sku一条记录，按sku进行分页）
     *  2.取得实时库存状态
     *    2.0 根据任务ID，Code，Sku，状态和各个属性从cms_bt_stock_separate_item表取得Sku信息。（按一个sku一条记录，按sku进行分页）
     *    2.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    2.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    2.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    2.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    2.5.可用库存数（realStockList.qty） = 2.1：取得逻辑库存 - （2.2：隔离库存数 + 2.3：增量隔离库存数 - 2.4：隔离平台的销售数量）
     *        平台销售数量(realStockList.platformStock.salesQty) = 2.4
     *        平台隔离数量(realStockList.platformStock.separationQty) = 2.2 + 2.3
     *   注：如果隔离结束的话，还原后，数据会移动到历史表里，这个时候取数据需要到cms_bt_stock_separate_item_history表和cms_bt_increment_stock_separate_item_history表里取得数据。
     *   对于已经结束的隔离任务（状态=CLOSE）来说，实时库存状态页只要显示出平台隔离数量
     *
     * @apiExample 使用表
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *  cms_bt_stock_separate_item_history
     *  cms_bt_increment_stock_separate_item_history
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SEARCH)
    public AjaxResponse search(@RequestBody Map param) {


        // 返回
        return success(null);
    }


    /**
     * @api {post} /cms/promotion/task_stock/getCommonStockList 获取库存隔离明细(翻页用)
     * @apiName getCommonStockList
     * @apiDescription 获取库存隔离明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} property1 属性1（品牌）
     * @apiParam (应用级参数) {String} property2 属性2（英文短描述）
     * @apiParam (应用级参数) {String} property3 属性3（性别）
     * @apiParam (应用级参数) {String} property4 属性4（SIZE）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 合计数
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":5000,
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":30, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]
     *  }
     * }
     * 说明："qty":-1为动态。
     * @apiExample  业务说明
     *  1.根据任务ID，Code，Sku和状态从cms_bt_stock_separate_item表检索库存隔离明细。（按一个sku一条记录，按sku进行分页）
     *  注：如果隔离结束的话，还原后，数据会移动到历史表里，这个时候取数据需要到cms_bt_stock_separate_item_history表里取得数据。
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *  cms_bt_stock_separate_item_history
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_COMMON_STOCK_LIST)
    public AjaxResponse getCommonStockList(@RequestBody Map param) {


        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getRealStockInfo 获取实时库存状态(翻页用)
     * @apiName getRealStockInfo
     * @apiDescription 获取实时库存状态
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} property1 属性1（品牌）
     * @apiParam (应用级参数) {String} property2 属性2（英文短描述）
     * @apiParam (应用级参数) {String} property3 属性3（性别）
     * @apiParam (应用级参数) {String} property4 属性4（SIZE）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 合计数
     * @apiSuccess (应用级返回字段) {Object} realStockList 实时库存状态（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":10000,
     *   "realStockList": [ {"code":"35265465", "Sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":-1, },
     *                                                                          {"cart_id":"27", "separationQty":-1, }]},
     *                    ...]
     *  }
     * }
     * 说明："separationQty":-1为动态。
     * @apiExample  业务说明
     *  1.取得实时库存状态
     *    1.0 根据任务ID，Code，Sku，状态和各个属性从cms_bt_stock_separate_item表取得Sku信息。（按一个sku一条记录，按sku进行分页）
     *    2.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    2.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    2.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    2.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    2.5.可用库存数（realStockList.qty） = 2.1：取得逻辑库存 - （2.2：隔离库存数 + 2.3：增量隔离库存数 - 2.4：隔离平台的销售数量）
     *   注：如果隔离结束的话，还原后，数据会移动到历史表里，这个时候取数据需要到cms_bt_stock_separate_item_history表和cms_bt_increment_stock_separate_item_history表里取得数据。
     *   对于已经结束的隔离任务（状态=CLOSE）来说，实时库存状态页只要显示出平台隔离数量
     * @apiExample 使用表
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *  cms_bt_stock_separate_item_history
     *  cms_bt_increment_stock_separate_item_history
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_REAL_STOCK_INFO)
    public AjaxResponse getRealStockInfo(@RequestBody Map param) {


        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/initNewRecord 新建一条新隔离明细前初始化操作（取得隔离平台）
     * @apiName initNewRecord
     * @apiDescription 新建一条新隔离明细前初始化操作（取得隔离平台）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台信息（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际"},
     *                     {"cartId":"27", "cartName":"聚美优品"} ]
     *  }
     * }
     * @apiExample  业务说明
     *  1.根据任务id，从cms_bt_stock_separate_platform_info取得隔离平台的信息。
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.INIT_NEW_RECORD)
    public AjaxResponse initNewRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getUsableStock 取得可用库存
     * @apiName getUsableStock
     * @apiDescription 取得可用库存
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} sku Sku
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Integer} stockNum 可用库存数
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "stockNum":10000
     *  }
     * }
     * @apiExample  业务说明
     *  按下面的逻辑计算出可用库存数
     *  1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *  2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *  3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *  4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *  5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *
     * @apiExample 使用表
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_USABLE_STOCK)
    public AjaxResponse getUsableStock(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/saveNewRecord 保存新增库存隔离明细
     * @apiName saveNewRecord
     * @apiDescription 保存新增库存隔离明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} usableStock 可用库存
     * @apiParam (应用级参数) {Object} platformStockList 平台隔离库存（json数组）
     * @apiParamExample  platformStockList示例
     *   "platformList": [ {"cartId":"23", "qty":10},
     *                     {"cartId":"24", "qty":20}，
     *                     {"cartId":"27", "qty":，"dynamic":true}]
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  将增加的隔离明细数据插入到cms_bt_stock_separate_platform_info表。
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_NEW_RECORD)
    public AjaxResponse saveNewRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/importSkuInfo 批量导入Sku
     * @apiName importSkuInfo
     * @apiDescription 批量导入Sku
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {Object} file 导入文件
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "第1行数据格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  从任务一览新建好隔离任务之后，需要进行批量Sku导入。
     *  1.进行导入Sku的文件Check。
     *      1.1 Code和Sku都不能为空白。
     *      1.2 从com_mt_value_channel表（type=61）取得有几个属性（例如 N个），属性名是多少。check属性名和第一行第两列开始是否匹配。
     *      1.3 第一行的第N+2列之后（平台信息）必须和任务所对应的平台匹配。（N由1.2计算出来）
     *      1.4 第二行开始，第N+2列之后只要有内容只，则内容必须是"动态"。
     *  2.按导入的Sku，取得可用库存，计算隔离库存后，插入到cms_bt_stock_separate_item表。（内容为"动态"的数据不插入）
     *    2.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    2.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    2.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    2.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    2.5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *    2.6.隔离平台隔离库存 = 可用库存数 * 设定的百分比     *
     *  注：当这个任务下面没有明细的情况下，可以进行导入Sku操作。
     *  导入文件示例
     *  Code	    Sku	            品牌	    Name(英文)	            性别	SIZE	天猫	京东
     *  302370-013	302370-013-10	Air Jordan	Air Jordan IX (9) Retro	Women	10	    	    动态
     *  302370-013	302370-013-10.5	Air Jordan	Air Jordan IX (9) Retro	Women	10.5
     *  302370-013	302370-013-11	Air Jordan	Air Jordan IX (9) Retro	Man   	11	    动态
     * @apiExample 使用表
     *  com_mt_value_channel
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.IMPORT_SKU_INFO)
    public AjaxResponse importSkuInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    /**
     * @api {post} /cms/promotion/task_stock/importStockInfo 批量导入库存明细
     * @apiName importStockInfo
     * @apiDescription 批量导入库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {Object} file 导入文件
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "第1行数据格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.系统时间已经超过这个任务中最晚的平台活动开始时间的话，不能进行导入。
     *  2.进行导入Sku的文件Check。
     *      2.1 Code和Sku都不能为空白。
     *      2.2 根据渠道id从com_mt_value_channel表（type=61）取得有几个属性（例如 N个）
     *      2.3 第一行的第N+2列之后（平台信息）必须和任务所对应的平台匹配。（N由1.2计算出来）
     *      2.4 第二行开始，第N+2列之后，内容必须是"动态"，或者是大于0的整数。
     *      2.5 导入方式：增量方式。
     *               check文件中的所有sku在cms_bt_stock_separate_item表中不存在
     *          导入方式：变更方式。
     *               check文件中的所有sku在cms_bt_stock_separate_item表中存在
     *
     *  3.将导入的库存隔离数据插入到cms_bt_stock_separate_item表。
     *    导入方式：增量方式。（只插入数据，导入后状态为"0：未进行"）
     *    导入方式：变更方式。（只有文件内容和DB的实际值发生变化时才修改数据和状态，
     *                          导入前状态为"7：再修正"和"2 隔离成功"以外，则导入后状态为"0：未进行"，导入前状态为"2 隔离成功"，则导入后状态为"7：再修正"）
     *    注：系统时间已经超过导入平台的活动开始时间，则不进行导入。
     *
     *  导入文件示例
     *  Code	    Sku	            品牌	    Name(英文)	            性别	SIZE	可用库存	天猫	京东	其他
     *  302370-013	302370-013-10	Air Jordan	Air Jordan IX (9) Retro	Women	10	    100      	50	    30	    动态
     *  302370-013	302370-013-10.5	Air Jordan	Air Jordan IX (9) Retro	Women	10.5	200	        100	    60	    动态
     *  302370-013	302370-013-11	Air Jordan	Air Jordan IX (9) Retro	Man   	11	    100	        动态	50	    动态
     * 说明：设置动态的隔离平台不插入数据，某个平台下面的隔离库存可以设置为0。
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.IMPORT_STOCK_INFO)
    public AjaxResponse importStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/exportStockInfo 批量导出
     * @apiName exportStockInfo
     * @apiDescription 批量导出
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiSuccess (系统级返回字段) {String} statusCode HttpStatus（eg:200:"OK"）
     * @apiSuccess (系统级返回字段) {byte[]} byte 导出的文件流
     * @apiExample  业务说明
     *  根据渠道id从com_mt_value_channel表（type=61）取得有几个属性（例如 N个），和属性的名称
     *  根据任务id以Sku为单位从cms_bt_stock_separate_item表导出库存隔离数据。
     *
     *  导出文件示例
     *  Code	    Sku	            品牌	    Name(英文)	            性别	SIZE	可用库存	天猫	京东	其他
     *  302370-013	302370-013-10	Air Jordan	Air Jordan IX (9) Retro	Women	10	    100      	50	    30	    动态
     *  302370-013	302370-013-10.5	Air Jordan	Air Jordan IX (9) Retro	Women	10.5	200	        100	    60	    动态
     *   302370-013	302370-013-11	Air Jordan	Air Jordan IX (9) Retro	Man   	11	    100	        动态	50	    动态
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.EXPORT_STOCK_INFO)
    public ResponseEntity exportStockInfo(@RequestBody Map param) {

        // 返回
        return genResponseEntityFromBytes("fileName", new byte[]{});
    }

    /**
     * @api {post} /cms/promotion/task_stock/executeStockSeparation 启动/重刷库存隔离
     * @apiName executeStockSeparation
     * @apiDescription 启动/重刷库存隔离
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} sku Sku （不存在的场合，进行所有隔离）
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  只有状态为"0:未进行"，"3:隔离失败"，"7:再修正"的数据可以进行隔离库存操作。
     *  根据参数sku进行判断，不存在的场合，对所有sku进行进行隔离。否则对指定sku进行库存隔离。
     *  启动隔离后，将状态变为"1:等待隔离"。
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.EXECUTE_STOCK_SEPARATION)
    public AjaxResponse executeStockSeparation(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/executeStockRestore 还原库存隔离
     * @apiName executeStockRestore
     * @apiDescription 还原库存隔离
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} sku Sku （不存在的场合，进行所有还原）
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  只有状态为"2:隔离成功"，"6:还原失败"的数据可以进行还原库存隔离操作。
     *  根据参数sku进行判断，不存在的场合，对所有sku进行还原库存隔离。否则对指定sku进行还原库存隔离。
     *  启动还原库存隔离后，将状态变为"4:等待还原"。
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.EXECUTE_STOCK_RESTORE)
    public AjaxResponse executeStockRestore(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/saveSkuRecord 保存一条Sku隔离库存明细
     * @apiName saveSkuRecord
     * @apiDescription 保存一条Sku隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {Object} stockInfo（json数据） 一条Sku的隔离明细
     * @apiParamExample  stockInfo参数示例
     * {
     *   "taskId":1,
     *   "stockInfo":  {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]}
     * }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "stockInfo": {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"再修正"},
     *                                                                          {"cartId":"27", "qty":10, "status":"再修正"}]}
     *  }
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "隔离库存格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。各个隔离平台的隔离库存必须是大于0的整数。（动态的除外，status为空白的，隔离库存可以设置为-1）。
     *  2.如果修改的隔离库存<>cms_bt_stock_separate_item表里对应的值，那么更新cms_bt_stock_separate_item表的值。
     *    （导入前状态为"7：再修正"和"2 隔离成功"以外，则导入后状态为"0：未进行"，导入前状态为"2 隔离成功"，则导入后状态为"7：再修正"）
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_SKU_RECORD)
    public AjaxResponse saveSkuRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/saveSkuRecord 保存一条Sku隔离库存明细
     * @apiName saveSkuRecord
     * @apiDescription 保存一条Sku隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {Object} stockList（json数组） 隔离明细
     * @apiParamExample  stockList参数示例
     * {
     *   "taskId":1,
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]
     * }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{,
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"再修正"},
     *                                                                          {"cartId":"27", "qty":10, "status":"再修正"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]
     *  }
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "隔离库存格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。各个隔离平台的隔离库存必须是大于0的整数。（动态的除外，status为空白的，隔离库存可以设置为-1）。
     *  2.如果修改的隔离库存<>cms_bt_stock_separate_item表里对应的值，那么更新cms_bt_stock_separate_item表的值。
     *    （导入前状态为"7：再修正"和"2 隔离成功"以外，则导入后状态为"0：未进行"，导入前状态为"2 隔离成功"，则导入后状态为"7：再修正"）
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_ALL_RECORD)
    public AjaxResponse saveAllRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/delRecord 删除一条Sku相关的隔离库存明细
     * @apiName delRecord
     * @apiDescription 删除一条Sku相关的隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {Object} stockInfo（json数据） 一条Sku的隔离明细
     * @apiParamExample  stockInfo参数示例
     * {
     *   "taskId":1,
     *   "stockInfo":  {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"未进行"},
     *                                                                          {"cartId":"27", "qty":10, "status":"未进行"}]}
     * }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "删除失败（库存隔离后不能进行删除）", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  如果选择的Sku对应的平台中，有发生过库存隔离的（状态<>0:未进行），则删除失败。否则删除这个sku的所有平台的隔离数据。
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.DEL_RECORD)
    public AjaxResponse delRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getSkuSeparationDetail 获取某个Sku的所有隔离详细
     * @apiName getSkuSeparationDetail
     * @apiDescription 获取某个Sku的所有隔离详细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object} stockHistoryList 库存隔离历史明细（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "code":"35265465",
     *   "sku":"256354566-9",
     *   "stockHistoryList": [ {"type":"一般库存隔离", "taskName":"天猫双11任务", "qty":10 },
     *                         {"type":"增量库存隔离", "taskName":"天猫双11-增量1", "qty":1 },
     *                         {"type":"增量库存隔离", "taskName":"天猫双11-增量2", "qty":2 },
     *                    ...]
     *  }
     * }
     * 说明："type"：隔离类型;"taskName"：隔离/增量任务名；"qty":隔离/增量数量
     * @apiExample  业务说明
     *  1.根据任务id，平台id，Sku从cms_bt_stock_separate_item表里检索一般库存隔离的数据
     *  2.根据任务id，平台id，Sku从cms_bt_increment_stock_separate_item表里检索增量库存隔离的数据
     *  注：如果隔离结束的话，还原后，数据会移动到历史表里，这个时候取数据需要到cms_bt_stock_separate_item_history表和cms_bt_increment_stock_separate_item_history里取得数据。
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *  cms_bt_stock_separate_item_history
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_increment_stock_separate_item_history
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_SKU_SEPARATION_DETAIL)
    public AjaxResponse getSkuSeparationDetail(@RequestBody Map param) {

        // 返回
        return success(null);
    }

//    /**
//     * @api {post} /cms/promotion/task_stock/exportRealStockInfo 批量库存实时状态
//     * @apiName exportRealStockInfo
//     * @apiDescription 批量库存实时状态
//     * @apiGroup promotion
//     * @apiVersion 0.0.1
//     * @apiPermission 认证商户
//     * @apiParam (系统级参数) {String} channelId 渠道id
//     * @apiParam (应用级参数) {String} taskId 任务id
//     * @apiSuccess (系统级返回字段) {String} statusCode HttpStatus（eg:200:"OK"）
//     * @apiSuccess (系统级返回字段) {byte[]} byte 导出的文件流
//     * @apiExample  业务说明
//     *  根据渠道id从com_mt_value_channel表（type=61）取得有几个属性（例如 N个），和属性的名称
//     *  根据任务id以Sku为单位从cms_bt_stock_separate_item表导出库存隔离数据。
//     *
//     *  导出文件示例
//     *  Code	    Sku	            品牌	    Name(英文)	            性别	SIZE	可用库存	天猫	京东	其他
//     *  302370-013	302370-013-10	Air Jordan	Air Jordan IX (9) Retro	Women	10	    100      	50	    30	    动态
//     *  302370-013	302370-013-10.5	Air Jordan	Air Jordan IX (9) Retro	Women	10.5	200	        100	    60	    动态
//     *   302370-013	302370-013-11	Air Jordan	Air Jordan IX (9) Retro	Man   	11	    100	        动态	50	    动态
//     * @apiExample 使用表
//     *  cms_bt_stock_separate_item
//     *  com_mt_value_channel
//     *
//     */
//    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.EXPORT_REAL_STOCK_INFO)
//    public AjaxResponse exportRealStockInfo(@RequestBody Map param) {
//
//        // 返回
//        return success(null);
//    }

    /**
     * @api {post} /cms/promotion/task_stock/exportStockInfo 批量导出
     * @apiName exportStockInfo
     * @apiDescription 批量导出
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiSuccess (系统级返回字段) {String} statusCode HttpStatus（eg:200:"OK"）
     * @apiSuccess (系统级返回字段) {byte[]} byte 导出的文件流
     * @apiExample  业务说明
     *  根据任务id以Sku为单位从cms_bt_stock_separate_item表和cms_bt_increment_stock_separate_item表导出库存隔离数据。（按发生时间升序）
     *  导出文件示例
     *  Code	    Sku	            平台	隔离类型	错误信息	发生时间
     *  302370-013	302370-013-10	天猫	一般	    XXXXXXXXXXX	2016/2/19 00:00:13
     *  302370-013	302370-013-10.5	天猫	一般	    XXXXXXXXXXX	2016/2/19 00:00:14
     *  302370-013	302370-013-10.5	京东	增量	    XXXXXXXXXXX	2016/2/19 00:00:15
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.EXPORT_ERROR_INFO)
    public ResponseEntity exportErrorInfo(@RequestBody Map param) {

        // 返回
        return genResponseEntityFromBytes("fileName", new byte[]{});
    }
}