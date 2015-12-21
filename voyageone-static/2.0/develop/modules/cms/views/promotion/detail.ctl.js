/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope, promotionService, promotionDetailService, $routeParams) {
        pageSize = 5;
        $scope.vm = {
            "promotionId":$routeParams.promotionId,
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
            searchGroup()
            searchCode();
            searchSku();

        }

        function searchGroup(){
            promotionDetailService.getPromotionGroup({
                "promotionId": $routeParams.promotionId,
                "start": ($scope.vm.groupPageOption.curr - 1) * $scope.vm.groupPageOption.size,
                "length": $scope.vm.groupPageOption.size
            }).then(function (res) {
                $scope.vm.groupPageOption.total = res.data.total;
                $scope.vm.groupList = res.data.resultData;
            }, function (err) {

            })
        }
        function searchCode(){
            promotionDetailService.getPromotionCode({
                "promotionId": $routeParams.promotionId,
                "start": ($scope.vm.codePageOption.curr - 1) * $scope.vm.codePageOption.size,
                "length": $scope.vm.codePageOption.size
            }).then(function (res) {
                $scope.vm.codePageOption.total = res.data.total;
                $scope.vm.codeList = res.data.resultData;
            }, function (err) {

            })
        }

        function searchSku(){
            promotionDetailService.getPromotionSku({
                "promotionId": $routeParams.promotionId,
                "start": ($scope.vm.skuPageOption.curr - 1) * $scope.vm.skuPageOption.size,
                "length": $scope.vm.skuPageOption.size
            }).then(function (res) {
                $scope.vm.skuPageOption.total = res.data.total;
                $scope.vm.skuList = res.data.resultData;
            }, function (err) {

            })
        }
    };
});
