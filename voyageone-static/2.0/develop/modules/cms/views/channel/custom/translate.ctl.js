define([
    'cms'
], function (cms) {

    cms.controller('translateController', (function () {

        function TranslateCtl($routeParams, attrTranslateService, popups, confirm, notify, alert) {
            var self = this;
            self.nameEn = $routeParams.nameEn;
            self.popups = popups;
            self.confirm = confirm;
            self.notify = notify;
            self.alert = alert;
            self.attrTranslateService = attrTranslateService;
            self.searchInfo = {
                type: '',
                propName: '',
                propValue: ''
            };
            self.paging = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
        }

        TranslateCtl.prototype.TYPE = {
            1: '共通属性',
            2: '主类目属性',
            3: '供货商属性',
            4: '自定义属性'
        };

        TranslateCtl.prototype.init = function () {
            var self = this, entity, jsonStr,
                searchInfo = self.searchInfo;

            jsonStr = sessionStorage.getItem(self.nameEn);

            if (!jsonStr) {
                document.open();
                document.write('<h3>请从自定义属性管理链接到此页面！</h3>');
                document.close();
                return;
            }

            self.datasource = angular.fromJson(jsonStr);
            entity = self.datasource.entity;

            searchInfo.type = entity.type + '';
            searchInfo.propName = entity.nameEn;

            self.search();

        };

        TranslateCtl.prototype.clear = function () {
            this.searchInfo.propValue = '';
            //this.searchInfo.type = '';
            this.searchInfo.propName = '';
        };

        TranslateCtl.prototype.search = function () {
            var self = this, searchInfo = self.searchInfo;

            self.attrTranslateService.init(_.extend(searchInfo, self.paging)).then(function (res) {
                self.attrValues = res.data.resultData;
                self.paging.total = res.data.total;
            });
        };

        TranslateCtl.prototype.openAddAttributeValue = function () {
            var self = this,
                popups = self.popups;

            popups.openAddAttrValue({
                nameEn: self.searchInfo.propName,
                type: self.searchInfo.type,
                nameEnArr: self.datasource.nameEnArr
            }).then(function () {
                self.search();
            });
        };

        TranslateCtl.prototype.save = function (entity) {
            var self = this,
                _preValue = angular.copy(entity.valueCn);

            if (!_preValue || _preValue == '') {
                self.confirm('属性中文值为空，是否还要保存').then(function () {
                    self.callSave(entity);
                }, function () {
                    self.search();
                });
            } else {
                self.callSave(entity);
            }

        };

        TranslateCtl.prototype.callSave = function (upEntity) {
            var self = this;
            self.attrTranslateService.update(upEntity).then(function (res) {
                self.notify.success('更新成功！');
            });
        };

        return TranslateCtl;

    })());

});