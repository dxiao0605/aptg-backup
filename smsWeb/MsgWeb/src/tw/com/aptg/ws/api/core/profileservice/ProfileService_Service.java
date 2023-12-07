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
 * 01        20180323    Gary Chang          新增從config取得WSDL URL
 */


package tw.com.aptg.ws.api.core.profileservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2018-03-22T12:05:10.845+08:00
 * Generated source version: 2.7.18
 * 
 */
@WebServiceClient(name = "ProfileService", 
                  wsdlLocation = "http://10.31.79.7:8000/profile-service/ProfileService?wsdl",
                  targetNamespace = "http://www.aptg.com.tw/ws/api/core/ProfileService") 
public class ProfileService_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.aptg.com.tw/ws/api/core/ProfileService", "ProfileService");
    public final static QName ProfileServicePort = new QName("http://www.aptg.com.tw/ws/api/core/ProfileService", "ProfileServicePort");
    static {
    	//#01
    	String wsdlLocation = "";
        URL url = null;
        try {
        	//#01
        	ResourceBundle config = ResourceBundle.getBundle("config");
        	wsdlLocation = config.getString("profile.service.wsdl.location");
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ProfileService_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", wsdlLocation);
        }
        WSDL_LOCATION = url;
    }

    public ProfileService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ProfileService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ProfileService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ProfileService_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ProfileService_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ProfileService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ProfileService
     */
    @WebEndpoint(name = "ProfileServicePort")
    public ProfileService getProfileServicePort() {
        return super.getPort(ProfileServicePort, ProfileService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ProfileService
     */
    @WebEndpoint(name = "ProfileServicePort")
    public ProfileService getProfileServicePort(WebServiceFeature... features) {
        return super.getPort(ProfileServicePort, ProfileService.class, features);
    }

}