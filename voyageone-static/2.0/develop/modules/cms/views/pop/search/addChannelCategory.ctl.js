/**
 * Created by sofia on 5/19/2016.
 */
define([
    'cms'
], function (cms) {
    //将树递归拍平
    function flatCategories(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.catId] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatCategories(curr.children, curr));
            return map;
        }, {});
    }

    cms.controller('popAddChannelCategoryCtrl', (function () {

        function PopAddChannelCategoryCtrl(context, $rootScope, $addChannelCategoryService, notify, $uibModalInstance) {
            this.code = context.productIds;
            this.cartList = [];
            this.channelCategoryList = null;
            this.isSelectCid = [];
            this.cartId = $rootScope.platformType.cartId.toString();
            this.cnt = "";
            this.addChannelCategoryService = $addChannelCategoryService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.checkedCountValid = false;
            this.cartIdValid = false;
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                if(self.cartId == null){
                    self.cartId = "0";

                }
                self.addChannelCategoryService.init({"code": self.code, "cartId": self.cartId}).then(function (res) {
                    //默认对打钩的数目和店铺渠道选择的验证处于隐藏状态
                    self.checkedCountValid = false;
                    self.cartIdValid = false;
                    self.cartList = res.data.cartList;
                    self.cnt = res.data.cnt;
                    // 如果店铺渠道选择master或feed，不显示分类列
                    if (self.cartId == "0" || self.cartId == "1") {
                        self.isSelectCid = [];
                        self.channelCategoryList = null;
                        return;
                    }
                    self.isSelectCid = res.data.isSelectCid;
                    self.channelCategoryList = res.data.channelCategoryList;
                });
            },

            /**
             * 点击保存按钮时
             */
            save: function () {
                var self = this;
                //save保存时，如果店铺渠道选择的是master或feed，则显示警告：操作无效
                if (self.cartId == "0" || self.cartId == "1") {
                    self.cartIdValid = true;
                    return;
                }
                var cIds = [], cNames = [], fullCNames = [], fullCIds = [];
                var map = flatCategories(this.channelCategoryList);
                _.map(this.isSelectCid, function (value, key) {
                    return {categoryId: key, selected: value};
                }).filter(function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    var category = map[item.categoryId];
                    //查询叶子节点
                    if (category && category.isParent == 0) {
                        if (fullCNames.indexOf(category.catPath) < 0) fullCNames.push(category.catPath);
                        if (fullCIds.indexOf(category.fullCatId) < 0)  fullCIds.push(category.fullCatId);
                    }
                    //查询叶子节点和父节点
                    while (category) {
                        if (cIds.indexOf(category.catId) < 0) cIds.push(category.catId);
                        if (cNames.indexOf(category.catName) < 0) cNames.push(category.catName);
                        category = category.parent;
                    }
                });
                //save保存时，如果类目打钩数目超过cnt的值，则显示警告：超过最大值了
                if (fullCIds.length > self.cnt) {
                    self.checkedCountValid = true;
                    return;
                }
                self.addChannelCategoryService.save({
                    "cIds": cIds,
                    "cNames": cNames,
                    "fullCNames": fullCNames,
                    "fullCatId": fullCIds,
                    "code": self.code,
                    "cartId": self.cartId
                }).then(function (res) {
                    self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                    self.$uibModalInstance.close();
                });
            }
        };
        return PopAddChannelCategoryCtrl;
    })());
});