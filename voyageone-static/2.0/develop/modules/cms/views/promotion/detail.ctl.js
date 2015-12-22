/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope, promotionService, promotionDetailService, notify, $routeParams) {
        pageSize = 5;
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "tabIndex": 0,
            "searchKey": '',
            "promotion": {},
            "groupList": [],
            "codeList": [],
            "skuList": [],
            "groupPageOption": {curr: 1, total: 5, size: 5, fetch: searchGroup},
            "codePageOption": {curr: 1, total: 5, size: 5, fetch: searchCode},
            "skuPageOption": {curr: 1, total: 7, size: 10, fetch: searchSku}
        };


        $scope.initialize = function () {
            promotionService.getPromotionList({"promotionId": $routeParams.promotionId}).then(function (res) {
                $scope.vm.promotion = res.data[0];
            }, function (err) {

            });
            $scope.search();
        }

        function searchGroup() {
            promotionDetailService.getPromotionGroup({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.groupPageOption.curr - 1) * $scope.vm.groupPageOption.size,
                "length": $scope.vm.groupPageOption.size
            }).then(function (res) {
                $scope.vm.groupPageOption.total = res.data.total;
                $scope.vm.groupList = res.data.resultData;
            }, function (err) {

            })
        }

        function searchCode() {
            promotionDetailService.getPromotionCode({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.codePageOption.curr - 1) * $scope.vm.codePageOption.size,
                "length": $scope.vm.codePageOption.size
            }).then(function (res) {
                $scope.vm.codePageOption.total = res.data.total;
                $scope.vm.codeList = res.data.resultData;
                _.each()
            }, function (err) {

            })
        }

        function searchSku() {
            promotionDetailService.getPromotionSku({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.skuPageOption.curr - 1) * $scope.vm.skuPageOption.size,
                "length": $scope.vm.skuPageOption.size
            }).then(function (res) {
                $scope.vm.skuPageOption.total = res.data.total;
                $scope.vm.skuList = res.data.resultData;
            }, function (err) {

            })
        }

        $scope.search = function () {
            searchGroup();
            searchCode();
            searchSku();
        }

        $scope.updateCode = function(code){
            promotionDetailService.updatePromotionProduct(code).then(function (res) {
                notify.success("success");
            }, function (err) {
                notify.warning("fail");
            })
        }
    };
});
