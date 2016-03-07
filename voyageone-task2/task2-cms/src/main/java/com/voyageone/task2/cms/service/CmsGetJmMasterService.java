package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.JmMasterBean;
import com.voyageone.task2.cms.dao.JmCategoryDao;
import com.voyageone.task2.cms.dao.JmMasterDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.Bean.JmBrandBean;
import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.Bean.JmCurrencyBean;
import com.voyageone.common.components.jumei.Bean.JmWarehouseBean;
import com.voyageone.common.components.jumei.JumeiBrandService;
import com.voyageone.common.components.jumei.JumeiCategoryService;
import com.voyageone.common.components.jumei.JumeiCurrencyService;
import com.voyageone.common.components.jumei.JumeiWarehouseService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsGetJmMasterService extends BaseTaskService {

    @Autowired
    private JmCategoryDao jmCategoryDao;
    @Autowired
    private JmMasterDao jmMasterDao;

    @Autowired
    private JumeiBrandService jumeiBrandService;

    @Autowired
    private JumeiCategoryService jumeiCategoryService;

    @Autowired
    private JumeiCurrencyService jumeiCurrencyService;

    @Autowired
    private JumeiWarehouseService jumeiWarehouseService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetJmMasterJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        ShopBean shopBean = ShopConfigs.getShop("001", CartEnums.Cart.JM.getId());
//        shopBean.setAppKey("131");
//        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
//        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
//        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        insertCategory(shopBean);
        insertBrand(shopBean);
        inserCurrency(shopBean);
        inserWarehouse(shopBean);

    }

    /**
     * 插入分类信息
     *
     * @param shopBean
     * @throws Exception
     */
    public void insertCategory(ShopBean shopBean) throws Exception {
        try {
            List<JmCategoryBean> categorys = jumeiCategoryService.getCategoryListALL(shopBean);
            jmCategoryDao.clearJmCategory();
            jmCategoryDao.insertJmCategory(categorys);
        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, getSubSystem());
        }
    }

    /**
     * 插入品牌
     *
     * @param shopBean
     * @throws Exception
     */
    public void insertBrand(ShopBean shopBean) throws Exception {
        try {
            List<JmBrandBean> JmBrands = jumeiBrandService.getBrands(shopBean);
            jmMasterDao.clearJmMasterByCode("0");
            List<JmMasterBean> jmMasterBeans = new ArrayList<>();
            for (JmBrandBean jmBrandBean : JmBrands) {
                jmMasterBeans.add(new JmMasterBean(jmBrandBean, getTaskName()));
            }
            jmMasterDao.insertJmMaster(jmMasterBeans);
        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, getSubSystem());
        }

    }

    /**
     * 插入货币
     *
     * @param shopBean
     * @throws Exception
     */
    public void inserCurrency(ShopBean shopBean) throws Exception {
        try {
            List<JmCurrencyBean> jmCurrencys = jumeiCurrencyService.getCurrencys(shopBean);
            jmMasterDao.clearJmMasterByCode("1");
            List<JmMasterBean> jmMasterBeans = new ArrayList<>();
            for (JmCurrencyBean jmCurrenc : jmCurrencys) {
                jmMasterBeans.add(new JmMasterBean(jmCurrenc, getTaskName()));
            }
            jmMasterDao.insertJmMaster(jmMasterBeans);
        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, getSubSystem());
        }
    }
    /**
     * 插入仓库
     *
     * @param shopBean
     * @throws Exception
     */
    public void inserWarehouse(ShopBean shopBean) {
        try {
            List<JmWarehouseBean> jmWarehouses = jumeiWarehouseService.getWarehouseList(shopBean);
            jmMasterDao.clearJmMasterByCode("2");
            List<JmMasterBean> jmMasterBeans = new ArrayList<>();
            for (JmWarehouseBean jmWarehouse : jmWarehouses) {
                jmMasterBeans.add(new JmMasterBean(jmWarehouse, getTaskName()));
            }
            jmMasterDao.insertJmMaster(jmMasterBeans);
        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, getSubSystem());
        }
    }
}