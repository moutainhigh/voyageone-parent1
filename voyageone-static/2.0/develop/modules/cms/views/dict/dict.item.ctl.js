// Generated by CoffeeScript 1.9.3

/*
  @Name: DictItemController
  @Date: 2015-09-15 13:48:01

  @User: Jonas
  @Version: 0.0.1
 */
var bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

define(['modules/cms/views/dict/dict.custom.ctl',
        'modules/cms/views/dict/dict.value.ctl'],

    function(cmsModule) {
        return cmsModule.controller('DictItemController', (function() {
          _Class.$inject = ['$scope', '$translate', 'DictService', '$location', '$modal', 'alert', 'notify'];

          function _Class($scope, $translate, DictService, $location, $modal, alert, notify) {
            var dict;
            this.$translate = $translate;
            this.DictService = DictService;
            this.$location = $location;
            this.$modal = $modal;
            this.alert = alert;
            this.notify = notify;
            this.saveCustom = bind(this.saveCustom, this);
            this.broadcast = function(name, msg) {
              return $scope.$broadcast(name, msg);
            };
            $scope.$on('custom.save', this.saveCustom);
            $scope.$on('custom.cancel', (function(_this) {
              return function() {
                return _this.showCustom = false;
              };
            })(this));
            dict = sessionStorage.dict;
            if (dict) {
              dict = angular.fromJson(dict);
            }
            if (!dict || !dict.item || !dict.item.id) {
              sessionStorage.dict = angular.toJson(dict = {
                item: {
                  isUrl: false,
                  type: 'DICT'
                }
              });
              this.isEdit = false;
            } else {
              this.isEdit = true;
              dict = angular.fromJson(dict);
            }
            this.item = dict.item || (dict.item = {});
            if (this.item.value) {
              this.value = angular.fromJson(this.item.value);
            }
            if (!this.value.expression) {
              this.value.expression = {};
            }
            if (!this.value.expression.ruleWordList) {
              this.value.expression.ruleWordList = [];
            }
            this.wordList = this.value.expression.ruleWordList;
            this.DictService.getConst().then((function(_this) {
              return function(res) {
                return _this.constData = res.data;
              };
            })(this));
            this.DictService.getDictList().then((function(_this) {
              return function(res) {
                var selfIndex;
                selfIndex = _.findIndex(res.data, function(i) {
                  return i.id === _this.item.id;
                });
                res.data.splice(selfIndex, 1);
                return _this.dictList = res.data;
              };
            })(this));
          }

          _Class.prototype.isEdit = false;

          _Class.prototype.item = null;

          _Class.prototype.value = {};

          _Class.prototype.wordList = [];

          _Class.prototype.showCustom = false;

          _Class.prototype.constData = null;

          _Class.prototype.dictList = null;

          _Class.prototype.cancel = function() {
            sessionStorage.dict = null;
            return this.$location.path('/cms/dict');
          };

          _Class.prototype.addCustom = function() {
            this.broadcast('custom.add');
            return this.showCustom = true;
          };

          _Class.prototype.addValue = function() {
            return this.openValue(null, (function(_this) {
              return function(res) {
                return _this.wordList.push(res);
              };
            })(this));
          };
          _Class.prototype.up = function(index) {
            var temp =  angular.copy (this.wordList[index]);
            this.wordList.splice(index, 1);
            this.wordList.splice(index - 1, 0,temp);
          };

          _Class.prototype.down = function(index) {
            var temp =  angular.copy (this.wordList[index]);
            this.wordList.splice(index, 1);
            this.wordList.splice(index+1 , 0,temp);
          };

          _Class.prototype.delValue = function(index) {
            return this.wordList.splice(index, 1);
          };

          _Class.prototype.editValue = function(word,mode) {
            if (word.type === 'CUSTOM'&& mode !=null) {
              this.broadcast('custom.edit', word);
              this.showCustom = true;
              return;
            }
            return this.openValue(word, function(res) {
              word.type = res.type;
              return word.value = res.value;
            });
          };

          _Class.prototype.openValue = function(word, callback) {
            return this.$modal.open({
              templateUrl: 'modules/cms/views/dict/dict.value.tpl.html',
              controller: 'DictValueController',
              controllerAs: 'vm',
              resolve: {
                masterProps: (function(_this) {
                  return function() {
                    return _this.constData.masterProps;
                  };
                })(this),
                cmsValues: (function(_this) {
                  return function() {
                    return _this.constData.cmsValues;
                  };
                })(this),
                dictList: (function(_this) {
                  return function() {
                    return _this.dictList;
                  };
                })(this),
                word: function() {
                  return word;
                }
              }
            }).result.then(callback);
          };

          _Class.prototype.save = function() {
            if (!this.value.value) {
              this.notify.warning('TXT_MSG_DICT_NO_NAME');
              return;
            }
            if (!this.wordList.length) {
              this.notify.warning('TXT_MSG_DICT_NO_VALUE');
              return;
            }
            this.item.value = angular.toJson(this.value);
            this.item.name = this.value.value;
            return this.DictService[!this.isEdit ? 'addDict' : 'setDict'](this.item).then((function(_this) {
              return function(res) {
                if (res) {
                  return _this.alert('TXT_MSG_SAVE_SUCCESS').result.then(function() {
                    return _this.cancel();
                  });
                }
              };
            })(this));
          };

          _Class.prototype.saveCustom = function(event, msg) {
            if (!msg.editing) {
              this.wordList.push(msg.custom);
            }
            return this.showCustom = false;
          };

          return _Class;

        })());
});
