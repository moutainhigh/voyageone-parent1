/**
 * @Name:    translate_wms_zn.js
 * @Date:    2015/06/10
 *
 * @User:    sky
 * @Version: 0.0.2
 */

define(['components/app', 'components/services/language.service', "underscore"], function (app) {
    app.config(["$translateProvider", "languageType",
        function ($translateProvider, languageType) {
            $translateProvider.translations(languageType.zh,
                {
                    WMS_CREATE: '创建',
                    WMS_DELETE: '删除',
                    WMS_CANCEL: '取消',
                    WMS_EDIT: '编辑',
                    WMS_ADD: '添加',
                    WMS_CLOSE: '关闭',
                    WMS_CHANGE: '变更',
                    WMS_SUBMIT: '提交',
                    WMS_SAVE: '保存',
                    WMS_COMPARE: '比较',
                    WMS_BACK: '返回',
                    WMS_SYNCINVENTORY: '同步库存',
                    WMS_REOPEN: '重开',

                    WMS_STORE: '仓库',
                    WMS_SEARCH: '搜索',
                    WMS_LASTUPDATEUSER: '最后变更者',
                    WMS_LASTUPDATETIME: '最后变更时间',
                    WMS_OPERATION: '操作',
                    WMS_ADDITEMTOBACKORDER: '添加超卖',
                    WMS_ORDERNUMBER: '订单号',
                    WMS_ClIENTID: '品牌方ID',
                    WMS_RESID: 'ResId',
                    WMS_SKU: 'SKU',
                    WMS_CLIENT_SKU: '品牌方SKU',
                    WMS_STATUS: '状态',
                    WMS_FROM: 'From',
                    WMS_TO: 'To',
                    WMS_TYPE: '类型',
                    WMS_SCAN: '扫码',
                    WMS_RELABEL: '重打',
                    WMS_PRINT: '打印',
                    WMS_CHANNEL: '渠道',
                    WMS_SHIP: '发货',
                    WMS_DESCRIPTION: '描述',
                    WMS_CREATETIME: '创建时间',
                    WMS_MODIFYTIME: '更新时间',
                    WMS_DLPICKUPITEMSBYRESVERTION: '下载可捡货清单（按物品）',
                    WMS_DLPICKUPITEMSBYSKU: '下载可捡货清单（按SKU）',
                    WMS_NEWEDITSESSION: '新建/编辑 Session',
                    WMS_SESSIONLIST: 'Session 列表',
                    WMS_CONDITION: '商品好坏',
                    WMS_REASON: '退货原因',
                    WMS_EXPRESSCOMPANY: '快递公司',
                    WMS_TRACKINGNO: '运单号',
                    WMS_NOTE: '备注',
                    WMS_SESSIONNAME: 'Session 名称',
                    WMS_CLOSESESSION: '关闭 Session',
                    WMS_PRODUCTNAME: '商品名称',
                    WMS_BARCODE: 'Barcode',
                    WMS_CREATEBY: '创建者',
                    WMS_DETAIL: '详情',
                    WMS_LOCK: '锁单',
                    WMS_IDCARD: '身份证',
                    WMS_SESTATUS: 'OMS 状态',
                    WMS_SHOP: '店铺',
                    WMS_WORKLOAD: 'Work Load',
                    WMS_SYNSHIPNO: 'SynShipNo',
                    WMS_WEBORDERID: '平台订单号',
                    WMS_ORDERTIME: '下单时间',
                    WMS_BACKORDER: '超卖',
                    WMS_OPEN: 'Open',
                    WMS_CONFIRMED: 'Confirmed',
                    WMS_RESERVATIONLOG: 'Reservation 日志',
                    WMS_INVENTORYINFO: '库存详情',
                    WMS_INVENTORY: '库存',
                    WMS_PRODUCTCODE: 'Product Code',
                    WMS_NEWORDER: 'New Order',
                    WMS_TOTAL: '合计',
                    WMS_SKUHIS: 'SKU 库存变更详情',
                    WMS_DATE: '日期',
                    WMS_PONUMBER: '入出库单号',
                    WMS_NEWTRANSFEROUT: '新建出库',
                    WMS_NEWTRANSFERIN: '新建入库',
                    WMS_NEWPURCHASEORDER: '新建采购',
                    WMS_NEWWITHDRAWAL: '新建退库',
                    WMS_NAME: '名称',
                    WMS_LOCATIONNAME: '货架名称',
                    WMS_DOWNLOAD: '下载',
                    WMS_DOWNLOADO: '下载 1',
                    WMS_DOWNLOADT: '下载 2',
                    WMS_TRANSFER: 'Transfer',
                    WMS_TRANSFERNAME: '入出库名',
                    WMS_TARGETOUT: '关联的 Transfer Out',
                    WMS_LOCATION: '货架',
                    WMS_ADDLOCATION: '添加货架',
                    WMS_PLEASESAVETRANSFERFIREST: '请先保存。',
                    WMS_NODATA: '无对应数据.',
                    WMS_QTY: '数量',
                    WMS_PACKAGENAME: 'Container Name',
                    WMS_CLOSEPACKAGE: '封箱',
                    WMS_ACCCEPT: '接受',
                    WMS_LOCATIONBIND: '货架绑定',
                    WMS_LOGS: '日志',
                    WMS_ITEMINFO: '产品信息',
                    WMS_COLOR: '颜色',
                    WMS_MSRP: '官方建议售价',
                    WMS_PLEASESELECTATYPEFORPRODUCT: '请先保存新建的商品，或选择一个商品类型',
                    WMS_TRANSFER_TYPE: '单据类型',
                    WMS_FIXED: 'Fixed',
                    WMS_ISFIXED: 'Is Fixed',
                    WMS_QTYOFFSETS: '库存偏移量',
                    WMS_RESCAN: '重新扫码',
                    WMS_NEWSECTION: '新添货架',
                    WMS_NEWSESSION: '创建 Session',
                    WMS_VIEW: '查看',
                    WMS_STOCKTAKETYPE: '盘点类型',
                    WMS_STOCKTAKEQRY: '盘点数量',
                    WMS_CURQTY: '当前物理库存',
                    WMS_ORIGIN: '库存变化原因',
                    WMS_UPC_NOTFOUNDSIZES: '找不到任何尺码',
                    WMS_ORDERNUMBERANDTRANSFERNAME: '订单号/入出库名',
                    WMS_RECORDNUMBER: '全部记录数 : ',
                    WMS_RETURNTYPE: '退货类型',
                    WMS_SIZE: '尺寸',
                    WMS_ITEM_CODE: '型号',

                    // 弹出提示框的内容翻译部分
                    WMS_ALERT_UPC_MSRP: "官方建议售价，必须是数字！",
                    WMS_ALERT_UPC_PRODUCT: "商品类型无效！",
                    WMS_ALERT_UPC_MSRPGREATTHEN0: "官方建议售价，至少要大于0",

                    WMS_ALERT_TRANSFER_DELETE: "是否确定删除 Transfer： [ {{transfer_name}} ]？",
                    WMS_ALERT_TRANSFER_DELETE_FAIL: "删除失败了。请检查以下 Transfer 的当前状态。",

                    WMS_ALERT_TRANSFER_EDIT_NO_TYPE: "没有找到对应的 Transfer 类型，所以不能进行创建",
                    WMS_ALERT_TRANSFER_EDIT_CLOSED: "Transfer 已经关闭了！",
                    WMS_TRANSFER_EDIT_NO_CODE: "必须输入 code！",
                    WMS_TRANSFER_EDIT_NUM_VALID:"数字必须大于0！",
                    WMS_TRANSFER_EDIT_NO_ITEM: "没有找到任何东西。",
                    WMS_TRANSFER_EDIT_CANCEL: "是否确定放弃保存 Transfer？",
                    WMS_TRANSFER_EDIT_PKG_CLOSED: "Container 已经关闭了！",
                    WMS_TRANSFER_EDIT_PKG_DEL: "是否确定删除 Container [ {{transfer_package_name}} ]？",
                    WMS_TRANSFER_EDIT_NO_TYPE: "找不到对应的 Transfer 类型",
                    WMS_TRANSFER_EDIT_PKG_REOPEN: "是否确定重新打开 Container [ {{transfer_package_name}} ]？",

                    WMS_TRANSFER_EDIT_NAME_VALID: "Transfer 的名称不能为空！",
                    WMS_TRANSFER_EDIT_MAP_VALID: "必须为入库单匹配一个对应的出库单！",
                    WMS_TRANSFER_EDIT_PO_VALID: "入出库单号不能为空！",
                    WMS_TRANSFER_EDIT_FROM_VALID: "必须选择一个出货仓！",
                    WMS_TRANSFER_EDIT_TO_VALID: "必须选择一个入货仓！",

                    WMS_STOCK_TAKE_SESSION_STATUS_UNVALID: "本次盘点的状态必须是 [进行中]！",
                    WMS_STOCK_TAKE_SESSION_DEL: "是否确定删除当前的盘点？",
                    WMS_STOCK_TAKE_NEW_FAIL: "新建盘点失败，必须要选择一个仓库！",

                    WMS_STOCK_TAKE_TO_STOCK: "是否确定把盘点状态改变到 [存档]？",
                    WMS_STOCK_TAKE_SECTION_DEL: "是否确定删除当前这个部分的盘点内容？",

                    WMS_STOCK_TAKE_SECTION_CLOSE: "是否确定要把当前这部分的盘点内容锁定？",
                    WMS_STOCK_TAKE_SECTION_RESCAN: "是否确定要重新扫描这部分的盘点内容？",

                    WMS_STOCK_TAKE_SESSION_FIX: "是否确定修正库存？",

                    WMS_RETURN_SAVE: "是否确定保存你所做的修改？",
                    WMS_RETURN_TO_REFUNDED: "是否确定把状态改为 Refunded。",
                    WMS_RETURN_NO_SESSION: "请选择一个 Session！",
                    WMS_RETURN_SESSION_CLOSE: "是否确定关闭当前的 Session？",
                    WMS_RETURN_ROW_DEL: "是否确定删除这一行？",

                    WMS_INVENTORY_SYNC: "是否确定同步库存到各平台？",

                    WMS_PICKUP_STORE_UN_VALID: "请选择仓库！",
                    WMS_PICKUP_RELABEL: '是否确定重新打印 {{scanTypeName}} [ {{scanNo}} ] 的捡货单？',
                    WMS_PICKUP_TYPE_UN_VALID: "请输入{{scanTypeName}}！",
                    WMS_PICKUP_PERMIT: "没有权限捡货 !",
                    WMS_RECEIVE_PERMIT: "没有权限收货 !",

                    WMS_LOCATION_NOT_FOUND:"没找到货架！",
                    WMS_LOCATION_DEL: "是否确定删除货架？",
                    WMS_LOCATION_ADD_FAIL: '新增货架失败，请先选择仓库！',
                    WMS_LOCATION_NAME_UN_VALID: '货架名称无效！',
                    WMS_LOCATION_CODE_UN_VALID: '条形码或商品代码无效！',
                    WMS_LOCATION_SEARCH: '请先搜索一个商品！',

                    WMS_BACK_DEL: '是否确定删除 [ {{ sku }} ]？',
                    WMS_BACK_STORE_UA: '新增失败，请选择仓库！',
                    WMS_BACK_SKU_UA: '请先输入 SKU！',

                    // Notify 提示内容
                    WMS_NOTIFY_OP_SUCCESS: "操作成功",
                    WMS_NOTIFY_OP_FAIL: "操作失败",

                    WMS_NOTIFY_UPC_CODE: "请输入 code。",
                    WMS_NONEXISTENT_ORDERNUMBER: "不存在的订单号！",
                    WMS_CAN_NOT_FIND_OBJECT: "您输入的检索条件无法找到对应的记录，或者对应的记录已经被return！",
                    WMS_MUST_ENTER_ORDERNUMBER: '您必须输入订单号！',
                    WMS_NOT_TO_CALCULATE_INVENTORY: '( 当前 session return 内容不计入库存! )',
                    WMS_INPROCESS: 'In Process',

                    //报表
                    WMS_INVENTORY_DETAIL_REPORT: '库存变更详情报表',
                    WMS_STOCKTAKE_COMPARERES_REPORT: '盘点比较结果报表',
                    WMS_INCLUDING_RETURN_STORE: '包含退货仓库'
                });
        }]);
});
