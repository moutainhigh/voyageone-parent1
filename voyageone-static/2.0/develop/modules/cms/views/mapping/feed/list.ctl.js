/**
 * controller FeedMappingController
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('feedMappingController', (function () {
        /**
         * @typedef {{parentPath:string,level:int,model:object,mapping:object}} FeedCategoryBean
         */
        /**
         * @description
         * Feed Mapping 画面的 Controller 类
         * @param {FeedMappingService} feedMappingService
         * @param {voNotify} notify
         * @param {function} confirm
         * @param $translate
         * @param blockUI
         * @constructor
         */
        function FeedMappingController(feedMappingService, notify, confirm, $translate, blockUI) {

            this.confirm = confirm;
            this.notify = notify;
            this.$translate = $translate;
            this.feedMappingService = feedMappingService;
            this.blockUI = blockUI;

            /**
             * feed 类目集合
             * @type {object}
             */
            this.feedCategories = null;
            /**
             * 所有顶层的 Key 值
             * @type {string[]}
             */
            this.topKeys = null;
            /**
             * 当前选择的 TOP 类目
             * @type {string}
             */
            this.selectedTop = null;
            /**
             * 表格的数据源
             * @type {object[]}
             */
            this.tableSource = null;
            /**
             * 匹配情况筛选条件
             * @type {{category: boolean, property: boolean}}
             */
            this.matched = {
                category: null,
                property: null,
                keyWord: null
            };
        }

        FeedMappingController.prototype = {
            /**
             * select 的绑定数据源
             */
            options: {
                category: {
                    '类目匹配情况(ALL)': null,
                    '类目已匹配': true,
                    '类目未匹配': false
                },
                property: {
                    '匹配属性情况(ALL)': null,
                    '属性已匹配完成': true,
                    '属性未匹配完成': false
                }
            },
            /**
             * 画面初始化时
             */
            init: function () {

                this.feedMappingService.getFeedCategoryTree().then(function (res) {
                    this.feedCategories = res.data;
                    this.topKeys = _.keys(this.feedCategories);
                    // 如果有数据就默认选中
                    if (this.topKeys.length) {
                        this.selectedTop = this.topKeys[0];
                    }
                    this.refreshTable();
                }.bind(this));
            },
            /**
             * 继承其父类目的 Mapping
             * @param {object} feedCategory Feed 类目
             */
            extendsMapping: function (feedCategory) {

                this.confirm(this.$translate.instant('TXT_MSG_CONFIRM_FROWARD_PARENT_CATEGORY')).result.then(function () {
                    this.feedMappingService.extendsMapping(feedCategory).then(function (res) {
                        if (res.data) {
                            // 从后台获取更新后的 mapping
                            // 刷新数据
                            var feedCategoryBean = this.findCategory(feedCategory.path);
                            feedCategoryBean.mapping = _.find(res.data, function (m) {
                                return m.defaultMapping === 1;
                            });
                        } else {
                            this.notify.warning(this.$translate.instant('TXT_MSG_NO_FIND_FORWARD_CATEGORY'));
                        }
                    }.bind(this));
                }.bind(this));
            },
            /**
             * 获取类目的默认 Mapping 类目
             * @param {FeedCategoryBean} feedCategory
             * @returns {string}
             */
            getDefaultMapping: function (feedCategory) {

                if (!feedCategory) {
                    return '?';
                }

                var defMapping = this.findDefaultMapping(feedCategory);

                if (defMapping) {
                    return defMapping.mainCategoryPath;
                }

                var parent = null;
                while (!defMapping && (parent = this.findParent(parent || feedCategory))) {
                    defMapping = this.findDefaultMapping(parent);
                }


                return defMapping ? (this.$translate.instant('TXT_FORWARD_WITH_COLON') + defMapping.mainCategoryPath) : this.$translate.instant('TXT_UN_SETTING');
            },
            /**
             * 查找父级类目
             * @param {object} category
             * @returns {object}
             */
            findParent: function (category) {

                var path = category.model.path;
                var index = path.lastIndexOf('-');
                return index < 0
                    ? null
                    : this.findCategory(path.substring(0, index));
            },
            /**
             * 在树中查找类目
             * @param {string} path 类目路径
             * @return {object|undefined} Feed 类目对象
             */
            findCategory: function (path) {
                return this.feedCategories[this.selectedTop][path];
            },
            /**
             * 在类目中查找默认的 Mapping 关系
             * @param {FeedCategoryBean} category
             * @return {object} Mapping 对象
             */
            findDefaultMapping: function (category) {
                return category.mapping;
            },
            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:string, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {

                this.feedMappingService.setMapping({
                    from: context.from,
                    to: context.selected.catPath
                }).then(function (res) {
                    // 从后台获取更新后的 mapping
                    // 刷新数据
                    var feedCategoryBean = this.findCategory(context.from);
                    feedCategoryBean.mapping = _.find(res.data, function (m) {
                        return m.defaultMapping === 1;
                    });
                }.bind(this));
            },

            /**
             * 检查当前类目是否已进行类目匹配
             * @param {FeedCategoryBean} feedCategoryBean
             * @return {boolean}
             */
            isCategoryMatched: function (feedCategoryBean) {
                // 只要默认 mapping 存在即已匹配
                return !!feedCategoryBean.mapping;
            },

            /**
             * 检查当前类目是否已经完成了属性匹配
             * @param {FeedCategoryBean} feedCategoryBean
             * @return {boolean}
             */
            isPropertyMatched: function (feedCategoryBean) {
                // 如果有匹配并且确实匹配完了,才算是属性匹配完成
                return this.isCategoryMatched(feedCategoryBean)
                    && !!feedCategoryBean.mapping.matchOver;
            },

            /**
             * 刷新表格
             */
            refreshTable: function () {
                // 选定数据源
                var ttt = this;
                var rows = ttt.feedCategories[ttt.selectedTop];
                var keyWord = ttt.matched.keyWord;

                ttt.blockUI.start();

                // 过滤
                rows = _.filter(rows, function (feedCategoryBean) {
                    var result = true;

                    // 如果关键字存在, 先进行关键字过滤
                    if (keyWord && feedCategoryBean.model.path.indexOf(keyWord) < 0)
                        return false;

                    if (ttt.matched.category !== null)
                        result = ttt.matched.category === ttt.isCategoryMatched(feedCategoryBean);
                    else if (ttt.matched.property !== null)
                        result = ttt.matched.property === ttt.isPropertyMatched(feedCategoryBean);

                    return result;
                });

                // 绑定&显示
                ttt.tableSource = rows.sort(function (a, b) {
                    return a.seq > b.seq ? 1 : -1;
                });

                ttt.blockUI.stop();
            },

            openCategoryMapping: function (categoryModel, popupNewCategory) {

                this.feedMappingService.getMainCategories()
                    .then(function (res) {

                        popupNewCategory({

                            categories: res.data,
                            from: categoryModel.path

                        }).then(this.bindCategory.bind(this));

                    }.bind(this));
            },

            /**
             * 重置搜索条件
             */
            clear: function () {
                this.matched.category = null;
                this.matched.property = null;
                this.matched.keyWord = null;
                this.refreshTable();
            }
        };

        return FeedMappingController
    })());
});