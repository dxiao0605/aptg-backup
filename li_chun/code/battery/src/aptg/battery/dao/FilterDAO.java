package aptg.battery.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.vo.FilterVO;

public class FilterDAO extends BaseDAO{
	/**
	 * 查詢篩選
	 * @param filterVO
	 * @return
	 * @throws SQLException
	 */
	public List<DynaBean> getFilter(FilterVO filterVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select * ");
		sql.append(" from Filter ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(filterVO.getAccount())) {
			sql.append(" and Account = ? ");
			parameterList.add(filterVO.getAccount());
		}
		if(StringUtils.isNotBlank(filterVO.getFunctionId())) {
			sql.append(" and FunctionId = ? ");
			parameterList.add(filterVO.getFunctionId());
		}
		if(StringUtils.isNotBlank(filterVO.getFilterName())) {
			sql.append(" and FilterName = ? ");
			parameterList.add(filterVO.getFilterName());
		}
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 新增篩選
	 * @param filterVO
	 * @throws SQLException
	 */
	public void addFilter(FilterVO filterVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 		
		sql.append(" insert into Filter ( ");
		sql.append(" Account, ");
		sql.append(" FunctionId, ");
		sql.append(" FilterName, ");
		sql.append(" FilterConfig, ");
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		sql.append(" ) values (?,?,?,?,?,?) ");
		parameterList.add(filterVO.getAccount());
		parameterList.add(filterVO.getFunctionId());
		parameterList.add(filterVO.getFilterName());
		parameterList.add(filterVO.getFilterConfig());
		parameterList.add(filterVO.getUserName());
		parameterList.add(filterVO.getUserName());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 修改篩選
	 * @param filterVO
	 * @throws SQLException
	 */
	public void updFilter(FilterVO filterVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 		
		sql.append(" update Filter set ");	
		sql.append(" FilterConfig = ?, ");
		sql.append(" UpdateUserName = ? ");
		sql.append(" where Account = ? ");
		sql.append(" and FilterName = ? ");
		sql.append(" and FunctionId = ? ");
		parameterList.add(filterVO.getFilterConfig());
		parameterList.add(filterVO.getUserName());
		parameterList.add(filterVO.getAccount());
		parameterList.add(filterVO.getFilterName());
		parameterList.add(filterVO.getFunctionId());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 刪除篩選
	 * @param filterVO
	 * @throws SQLException
	 */
	public void delFilter(FilterVO filterVO) throws SQLException {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(); 		
		sql.append(" delete from Filter ");
		sql.append(" where SeqNo = ? ");
		parameterList.add(filterVO.getSeqNo());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
	
}
