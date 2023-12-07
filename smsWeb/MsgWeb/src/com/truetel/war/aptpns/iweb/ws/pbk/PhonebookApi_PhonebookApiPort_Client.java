
package com.truetel.war.aptpns.iweb.ws.pbk;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2018-03-29T10:49:30.525+08:00
 * Generated source version: 2.7.18
 * 
 */
public final class PhonebookApi_PhonebookApiPort_Client {

    private static final QName SERVICE_NAME = new QName("http://truetel.com/war/aptpns/iweb/ws/pbk", "PhonebookApiImplService");

    private PhonebookApi_PhonebookApiPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = PhonebookApiImplService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        PhonebookApiImplService ss = new PhonebookApiImplService(wsdlURL, SERVICE_NAME);
        PhonebookApi port = ss.getPhonebookApiPort();  
        
        {
        System.out.println("Invoking updateContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsReq _updateContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsReq();
        java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType> _updateContacts_parametersContact = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType>();
        com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType _updateContacts_parametersContactVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType();
        _updateContacts_parametersContactVal1.setContactId(Integer.valueOf(-178564455));
        _updateContacts_parametersContactVal1.setFamilyName("FamilyName-428010414");
        _updateContacts_parametersContactVal1.setGivenName("GivenName893989796");
        _updateContacts_parametersContactVal1.setCell("Cell1290663594");
        _updateContacts_parametersContactVal1.setCellHome("CellHome-1893130568");
        _updateContacts_parametersContactVal1.setCellWork("CellWork1801101713");
        _updateContacts_parametersContactVal1.setPhone("Phone-650510849");
        _updateContacts_parametersContactVal1.setPhoneHome("PhoneHome-1755722300");
        _updateContacts_parametersContactVal1.setPhoneWork("PhoneWork2013180672");
        _updateContacts_parametersContactVal1.setFax("Fax1410077536");
        _updateContacts_parametersContactVal1.setEmail("Email-1148212354");
        _updateContacts_parametersContactVal1.setEmailHome("EmailHome817749331");
        _updateContacts_parametersContactVal1.setEmailWork("EmailWork-577908248");
        _updateContacts_parametersContactVal1.setWebUrlHome("WebUrlHome-1189948809");
        _updateContacts_parametersContactVal1.setWebUrlWork("WebUrlWork79116614");
        _updateContacts_parametersContactVal1.setCompany("Company1166722987");
        _updateContacts_parametersContactVal1.setJobTitle("JobTitle1644509190");
        _updateContacts_parametersContactVal1.setNickName("NickName125452456");
        _updateContacts_parametersContactVal1.setCountry("Country2036156966");
        _updateContacts_parametersContactVal1.setPostcode("Postcode-245669531");
        _updateContacts_parametersContactVal1.setCity("City740252285");
        _updateContacts_parametersContactVal1.setAddress("Address1093339203");
        _updateContacts_parametersContactVal1.setBirthday(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2018-03-29T10:49:30.651+08:00"));
        _updateContacts_parametersContactVal1.setYahooId("YahooId-1620390861");
        _updateContacts_parametersContactVal1.setMsLiveId("MsLiveId-138794655");
        _updateContacts_parametersContactVal1.setGoogleId("GoogleId414849203");
        _updateContacts_parametersContactVal1.setSkypeId("SkypeId1310293436");
        _updateContacts_parametersContactVal1.setNote("Note900588436");
        _updateContacts_parametersContactVal1.setIndex(Integer.valueOf(-847790834));
        _updateContacts_parametersContact.add(_updateContacts_parametersContactVal1);
        _updateContacts_parameters.getContact().addAll(_updateContacts_parametersContact);
        _updateContacts_parameters.setSubId("SubId-2016717497");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsResp _updateContacts__return = port.updateContacts(_updateContacts_parameters);
            System.out.println("updateContacts.result=" + _updateContacts__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking listContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.ListContactsReq _listContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.ListContactsReq();
        com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType _listContacts_parametersQueryContact = new com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType();
        _listContacts_parametersQueryContact.setGroupName("GroupName-1698302069");
        _listContacts_parametersQueryContact.setExactGroupName("ExactGroupName-1113182567");
        _listContacts_parametersQueryContact.setContactId(Integer.valueOf(-465711896));
        _listContacts_parametersQueryContact.setFamilyName("FamilyName1934731842");
        _listContacts_parametersQueryContact.setGivenName("GivenName1452294227");
        _listContacts_parametersQueryContact.setCell("Cell1858647826");
        _listContacts_parametersQueryContact.setNameOrCell("NameOrCell-1698802366");
        _listContacts_parameters.setQueryContact(_listContacts_parametersQueryContact);
        _listContacts_parameters.setSubId("SubId1830792331");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.ListContactsResp _listContacts__return = port.listContacts(_listContacts_parameters);
            System.out.println("listContacts.result=" + _listContacts__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking createContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsReq _createContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsReq();
        java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactBaseType> _createContacts_parametersContact = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactBaseType>();
        com.truetel.war.aptpns.iweb.ws.pbk.ContactBaseType _createContacts_parametersContactVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactBaseType();
        _createContacts_parametersContactVal1.setFamilyName("FamilyName-389183674");
        _createContacts_parametersContactVal1.setGivenName("GivenName453059806");
        _createContacts_parametersContactVal1.setCell("Cell-1818812603");
        _createContacts_parametersContactVal1.setCellHome("CellHome1800630944");
        _createContacts_parametersContactVal1.setCellWork("CellWork-1465748793");
        _createContacts_parametersContactVal1.setPhone("Phone-897907263");
        _createContacts_parametersContactVal1.setPhoneHome("PhoneHome-268579272");
        _createContacts_parametersContactVal1.setPhoneWork("PhoneWork-1976348029");
        _createContacts_parametersContactVal1.setFax("Fax1872312656");
        _createContacts_parametersContactVal1.setEmail("Email-1441317253");
        _createContacts_parametersContactVal1.setEmailHome("EmailHome-238486271");
        _createContacts_parametersContactVal1.setEmailWork("EmailWork-398193935");
        _createContacts_parametersContactVal1.setWebUrlHome("WebUrlHome1275454613");
        _createContacts_parametersContactVal1.setWebUrlWork("WebUrlWork832502037");
        _createContacts_parametersContactVal1.setCompany("Company-1901121558");
        _createContacts_parametersContactVal1.setJobTitle("JobTitle-1107571751");
        _createContacts_parametersContactVal1.setNickName("NickName-725015831");
        _createContacts_parametersContactVal1.setCountry("Country-1777949571");
        _createContacts_parametersContactVal1.setPostcode("Postcode1021091496");
        _createContacts_parametersContactVal1.setCity("City-1061629428");
        _createContacts_parametersContactVal1.setAddress("Address2116307681");
        _createContacts_parametersContactVal1.setBirthday(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2018-03-29T10:49:30.687+08:00"));
        _createContacts_parametersContactVal1.setYahooId("YahooId-165095083");
        _createContacts_parametersContactVal1.setMsLiveId("MsLiveId-1282598247");
        _createContacts_parametersContactVal1.setGoogleId("GoogleId-1478915536");
        _createContacts_parametersContactVal1.setSkypeId("SkypeId-984124106");
        _createContacts_parametersContactVal1.setNote("Note-1903544313");
        _createContacts_parametersContactVal1.setIndex(Integer.valueOf(23565645));
        _createContacts_parametersContact.add(_createContacts_parametersContactVal1);
        _createContacts_parameters.getContact().addAll(_createContacts_parametersContact);
        _createContacts_parameters.setSubId("SubId1715433210");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsResp _createContacts__return = port.createContacts(_createContacts_parameters);
            System.out.println("createContacts.result=" + _createContacts__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking createGroup...");
        com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupReq _createGroup_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupReq();
        com.truetel.war.aptpns.iweb.ws.pbk.GroupType _createGroup_parametersGroup = new com.truetel.war.aptpns.iweb.ws.pbk.GroupType();
        _createGroup_parametersGroup.setGroupName("GroupName820826351");
        _createGroup_parametersGroup.setGroupDesc("GroupDesc1345623068");
        _createGroup_parameters.setGroup(_createGroup_parametersGroup);
        _createGroup_parameters.setSubId("SubId2049772360");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupResp _createGroup__return = port.createGroup(_createGroup_parameters);
            System.out.println("createGroup.result=" + _createGroup__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking countContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.CountContactsReq _countContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.CountContactsReq();
        com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType _countContacts_parametersQueryContact = new com.truetel.war.aptpns.iweb.ws.pbk.QueryContactType();
        _countContacts_parametersQueryContact.setGroupName("GroupName-379251045");
        _countContacts_parametersQueryContact.setExactGroupName("ExactGroupName2082641343");
        _countContacts_parametersQueryContact.setContactId(Integer.valueOf(-1194786610));
        _countContacts_parametersQueryContact.setFamilyName("FamilyName-568011004");
        _countContacts_parametersQueryContact.setGivenName("GivenName885203834");
        _countContacts_parametersQueryContact.setCell("Cell-283690837");
        _countContacts_parametersQueryContact.setNameOrCell("NameOrCell-741502245");
        _countContacts_parameters.setQueryContact(_countContacts_parametersQueryContact);
        _countContacts_parameters.setSubId("SubId1271705660");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CountContactsResp _countContacts__return = port.countContacts(_countContacts_parameters);
            System.out.println("countContacts.result=" + _countContacts__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking deleteContacts...");
        com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsReq _deleteContacts_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsReq();
        java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType> _deleteContacts_parametersContact = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType>();
        com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType _deleteContacts_parametersContactVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType();
        _deleteContacts_parametersContactVal1.setContactId(-1278800860);
        _deleteContacts_parametersContactVal1.setIndex(Integer.valueOf(-378419473));
        _deleteContacts_parametersContact.add(_deleteContacts_parametersContactVal1);
        _deleteContacts_parameters.getContact().addAll(_deleteContacts_parametersContact);
        _deleteContacts_parameters.setSubId("SubId137685915");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsResp _deleteContacts__return = port.deleteContacts(_deleteContacts_parameters);
            System.out.println("deleteContacts.result=" + _deleteContacts__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking addContactToGroup...");
        com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupReq _addContactToGroup_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupReq();
        java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType> _addContactToGroup_parametersContactAndGroup = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType>();
        com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType _addContactToGroup_parametersContactAndGroupVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType();
        _addContactToGroup_parametersContactAndGroupVal1.setGroupName("GroupName401791847");
        _addContactToGroup_parametersContactAndGroupVal1.setContactId(156888704);
        _addContactToGroup_parametersContactAndGroup.add(_addContactToGroup_parametersContactAndGroupVal1);
        _addContactToGroup_parameters.getContactAndGroup().addAll(_addContactToGroup_parametersContactAndGroup);
        _addContactToGroup_parameters.setSubId("SubId746228514");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupResp _addContactToGroup__return = port.addContactToGroup(_addContactToGroup_parameters);
            System.out.println("addContactToGroup.result=" + _addContactToGroup__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking deleteGroup...");
        com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupReq _deleteGroup_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupReq();
        _deleteGroup_parameters.setGroupName("GroupName1047191892");
        _deleteGroup_parameters.setSubId("SubId1707429250");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupResp _deleteGroup__return = port.deleteGroup(_deleteGroup_parameters);
            System.out.println("deleteGroup.result=" + _deleteGroup__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking removeContactFromGroup...");
        com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupReq _removeContactFromGroup_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupReq();
        java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType> _removeContactFromGroup_parametersContactAndGroup = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType>();
        com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType _removeContactFromGroup_parametersContactAndGroupVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactGroupType();
        _removeContactFromGroup_parametersContactAndGroupVal1.setGroupName("GroupName1495589447");
        _removeContactFromGroup_parametersContactAndGroupVal1.setContactId(171335832);
        _removeContactFromGroup_parametersContactAndGroup.add(_removeContactFromGroup_parametersContactAndGroupVal1);
        _removeContactFromGroup_parameters.getContactAndGroup().addAll(_removeContactFromGroup_parametersContactAndGroup);
        _removeContactFromGroup_parameters.setSubId("SubId336779469");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupResp _removeContactFromGroup__return = port.removeContactFromGroup(_removeContactFromGroup_parameters);
            System.out.println("removeContactFromGroup.result=" + _removeContactFromGroup__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking terminateSubrs...");
        com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsReq _terminateSubrs_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsReq();
        _terminateSubrs_parameters.setSubId("SubId533252114");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsResp _terminateSubrs__return = port.terminateSubrs(_terminateSubrs_parameters);
            System.out.println("terminateSubrs.result=" + _terminateSubrs__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking listGroups...");
        com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsReq _listGroups_parameters = new com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsReq();
        _listGroups_parameters.setSubId("SubId-1750892878");
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsResp _listGroups__return = port.listGroups(_listGroups_parameters);
            System.out.println("listGroups.result=" + _listGroups__return);

        } catch (ErrorException e) { 
            System.out.println("Expected exception: ErrorException has occurred.");
            System.out.println(e.toString());
        }
            }

        System.exit(0);
    }

}
