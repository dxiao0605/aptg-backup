package aptg.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aptg.model.CompanyModel;
import aptg.utils.DBQueryUtil;

public class CompanyManager {

	private Map<Integer, CompanyModel> companyMap = new HashMap<>();
	
	private static CompanyManager instances;
	public static CompanyManager getInstance() {
		if (instances==null) {
			synchronized (CompanyManager.class) {
				if (instances==null) {
					instances = new CompanyManager();
				}
			}
		}
		return instances;
	}

	/**
	 * 取得Company Info
	 * 
	 * @param companyCode
	 * @return
	 */
	public CompanyModel getCompany(int companyCode) {
		if (companyMap.containsKey(companyCode)) {
			return companyMap.get(companyCode);
		} else {
			CompanyModel company = DBQueryUtil.getInstance().queryCompany(companyCode);
			if (company!=null) {
				setCompanyMap(companyCode, company);	// update cache
				return company;
			}
		}
		return null;
	}
	
	/**
	 * MQTT即時更新Company Info
	 * 
	 * @param model
	 */
	public void updateCompany(CompanyModel model) {
		setCompanyMap(model.getCompanyCode(), model);
	}
	
	
	/**
	 * init
	 */
	public void init() {
		List<CompanyModel> list = DBQueryUtil.getInstance().queryCompany();
		for (CompanyModel model: list) {
			int companyCode = model.getCompanyCode();
			
			setCompanyMap(companyCode, model);
		}
	}
	private void setCompanyMap(int companyCode, CompanyModel model) {
		synchronized (companyMap) {
			companyMap.put(companyCode, model);	
		}
	}
}
