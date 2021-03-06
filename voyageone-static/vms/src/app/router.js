define(function () {
    return {
        otherwise: '/home',
        routes: [
            {
                "hash": "/home",
                "templateUrl": "views/home/index.html",
                "controllerUrl": "./views/home/index.controller",
                "controller": "HomeController as ctrl"
            },
            {
                "hash": "/order/order_info",
                "templateUrl": "views/order/order_info.html",
                "controllerUrl": "./views/order/order_info.controller",
                "controller": "OrderInfoController as ctrl"
            },
            {
                "hash": "/feed/product_feed_upload",
                "templateUrl": "./views/feed/fileUpload/index.html",
                "controllerUrl": "./views/feed/fileUpload/index.controller",
                "controller": "FeedFileUploadController as ctrl"
            },
            {
                "hash": "/feed/product_feed_import_status",
                "templateUrl": "./views/feed/feedImport/index.html",
                "controllerUrl": "./views/feed/feedImport/index.controller",
                "controller": "FeedImportResultController as ctrl"
            },
            {
                "hash": "/feed/product_feed_search",
                "templateUrl": "./views/feed/feedSearch/index.html",
                "controllerUrl": "./views/feed/feedSearch/index.controller",
                "controller": "FeedInfoSearchController as ctrl"
            },
            {
                "hash": "/shipment/shipment_info",
                "templateUrl": "./views/shipment/shipment_info.html",
                "controllerUrl": "./views/shipment/shipment_info.controller",
                "controller": "ShipmentInfoController as ctrl"
            },
            {
                "hash": "/shipment/shipment_info/shipment_detail/:shipmentId",
                "templateUrl": "./views/shipment/shipment_detail.html",
                "controllerUrl": "./views/shipment/shipment_detail.controller",
                "controller": "ShipmentDetailController as ctrl"
            },
            {
                "hash": "/inventory/inventory_feed_upload",
                "templateUrl": "./views/inventory/index.html",
                "controllerUrl": "./views/inventory/index.controller",
                "controller": "InventoryFileUploadController as ctrl"
            },
            {
                "hash": "/reports/financial_report",
                "templateUrl": "./views/reports/index.html",
                "controllerUrl": "./views/reports/index.controller",
                "controller": "FinancialReportController as ctrl"
            },
            {
                "hash": "/settings/vendor_settings",
                "templateUrl": "./views/settings/vendor_settings.html",
                "controllerUrl": "./views/settings/vendor_settings.controller",
                "controller": "VendorSettingsController as ctrl"
            },
            {
                "hash": "/inventory/inventory_import_status",
                "templateUrl": "./views/inventory/inventoryImport/index.html",
                "controllerUrl": "./views/inventory/inventoryImport/index.controller",
                "controller": "InventoryImportResultController as ctrl"
            }
        ]
    };
});