package aptg.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

	private static JsonUtil instances;
	
	private JsonUtil() {}
	
	public static JsonUtil getInstance() {
		if (instances==null) {
			synchronized (JsonUtil.class) {
				if (instances==null) {
					instances = new JsonUtil();
				}
			}
		}
		return instances;
	}
	
	/**
	 * JSON convert to String
	 * 
	 * @param obj
	 * @return
	 */
	public String convertObjectToJsonstring(Object obj) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(obj);
//		return new Gson().toJson(obj);
	}
	

	public Object convertStringToObject(String json, Class beanClass) {
		Gson gson = new Gson();
	    Object res = gson.fromJson(json, beanClass);
		return res;
	}
	

	public Object convertStringToObjectByType(String json, Type type) {
		json = json.trim();
		
		Gson gson = new Gson();
	    Object res = gson.fromJson(json, type);
		return res;
	}
}
