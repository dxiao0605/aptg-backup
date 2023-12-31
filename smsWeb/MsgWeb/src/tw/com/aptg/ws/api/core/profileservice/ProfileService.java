package tw.com.aptg.ws.api.core.profileservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2018-03-22T12:05:10.815+08:00
 * Generated source version: 2.7.18
 * 
 */
@WebService(targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", name = "ProfileService")
@XmlSeeAlso({ObjectFactory.class})
public interface ProfileService {

    @WebMethod
    @RequestWrapper(localName = "memberChangeAccount", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberChangeAccount")
    @ResponseWrapper(localName = "memberChangeAccountResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberChangeAccountResponse")
    @WebResult(name = "return", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response memberChangeAccount(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "oldAccountID", targetNamespace = "")
        java.lang.String oldAccountID,
        @WebParam(name = "personID", targetNamespace = "")
        java.lang.String personID,
        @WebParam(name = "newAccountID", targetNamespace = "")
        java.lang.String newAccountID
    );

    @WebMethod
    @RequestWrapper(localName = "memberResetPassword", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberResetPassword")
    @ResponseWrapper(localName = "memberResetPasswordResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberResetPasswordResponse")
    @WebResult(name = "return", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response memberResetPassword(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "memberID", targetNamespace = "")
        java.lang.String memberID,
        @WebParam(name = "personID", targetNamespace = "")
        java.lang.String personID
    );

    @WebMethod
    @RequestWrapper(localName = "setContractEMail", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SetContractEMail")
    @ResponseWrapper(localName = "setContractEMailResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SetContractEMailResponse")
    @WebResult(name = "response", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response setContractEMail(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "userID", targetNamespace = "")
        java.lang.String userID,
        @WebParam(name = "contractID", targetNamespace = "")
        java.lang.String contractID,
        @WebParam(name = "mdn", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "min", targetNamespace = "")
        java.lang.String min,
        @WebParam(name = "email", targetNamespace = "")
        java.lang.String email
    );

    @WebMethod
    @RequestWrapper(localName = "memberChangePassword", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberChangePassword")
    @ResponseWrapper(localName = "memberChangePasswordResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.MemberChangePasswordResponse")
    @WebResult(name = "return", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response memberChangePassword(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "memberID", targetNamespace = "")
        java.lang.String memberID,
        @WebParam(name = "oldPassword", targetNamespace = "")
        java.lang.String oldPassword,
        @WebParam(name = "newPassword", targetNamespace = "")
        java.lang.String newPassword
    );

    @WebMethod
    @RequestWrapper(localName = "subscriberChangePassword", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SubscriberChangePassword")
    @ResponseWrapper(localName = "subscriberChangePasswordResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SubscriberChangePasswordResponse")
    @WebResult(name = "return", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response subscriberChangePassword(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "MDN", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "oldPassword", targetNamespace = "")
        java.lang.String oldPassword,
        @WebParam(name = "newPassword", targetNamespace = "")
        java.lang.String newPassword
    );

    @WebMethod
    @RequestWrapper(localName = "subscriberResetPassword", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SubscriberResetPassword")
    @ResponseWrapper(localName = "subscriberResetPasswordResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SubscriberResetPasswordResponse")
    @WebResult(name = "return", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response subscriberResetPassword(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "MDN", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "personID", targetNamespace = "")
        java.lang.String personID
    );

    @WebMethod
    @RequestWrapper(localName = "getTerminateProfile", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.GetTerminateProfile")
    @ResponseWrapper(localName = "getTerminateProfileResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.GetTerminateProfileResponse")
    @WebResult(name = "terminateResponse", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.TerminateResponse getTerminateProfile(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "userID", targetNamespace = "")
        java.lang.String userID,
        @WebParam(name = "contractID", targetNamespace = "")
        java.lang.String contractID,
        @WebParam(name = "mdn", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "min", targetNamespace = "")
        java.lang.String min
    );

    @WebMethod
    @RequestWrapper(localName = "setReadAgreement", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SetReadAgreement")
    @ResponseWrapper(localName = "setReadAgreementResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.SetReadAgreementResponse")
    @WebResult(name = "response", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response setReadAgreement(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "userID", targetNamespace = "")
        java.lang.String userID,
        @WebParam(name = "contractID", targetNamespace = "")
        java.lang.String contractID,
        @WebParam(name = "mdn", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "min", targetNamespace = "")
        java.lang.String min
    );

    @WebMethod
    @RequestWrapper(localName = "getUserProfile", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.GetUserProfile")
    @ResponseWrapper(localName = "getUserProfileResponse", targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService", className = "tw.com.aptg.ws.api.core.profileservice.GetUserProfileResponse")
    @WebResult(name = "response", targetNamespace = "")
    public tw.com.aptg.ws.api.core.profileservice.Response getUserProfile(
        @WebParam(name = "serviceID", targetNamespace = "")
        java.lang.String serviceID,
        @WebParam(name = "servicePWD", targetNamespace = "")
        java.lang.String servicePWD,
        @WebParam(name = "userID", targetNamespace = "")
        java.lang.String userID,
        @WebParam(name = "contractID", targetNamespace = "")
        java.lang.String contractID,
        @WebParam(name = "mdn", targetNamespace = "")
        java.lang.String mdn,
        @WebParam(name = "min", targetNamespace = "")
        java.lang.String min,
        @WebParam(name = "email", targetNamespace = "")
        java.lang.String email
    );
}
