INSERT INTO Synship.com_mt_task (task_type, task_name, task_comment, task_freq) VALUES
  ('cms', 'BcbgAnalysis', 'BCBG的Feed数据解析', '');


INSERT INTO Synship.tm_task_control (task_id, cfg_name, cfg_val1, cfg_val2, end_time, comment) VALUES
  ('BcbgAnalysis', 'run_flg', '1', '', '', 'BCBG的Feed数据解析');

# 变更 config 表的结构
ALTER TABLE `voyageone_cms`.`cms_mt_feed_config`
DROP PRIMARY KEY;
ALTER TABLE `voyageone_cms`.`cms_mt_feed_config`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT
FIRST,
ADD PRIMARY KEY (`id`);

# 老版本解析配置
INSERT INTO voyageone_cms.cms_mt_feed_config (order_channel_id, cfg_val2, cfg_val3, is_attribute, attribute_type, `comment`, cfg_name, cfg_val1)
VALUES
  ('012', '', '', 0, 0, '推送过来的数据文件存放的位置', 'feed_ftp_filename',
   '/opt/ftp-shared/clients-ftp/voyageone-bcbg-sftp/products;/Users/Jonas/Desktop/BCBG-20151015.json'),
  ('012', '', '', 0, 0, '数据文件的备份', 'feed_backup_dir', '/xvdb/ftp-shared/clients-ftp/voyageone-bcbg-sftp-bak/%s/%s'),

  ('012', '', '', 0, 0, '数据表', 'table_id', 'voyageone_cms.cms_zz_worktable_bcbg_superfeed'),
  ('012', '', '', 0, 0, '类目分割符', 'category_split', '-'),
  ('012', '', '', 0, 0, '类目分割符', 'category_column', 'MATKL'),

  ('012', '', '', 0, 0, 'Model 级别', 'model_table_id', 'voyageone_cms.cms_zz_worktable_bcbg_superfeed'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_table_join', 'JOIN voyageone_cms.cms_zz_worktable_bcbg_styles b ON b.styleID = CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_sql_ending', 'GROUP BY CONCAT(MATKL, ''-'', SATNR), MATKL, WLADG, BRAND_ID, SATNR, ATBEZ, SIZE1_ATINN'),

  ('012', '', '', 0, 0, 'Model 级别', 'model_url_key', 'CONCAT(MATKL, ''-'', SATNR)'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_category_url_key', 'MATKL'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_product_type', 'WLADG'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_brand', 'BRAND_ID'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_model', 'SATNR'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_name', 'LEFT(MAKTX, LOCATE('','', MAKTX) - 1)'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_short_description', ''''''),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_long_description', 'productDesc'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_size_type', '''Women''''s'''),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_is_unisex', '0'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_weight', ''''''),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_is_taxable', '1'),
  ('012', '', '', 0, 0, 'Model 级别', 'model_m_is_effective', '1'),

  ('012', '', '', 0, 0, 'Product 级别', 'product_table_id', 'voyageone_cms.cms_zz_worktable_bcbg_superfeed'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_table_join', 'JOIN voyageone_cms.cms_zz_worktable_bcbg_styles b ON b.styleID = CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_sql_ending', 'GROUP BY CONCAT(MATKL, ''-'', SATNR, COLOR),MATKL,CONCAT(MATKL, ''-'', SATNR),CONCAT(SATNR, ''-'', COLOR),COLOR_ATWTB,WHERL,productDesc,A304_KBETR,A073_KBETR,A304_KBETR,A073_KBETR,A073_KBETR'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_url_key', 'CONCAT(MATKL, ''-'', SATNR, COLOR)'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_category_url_key', 'MATKL'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_model_url_key', 'CONCAT(MATKL, ''-'', SATNR)'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_p_code', 'CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_p_name', 'MAX(LEFT(MAKTX, LOCATE('','', MAKTX, LOCATE('','', MAKTX) + 1) - 1))'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_p_color', 'COLOR_ATWTB'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_p_made_in_country', 'WHERL'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_pe_short_description', 'MAX(LEFT(MAKTX, LOCATE('','', MAKTX, LOCATE('','', MAKTX) + 1) - 1))'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_pe_long_description', 'productDesc'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_p_msrp', 'A304_KBETR'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_ps_price', 'A073_KBETR'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_cps_cn_price_rmb', 'A304_KBETR'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_cps_cn_price', 'A073_KBETR'),
  ('012', '', '', 0, 0, 'Product 级别', 'product_cps_cn_price_final_rmb', 'A073_KBETR'),

  ('012', '', '', 0, 0, 'Item 级别', 'item_code', 'CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Item 级别', 'item_i_sku', 'MATNR'),
  ('012', '', '', 0, 0, 'Item 级别', 'item_i_itemcode', 'CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Item 级别', 'item_i_size', 'SIZE1'),
  ('012', '', '', 0, 0, 'Item 级别', 'item_i_barcode', 'EAN11'),

  ('012', '', '', 0, 0, 'Image 级别', 'image_table_id', 'voyageone_cms.cms_zz_worktable_bcbg_styles'),
  ('012', '', '', 0, 0, 'Image 级别', 'image_table_join', 'JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed ON styleID = CONCAT(SATNR, ''-'', COLOR)'),
  ('012', '', '', 0, 0, 'Image 级别', 'images', 'productImgURLs'),
  ('012', '', '', 0, 0, 'Image 级别', 'image_split', ';;'),

  ('012', '', '', 0, 0, 'WebService 授权配置', 'webServiesAppKey', '21006636'),
  ('012', '', '', 0, 0, 'WebService 授权配置', 'webServiesAppSecret', 'ca16bd08019790b2a9332e000e52e19f'),
  ('012', '', '', 0, 0, 'WebService 授权配置', 'webServiesSessionKey',
   '7200a23ce180124c6Z248fa2bd5b420Zdf0df34db94bd5a90702966b');

# 属性配置
INSERT INTO voyageone_cms.cms_mt_feed_config (order_channel_id, cfg_name, cfg_val1, cfg_val2, cfg_val3, is_attribute, attribute_type, `comment`)
VALUES
  ('012', 'attribute1', 'MATNR', 'MATNR', '', '1', '0', ''),
  ('012', 'attribute2', 'EAN11', 'EAN11', '', '1', '0', ''),
  ('012', 'attribute3', 'BRAND_ID', 'BRAND_ID', '', '1', '0', ''),
  ('012', 'attribute4', 'MATKL', 'MATKL', '', '1', '0', ''),
  ('012', 'attribute5', 'ZZCODE1', 'ZZCODE1', '', '1', '0', ''),
  ('012', 'attribute6', 'ZZCODE2', 'ZZCODE2', '', '1', '0', ''),
  ('012', 'attribute7', 'ZZCODE3', 'ZZCODE3', '', '1', '0', ''),
  ('012', 'attribute8', 'MEINS', 'MEINS', '', '1', '0', ''),
  ('012', 'attribute9', 'BSTME', 'BSTME', '', '1', '0', ''),
  ('012', 'attribute10', 'COLOR', 'COLOR', '', '1', '0', ''),
  ('012', 'attribute11', 'COLOR_ATWTB', 'COLOR_ATWTB', '', '1', '0', ''),
  ('012', 'attribute12', 'SIZE1', 'SIZE1', '', '1', '0', ''),
  ('012', 'attribute13', 'SIZE1_ATWTB', 'SIZE1_ATWTB', '', '1', '0', ''),
  ('012', 'attribute14', 'SIZE1_ATINN', 'SIZE1_ATINN', '', '1', '0', ''),
  ('012', 'attribute15', 'ATBEZ', 'ATBEZ', '', '1', '0', ''),
  ('012', 'attribute16', 'SAISO', 'SAISO', '', '1', '0', ''),
  ('012', 'attribute17', 'SAISJ', 'SAISJ', '', '1', '0', ''),
  ('012', 'attribute18', 'SAITY', 'SAITY', '', '1', '0', ''),
  ('012', 'attribute19', 'SATNR', 'SATNR', '', '1', '0', ''),
  ('012', 'attribute20', 'MAKTX', 'MAKTX', '', '1', '0', ''),
  ('012', 'attribute21', 'WLADG', 'WLADG', '', '1', '0', ''),
  ('012', 'attribute22', 'WHERL', 'WHERL', '', '1', '0', ''),
  ('012', 'attribute23', 'MEAN_EAN11', 'MEAN_EAN11', '', '1', '0', ''),
  ('012', 'attribute24', 'A304_DATAB', 'A304_DATAB', '', '1', '0', ''),
  ('012', 'attribute25', 'A304_DATBI', 'A304_DATBI', '', '1', '0', ''),
  ('012', 'attribute26', 'A304_KBETR', 'A304_KBETR', '', '1', '0', ''),
  ('012', 'attribute27', 'A304_KONWA', 'A304_KONWA', '', '1', '0', ''),
  ('012', 'attribute28', 'A073_DATAB', 'A073_DATAB', '', '1', '0', ''),
  ('012', 'attribute29', 'A073_DATBI', 'A073_DATBI', '', '1', '0', ''),
  ('012', 'attribute30', 'A073_KBETR', 'A073_KBETR', '', '1', '0', ''),
  ('012', 'attribute31', 'A073_KONWA', 'A073_KONWA', '', '1', '0', ''),
  ('012', 'attribute32', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute33', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute34', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute35', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute36', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute37', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute38', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute39', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute40', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute41', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute42', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute43', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute44', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute45', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute46', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute47', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute48', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute49', '''''', '预留的空字符串', '', '1', '0', ''),
  ('012', 'attribute50', '''''', '预留的空字符串', '', '1', '0', '');

# BCBG解析新增配置
INSERT INTO voyageone_cms.cms_mt_feed_config (`comment`, order_channel_id, cfg_name, cfg_val1, cfg_val2, cfg_val3, is_attribute, attribute_type, display_sort)
VALUES
  # 先查一波数据,用来发送 master 关联的警告邮件
  ('Get data that cant context to master', '012', 'transform', 'select_str',
   'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON BRAND_ID = m.value AND m.channel_id = ''012'' AND m.master_attr = ''BRAND_ID'' WHERE m.value IS NULL',
   'set:noMasterSku', 0, 0, 1),
  ('Get data that cant context to master', '012', 'transform', 'select_str', 'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.channel_id = ''012'' AND m.master_attr = ''SAISO'' WHERE m.value IS NULL', 'append:noMasterSku', 0, 0, 2),
  ('Get data that cant context to master', '012', 'transform', 'select_str', 'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.channel_id = ''012'' AND m.master_attr = ''SAITY'' WHERE m.value IS NULL', 'append:noMasterSku', 0, 0, 3),
  ('Get data that cant context to master', '012', 'transform', 'select_str', 'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.channel_id = ''012'' AND m.master_attr = ''WLADG'' WHERE m.value IS NULL', 'append:noMasterSku', 0, 0, 4),
  ('Get data that cant context to master', '012', 'transform', 'select_str', 'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = ''012'' AND m.master_attr = ''MATKL'' WHERE m.value IS NULL', 'append:noMasterSku', 0, 0, 5),
  # 定义发邮件操作
  ('Send mail with last step get data', '012', 'transform', 'mail', '这是BCBG数据处理的测试邮件', 'get:noMasterSku', 0, 0, 6),
  # 删除 master 关联失败的
  ('Clear data that cant context to master', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON BRAND_ID = m.value AND m.channel_id = ''012'' AND m.master_attr = ''BRAND_ID'' WHERE m.value IS NULL', '', 0, 0, 7),
  ('Clear data that cant context to master', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.channel_id = ''012'' AND m.master_attr = ''SAISO'' WHERE m.value IS NULL', '', 0, 0, 7),
  ('Clear data that cant context to master', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.channel_id = ''012'' AND m.master_attr = ''SAITY'' WHERE m.value IS NULL', '', 0, 0, 7),
  ('Clear data that cant context to master', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.channel_id = ''012'' AND m.master_attr = ''WLADG'' WHERE m.value IS NULL', '', 0, 0, 7),
  ('Clear data that cant context to master', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed LEFT JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = ''012'' AND m.master_attr = ''MATKL'' WHERE m.value IS NULL', '', 0, 0, 7),
  # 查一波 DO NOT USE 类目的数据,发一波邮件
  ('Get data that category is DO NOT USE', '012', 'transform', 'select_str', 'SELECT MATNR FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = ''012'' AND m.master_attr = ''MATKL'' AND m.label = ''DO NOT USE''', 'set:noCategorySku', 0, 0, 8),
  # 定义发邮件操作
  ('Send mail with last step get data', '012', 'transform', 'mail', '这是BCBG数据处理的测试邮件', 'get:noCategorySku', 0, 0, 9),
  # 删除 DO NOT USE
  ('Clear no category data', '012', 'transform', 'delete', 'DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = ''012'' AND m.master_attr = ''MATKL'' AND m.label = ''DO NOT USE''', '', 0, 0, 10),
  # 转换一波 master 数据
  ('Update data to master', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON BRAND_ID = m.value AND m.master_attr = ''BRAND_ID'' AND m.channel_id = ''012'' SET BRAND_ID = label', '', 0, 0, 11),
  ('Update data to master', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.master_attr = ''SAISO'' AND m.channel_id = ''012'' SET SAISO = label', '', 0, 0, 12),
  ('Update data to master', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.master_attr = ''SAITY'' AND m.channel_id = ''012'' SET SAITY = label', '', 0, 0, 13),
  ('Update data to master', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.master_attr = ''WLADG'' AND m.channel_id = ''012'' SET WLADG = label', '', 0, 0, 14),
  ('Update data to master', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.master_attr = ''MATKL'' AND m.channel_id = ''012'' SET MATKL = label', '', 0, 0, 15),
  # 标记数据
  ('Update the flg', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf ON b.MATNR = bf.MATNR SET b.update_flg = 1 WHERE bf.MATNR IS NULL', '', 0, 0, 16),
  ('Update the flg', '012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf ON b.MATNR = bf.MATNR SET b.update_flg = 2 WHERE bf.MATNR IS NOT NULL', '', 0, 0, 17);

COMMIT;