/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function detailController($scope, popups,jmPromotionService,cmsBtJmPromotionImportTaskService,cmsBtJmPromotionExportTaskService, jmPromotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {

        //($scope);

        $scope.datePicker = [];
        $scope.vm = {
            "promotionId": $routeParams.parentId,
            modelList: [],
            cmsBtJmPromotionImportTaskList: [],
            cmsBtJmPromotionExportTaskList: [],
            tagList: []
        };
        $scope.searchInfo = {cmsBtJmPromotionId: $routeParams.parentId};
        $scope.parentModel = {};
        $scope.modelUpdateDealEndTime = {};
        $scope.modelAllUpdateDealEndTime = {};
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage.bind(this)}
        $scope.initialize = function () {
            jmPromotionService.get($routeParams.parentId).then(function (res) {
                $scope.parentModel = res.data;
            });
            jmPromotionService.getTagListByPromotionId($routeParams.parentId).then(function (res) {
                $scope.vm.tagList = res.data;
            });
            $scope.search();
            $scope.modelUpdateDealEndTime.promotionId = $routeParams.parentId;
            $scope.modelUpdateDealEndTime.getSelectedProductIdList = getSelectedProductIdList;
            $scope.modelUpdateDealEndTime.isBatch = true;
        };
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.search = function () {
            // console.log("searchInfo");
            console.log($scope.searchInfo);
            // loadSearchInfo();
            var data = getSearchInfo();

            goPage(1, 10)
            jmPromotionDetailService.getPromotionProductInfoCountByWhere(data).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
        };
        function getSearchInfo() {
            loadSearchInfo();
            var data = angular.copy($scope.searchInfo);
            for (var key in data) {
                if (!data[key]) {
                    data[key] = undefined;
                }
            }
            return data;
        }

        function goPage(pageIndex, size) {
            var data = getSearchInfo();
            data.start = (pageIndex - 1) * size;
            data.length = size;
            jmPromotionDetailService.getPromotionProductInfoListByWhere(data).then(function (res) {
                $scope.vm.modelList = res.data;
            }, function (res) {
            })
        }

        var getSelectedProductIdList = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            return productIdList;
        }
        $scope.jmNewByProductIdListInfo = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            var parameter = {promotionId: $scope.vm.promotionId, productIdList: productIdList};
            //console.log(parameter);
            //console.log(angular.toJson(parameter));
            confirm($translate.instant('TXT_Do_You_Want_To_Selected')).result.then(function () {
                jmPromotionDetailService.jmNewByProductIdListInfo(parameter).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList[i].synchState = 1;
                            $scope.vm.modelList[i].isChecked = false;
                        }
                    }
                }, function (res) {
                })
            });
        };
        $scope.jmNewUpdateAll = function () {
            confirm($translate.instant('TXT_Do_You_Want_To_Update_ All')).result.then(function () {
                jmPromotionDetailService.jmNewUpdateAll($scope.vm.promotionId).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {

                        $scope.vm.modelList[i].synchState = 1;

                    }
                }, function (res) {
                })
            })
        };


        $scope.deleteByProductIdList = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            var parameter = {promotionId: $scope.vm.promotionId, productIdList: productIdList};
            //console.log(parameter);
            // console.log(angular.toJson(parameter));
            confirm($translate.instant('TXT_MSG_DO_DELETE')).result.then(function () {
                jmPromotionDetailService.deleteByProductIdList(parameter).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList.splice(i, 1);
                        }
                    }
                }, function (res) {
                })
            });
        }
        $scope.deleteByPromotionId = function () {
            confirm($translate.instant('TXT_MSG_DO_DELETE')).result.then(function () {
                jmPromotionDetailService.deleteByPromotionId($scope.vm.promotionId).then(function () {
                    $scope.vm.modelList = [];
                }, function (res) {
                })
            })
        }
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.productCode).result.then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                data.isDelete = 1;
                jmPromotionDetailService.delete(data.id).then(function () {
                    $scope.vm.modelList.splice(index, 1);
                }, function (res) {
                })
            })
        };

        $scope.updateDealPrice = function (data) {
            jmPromotionDetailService.updateDealPrice({id: data.id, dealPrice: data.dealPrice}).then(function () {
                data.isSave = false;
            }, function (res) {
            })
        };
        $scope.searchImport = function () {
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                // console.log(res);
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.searchExport = function () {
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                // console.log(res);
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.addExport = function (templateType) {
            var model = {templateType: templateType, cmsBtJmPromotionId: $scope.vm.promotionId};
            cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
                $scope.searchExport();
            }, function (res) {

            });
        }
        $scope.downloadImportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadExcel", angular.toJson({id: id}));
        }
        $scope.downloadImportErrorExcel = function (id) {
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadImportErrorExcel", angular.toJson({id: id}));
        }
        $scope.downloadExportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionExportTask/index/downloadExcel", angular.toJson({id: id}));
        }


        function ExportExcel(action, source)//导出excel方法
        {
            var Form = document.createElement("FORM");
            document.body.appendChild(Form);
            Form.method = "POST";
            var newElement = $("<input name='source' type='hidden' />")[0];
            Form.appendChild(newElement);
            newElement.value = source;
            var IsExcelElement = $("<input name='IsExcel' type='hidden' />")[0];
            Form.appendChild(IsExcelElement);
            IsExcelElement.value = 1;
            Form.action = action;
            Form.submit();
        };
        function loadSearchInfo() {
            $scope.searchInfo.synchStateList = [];
            if ($scope.searchInfo.synchState0) {
                $scope.searchInfo.synchStateList.push(0)
            }
            if ($scope.searchInfo.synchState1) {
                $scope.searchInfo.synchStateList.push(1)
            }
            if ($scope.searchInfo.synchState2) {
                $scope.searchInfo.synchStateList.push(2)
            }
            if ($scope.searchInfo.synchState3) {
                $scope.searchInfo.synchStateList.push(3)
            }
            if ($scope.searchInfo.synchState4) {
                $scope.searchInfo.synchStateList.push(4)
            }
        }

        $scope.getStatus = function (model) {
            //0:未更新 2:上新成功 3:上传异常
            if (model.synchStatus == 1) {
                return "待再售";
            }
            else if (model.updateStatus == 1 || model.priceStatus == 1) {
                return "待更新";
            }
            else if (model.dealEndTimeStatus == 1) {
                return "待延期";
            }
            else if (model.synchStatus == 0) {
                return "未更新";
            }
            return "更新完成";
        }
        $scope.getSelectedProductIdList = function () {
            var listPromotionProductId = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductId.push($scope.vm.modelList[i].id);
                }
            }
            return listPromotionProductId;
        }
        $scope.updateJM = function (promotionProductId) {
            var listPromotionProductId = [promotionProductId];
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            jmPromotionDetailService.batchSynchPrice(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        };
        $scope.batchSynchPrice = function () {
            var listPromotionProductId = $scope.getSelectedProductIdList();
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            jmPromotionDetailService.batchSynchPrice(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.synchAllPrice = function () {
            jmPromotionDetailService.synchAllPrice($scope.vm.promotionId).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.batchCopyDeal = function () {
            var listPromotionProductId = $scope.getSelectedProductIdList();
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            jmPromotionDetailService.batchCopyDeal(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.copyDealAll = function () {
            jmPromotionDetailService.copyDealAll($scope.vm.promotionId).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }

        $scope.batchDeleteProduct = function () {
            //已再售的不删除
            var listPromotionProductId = $scope.getSelectedProductIdList();
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            if (listPromotionProductId.length == 0) {
                alert("请选择删除的商品!");
                return;
            }
            jmPromotionDetailService.batchDeleteProduct(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }

        $scope.deleteAllProduct = function () {//已再售的不删除
            jmPromotionDetailService.deleteAllProduct($scope.vm.promotionId).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('TXT_SUCCESS'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.selectAll = function ($event) {
            var checkbox = $event.target;
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                // if ($scope.vm.modelList[i].isChecked) {
                $scope.vm.modelList[i].isChecked = checkbox.checked;
                //}
            }
        };
        $scope.openPriceModifyWin = function () {
            var listPromotionProductId = $scope.getSelectedProductIdList();
            if (listPromotionProductId.length == 0) {
                alert("请选择修改价格的商品!");
                return;
            }
            popups.openPriceModify({search: $scope.search, listPromotionProductId: listPromotionProductId})
        }
        $scope.openProductDetailWin = function (object) {
            popups.openJmProductDetail(object).then(function () {
                $scope.search();
            });
        }
        $scope.openJmPromotionProductImportWin = function () {
            popups.openJmPromotionProductImport($scope.parentModel, $scope.selectImport);
        }
        $scope.openJmPromotionDetailWin = function (parameter) {
            popups.openJmPromotionDetail(parameter);
        }
    }
    detailController.$inject = ['$scope','popups', 'jmPromotionService','cmsBtJmPromotionImportTaskService','cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory'];
    return detailController;
});