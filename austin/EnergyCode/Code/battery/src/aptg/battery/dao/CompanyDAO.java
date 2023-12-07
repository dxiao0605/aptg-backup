package aptg.battery.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.vo.CompanyVO;

public class CompanyDAO extends BaseDAO {
	
	/**
	 * 取得公司資訊
	 * @param companyVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCompanyInfo(CompanyVO companyVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Company ");	
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(companyVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(companyVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(companyVO.getAdmin())) {
			sql.append(" and Admin = ? ");
			parameterList.add(companyVO.getAdmin());
		}
		if(StringUtils.isNotBlank(companyVO.getCompanyName())) {
			sql.append(" and CompanyName = ? ");
			parameterList.add(companyVO.getCompanyName());
		}
		sql.append(" order by CompanyName ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改告警條件
	 * @param companyVO
	 * @throws Exception
	 */
	public void updateAlertSetup(CompanyVO companyVO) throws Exception {
		Map<String, List<String>> sqlMap = new LinkedHashMap<String, List<String>>();
			
		//修改告警條件
		StringBuffer sql = new StringBuffer();
		sql.append(" update Company set ");
		sql.append(" Alert1 = ?, ");
		sql.append(" Alert2 = ?, ");
		sql.append(" Disconnect = ?, ");
		if(StringUtils.isNotBlank(companyVO.getTemperature1())) {
			sql.append(" Temperature1 = ?, ");
		}
		sql.append(" UpdateUserName = ? ");
		sql.append(" where CompanyCode = ? ");

		List<String> parameterList = new ArrayList<String>();
		parameterList.add(companyVO.getAlert1());
		parameterList.add(companyVO.getAlert2());
		parameterList.add(companyVO.getDisconnect());
		if(StringUtils.isNotBlank(companyVO.getTemperature1())) {
			parameterList.add(companyVO.getTemperature1());
		}
		parameterList.add(companyVO.getUserName());
		parameterList.add(companyVO.getCompanyCode());			
		sqlMap.put(sql.toString(), parameterList);
		
		//將待處理工作改為取消
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" update AlertTask set ");
		sql2.append(" TaskStatus = 1, ");
		sql2.append(" UpdateUserName = ? ");
		sql2.append(" where CompanyCode = ? ");
		sql2.append(" and TaskStatus = 0 ");

		List<String> parameter2List = new ArrayList<String>();
		parameter2List.add(companyVO.getUserName());
		parameter2List.add(companyVO.getCompanyCode());
		sqlMap.put(sql2.toString(), parameter2List);

