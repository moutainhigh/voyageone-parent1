package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductImportDao extends BaseDao {

	/**
	 * Product产品数据追加
	 *
	 * @param jmBtProductImportModel
	 * @return
	 */
	public boolean insertProductImportInfo(JmBtProductImportModel jmBtProductImportModel) {
		boolean isSuccess = true;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_product_import_insert", jmBtProductImportModel);
//		if (count > 0) {
//			isSuccess = true;
//		}

		return isSuccess;
	}

	/**
	 * 更新ProductImportModel
	 * @param jmBtProductImportModel
	 * @return
	 */
	public int updateProductImportInfo(JmBtProductImportModel jmBtProductImportModel){
		return updateTemplate.update("jm_bt_product_import_update", jmBtProductImportModel);
	}
}