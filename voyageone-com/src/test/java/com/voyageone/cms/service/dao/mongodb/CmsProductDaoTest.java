package com.voyageone.cms.service.dao.mongodb;


import com.mongodb.*;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.*;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductDaoTest {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSelectProductById() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel model = cmsBtProductDao.selectById("565d97127b69bf04fdd12450", "100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        System.out.println(model.toString());
    }

    @Test
    public void testSelectProductByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel model = cmsBtProductDao.selectProductByCode("100", "100011");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        System.out.println(model.toString());
    }

    @Test
    public void testSelectProductLikeCatIdPath() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> modelList = cmsBtProductDao.selectLeftLikeCatIdPath("100", "-100-10000-17");
        System.out.println("time total:=" + (System.currentTimeMillis() - start) + "; size:=" + modelList.size());
        for (CmsBtProductModel model : modelList) {
            //System.out.println(model.toString());
        }
    }

    @Test
    public void selectAllReturnCursor() {
        long start = System.currentTimeMillis();
        Iterator<CmsBtProductModel> iterator = cmsBtProductDao.selectCursorAll("100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        int count = 0;
        while(iterator.hasNext()) {
            CmsBtProductModel model = iterator.next();
            System.out.println(model.toString());
            count++;
            if (count>10) {
                break;
            }
        }
    }

    @Test
    public void testUpsertTrue() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        BasicDBObject o1 = new BasicDBObject();
        o1.append("$set", new BasicDBObject("name", "Innova1"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");

        WriteResult c1 = coll.update(query, o1, true, false);
        DBCursor carcursor = coll.find();
        try {
            while (carcursor.hasNext()) {
                System.out.println(carcursor.next());
            }
        } finally {
            carcursor.close();
        }
    }

    @Test
    public void testUpsertBulkUnorderedDocsForUpdate() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");
        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        o1.append("$setOnInsert", new BasicDBObject("name", "innova").append("speed", 54));
        o1.append("$set", new BasicDBObject("cno", "H456"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");

        b1.find(query).upsert().update(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

    @Test
    public void upsertBulkUnordereDocsForUpdateOne() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        o1.append(
                "$set",new BasicDBObject("name", "Xylo").append("speed", 67).append("cno", "H654"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().updateOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

    @Test
    public void upsertBulkForUpdateOneWithOperators() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");
        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        // insert if document not found and set the fields with updated value
        o1.append("$setOnInsert", new BasicDBObject("cno", "H123"));
        o1.append("$set", new BasicDBObject("speed", "63"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().updateOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }
    }

    @Test
    public void upsertBulkUnorderedDocsForReplaceOne()
            throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

        // insert query
        BasicDBObject o1 = new BasicDBObject("name", "Qualis").append("speed", null).append("color", "Palebrown");

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().replaceOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

}