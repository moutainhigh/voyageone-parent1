package com.voyageone.components.channeladvisor.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.voyageone.components.channeladvisor.bean.orders.OrderResponseDetailHigh;
import com.voyageone.components.channeladvisor.bean.orders.OrderResponseItem;

/**
 * <p>
 * Java class for ArrayOfOrderResponseItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderResponseItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderResponseItem" type="{http://api.channeladvisor.com/datacontracts/orders}OrderResponseItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderResponseItem", propOrder = { "orderResponseItem" })
public class ArrayOfOrderResponseItem {

	@XmlElement(name = "OrderResponseItem", nillable = true)
	protected List<OrderResponseDetailHigh> orderResponseItem;

	/**
	 * Gets the value of the orderResponseItem property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderResponseItem property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOrderResponseItem().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderResponseItem }
	 * 
	 * 
	 */
	public List<OrderResponseDetailHigh> getOrderResponseItem() {
		if (orderResponseItem == null) {
			orderResponseItem = new ArrayList<OrderResponseDetailHigh>();
		}
		return this.orderResponseItem;
	}

}
