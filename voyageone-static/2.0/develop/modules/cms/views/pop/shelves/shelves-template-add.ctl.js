/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("ShelvesTemplateAddController", (function () {

            function ShelvesTemplateAddController($scope, shelvesTemplateService, popups, context) {
                $scope.vm = {
                    templateTypes:{},
                    clientTypes:{},
                    carts:{}
                };
                $scope.vm.templateTypes = context.templateTypes;
                $scope.vm.clientTypes = context.clientTypes;
                $scope.vm.carts = context.carts;
                $scope.layout = true;
                $scope.single = false;
                $scope.modelBean = {
                    "templateType":"0",
                    "numPerLine":1
                };

                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.init().then(function (resp) {
                        $scope.vm.templates = resp.data == null ? [] : resp.data;
                    });
                }

                $scope.popNewShelvesTemplate = function () {
                    popups.shelvesTemplateAdd();
                }

                $scope.addSubmit = function () {
                    shelvesTemplateService.add($scope.modelBean).then(function (resp) {
                        $scope.$close();
                    });
                }

                $scope.selTemplateType = function () {
                    var templateTypeVal = $scope.modelBean.templateType;
                    if ("0" == templateTypeVal){
                        $scope.layout = true;
                        $scope.single = false;
                    }else if("1" == templateTypeVal) {
                        $scope.layout = false;
                        $scope.single = true;
                    }
                }
            }
            return ShelvesTemplateAddController;

        })());
    }
);
