/*
 * #01  Fix:Cross-Site Scripting: Reflected (5649)
 * #02	Fix:Cross-Site Scripting: Reflected (5650)
 * 
 * https://www.ascii.cl/htmlcodes.htm
 * 
 */

package tw.com.aptg.filter;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletRequestWrapper;    
import org.apache.commons.text.StringEscapeUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
  	    
    public XssHttpServletRequestWrapper(HttpServletRequest request) {  
        super(request);  
    }  
  
    @Override  
    public String getHeader(String name) {  
    	//System.out.println("getHeader=" + StringEscapeUtils.escapeHtml4(super.getHeader(name)));
    	//#02
    	return cleanXSS(StringEscapeUtils.escapeHtml4(super.getHeader(name)));  
    }  
  
    @Override  
    public String getQueryString() {
    	//System.out.println("getQueryString=" + StringEscapeUtils.escapeHtml4(super.getQueryString()));
    	//#02
    	return cleanXSS(StringEscapeUtils.escapeHtml4(super.getQueryString()));  
    }  
  
    @Override  
    public String getParameter(String name) {
    	//System.out.println("getParameter=" + cleanXSS(StringEscapeUtils.escapeHtml4(super.getParameter(name))));	
    	//#02
    	return cleanXSS(StringEscapeUtils.escapeHtml4(super.getParameter(name)));
    }  
  
    @Override  
    public String[] getParameterValues(String name) {  
        String[] values = super.getParameterValues(name);  
        if(values != null) {  
            int length = values.length;  
            String[] escapseValues = new String[length];  
            for(int i = 0; i < length; i++){
            	//System.out.println("values[" + i + "]=" + values[i] );
            	//#02
                escapseValues[i] = cleanXSS(StringEscapeUtils.escapeHtml4(values[i])); 
                //System.out.println("escapseValues[" + i + "]=" + escapseValues[i] );
            }  
            return escapseValues;  
        }  
        return super.getParameterValues(name);  
    } 
    
    //#02
    private String cleanXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            value = value.replaceAll("=", "&#61;"); //等於
            value = value.replaceAll("\\(", "&#40;"); //左括號 
            value = value.replaceAll("\\)", "&#41;"); //右括號
            value = value.replaceAll("\"", "&quot;"); //雙引號
            value = value.replaceAll("'", "&#39;"); //單引號
        
        }
        return value;
    }
      
}  