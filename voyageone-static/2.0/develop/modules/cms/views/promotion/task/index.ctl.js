/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskIndexController", (function () {
    
        function TaskIndexController(taskService, taskStockService, confirm, notify) {
            this.taskService = taskService;
            this.taskStockService = taskStockService;
            this.tasks = [];
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo={};

            this.taskType=[{"name":"特价宝","value":"0"},{"name":"价格披露","value":"1"}];
            this.datePicker = [];
        }
    
        TaskIndexController.prototype = {
            init: function () {
                var ttt = this;
                ttt.taskService.page(ttt.searchInfo).then(function(res){
                    ttt.tasks = res.data;
                });
            },

            typeName: function(task) {
                switch (task.task_type) {
                    case 0:
                        return '特价宝';
                    case 1:
                        return '价格披露';
                    case 2:
                        return '库存隔离';
                    default:
                        return '未知类型';
                }
            },
            search: function(){
                var ttt = this;
                this.taskService.page(this.searchInfo).then(function(res){
                    ttt.tasks = res.data;
                });
            },
            clear: function(){
                this.searchInfo={};
            },
            delete: function(task){
                var ttt = this;
                ttt.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    // 库存隔离
                    if (task.task_type == '2') {
                        ttt.taskStockService.delTask({
                            "taskId" : task.task_id
                        }).then(function (res) {
                            ttt.notify.success('TXT_MSG_DELETE_SUCCESS');
                            ttt.search();
                        }, function (err) {
                            if (err.displayType == null) {
                                ttt.alert('TXT_MSG_DELETE_FAIL');
                            }
                        })
                    }
                })
            },
        };
            
        return TaskIndexController;
        
    })());
});
