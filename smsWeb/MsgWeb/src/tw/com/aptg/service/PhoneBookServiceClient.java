/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2018
 *
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20180329    Gary Chang          initial
 */

package tw.com.aptg.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType;
import com.truetel.war.aptpns.iweb.ws.pbk.ErrorException;
import com.truetel.war.aptpns.iweb.ws.pbk.GroupType;
import com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi;
import com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApiImplService;

import tw.com.aptg.beans.AddressBookGroup;

public class PhoneBookServiceClient {
	private static final Logger logger = LogManager.getFormatterLogger(PhoneBookServiceClient.class.getName()); 
	private static final QName SERVICE_NAME = new QName("http://truetel.com/war/aptpns/iweb/ws/pbk", "PhonebookApiImplService");
	
	private String subId = ""; //合約編號
	private static PhonebookApi port = null;
	
	public PhoneBookServiceClient(String subId) {
         this.subId = subId;
 		 URL wsdlURL = PhonebookApiImplService.WSDL_LOCATION;
 		 PhonebookApiImplService ws = new PhonebookApiImplService(wsdlURL, SERVICE_NAME);
 		 port = ws.getPhonebookApiPort();
	}
	
    /**
     * 
     * 取得通訊錄聯絡人之電話
     * exactGroupName value空白表示查詢預設群組
     * 
     */
	public List<ContactFullType> getListContacts(String exactGroupName) {
		logger.info("Invoking listContacts...");
		com.truetel.war.aptpns.iweb.ws.pbk.ListContactsReq _listContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.ListContactsReq();
		com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType _listContacts_parametersQueryContact = new com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType();
		_listContacts_parametersQueryContact.setExactGroupName(exactGroupName);
		_listContacts_parameters.setQueryContact(_listContacts_parametersQueryContact);
		_listContacts_parameters.setSubId(this.subId);		
		List<ContactFullType> contactsList = null;
		try {			
			com.truetel.war.aptpns.iweb.ws.pbk.ListContactsResp _listContacts__return = port.listContacts(_listContacts_parameters);		
			contactsList = _listContacts__return.getContact();				
		} catch (ErrorException e) {
			logger.error("", e);
		}
		return contactsList;	
	}
	
	/**
     * 
     * 取得通訊錄所有群組名稱(不包含筆數)
     * 
     */
	public List<GroupType> getListGroups() {
        logger.info("Invoking listGroups...");
        com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsReq _listGroups_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsReq();
        _listGroups_parameters.setSubId(this.subId);
        List<GroupType> groupsList = null;
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsResp _listGroups__return = port.listGroups(_listGroups_parameters);
            groupsList = _listGroups__return.getGroup();
        } catch (ErrorException e) { 
        	logger.error("", e);
        }
        return groupsList;
	}
	
	
    /**
     * 
     * 取得群組名稱之對映筆數
     * 
     */
	public int getCountContacts(String exactGroupName) {
        logger.info("Invoking countContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.CountContactsReq _countContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.CountContactsReq();
        com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType _countContacts_parametersQueryContact = new com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType();
        _countContacts_parametersQueryContact.setExactGroupName(exactGroupName);
        _countContacts_parameters.setQueryContact(_countContacts_parametersQueryContact);
        _countContacts_parameters.setSubId(this.subId);
        int count = 0;
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CountContactsResp _countContacts__return = port.countContacts(_countContacts_parameters);
            count = _countContacts__return.getNumContacts();
        } catch (ErrorException e) { 
        	logger.error("", e);
        }
		return count;
	}
	
    /**
     * 
     * 取得通訊錄中所有群組名稱及對映筆數
     * 
     */
	public List<AddressBookGroup> getGroupAddressBooks() {
		List<GroupType> groupsList = getListGroups();
		Iterator<GroupType> groupsIter = groupsList.iterator();
		List<AddressBookGroup> list = new ArrayList<AddressBookGroup>();
		while (groupsIter.hasNext()) {
			AddressBookGroup addressBookGroup = new AddressBookGroup();
			GroupType group = (GroupType)groupsIter.next();
			addressBookGroup.setGroupName(group.getGroupName());
			addressBookGroup.setGroupDesc(group.getGroupDesc());
			addressBookGroup.setCount(getCountContacts(group.getGroupName()));
			list.add(addressBookGroup);
		}
		return list;
	}
	
	
	//For Test Sample
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String subId = "4G-14122301116";
		String groupName = "公司同事";
		PhoneBookServiceClient client = new PhoneBookServiceClient(subId);
		List<ContactFullType> contactsList = client.getListContacts(groupName);
		Iterator<ContactFullType> contactsIter = contactsList.iterator();	
		System.out.println("groupName=" + groupName);
		int i = 0;
		while (contactsIter.hasNext()) {
			i++;
			System.out.println("----- 第" + i + "筆 -----");
			ContactFullType contact = (ContactFullType)contactsIter.next();	
			System.out.println("姓氏:" + contact.getFamilyName()); //姓氏  (required)
			System.out.println("名字:" +contact.getGivenName());  //名字  (required)
			System.out.println("暱稱:" +contact.getNickName()); //暱稱
			System.out.println("行動電話:" +contact.getCell()); //行動電話 (required)
			System.out.println("電話號碼:" +contact.getPhone());//電話號碼
			System.out.println("電子郵件:" +contact.getEmail());//電子郵件
			System.out.println("生日:" +contact.getBirthday());//生日
			System.out.println("ContactId:" +contact.getContactId());
		}
		
		List<GroupType> groupsList = client.getListGroups();
		Iterator<GroupType> groupsIter = groupsList.iterator();
		int j = 0;
		while (groupsIter.hasNext()) {
			j++;
			System.out.println("----- 第" + j + "筆 -----");
			GroupType group = (GroupType)groupsIter.next();
			System.out.println("GroupName:" + group.getGroupName());
			System.out.println("GroupDesc:" + group.getGroupDesc());
			System.out.println("Count:" + client.getCountContacts(group.getGroupName()));	
		} 	
	}

}
