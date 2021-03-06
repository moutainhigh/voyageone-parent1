/**
 * Created by sofia on 2016/8/18.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('ChannelTypeAttrManagementController', (function () {
        function ChannelTypeAttrManagementController(popups, alert, confirm, channelService, typeService, channelAttributeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.typeService = typeService;
            this.channelAttributeService = channelAttributeService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.channelList = [];
            this.channelTypeSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                channelId: '',
                typeId: '',
                langId: '',
                name: '',
                value: '',
                active: '',
                pageInfo: this.channelPageOption
            }
        }

        ChannelTypeAttrManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.typeService.getAllType().then(function (res) {
                    self.typeList = res.data;
                });
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.channelAttributeService.searchChannelAttributeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'channelId': self.searchInfo.orderChannelId,
                        'typeId': self.searchInfo.typeId,
                        'langId': self.searchInfo.langId,
                        'name': self.searchInfo.name,
                        'value': self.searchInfo.value,
                        'active': self.searchInfo.active
                    })
                    .then(function (res) {
                        self.channelList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempChannelSelect == null) {
                            self.tempChannelSelect = new self.selectRowsFactory();
                        } else {
                            self.tempChannelSelect.clearCurrPageRows();
                            self.tempChannelSelect.clearSelectedList();
                        }
                        _.forEach(self.channelList, function (channelInfo) {
                            if (channelInfo.updFlg != 8) {
                                self.tempChannelSelect.currPageRows({
                                    "id": channelInfo.id,
                                    "code": channelInfo.name
                                });
                            }
                        });
                        self.channelTypeSelList = self.tempChannelSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.channelPageOption,
                    channelId: '',
                    typeId: '',
                    langId: '',
                    name: '',
                    active: '',
                    value: ''
                }
            },
            edit: function (item) {
                var self = this;
                if (item == 'add') {
                    self.popups.openAddChannelType('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    self.popups.openAddChannelType(item).then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                }
            },
            delete: function (item) {
                var self = this, delList = [];
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                    if(item=='batchDel'){
                        _.forEach(self.channelTypeSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                    }else{
                        delList.push(item);
                    }
                        self.channelAttributeService.deleteChannelAttribute(delList).then(function (res) {
                            if (res.data == false)self.confirm(res.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return ChannelTypeAttrManagementController;
    })())
});
