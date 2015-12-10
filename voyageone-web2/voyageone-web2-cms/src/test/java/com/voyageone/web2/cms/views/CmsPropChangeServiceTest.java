package com.voyageone.web2.cms.views;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPropChangeServiceTest {

    @Autowired
    private CmsPropChangeService cmsPropChangeService;

    @Test
    public void testGetPropOptions() throws Exception {
        List<JSONObject> resultList = cmsPropChangeService.getPropOptions("001");
        System.out.println(resultList);
        assert resultList.size() > 0;
    }

    @Test
    public void testSavePropOptions() throws Exception {
        JSONObject params = new JSONObject();
        JSONArray codes = new JSONArray();
        codes.add("100001");
        codes.add("100002");
        params.put("channelId","001");
        params.put("propId","test1");
        params.put("propValue","ng");
        params.put("codes",codes);
        cmsPropChangeService.savePropOptions(params);
        assert true;
    }

}