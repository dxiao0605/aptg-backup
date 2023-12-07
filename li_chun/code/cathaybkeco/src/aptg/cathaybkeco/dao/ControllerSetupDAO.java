package aptg.cathaybkeco.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.ControllerSetupVO;

public class ControllerSetupDAO extends BaseDAO {

	/**
	 * 取得ECO5資訊
	 * 
	 * @param controllerSetupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getControllerSetup(ControllerSetupVO controllerSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ControllerSetup ");
		sql.append(" where 1=1 ");

		if (StringUtils.isNotBlank(controllerSetupVO.getBankCode())) {
			sql.append(" and BankCode = ? ");
			parameterList.add(controllerSetupVO.getBankCode());
		}
		if (StringUtils.isNotBlank(controllerSetupVO.getEco5Account())) {
			sql.append(" and ECO5Account = ? ");
			parameterList.add(controllerSetupVO.getEco5Account());
		}

		sql.append(" order by ECO5Account ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 新增ECO5資料
	 * @param controllerSetupVO
	 * @throws Exception
	 */
	public void addControllerSetup(ControllerSetupVO controllerSetupVO) throws Exception {		
		
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ControllerSetup ( ");
		sql.append(" ECO5Account, ");
		sql.append(" ECO5Password, ");
		sql.append(" BankCode, ");
		sql.append(" InstallPosition, ");
		sql.append(" Enabled, ");
		sql.append(" EnabledTime, ");
		sql.append(" IP, ");
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		sql.append(" )VALUES(?,?,?,?,?,?,?,?,?) ");
		
		List<String> parameterList = new ArrayList<String>();
		parameterList.add(controllerSetupVO.getEco5Account());
		parameterList.add(controllerSetupVO.getEco5Password());
		parameterList.add(controllerSetupVO.getBankCode());
		parameterList.add(controllerSetupVO.getInstallPosition());
		parameterList.add(controllerSetupVO.getEnabled());
		if("1".equals(controllerSetupVO.getEnabled())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			parameterList.add(sdf.format(new Date()));	
		}else {
			parameterList.add(null);
		}		
		parameterList.add(controllerSetupVO.getIp());
		parameterList.add(controllerSetupVO.getUserName());
		parameterList.add(controllerSetupVO.getUserName());

		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改ECO5資料
	 * @param controllerSetupVO
	 * @throws Exception
	 */
	public void updControllerSetup(ControllerSetupVO controllerSetupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ControllerSetup ");
		sql.append(" SET ");
		sql.append(" ECO5Password = ?, ");
		sql.append(" InstallPosition = ?, ");
		sql.append(" Enabled = ?, ");
		sql.append(" EnabledTime = ?, ");
		sql.append(" IP = ?, ");
		sql.append(" UpdateUserName = ? ");	
		sql.append(" WHERE ECO5Account = ? ");
			
		parameterList.add(controllerSetupVO.getEco5Password());
		parameterList.add(controllerSetupVO.getInstallPosition());
		parameterList.add(controllerSetupVO.getEnabled());
		if("1".equals(controllerSetupVO.getEnabled())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			parameterList.add(sdf.format(new Date()));	
		}else {
			parameterList.add(null);
		}		
		parameterList.add(controllerSetupVO.getIp());
		parameterList.add(controllerSetupVO.getUserName());
		parameterList.add(controllerSetupVO.getEco5Account());
	
		this.executeUpdate(sql.toString(), parameterList);
	}
}
