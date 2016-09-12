/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideBatchJobController', (function () {
        function GuideBatchJobController(popups, alert, confirm, selectRowsFactory, newShopService) {
            this.context = JSON.parse(window.sessionStorage.getItem('valueBean'));
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.newShopService = newShopService;
            this.tempTaskSelect = null;
            this.taskSelList = {selList: []};
        }

        GuideBatchJobController.prototype = {
            init: function () {
                var self = this;
                self.taskList = self.context.task;

                // 设置勾选框
                if (self.tempTaskSelect == null) {
                    self.tempTaskSelect = new self.selectRowsFactory();
                } else {
                    self.tempTaskSelect.clearCurrPageRows();
                    self.tempTaskSelect.clearSelectedList();
                }
                _.forEach(self.taskList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempTaskSelect.currPageRows({
                            "id": Info.taskId
                        });
                    }
                });
                self.taskSelList = self.tempTaskSelect.selectRowsInfo;
                // End 设置勾选框
            },
            config: function (type) {
                var self = this;
                if (self.taskSelList.selList.length < 1) {
                    self.popups.openConfig({'configType': type,'isReadOnly': true,
                        'sourceData': self.context.task,
                        'orderChannelId': self.context.cartShop[0].orderChannelId});
                    return;
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskId == self.taskSelList.selList[0].id) {
                            _.extend(Info, {'configType': type});
                            self.popups.openConfig(Info);
                        }
                    })
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openTask({
                        'kind': 'add',
                        'isReadOnly': true,
                        'orderChannelId': self.taskList[0].orderChannelId
                    }).then(function (res) {
                        var list = self.taskList;
                        list.push(res);
                        self.init(1);
                    });
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskId == self.taskSelList.selList[0].id) {
                            _.extend(Info, {'isReadOnly': true});
                            self.popups.openTask(Info).then(function () {
                                self.init(1);
                            });
                        }
                    })
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.taskSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        // self.taskService.deleteTask(delList).then(function (res) {
                        //     self.search(1);
                        // })
                    }
                );
            },
            complete: function () {
                var self = this;
                self.confirm('您确定要提交全部新店的数据吗？').then(function () {
                    self.newShopService.saveChannelSeries(self.context).then(function (res) {
                        if(res.data == true){
                            window.location.href = "#/newShop/history";
                        }
                    })
                })
            }
        };
        return GuideBatchJobController;
    })())
});