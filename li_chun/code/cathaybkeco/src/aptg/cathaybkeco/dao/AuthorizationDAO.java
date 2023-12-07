package aptg.cathaybkeco.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.AuthorizationVO;




public class AuthorizationDAO extends BaseDAO{

	/**
	 * 取得帳戶資訊
	 * @param authorizationVO
	 * @return List<DynaBean>
	 * @throws Exception 
	 */
	public List<DynaBean> getAccount(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from Account where 1=1 ");
		if(StringUtils.isNotBlank(authorizationVO.getSystemId())) {
			sql.append(" and SystemId = ? ");
			parameterList.add(authorizationVO.getSystemId());
		}
		if(StringUtils.isNotBlank(authorizationVO.getAccount())) {
			sql.append(" and Account = ? ");
			parameterList.add(authorizationVO.getAccount());
		}
		
		if(StringUtils.isNotBlank(authorizationVO.getPassword())) {
			sql.append(" and Password = ? ");
			parameterList.add(authorizationVO.getPassword());
		}
		
		if(StringUtils.isNotBlank(authorizationVO.getEnabled())) {
			sql.append(" and Enabled = ? ");
			parameterList.add(authorizationVO.getEnabled());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得使用者權限
	 * @param authorizationVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getAuthorityInfo(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select f.FunctionId, ");
		sql.append(" f.FunctionName, ");
		sql.append(" f.Type, ");
		sql.append(" f.ParentId, ");
		sql.append(" a.AuthorityId, ");
		sql.append(" p.ProgramId, ");
		sql.append(" p.Url, ");
		sql.append(" i.IconPath, ");
		sql.append(" a.Edit, ");
		sql.append(" a.Operate, ");
		sql.append(" f.Sort ");
		sql.append(" from RoleMapping r,  ");
		sql.append(" Authority a, ");
		sql.append(" FunctionList f ");
		sql.append(" left join ProgramList p on f.ProgramId = p.ProgramId ");
		sql.append(" left join Icon i on f.SystemId = i.SystemId and f.IconId = i.IconId ");
		sql.append(" where r.AuthorityId = a.AuthorityId ");
		sql.append(" and a.SystemId = f.SystemId ");
		sql.append(" and a.FunctionId = f.FunctionId ");
		sql.append(" and a.SystemId = ? ");
		parameterList.add(authorizationVO.getSystemId());
		sql.append(" and r.RoleID = ? ");
		parameterList.add(authorizationVO.getRoleId());
		sql.append(" order by f.Sort ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改Token
	 * @param authorizationVO
	 * @throws Exception
	 */
	public void updateToken(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update Account set Token = ?,  ");
		sql.append(" TokenDisableTime = DATE_SUB(NOW(),INTERVAL -12 HOUR)  ");
		sql.append(" where SystemId = ?");
		sql.append(" and Account = ? ");
		sql.append(" and Password = ? ");
		
		parameterList.add(authorizationVO.getAddToken());
		parameterList.add(authorizationVO.getSystemId());
		parameterList.add(authorizationVO.getAccount());
		parameterList.add(authorizationVO.getPassword());
		
		this.executeUpdate(sql.toString(), parameterList);
	}

	/**
	 * 檢核TOKEN
	 * @param authorizationVO
	 * @return
	 * @throws Exception 
	 */
	public boolean checkToken(AuthorizationVO authorizationVO) throws Exception {
		List<DynaBean> rows = new ArrayList<DynaBean>();
		List<String> parameterList = new ArrayList<String>();
		boolean check = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Account where Token = ? ");
		parameterList.add(authorizationVO.getToken());
		
		if(StringUtils.isNotBlank(authorizationVO.getSystemId())) {
			sql.append(" and SystemId = ? ");
			parameterList.add(authorizationVO.getSystemId());
		}

		rows = this.executeQuery(sql.toString(), parameterList);
		if(rows!=null && rows.size()>0) {
			check = true;
		}
		return check;
	}

	/**
	 * 修改最後登入時間
	 * @param authorizationVO
	 * @throws Exception 
	 */
	public void updateLoginTime(AuthorizationVO authorizationVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update Account set LastLogin = ? ");	
		parameterList.add(sdf.format(new Date()));
		sql.append(" where SystemId = ? ");
		parameterList.add(authorizationVO.getSystemId());
		sql.append(" and Account = ? ");
		parameterList.add(authorizationVO.getAccount());

		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 取得按鈕資訊
	 * @param authorizationVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getButtonInfo(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from Button ");		
		sql.append(" where AuthorityId = ? ");	
		parameterList.add(authorizationVO.getAuthorityId());
		sql.append(" and ProgramId = ? ");
		parameterList.add(authorizationVO.getProgramId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增帳戶資訊
	 * @param authorizationVO
	 * @throws Exception
	 */
	public void addAccount(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO Account ( ");
		sql.append(" SystemId, ");
		sql.append(" Account, ");
		sql.append(" Company, ");
		sql.append(" UserName, ");
		sql.append(" Password, ");
		sql.append(" RoleId, ");
		sql.append(" Enabled, ");
		sql.append(" EnabledTime ");				
		sql.append(" ) VALUES (?,?,?,?,?,?,?,? )  ");	
	
		parameterList.add(authorizationVO.getSystemId());
		parameterList.add(authorizationVO.getAccount());
		parameterList.add(authorizationVO.getCompany());
		parameterList.add(authorizationVO.getUserName());
		parameterList.add(authorizationVO.getPassword());
		parameterList.add(authorizationVO.getRoleId());
		parameterList.add(authorizationVO.getEnabled());
		parameterList.add(authorizationVO.getEnabledTime());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	
	/**
	 * 修改帳戶資訊
	 * @param authorizationVO
	 * @throws Exception
	 */
	public void updAccount(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE Account Set ");
		sql.append(" Company = ?, ");
		sql.append(" UserName = ?, ");
		sql.append(" Password = ?, ");
		sql.append(" RoleId = ?, ");
		sql.append(" Enabled = ?, ");
		sql.append(" EnabledTime = ? ");				
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(authorizationVO.getCompany());
		parameterList.add(authorizationVO.getUserName());
		parameterList.add(authorizationVO.getPassword());
		parameterList.add(authorizationVO.getRoleId());
		parameterList.add(authorizationVO.getEnabled());
		parameterList.add(authorizationVO.getEnabledTime());		
		parameterList.add(authorizationVO.getSystemId());
		parameterList.add(authorizationVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改密碼
	 * @param authorizationVO
	 * @throws Exception
	 */
	public void updPassword(AuthorizationVO authorizationVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE Account Set ");
		sql.append(" Password = ? ");
		sql.append(" where SystemId = ? ");
		sql.append(" and Account = ? ");

		parameterList.add(authorizationVO.getPassword());		
		parameterList.add(authorizationVO.getSystemId());
		parameterList.add(authorizationVO.getAccount());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
}
