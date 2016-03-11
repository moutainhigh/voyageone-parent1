/**
 * controller FeedPropMappingController
 */

define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/RuleTypes',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes, RuleTypes, _) {
    'use strict';

    function isRequiredField(field) {

        return _.find(field.rules, function (rule) {
            return rule.name === RuleTypes.REQUIRED_RULE && rule.value === 'true';
        });
    }

    function isSimpleType(field) {

        return field.type !== FieldTypes.complex &&
            field.type !== FieldTypes.multiComplex;
    }

    function getIconClass(field) {

        var iconClass = '';

        switch (field.type) {
            case FieldTypes.label:
                iconClass = 'badge badge-initialize';
                break;
            case FieldTypes.input:
                iconClass = 'badge badge-initialize';
                break;
            case FieldTypes.complex:
                iconClass = 'badge badge-refresh';
                break;
            case FieldTypes.singleCheck:
                iconClass = 'badge badge-success';
                break;
            case FieldTypes.multiCheck:
                iconClass = 'badge badge-success';
                break;
            case FieldTypes.multiComplex:
                iconClass = 'badge badge-Purple';
                break;
        }

        return iconClass;
    }

    function hasRequired(bean, beans, required) {

        if (!bean.children || !bean.children.length) {
            return (!!bean.required) === required;
        }
        var child = _.find(bean.children, function (id) {
            return hasRequired(beans[id], beans, required);
        });

        return !!child;
    }

    function hasMatched(bean, beans, matched) {

        if (!bean.children || !bean.children.length) {
            return bean.matched === matched;
        }
        var child = _.find(bean.children, function (id) {
            return hasMatched(beans[id], beans, matched);
        });

        return !!child;
    }

    function getHeadClass(bean) {
        return bean.isSimple ? 'fa-minus' : 'fa-plus';
    }

    function toArray(beans) {

        // 转换为包装后的数组
        var beanArray = _.map(beans, function (bean) {

            if (bean.parentId) {
                var parent
                    = bean.parent
                    = beans[bean.parentId];

                if (!parent.children) {
                    parent.children = [];
                }

                parent.children.push(bean.field.id);
            }

            bean.iconClass = getIconClass(bean.field);
            bean.isSimple = isSimpleType(bean.field);
            bean.required = isRequiredField(bean.field);
            bean.matched = _.contains(toArray.matchedMains[bean.type], bean.field.id);

            return bean;
        });

        // 设定字段,是否其子字段为必填
        // 用于过滤
        _.each(beanArray, function (bean) {
            bean.hasRequired = hasRequired(bean, beans, true);
            bean.hasOptional = hasRequired(bean, beans, false);
            bean.hasMatched = hasMatched(bean, beans, true);
            bean.hasUnMatched = hasMatched(bean, beans, false);
            bean.headClass = getHeadClass(bean);
        });

        // 整理展示顺序
        return beanArray.sort(function (a, b) {
            return a.seq > b.seq ? 1 : -1
        });
    }

    /**
     * 已匹配的主类目属性
     * @type {string[]}
     */
    toArray.matchedMains = null;

    return cms.controller('feedPropMappingController', (function () {
            /**
             * @description
             * Feed Mapping 属性匹配画面的 Controller 类
             * @param $scope
             * @param $routeParams
             * @param {FeedMappingService} feedMappingService
             * @param notify
             * @param $translate
             * @constructor
             */
            function FeedPropMappingController($scope, $routeParams, feedMappingService, notify, $translate) {

                this.$scope = $scope;
                this.feedCategoryPath = $routeParams['feedCategoryPath'];
                this.isCommon = $routeParams['commonFlg'] === '0';
                this.feedMappingService = feedMappingService;
                this.notify = notify;
                this.$translate = $translate;

                /**
                 * 主类目模型,不包含字段信息
                 * @type {object}
                 */
                this.mainCategory = null;
                /**
                 * 类目字段的 Map
                 * @type {object}
                 */
                this.fields = null;
                /**
                 * 共通字段的 Map
                 * @type {object}
                 */
                this.commonFields = null;
                /**
                 * 类目 SKU 级别字段 Map
                 * @type {object}
                 */
                this.skuFields = null;
                /**
                 * 上述字段的真实绑定对象
                 * @type {{fields: object[], common: object[], sku: object[]}}
                 */
                this.dataSources = {
                    fields: null,
                    common: null,
                    sku: null
                };
                /**
                 * 显示的过滤条件
                 */
                this.show = {
                    hasRequired: true,
                    matched: null,
                    keyWord: null
                };
                
                this.mappingModel = null;

                this.saveMapping = this.saveMapping.bind(this);

                this.matchOver = false;
            }

            FeedPropMappingController.prototype = {

                options: {
                    hasRequired: {
                        '必填情况(ALL)': null,
                        '非必填': false,
                        '必填': true
                    },
                    matched: {
                        '设定情况(ALL)': null,
                        '已设定': true,
                        '未设定': false
                    }
                },

                init: function () {

                    var ttt = this;

                    ttt.feedMappingService.getMainProps({
                            feedCategoryPath: ttt.feedCategoryPath
                        })

                        .then(function (res) {

                            // 保存主数据类目(完整类目)
                            ttt.mainCategory = res.data.category;

                            // 保存字段 Map 数据
                            ttt.fields = res.data.fields;
                            ttt.skuFields = res.data.sku;
                            ttt.commonFields = res.data.common;

                            // 根据类目信息继续查询
                            ttt.feedMappingService.getMatched(
                                {
                                    from: ttt.feedCategoryPath,
                                    isCommon: ttt.isCommon
                                }
                            ).then(function (res) {

                                var resultMap = res.data;
                                
                                // 保存已匹配的属性
                                toArray.matchedMains = resultMap.propMap;
                                
                                ttt.mappingModel = resultMap.mappingModel;

                                // 包装数据源
                                ttt.dataSources.fields = toArray(ttt.fields);
                                ttt.dataSources.common = toArray(ttt.commonFields);
                                ttt.dataSources.sku = toArray(ttt.skuFields);

                            });
                        });

                    ttt.feedMappingService.getMatchOver({
                            from: ttt.feedCategoryPath,
                            isCommon: ttt.isCommon
                        })
                        .then(function (res) {
                            ttt.matchOver = res.data == 1;
                        });
                },
                popupContext: function (bean) {
                    return {
                        isCommon: this.isCommon,
                        mappingModel: this.mappingModel,
                        field: bean.field,
                        bean: bean
                    };
                },
                /**
                 * 上层 popup 返回时的调用
                 * @param {{feedCategoryPath:string,mainCategoryPath:string,field:object,fieldMapping:object}} context
                 */
                saveMapping: function (context) {

                    var bean = context.bean;

                    var path = [];
                    var f = context.field;
                    path.push(f.id);
                    var p = bean.parent;
                    while (p) {
                        path.push(p.field.id);
                        p = p.parent;
                    }

                    context.fieldMapping.type = bean.type;

                    this.feedMappingService.saveFieldMapping({
                        mappingId: this.mappingModel._id,
                        fieldPath: path.reverse(),
                        propMapping: context.fieldMapping
                    }).then(function (res) {

                        var bool = res.data;
                        if (!bool) return;

                        var mappings = context.fieldMapping.mappings;

                        bean.matched = !!mappings && !!mappings.length;

                        // 循环设定上层标识
                        var parent = bean.parent;
                        while (!!parent) {
                            parent.hasUnMatched = !(parent.hasMatched = bean.matched);
                            parent = parent.parent;
                        }
                    }.bind(this));
                },

                /**
                 * 确定某字段是否要显示
                 */
                shown: function (bean) {

                    var result = true;
                    var keyWord = this.show.keyWord;

                    if (keyWord && bean.field.name.indexOf(keyWord) < 0)
                        return false;

                    if (this.show.hasRequired !== null) {
                        result = (this.show.hasRequired ? bean.hasRequired : bean.hasOptional);
                    }

                    if (result && this.show.matched !== null) {
                        result = (this.show.matched ? bean.hasMatched : bean.hasUnMatched);
                    }

                    return result;
                },

                switchMatchOver: function () {
                    var ttt = this;
                    ttt.feedMappingService.directMatchOver({
                            from: ttt.feedCategoryPath,
                            isCommon: ttt.isCommon
                        })
                        .then(function (res) {
                            if (res.data)
                                ttt.notify.success(ttt.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            else {
                                ttt.notify.danger(ttt.$translate.instant('TXT_MSG_UPDATE_FAIL'));
                                ttt.matchOver = !ttt.matchOver;
                            }
                        });
                },

                /**
                 * 重置过滤条件
                 */
                clear: function () {
                    this.show = {
                        hasRequired: null,
                        matched: null,
                        keyWord: null
                    };
                }
            };

            return FeedPropMappingController;

        })())
        .directive('fieldBean', function () {
            return {
                restrict: 'A',
                templateUrl: 'feedMapping.fieldBean.html',
                scope: true
            };
        });
});