package com.voyageone.components.channeladvisor;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.components.ComponentBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * Created by sn3 on 2015-07-29.
 */
public class CaBase extends ComponentBase {

    public static final int C_MAX_API_REPEAT_TIME = 3;

    protected String reqCaApi(String postUrl, String postAction, String postData) throws Exception {

        return HttpUtils.PostSoap(postUrl, postAction, postData);

    }

    /**
     * 超时的话，自动重调
     * @param postUrl String
     * @param postAction String
     * @param postData String
     * @return String
     */
    protected String reqCaApiOnTimeoutRepert(String postUrl, String postAction, String postData) throws Exception {


        for (int intApiErrorCount = 0; intApiErrorCount < C_MAX_API_REPEAT_TIME; intApiErrorCount++) {
            try {
                return HttpUtils.PostSoap(postUrl, postAction, postData);
            } catch (Exception e) {
                // 最后一次出错则直接抛出
                Thread.sleep(1000);
                if (C_MAX_API_REPEAT_TIME - intApiErrorCount == 1) throw e;
            }
        }

        return "";
    }

    /**
     * 交互XML保存
     * @param orderNumber 订单号
     * @param strXML 交互XML
     * @param type 类型（0：post 1:ret）
     */
    protected void backupTheXmlFile(String orderNumber, String strXML, int type) throws IOException {

        String strFolder = Properties.readValue("PostBackup") + File.separator + this.getClass().getName();
        File file = new File(strFolder);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        java.util.Date date = new java.util.Date();
        String fileName = orderNumber + "_" + sdf.format(date);
        FileWriter fs;

        if (type == 0) {
            fs = new FileWriter(strFolder + File.separator + "post_onestop_" + fileName + ".xml");
        } else {
            fs = new FileWriter(strFolder + File.separator + "ret_onestop_" + fileName + ".xml");
        }
        fs.write(strXML);
        fs.flush();
        fs.close();
    }
}