
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.truetel.war.aptpns.iweb.ws.pbk;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2018-03-29T10:49:30.712+08:00
 * Generated source version: 2.7.18
 * 
 */

@javax.jws.WebService(
                      serviceName = "PhonebookApiImplService",
                      portName = "PhonebookApiPort",
                      targetNamespace = "http://truetel.com/war/aptpns/iweb/ws/pbk",
                      wsdlLocation = "http://10.31.79.5:6500/aptpnsiweb/PhonebookApi?wsdl",
                      endpointInterface = "com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi")
                      
public class PhonebookApiImpl implements PhonebookApi {

    private static final Logger LOG = Logger.getLogger(PhonebookApiImpl.class.getName());

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#updateContacts(com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsResp updateContacts(UpdateContactsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation updateContacts");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.UpdateContactsResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#listContacts(com.truetel.war.aptpns.iweb.ws.pbk.ListContactsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.ListContactsResp listContacts(ListContactsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation listContacts");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.ListContactsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.ListContactsResp();
            java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType> _returnContact = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType>();
            com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType _returnContactVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactFullType();
            _returnContactVal1.setContactId(Integer.valueOf(542745632));
            _returnContactVal1.setFamilyName("FamilyName-531156631");
            _returnContactVal1.setGivenName("GivenName1642891124");
            _returnContactVal1.setCell("Cell1410046060");
            _returnContactVal1.setCellHome("CellHome-1215956268");
            _returnContactVal1.setCellWork("CellWork208973095");
            _returnContactVal1.setPhone("Phone-266078998");
            _returnContactVal1.setPhoneHome("PhoneHome-1715107025");
            _returnContactVal1.setPhoneWork("PhoneWork1938808619");
            _returnContactVal1.setFax("Fax1119828227");
            _returnContactVal1.setEmail("Email352897936");
            _returnContactVal1.setEmailHome("EmailHome-113845627");
            _returnContactVal1.setEmailWork("EmailWork-348617545");
            _returnContactVal1.setWebUrlHome("WebUrlHome-1305161269");
            _returnContactVal1.setWebUrlWork("WebUrlWork-851151177");
            _returnContactVal1.setCompany("Company-864173291");
            _returnContactVal1.setJobTitle("JobTitle648121395");
            _returnContactVal1.setNickName("NickName-1647811415");
            _returnContactVal1.setCountry("Country2034801268");
            _returnContactVal1.setPostcode("Postcode774549981");
            _returnContactVal1.setCity("City1358669967");
            _returnContactVal1.setAddress("Address1920864423");
            _returnContactVal1.setBirthday(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2018-03-29T10:49:30.726+08:00"));
            _returnContactVal1.setYahooId("YahooId-2015129381");
            _returnContactVal1.setMsLiveId("MsLiveId-2031727785");
            _returnContactVal1.setGoogleId("GoogleId-3014324");
            _returnContactVal1.setSkypeId("SkypeId1514896500");
            _returnContactVal1.setNote("Note-1786575251");
            _returnContactVal1.setIndex(Integer.valueOf(-2008137513));
            _returnContact.add(_returnContactVal1);
            _return.getContact().addAll(_returnContact);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#createContacts(com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsResp createContacts(CreateContactsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation createContacts");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.CreateContactsResp();
            java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType> _returnContact = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType>();
            com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType _returnContactVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.ContactIdOnlyType();
            _returnContactVal1.setContactId(-621222390);
            _returnContactVal1.setIndex(Integer.valueOf(377570408));
            _returnContact.add(_returnContactVal1);
            _return.getContact().addAll(_returnContact);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#createGroup(com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupResp createGroup(CreateGroupReq parameters) throws ErrorException    { 
        LOG.info("Executing operation createGroup");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.CreateGroupResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#countContacts(com.truetel.war.aptpns.iweb.ws.pbk.CountContactsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.CountContactsResp countContacts(CountContactsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation countContacts");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.CountContactsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.CountContactsResp();
            _return.setNumContacts(-234907594);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#deleteContacts(com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsResp deleteContacts(DeleteContactsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation deleteContacts");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.DeleteContactsResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#addContactToGroup(com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupResp addContactToGroup(AddContactToGroupReq parameters) throws ErrorException    { 
        LOG.info("Executing operation addContactToGroup");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.AddContactToGroupResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#deleteGroup(com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupResp deleteGroup(DeleteGroupReq parameters) throws ErrorException    { 
        LOG.info("Executing operation deleteGroup");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.DeleteGroupResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#removeContactFromGroup(com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupResp removeContactFromGroup(RemoveContactFromGroupReq parameters) throws ErrorException    { 
        LOG.info("Executing operation removeContactFromGroup");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.RemoveContactFromGroupResp();
            _return.setSuccExecute(false);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#terminateSubrs(com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsResp terminateSubrs(TerminateSubrsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation terminateSubrs");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.TerminateSubrsResp();
            _return.setSuccExecute(true);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

    /* (non-Javadoc)
     * @see com.truetel.war.aptpns.iweb.ws.pbk.PhonebookApi#listGroups(com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsReq  parameters )*
     */
    public com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsResp listGroups(ListGroupsReq parameters) throws ErrorException    { 
        LOG.info("Executing operation listGroups");
        System.out.println(parameters);
        try {
            com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsResp _return = new com.truetel.war.aptpns.iweb.ws.pbk.ListGroupsResp();
            java.util.List<com.truetel.war.aptpns.iweb.ws.pbk.GroupType> _returnGroup = new java.util.ArrayList<com.truetel.war.aptpns.iweb.ws.pbk.GroupType>();
            com.truetel.war.aptpns.iweb.ws.pbk.GroupType _returnGroupVal1 = new com.truetel.war.aptpns.iweb.ws.pbk.GroupType();
            _returnGroupVal1.setGroupName("GroupName121750591");
            _returnGroupVal1.setGroupDesc("GroupDesc-1639726022");
            _returnGroup.add(_returnGroupVal1);
            _return.getGroup().addAll(_returnGroup);
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ErrorException("ErrorException...");
    }

}
