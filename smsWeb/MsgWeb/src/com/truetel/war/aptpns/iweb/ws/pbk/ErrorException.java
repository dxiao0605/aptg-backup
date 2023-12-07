
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.7.18
 * 2018-03-29T10:49:30.701+08:00
 * Generated source version: 2.7.18
 */

@WebFault(name = "WsErrorException", targetNamespace = "http://truetel.com/war/aptpns/iweb/ws")
public class ErrorException extends Exception {
    
    private com.truetel.war.aptpns.iweb.ws.WsErrorException wsErrorException;

    public ErrorException() {
        super();
    }
    
    public ErrorException(String message) {
        super(message);
    }
    
    public ErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorException(String message, com.truetel.war.aptpns.iweb.ws.WsErrorException wsErrorException) {
        super(message);
        this.wsErrorException = wsErrorException;
    }

    public ErrorException(String message, com.truetel.war.aptpns.iweb.ws.WsErrorException wsErrorException, Throwable cause) {
        super(message, cause);
        this.wsErrorException = wsErrorException;
    }

    public com.truetel.war.aptpns.iweb.ws.WsErrorException getFaultInfo() {
        return this.wsErrorException;
    }
}
