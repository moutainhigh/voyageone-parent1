/**
 * Created by rex.wu on 2016/11/24.
 */
define([
           "cms",
           "modules/cms/controller/popup.ctl"
       ], function (cms) {
           cms.controller("combinedProductController", (function () {

               function CombinedProductController($scope, combinedProductService, popups, confirm) {
                   $scope.vm = {
                       config:{
                           open:true
                       },
                       carts:{},
                       products:[],
                       productPageOption: {page: 1, total: 0, fetch: goPage.bind(this)},
                       statuses:{},
                       platformStatuses:{},
                       searchBean : {
                           cartId:""
                       }
                   };
                   //跳转指定页
                   function goPage(page, pageSize) {
                       var pageParameter = angular.copy($scope.vm.searchBean);
                       pageParameter.page = page;
                       pageParameter.pageSize = pageSize;
                       combinedProductService.search(pageParameter).then(function (res) {
                           $scope.vm.products = res.data.products == null ? [] : res.data.products;
                           $scope.vm.productPageOption.total = res.data.total;
                       }, function (res) {
                       })
                   }

                   /**
                    * 分页处理product数据
                    */
                   function getProductList() {
                       var statusesObj = _.pick($scope.vm.searchBean.statuses, function (value, key, object) {
                           return value;
                       });
                       var statuses = _.keys(statusesObj);
                       var platformStatusesObj = _.pick($scope.vm.searchBean.platformStatuses, function (value, key, object) {
                           return value;
                       });
                       var platformStatuses = _.keys(platformStatusesObj);
                       var copy_searchBean = angular.copy($scope.vm.searchBean);
                       copy_searchBean.statuses = statuses;
                       copy_searchBean.platformStatuses = platformStatuses;
                       combinedProductService.search(copy_searchBean, $scope.vm.productPageOption)
                           .then(function (resp) {
                               $scope.vm.products = resp.data.products == null ? [] : resp.data.products;
                               $scope.vm.productPageOption.total = resp.data.total;
                           });
                   }

                   $scope.initialize = function () {
                       combinedProductService.init().then(function (resp) {
                           $scope.vm.carts = resp.data.carts == null ? {} : resp.data.carts;
                           $scope.vm.statuses = resp.data.statuses == null ? {} : resp.data.statuses;
                           $scope.vm.platformStatuses = resp.data.platformStatuses == null ? {} : resp.data.platformStatuses;
                       });
                       getProductList();
                   };

                   $scope.popCombinedProduct = function () {
                       popups.popNewCombinedProduct({"carts":$scope.vm.carts}).then(function () {
                           getProductList();
                       });
                   };
                   
                   $scope.search = function () {
                       $scope.vm.productPageOption.page = 1;
                       $scope.vm.productPageOption.pageSize = 10;
                       getProductList();
                   };

                   $scope.clear = function () {
                       $scope.vm.searchBean = {};
                   };
                   
                   // 删除组合套装商品
                   $scope.deleteCombinedProduct = function (product) {
                       if (!product) {
                           return;
                       }
                       confirm("是否确认删除该组合套装商品？").then(function () {
                           combinedProductService.delete(product).then(function () {
                               getProductList();
                           });
                       });
                   };
                   // 编辑组合套装商品
                   $scope.popEditCombinedProduct = function (product) {

                       popups.popEditCombinedProduct(_.extend({'product' : angular.copy(product)}, {'carts': $scope.vm.carts})).then(function () {
                           getProductList();
                       });
                   };
                   // 组合套装商品上下架
                   $scope.onShelves = function (product) {
                       alert("上架");
                   };
                   $scope.offShelves = function (product) {
                       alert("下架");
                   }

               }

               return CombinedProductController;

           })());
       }
);

