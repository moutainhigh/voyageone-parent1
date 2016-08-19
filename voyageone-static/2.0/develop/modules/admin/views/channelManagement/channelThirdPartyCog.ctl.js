/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('ChannelThirdPartyCogController', (function () {
        function ChannelThirdPartyCogController(popups, alert, confirm, channelService, thirdPartyConfigService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.thirdPartyConfigService = thirdPartyConfigService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.channelList = [];
            this.channelThirdSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                channelId: '',
                propName: '',
                propVal: '',
                pageInfo: this.channelPageOption
            }
        }

        ChannelThirdPartyCogController.prototype = {
            init: function () {
                var self = this;
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.thirdPartyConfigService.searchThirdPartyConfigByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'channelId': self.searchInfo.channelId,
                        'propName': self.searchInfo.propName,
                        'propVal': self.searchInfo.propVal
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
                                    "id": channelInfo.seq,
                                    "code": channelInfo.name
                                });
                            }
                        });
                        self.channelThirdSelList = self.tempChannelSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.channelPageOption,
                    channelId: '',
                    propName: '',
                    propVal: ''
                }
            },
            edit: function () {
                var self = this;
                if (self.channelThirdSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.channelList, function (channelInfo) {
                        if (channelInfo.seq == self.channelThirdSelList.selList[0].id) {
                            self.popups.openChannelThird(channelInfo).then(function () {
                                self.search(1);
                            });
                        }
                    })
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.channelThirdSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.thirdPartyConfigService.deleteThirdPartyConfig(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search();
                        })
                    }
                );
            }
        };
        return ChannelThirdPartyCogController;
    })())
});