		//新增一筆警告值設定工作
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" INSERT INTO AlertTask ( ");
		sql3.append(" TaskID, ");
		sql3.append(" CompanyCode, ");
		sql3.append(" IMPType, ");
		sql3.append(" Alert1, ");
		sql3.append(" Alert2, ");
		sql3.append(" CreateUserName, ");
		sql3.append(" UpdateUserName ");
		sql3.append(" ) VALUES (?,?,?,?,?,?,?) ");

		List<String> parameter3List = new ArrayList<String>();
		parameter3List.add(companyVO.getTaskID());
		parameter3List.add(companyVO.getCompanyCode());
		parameter3List.add(companyVO.getIMPType());
		parameter3List.add(companyVO.getAlert1());
		parameter3List.add(companyVO.getAlert2());
		parameter3List.add(companyVO.getUserName());
		parameter3List.add(companyVO.getUserName());
		sqlMap.put(sql3.toString(), parameter3List);	
		
		this.executeUpdateBatch(sqlMap);
	}
	
	/**
	 * 取得公司資訊
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDefault() throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select c.CompanyCode, c.CompanyName, b.SeqNo ");
		sql.append(" from Company c, BatteryGroup b  ");
		sql.append(" where c.CompanyCode = b.CompanyCode ");
		sql.append(" and c.Admin = 1  ");
		sql.append(" and DefaultGroup = 0 ");
		
		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 取得公司名稱
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCompanyName(String companyCode) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select CompanyName from Company ");	
		sql.append(" where CompanyCode = ? ");
		parameterList.add(companyCode);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得公司代碼
	 * @param companyName
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCompanyCode(String companyName) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select CompanyCode from Company ");	
		sql.append(" where CompanyName = ? ");
		parameterList.add(companyName);
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改內阻值or電導值
	 * @param companyVO
	 * @throws Exception
	 */
	public void updIMPType(CompanyVO companyVO) throws Exception {
		Map<String, List<String>> sqlMap = new LinkedHashMap<String, List<String>>();
		
			
		//修改告警條件
		StringBuffer sql = new StringBuffer();
		sql.append(" update Company set ");
		sql.append(" IMPType = ?, ");
		sql.append(" Alert1 = ?, ");
		sql.append(" Alert2 = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where CompanyCode = ? ");
		
		List<String> parameterList = new ArrayList<String>();
		parameterList.add(companyVO.getIMPType());
		parameterList.add(companyVO.getAlert1());
		parameterList.add(companyVO.getAlert2());
		parameterList.add(companyVO.getUserName());
		parameterList.add(companyVO.getCompanyCode());	
		sqlMap.put(sql.toString(), parameterList);
		
		//將待處理工作改為取消
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" update AlertTask set ");
		sql2.append(" TaskStatus = 1, ");
		sql2.append(" UpdateUserName = ? ");
		sql2.append(" where CompanyCode = ? ");
		sql2.append(" and TaskStatus = 0 ");

		List<String> parameter2List = new ArrayList<String>();
		parameter2List.add(companyVO.getUserName());
		parameter2List.add(companyVO.getCompanyCode());
		sqlMap.put(sql2.toString(), parameter2List);

		//新增一筆警告值設定工作
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" INSERT INTO AlertTask ( ");
		sql3.append(" TaskID, ");
		sql3.append(" CompanyCode, ");
		sql3.append(" IMPType, ");
		sql3.append(" Alert1, ");
		sql3.append(" Alert2, ");
		sql3.append(" CreateUserName, ");
		sql3.append(" UpdateUserName ");
		sql3.append(" ) VALUES (?,?,?,?,?,?,?) ");

		List<String> parameter3List = new ArrayList<String>();
		parameter3List.add(companyVO.getTaskID());
		parameter3List.add(companyVO.getCompanyCode());
		parameter3List.add(companyVO.getIMPType());
		parameter3List.add(companyVO.getAlert1());
		parameter3List.add(companyVO.getAlert2());
		parameter3List.add(companyVO.getUserName());
		parameter3List.add(companyVO.getUserName());
		sqlMap.put(sql3.toString(), parameter3List);		
		
		this.executeUpdateBatch(sqlMap);
	}
	
	/**
	 * 修改公司Logo路徑跟顯示名稱
	 * @param companyVO
	 * @throws Exception
	 */
	public void updLogoPathAndShowName(CompanyVO companyVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
			
		StringBuffer sql = new StringBuffer();
		sql.append(" update Company set ");
		if(StringUtils.isNotBlank(companyVO.getLogoPath())) {
			sql.append(" LogoPath = ?, ");	
			parameterList.add(companyVO.getLogoPath());
		}
		sql.append(" ShowName = ?, ");
		sql.append(" ShortName = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where CompanyCode = ? ");
		parameterList.add(companyVO.getShowName());
		parameterList.add(companyVO.getShortName());
		parameterList.add(companyVO.getUserName());
		parameterList.add(companyVO.getCompanyCode());			
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 移除公司Logo路徑
	 * @param companyVO
	 * @throws Exception
	 */
	public void delLogoPath(CompanyVO companyVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();			

		StringBuffer sql = new StringBuffer();
		sql.append(" update Company set ");
		sql.append(" LogoPath = null, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where CompanyCode = ? ");

		parameterList.add(companyVO.getUserName());
		parameterList.add(companyVO.getCompanyCode());			
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改指令限制
	 * @param companyVO
	 * @throws Exception
	 */
	public void updCommandSetup(CompanyVO companyVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();			

		StringBuffer sql = new StringBuffer();
		sql.append(" update Company set ");
		sql.append(" B3 = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where CompanyCode = ? ");

		parameterList.add(companyVO.getB3());
		parameterList.add(companyVO.getUserName());
		parameterList.add(companyVO.getCompanyCode());			
		
		this.executeUpdate(sql.toString(), parameterList);
	}
}
