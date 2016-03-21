package com.voyageone.task2.cms.model;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/9
 */
public class CmsBtFeedInfoVtmModel extends CmsBtFeedInfoModel {
    private String Manufacturer;
    private String DosageSize;
    private String DosageUnits;
    private String SuggestedUse;
    private String Warnings;

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getDosageSize() {
        return DosageSize;
    }

    public void setDosageSize(String dosageSize) {
        DosageSize = dosageSize;
    }

    public String getDosageUnits() {
        return DosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        DosageUnits = dosageUnits;
    }

    public String getSuggestedUse() {
        return SuggestedUse;
    }

    public void setSuggestedUse(String suggestedUse) {
        SuggestedUse = suggestedUse;
    }

    public String getWarnings() {
        return Warnings;
    }

    public void setWarnings(String warnings) {
        Warnings = warnings;
    }

    public CmsBtFeedInfoModel getCmsBtFeedInfoModel(){
        CmsBtFeedInfoModel cmsBtFeedInfoModel =new CmsBtFeedInfoModel(this.channelId);
        cmsBtFeedInfoModel.setCategory(this.getCategory());
        cmsBtFeedInfoModel.setCode(this.getCode());
        cmsBtFeedInfoModel.setName(this.getName());
        cmsBtFeedInfoModel.setModel(this.getModel());
        cmsBtFeedInfoModel.setColor(this.getColor());
        cmsBtFeedInfoModel.setOrigin(this.getOrigin());
        cmsBtFeedInfoModel.setSizeType(this.getSizeType());
        if(this.getImage().size()>0){
            cmsBtFeedInfoModel.setImage(Arrays.asList(this.getImage().get(0).split(",")));
        }else{
            cmsBtFeedInfoModel.setImage(new ArrayList<>());
        }
        cmsBtFeedInfoModel.setBrand(this.getBrand());
        cmsBtFeedInfoModel.setWeight(this.getWeight());
        cmsBtFeedInfoModel.setShort_description(this.getShort_description());
        cmsBtFeedInfoModel.setLong_description(this.getLong_description());
        cmsBtFeedInfoModel.setSkus(this.getSkus());
        cmsBtFeedInfoModel.setUpdFlg(0);
        return  cmsBtFeedInfoModel;
    }
}