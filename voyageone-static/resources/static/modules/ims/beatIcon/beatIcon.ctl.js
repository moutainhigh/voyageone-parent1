/*
 @Name: beat-icon controller
 @Date: 2015/06/29

 @User: Jonas
 @Version: 0.2.3
 */

(function () {
  var bind = function (fn, me) {
    return function () {
      return fn.apply(me, arguments);
    };
  };

  define([
    'modules/ims/ims.module',
    'modules/ims/beatIcon/beatIcon.service',
    'modules/ims/beatIcon/beatIcon.setCode.ctl',
    'modules/ims/beatIcon/beatIcon.newCode.ctl',
    'css!modules/ims/beatIcon/beatIcon.css',
    'filestyle'
  ], function (ims) {
    var BeatController, CreatorController, SetPriceController, dateFormat, deepClone;
    deepClone = function (obj) {
      return JSON.parse(JSON.stringify(obj));
    };
    dateFormat = function (date, type, filter) {
      var iMilliseconds;
      if (typeof date === 'string') {
        iMilliseconds = Date.parse(date);
        if (!isNaN(iMilliseconds)) {
          date = new Date(iMilliseconds);
        }
      }
      type = (function () {
        switch (type) {
          case 0:
            return 'yyyy-MM-dd HH:mm:00';
          case 1:
            return 'yyyy-MM-dd';
          case 2:
            return 'HH:mm:00';
          default:
            return type;
        }
      })();
      return filter(date, type);
    };
    CreatorController = (function () {
      CreatorController.$inject = ['$modalInstance', '$filter', 'carts'];

      function CreatorController($modalInstance, $filter, carts) {
        this.instance = $modalInstance;
        this.date = $filter('date');
        this.carts = carts;
        this.beat = deepClone(this.beatDef);
        this.target = {
          p1: false,
          p2: false,
          p3: false,
          p4: false,
          p5: false
        };
      }

      CreatorController.prototype.option = 0;

      CreatorController.prototype.ok = function () {
        var part1, part2;
        part1 = dateFormat(this.beat.end, 1, this.date);
        part2 = dateFormat(this.time.end, 2, this.date);
        this.beat.end = part1 + ' ' + part2;

        var targets = "";
        _.each(this.target, function (val, name) {
          if (!val) return;
          targets += name.substr(1) + ",";
        });
        this.beat.targets = targets.substr(0, targets.length - 1);

        if (this.option == 1) {
          this.beat.repeat = true;
          this.beat.extended = false;
        } else if (this.option == 2) {
          this.beat.repeat = false;
          this.beat.extended = true;
        } else {
          this.beat.repeat = false;
          this.beat.extended = false;
        }

        return this.instance.close(this.beat);
      };

      CreatorController.prototype.cancel = function () {
        return this.instance.dismiss('cancel');
      };

      CreatorController.prototype.carts = [];

      CreatorController.prototype.beat = {};

      CreatorController.prototype.beatDef = {
        description: '',
        channel_id: '',
        cart_id: '',
        end: new Date(),
        template_url: ''
      };

      CreatorController.prototype.time = {
        end: new Date()
      };

      return CreatorController;

    })();
    SetPriceController = (function () {
      SetPriceController.$inject = ['$modalInstance', 'lastPrice'];

      function SetPriceController(instance, lastPrice) {
        this.instance = instance;
        this.price = lastPrice;
      }

      SetPriceController.prototype.ok = function () {
        if (/^\d+\.?\d*$/.test(this.price)) {
          return this.instance.close(parseFloat(this.price));
        } else {
          return this.message = '不是数字';
        }
      };

      SetPriceController.prototype.cancel = function () {
        return this.instance.dismiss('cancel');
      };

      SetPriceController.prototype.price = 0;

      SetPriceController.prototype.message = '';

      return SetPriceController;

    })();
    BeatController = (function () {
      BeatController.$inject = ['ImsBeatService', 'userService', '$filter', 'FileUploader', 'imsAction', 'DTOptionsBuilder', 'DTColumnBuilder', 'vConfirm', 'vAlert', '$modal', '$compile', '$scope', 'notify'];

      function BeatController(imsBeatService, user, filter, FileUploader, imsAction, DTOptionsBuilder, DTColumnBuilder, vConfirm, vAlert, $modal, $compile, $scope, notify) {
        var root, url;
        this.$compile = $compile;
        this.$scope = $scope;
        this.notify = notify;
        this.$uploadItems = bind(this.$uploadItems, this);
        this.onDtGetBeats = bind(this.onDtGetBeats, this);
        this.onDtGetItems = bind(this.onDtGetItems, this);
        this.formatCart = bind(this.formatCart, this);
        this.confirm = vConfirm;
        this.alert = vAlert;
        this.beatService = imsBeatService;
        this.$modal = $modal;
        url = imsAction.beatIcon;
        root = '/VoyageOne' + url.root;
        this.downItemsAction = root + url.downItems;
        this.downErrAction = root + url.downErr;
        this.itemUploader = new FileUploader({
          url: root + url.addItems
        });
        this.priceUploader = new FileUploader({
          url: root + url.saveItems
        });
        this.dateFilter = filter('date');
        this.dtBeats = {
          options: DTOptionsBuilder.newOptions()
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('searching', false)
            .withOption('ordering', false)
            .withOption('ajax', this.onDtGetBeats)
            .withOption('createdRow', (function (_this) {
              return function (row) {
                return _this.$compile(angular.element(row).contents())(_this.$scope);
              };
            })(this))
            .withDataProp('data')
            .withPaginationType('full_numbers'),
          columns: [
            DTColumnBuilder.newColumn('cart_id', '店铺').renderWith(this.formatCart),
            DTColumnBuilder.newColumn('description', '名称'),
            DTColumnBuilder.newColumn('end', '还原时间'),
            DTColumnBuilder.newColumn('template_url', '活动模板'),
            DTColumnBuilder.newColumn('targets', '披露图片的位置'),
            DTColumnBuilder.newColumn('repeat', '其他选项').renderWith(function(val, type, row) {
              return row.repeat ? "覆盖重复" : row.extended ? "插入重复" : "无";
            }),
            DTColumnBuilder.newColumn('modifier', '修改人'),
            DTColumnBuilder.newColumn('', '').renderWith(function (val, type, row, cell) {
              return "<button class=\"btn btn-warning btn-sm\" ng-click=\"ctrl.openBeat(" + cell.row + ")\">&nbsp;查看&nbsp;</button>";
            })
          ],
          instance: null
        };

        this.dtItems = {
          options: DTOptionsBuilder.newOptions()
            .withDataProp('data')
            .withPaginationType('full_numbers')
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('ordering', false)
            .withOption('ajax', this.onDtGetItems)
            .withOption('createdRow', (function (_this) {
              return function (row) {
                return _this.$compile(angular.element(row).contents())(_this.$scope);
              };
            })(this))
            .withOption('initComplete', (function (ctrl) {
              return function () {
                $('.dataTables_filter input')
                  .off()
                  .on('keypress', function (e) {
                    if (e.keyCode == 13) {
                      ctrl.dtItems.instance.dataTable.api().search(this.value).draw();
                    }
                  });
              };
            })(this)),
          columns: [
            DTColumnBuilder.newColumn('code', 'Code'),
            DTColumnBuilder.newColumn('num_iid', 'Num iid').renderWith(function (col) {
              return "<a target=\"_blank\" href=\"https://detail.tmall.hk/hk/item.htm?id=" + col + "\">" + col + "</a>";
            }),
            DTColumnBuilder.newColumn('price', 'Price'),
            DTColumnBuilder.newColumn('beat_flg', 'Status').renderWith((function (_this) {
              return function (col) {
                return _this.status[col.toString()];
              };
            })(this)),
            DTColumnBuilder.newColumn('comment', 'Comment'),
            DTColumnBuilder.newColumn('modifier', 'Modify').renderWith(function (col, type, row) {
              return row.modifier + "<br>" + row.cnModified;
            }),
            DTColumnBuilder.newColumn('', '').renderWith(function (val, type, row, cell) {
              return '<button class="btn btn-sm btn-success fix-sel" context-menu="operations" ng-click="ctrl.setCurrItem(' + cell.row + ')">操作<span class="caret"></span></button>';
            })
          ],
          instance: null
        };
        this.channels = user.getChannels();
        if (this.channels.length === 1) {
          this.selected.channel = this.channels[0].id;
          this.changeChannel();
        }
      }

      BeatController.prototype.status = {
        '0': '未执行',
        '1': '等待执行',
        '2': '刷图成功',
        '3': '刷图失败',
        '4': '还原成功',
        '5': '还原失败',
        '10': '取消',
        '11': '取消成功',
        '12': '取消失败'
      };

      BeatController.prototype.showItemPanel = false;

      BeatController.prototype.carts = [];

      BeatController.prototype.itemsType = false;

      BeatController.prototype.beats = [];

      BeatController.prototype.items = [];

      BeatController.prototype.beat = {};

      BeatController.prototype.selected = {
        channel: null,
        flg: null,
        lastChannel: null
      };

      BeatController.prototype.beatSummary = {};

      BeatController.prototype.$formatCart = function (col) {
        var cart, i, len, ref;
        ref = this.carts;
        for (i = 0, len = ref.length; i < len; i++) {
          cart = ref[i];
          if (parseInt(cart.cart_id) === col) {
            return "[ " + cart.comment + " ] " + cart.shop_name;
          }
        }
        return col;
      };

      BeatController.prototype.formatCart = function (col) {
        if (this.carts.length) {
          return this.$formatCart(col);
        } else {
          return col;
        }
      };

      BeatController.prototype.changeChannel = function () {
        var ref;
        this.selected.beat = null;
        this.beatService.getCarts(this.selected.channel).then((function (_this) {
          return function (carts) {
            return _this.carts = carts;
          };
        })(this));
        this.selected.lastChannel = this.selected.channel;
        return (ref = this.dtBeats.instance) != null ? ref.reloadData() : void 0;
      };

      BeatController.prototype.reloadData = function (index) {
        return (!index ? this.dtBeats : this.dtItems).instance.reloadData();
      };

      BeatController.prototype.createBeat = function () {
        if (!this.carts.length) {
          this.alert('NO_SELECTED_CART');
          return;
        }
        return this.$modal.open({
          templateUrl: 'createBeat.tpl.html',
          controller: CreatorController,
          controllerAs: 'ctrl',
          resolve: {
            carts: (function (_this) {
              return function () {
                return _this.carts;
              };
            })(this)
          }
        }).result.then((function (_this) {
            return function (beat) {
              beat.channel_id = _this.selected.channel;
              return _this.beatService.create(beat).then(function () {
                return _this.dtBeats.instance.reloadData();
              });
            };
          })(this));
      };

      BeatController.prototype.openBeat = function (index) {
        var beat;
        beat = this.beats[index];

        this.showItemPanel = true;

        if (beat === this.beat) {
          this.notify.success("任务已经打开");
          return;
        }
        this.beat = beat;
        this.dtItems.instance.reloadData();
      };

      BeatController.prototype.onDtGetItems = function (data, draw) {
        if (data.draw === 1 || !this.beat) {
          draw({
            data: [],
            recordsTotal: 0,
            recordsFiltered: 0
          });
          return;
        }
        data.param = {
          beat: this.beat,
          flg: this.selected.flg
        };
        return this.beatService.dtGetItems(data).then((function (_this) {
          return function (data) {
            draw(data.dtResponse);
            _this.items = data.dtResponse.data;
            return _this.beatSummary = data.beatSummary;
          };
        })(this));
      };

      BeatController.prototype.onDtGetBeats = function (data, draw) {
        if (!this.selected.channel) {
          draw({
            data: [],
            recordsTotal: 0,
            recordsFiltered: 0
          });
          return;
        }
        data.param = {
          channel_id: this.selected.channel
        };
        return this.beatService.dtGetBeats(data).then((function (_this) {
          return function (data) {
            _this.beats = data.data;
            return draw(data);
          };
        })(this));
      };

      BeatController.prototype.$uploadItems = function () {
        var fileItem, progress, ref;
        if (!this.beat || !this.beat.beat_id) {
          this.alert('NO_SELECTED_BEAT');
          return;
        }
        ref = this.itemUploader.queue, fileItem = ref[ref.length - 1];
        if (!fileItem) {
          this.alert('NO_UPLOAD_FILE');
          return;
        }
        progress = this.showProgress(fileItem);
        fileItem.onSuccess = (function (_this) {
          return function (res) {
            progress.dismiss('cancel');
            if (res.result !== 'OK') {
              _this.alert(res.message);
              return;
            }
            return _this.dtItems.instance.reloadData();
          };
        })(this);
        fileItem.formData = [{
          beat_id: this.beat.beat_id,
          isCode: this.itemsType
        }];
        return fileItem.upload();
      };

      BeatController.prototype.uploadItems = function () {
        if (!this.items || !this.items.length) {
          this.$uploadItems();
          return;
        }
        return this.confirm('提交新的商品会删除原来提交的所有内容。确定提交么？').then(this.$uploadItems);
      };

      BeatController.prototype.$downloadHelper = null;

      BeatController.prototype.$downFile = function (action) {
        this.downFileAction = action;
        if (!this.$downloadHelper) {
          this.$downloadHelper = $('#beat-downloadHelper')[0];
          this.$downloadHelper.onload = (function (_this) {
            return function () {
              var res;
              res = JSON.parse($(_this.$downloadHelper.contentDocument.body).text());
              if (res.result === 'NG') {
                return _this.alert(res.message);
              }
            };
          })(this);
        }
        return $('#beat-downloadForm').attr('action', action).submit();
      };

      BeatController.prototype.downBeatIconExcel = function () {
        if (!this.beat || !this.beat.beat_id) {
          this.alert('NO_SELECTED_BEAT');
          return;
        }
        this.$downFile(this.downItemsAction);
        return false;
      };

      BeatController.prototype.uploadPrice = function () {
        return this.confirm('确定提交价格表么？').then((function (_this) {
          return function () {
            var fileItem, progress, ref;
            if (!_this.beat || !_this.beat.beat_id) {
              _this.alert('NO_SELECTED_BEAT');
              return;
            }
            ref = _this.priceUploader.queue, fileItem = ref[ref.length - 1];
            if (!fileItem) {
              _this.alert('NO_UPLOAD_FILE');
              return;
            }
            progress = _this.showProgress(fileItem);
            fileItem.onSuccess = function (res) {
              progress.dismiss('cancel');
              if (res.result !== 'OK') {
                _this.alert(res.message);
                return;
              }
              return _this.alert('SUCCESS');
            };
            return fileItem.upload();
          };
        })(this));
      };

      BeatController.prototype.downErrExcel = function (lev) {
        if (!this.beat || !this.beat.beat_id) {
          this.alert('NO_SELECTED_BEAT');
          return;
        }
        $('#errLev').val(lev);
        this.$downFile(this.downErrAction);
        return false;
      };

      BeatController.prototype.beatControl = function (action) {
        if (!this.beat || !this.beat.beat_id) {
          this.alert('NO_SELECTED_BEAT。');
          return;
        }

        if (!this.currItem) return console.log('beatControl: noCurrItem');

        var item_id = this.currItem.beat_item_id;

        return this.beatService.beatControl(this.beat.beat_id, item_id, action).then((function (_this) {
          return function (count) {
            _this.notify.success("[ " + (item_id || 'all') + " ] " + action + " complete. return count: [ " + count + " ]");
            if (count) {
              return _this.dtItems.instance.reloadData();
            }
          };
        })(this));
      };

      BeatController.prototype.setPrice = function () {
        var item = this.currItem;
        if (!item) {
          return this.alert('NO_SELECTED_ITEM');
        }
        return this.$modal.open({
          templateUrl: 'setPrice.tpl.html',
          controller: SetPriceController,
          controllerAs: 'ctrl',
          resolve: {
            lastPrice: (function () {
              return function () {
                return item.price;
              };
            })(this)
          }
        }).result.then((function (_this) {
            return function (price) {
              if (!price) {
                _this.alert('PRICE_ERR');
                return;
              }
              return _this.beatService.setItemPrice(_this.beat.beat_id, item.beat_item_id, price).then(function (count) {
                if (!count) {
                  _this.notify.warning('NO_ROW_MODIFIED');
                }
                return _this.dtItems.instance.reloadData();
              });
            };
          })(this));
      };

      BeatController.prototype.showProgress = function (uploader) {
        return this.$modal.open({
          templateUrl: 'progress.tpl.html',
          backdrop: 'static',
          controller: [
            'uploader',
            function (uploader1) {
              this.uploader = uploader1;
            }
          ],
          controllerAs: 'ctrl',
          resolve: {
            uploader: function () {
              return uploader;
            }
          }
        });
      };

      BeatController.prototype.setCurrItem = function (rowIndex) {
        this.currItem = this.items[rowIndex];
      };

      BeatController.prototype.addCode = function () {
        var ctrl = this;

        this.$modal.open({
          templateUrl: 'modules/ims/beatIcon/beatIcon.newCode.tpl.html',
          backdrop: 'static',
          controller: 'ims.beat.NewCodeController',
          controllerAs: 'ctrl'
        }).result.then(function(obj) {
            var item = {
              beat_id: ctrl.beat.beat_id,
              code: '',
              num_iid: '',
              price: obj.price
            };
            if (obj.isCode) {
              item.code = obj.code;
            } else {
              item.num_iid = obj.code;
            }
            ctrl.beatService.addCode(item).then(function(count) {
              if (count) ctrl.notify.success("已成功新增商品 ヾ(′▽｀*)ゝ");
            });
          });
      };

      BeatController.prototype.setCode = function () {
        if (!this.currItem) return console.log('setCode: noCurrItem');

        var item = this.currItem;
        var ctrl = this;

        this.$modal.open({
          templateUrl: 'modules/ims/beatIcon/beatIcon.setCode.tpl.html',
          backdrop: 'static',
          controller: 'ims.beat.SetCodeController',
          controllerAs: 'ctrl',
          resolve: {
            lastCode: function () { return item.code; }
          }
        }).result.then(function(code) {
            item.code = code;
            ctrl.beatService.setCode(item).then(function(count) {
              if (count) ctrl.notify.success("已成功修改商品 (・ω< )★");
            });
          });
      };

      return BeatController;

    })();
    return ims.controller("BeatController", BeatController);
  });

})();