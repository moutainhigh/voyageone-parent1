/**
 * @description 平台默认属性设置一览
 * @date 2016-8-11
 */
define(['cms', 'underscore', 'modules/cms/controller/popup.ctl'], function (cms, _) {

    cms.controller('DefaultAttributeController', (function () {

        function DefaultAttributeController(popups, alert, confirm, menuService, $productDetailService, platformMappingService) {

            var self = this;

            menuService.getPlatformType().then(function (resp) {
                var cartList = _.filter(resp, function (cart) {
                    return cart.value != 0 && cart.value != 1
                });
                self.cartList = cartList;
                var cartMap = {};
                _.each(cartList, function (cart) {
                    cartMap[cart.value] = cart.name;
                });
                self.cartMap = cartMap;
            });

            self.$productDetailService = $productDetailService;
            self.platformMappingService = platformMappingService;

            self.popups = popups;
            self.alert = alert;
            self.confirm = confirm;
            self.searchInfo = {
                cartId: null,
                categoryType: 3,
                categoryId: null
            };
            self.paging = {
                curr: 1, total: 0, fetch: function () {
                    self.search();
                }
            };
            // first
            self.search();
        }

        DefaultAttributeController.prototype.openCategorySelector = function () {

            var self = this,
                $productDetailService = self.$productDetailService;

            $productDetailService.getPlatformCategories({cartId: self.searchInfo.cartId}).then(function (resp) {

                var categoryList = resp.data;

                if (!categoryList || !categoryList.length) {
                    self.alert("数据还未准备完毕");
                    return;
                }

                self.popups.popupNewCategory({
                    from: "",
                    categories: categoryList,
                    divType: ">",
                    canSelectParent: true
                }).then(function (context) {
                    self.searchInfo.categoryPath = context.selected.catPath;
                    self.searchInfo.categoryId = context.selected.catId;
                });
            });
        };

        DefaultAttributeController.prototype.clear = function () {
            var self = this;
            self.searchInfo = {
                cartId: null,
                categoryType: 3,
                categoryId: null
            };
        };

        DefaultAttributeController.prototype.search = function () {
            var self = this,
                searchInfo = self.searchInfo,
                paging = self.paging;

            self.platformMappingService.page({
                pageIndex: paging.curr - 1,
                pageRowCount: paging.size,
                parameters: {
                    cartId: !searchInfo.cartId ? null : +searchInfo.cartId,
                    categoryType: +searchInfo.categoryType,
                    categoryPath: searchInfo.categoryPath
                }
            }).then(function (resp) {
                paging.total = resp.data.total;
                self.dataList = resp.data.list;
            });
        };

        DefaultAttributeController.prototype.deleteItem = function (item) {
            var self = this;
            self.confirm("TXT_MSG_DELETE_ITEM").then(function () {
                self.platformMappingService.delete(item).then(function (resp) {
                    if (resp.data) {
                        self.alert("TXT_MSG_DELETE_SUCCESS").then(function () {
                            self.search();
                        });
                    } else {
                        self.alert("TXT_MSG_DELETE_FAIL");
                    }
                });
            });
        };

        DefaultAttributeController.prototype.create = function () {
            open("#/channel/default_attribute_detail/");
        };

        DefaultAttributeController.prototype.editItem = function (item) {
            var id = item._id;
            localStorage.setItem(id, angular.toJson(item));
            open("#/channel/default_attribute_detail/" + id);
        };

        return DefaultAttributeController;
    })())
});