
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.truetel.war.aptpns.iweb.ws.pbk package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SuccExecute_QNAME = new QName("http://truetel.com/war/aptpns/iweb/ws/pbk", "SuccExecute");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.truetel.war.aptpns.iweb.ws.pbk
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RemoveContactFromGroupResp }
     * 
     */
    public RemoveContactFromGroupResp createRemoveContactFromGroupResp() {
        return new RemoveContactFromGroupResp();
    }

    /**
     * Create an instance of {@link CountContactsReq }
     * 
     */
    public CountContactsReq createCountContactsReq() {
        return new CountContactsReq();
    }

    /**
     * Create an instance of {@link QueryContactType }
     * 
     */
    public QueryContactType createQueryContactType() {
        return new QueryContactType();
    }

    /**
     * Create an instance of {@link UpdateContactsResp }
     * 
     */
    public UpdateContactsResp createUpdateContactsResp() {
        return new UpdateContactsResp();
    }

    /**
     * Create an instance of {@link TerminateSubrsReq }
     * 
     */
    public TerminateSubrsReq createTerminateSubrsReq() {
        return new TerminateSubrsReq();
    }

    /**
     * Create an instance of {@link DeleteContactsResp }
     * 
     */
    public DeleteContactsResp createDeleteContactsResp() {
        return new DeleteContactsResp();
    }

    /**
     * Create an instance of {@link AddContactToGroupReq }
     * 
     */
    public AddContactToGroupReq createAddContactToGroupReq() {
        return new AddContactToGroupReq();
    }

    /**
     * Create an instance of {@link ContactGroupType }
     * 
     */
    public ContactGroupType createContactGroupType() {
        return new ContactGroupType();
    }

    /**
     * Create an instance of {@link ListContactsReq }
     * 
     */
    public ListContactsReq createListContactsReq() {
        return new ListContactsReq();
    }

    /**
     * Create an instance of {@link ListContactsResp }
     * 
     */
    public ListContactsResp createListContactsResp() {
        return new ListContactsResp();
    }

    /**
     * Create an instance of {@link ContactFullType }
     * 
     */
    public ContactFullType createContactFullType() {
        return new ContactFullType();
    }

    /**
     * Create an instance of {@link ListGroupsResp }
     * 
     */
    public ListGroupsResp createListGroupsResp() {
        return new ListGroupsResp();
    }

    /**
     * Create an instance of {@link GroupType }
     * 
     */
    public GroupType createGroupType() {
        return new GroupType();
    }

    /**
     * Create an instance of {@link UpdateContactsReq }
     * 
     */
    public UpdateContactsReq createUpdateContactsReq() {
        return new UpdateContactsReq();
    }

    /**
     * Create an instance of {@link CreateContactsResp }
     * 
     */
    public CreateContactsResp createCreateContactsResp() {
        return new CreateContactsResp();
    }

    /**
     * Create an instance of {@link ContactIdOnlyType }
     * 
     */
    public ContactIdOnlyType createContactIdOnlyType() {
        return new ContactIdOnlyType();
    }

    /**
     * Create an instance of {@link ListGroupsReq }
     * 
     */
    public ListGroupsReq createListGroupsReq() {
        return new ListGroupsReq();
    }

    /**
     * Create an instance of {@link CreateGroupReq }
     * 
     */
    public CreateGroupReq createCreateGroupReq() {
        return new CreateGroupReq();
    }

    /**
     * Create an instance of {@link CreateContactsReq }
     * 
     */
    public CreateContactsReq createCreateContactsReq() {
        return new CreateContactsReq();
    }

    /**
     * Create an instance of {@link ContactBaseType }
     * 
     */
    public ContactBaseType createContactBaseType() {
        return new ContactBaseType();
    }

    /**
     * Create an instance of {@link DeleteContactsReq }
     * 
     */
    public DeleteContactsReq createDeleteContactsReq() {
        return new DeleteContactsReq();
    }

    /**
     * Create an instance of {@link CreateGroupResp }
     * 
     */
    public CreateGroupResp createCreateGroupResp() {
        return new CreateGroupResp();
    }

    /**
     * Create an instance of {@link CountContactsResp }
     * 
     */
    public CountContactsResp createCountContactsResp() {
        return new CountContactsResp();
    }

    /**
     * Create an instance of {@link AddContactToGroupResp }
     * 
     */
    public AddContactToGroupResp createAddContactToGroupResp() {
        return new AddContactToGroupResp();
    }

    /**
     * Create an instance of {@link DeleteGroupReq }
     * 
     */
    public DeleteGroupReq createDeleteGroupReq() {
        return new DeleteGroupReq();
    }

    /**
     * Create an instance of {@link RemoveContactFromGroupReq }
     * 
     */
    public RemoveContactFromGroupReq createRemoveContactFromGroupReq() {
        return new RemoveContactFromGroupReq();
    }

    /**
     * Create an instance of {@link DeleteGroupResp }
     * 
     */
    public DeleteGroupResp createDeleteGroupResp() {
        return new DeleteGroupResp();
    }

    /**
     * Create an instance of {@link TerminateSubrsResp }
     * 
     */
    public TerminateSubrsResp createTerminateSubrsResp() {
        return new TerminateSubrsResp();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk", name = "SuccExecute", defaultValue = "true")
    public JAXBElement<Boolean> createSuccExecute(Boolean value) {
        return new JAXBElement<Boolean>(_SuccExecute_QNAME, Boolean.class, null, value);
    }

}
