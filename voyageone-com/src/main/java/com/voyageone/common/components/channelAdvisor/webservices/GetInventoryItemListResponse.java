
package com.voyageone.common.components.channelAdvisor.webservices;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetInventoryItemListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfInventoryItemResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getInventoryItemListResult"
})
@XmlRootElement(name = "GetInventoryItemListResponse")
public class GetInventoryItemListResponse {

    @XmlElement(name = "GetInventoryItemListResult")
    protected APIResultOfArrayOfInventoryItemResponse getInventoryItemListResult;

    /**
     * Gets the value of the getInventoryItemListResult property.
     * 
     * @return
     *     possible object is
     *     {@link APIResultOfArrayOfInventoryItemResponse }
     *     
     */
    public APIResultOfArrayOfInventoryItemResponse getGetInventoryItemListResult() {
        return getInventoryItemListResult;
    }

    /**
     * Sets the value of the getInventoryItemListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link APIResultOfArrayOfInventoryItemResponse }
     *     
     */
    public void setGetInventoryItemListResult(APIResultOfArrayOfInventoryItemResponse value) {
        this.getInventoryItemListResult = value;
    }

}
