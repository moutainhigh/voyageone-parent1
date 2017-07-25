/**
 * @description 美国各平台详情页
 */
define([
    'cms'
], function (cms) {

    class usTabController {

        constructor($scope,detailDataService,$usProductDetailService,notify) {
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;
            this.productInfo = $scope.productInfo;
            this.cartInfo = $scope.cartInfo;

            // 平台信息
            this.platform = {};

            // SKU
            this.selAllSkuFlag = false;

            // 平台状态
            this.platformStatus = this.detailDataService.platformStatus;

        }

        init() {
            let self = this;

            self.detailDataService.getProductPlatform({
                cartId: Number(self.cartInfo.value),
                prodId: self.productInfo.productId
            }).then(res => {

                if (res.data) {
                    self.platform = res.data.platform;
                    self.productComm = res.data.productComm;

                    // SKU 是否全选
                    let flag = true;
                    _.each(self.platform.skus, sku => {
                        let isSale = sku.isSale;
                        if (!isSale) {
                            flag = false;
                        }
                    });
                    self.selAllSkuFlag = flag;
                }
            })
        }

        // SKU可售选择
        selAllSku() {
            let self = this;
            _.each(self.platform.skus, sku => {
                sku.isSale = self.selAllSkuFlag;
            });
        }
        checkSelAllSku(sku) {
            let self = this;
            let isSale = sku.isSale;
            if (!isSale) {
                self.selAllSkuFlag = false;
            } else {
                let notSelOne = _.find(self.platform.skus, sku => {
                    return !sku.isSale;
                });
                self.selAllSkuFlag = !notSelOne;
            }
        }

        // Save
        save(status) {
            let self = this;
            let platform = angular.copy(self.platform);
            if (status) {
                platform.pStatus = status;
            }
            let parameter = {
                prodId:self.$scope.productInfo.productId,
                data:{
                    platform:platform
                }
            };
            this.$usProductDetailService.updateProductPlatform(parameter).then(res => {
                if (res.data) {
                    self.notify.success("Save success.");
                    self.platform.pStatus = status;
                }
            });
        }

    }

    cms.directive('usTab', function () {
        return {
            restrict: 'E',
            controller: usTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/us-tab.directive.html'
        }
    })

});