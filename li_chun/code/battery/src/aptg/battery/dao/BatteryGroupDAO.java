package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.util.JdbcUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;

public class BatteryGroupDAO extends BaseDAO {

	/**
	 * 取得站台管理
	 * 
	 * @param batteryGroupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupManage(BatteryGroupVO batteryGroupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select ");
		sql.append(" c.CompanyName, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" bg.SeqNo, ");
		sql.append(" bg.Country, ");
		sql.append(" bg.Area, ");
		sql.append(" bg.GroupID, ");
		sql.append(" bg.GroupName, ");
		sql.append(" bg.Address, ");
		sql.append(" bg.DefaultGroup, ");
		sql.append(" a.BatteryCount ");
		sql.append(" from Company c, ");
		sql.append(" BatteryGroup bg ");
		sql.append(" left join ( ");
		sql.append(" select nb.GroupInternalID, count(1) as BatteryCount   ");
		sql.append(" from NBList nb,  ");
		sql.append(" NBGroupHis nh, ");
		sql.append(" Battery b, ");
		sql.append(" BattMaxRecTime m ");
		sql.append(" where nb.GroupInternalID = nh.GroupInternalID  ");
		sql.append(" and nb.Active = 13 ");
		sql.append(" and nb.NBID = nh.NBID ");
		sql.append(" and nb.NBID = b.NBID   ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		sql.append(" group by nb.GroupInternalID          ");
		sql.append(" ) a ");
		sql.append(" on bg.SeqNo = a.GroupInternalID  ");
		sql.append(" where c.CompanyCode = bg.CompanyCode ");
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryGroupVO.getCompanyCodeArr()+") ");
		}
		sql.append(" and c.CompanyCode = bg.CompanyCode ");
		if(StringUtils.isNotBlank(batteryGroupVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+batteryGroupVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryGroupVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+batteryGroupVO.getAreaArr()+") ");
		}
		if(StringUtils.isNotBlank(batteryGroupVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+batteryGroupVO.getGroupInternalIdArr()+") ");
		}
		sql.append(" order by bg.GroupName ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得站台設定
	 * 
	 * @param batteryGroupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getGroupSetup(BatteryGroupVO batteryGroupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" bg.SeqNo,  ");
		sql.append(" bg.Country,  ");
		sql.append(" bg.Area,  ");
		sql.append(" bg.GroupID,  ");
		sql.append(" bg.GroupName,  ");
		sql.append(" bg.Address ");
		sql.append(" from BatteryGroup bg  ");
		sql.append(" where 1=1  ");
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and bg.CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}
		sql.append(" and not exists ( ");
		sql.append(" select 1 from NBList n  where n.GroupInternalID = bg.SeqNo ");
		sql.append(" ) ");
		sql.append(" union  ");
		sql.append(" select  ");
		sql.append(" bg.SeqNo,  ");
		sql.append(" bg.Country,  ");
		sql.append(" bg.Area,  ");
		sql.append(" bg.GroupID,  ");
		sql.append(" bg.GroupName,  ");
		sql.append(" bg.Address ");
		sql.append(" from BatteryGroup bg  ");
		sql.append(" where 1=1  ");	
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and bg.CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}
		sql.append(" and bg.DefaultGroup = 0 ");
//		sql.append(" order by GroupName  ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(
				Comparator.comparing(new Function<DynaBean, String>() {
				public String apply(DynaBean bean) {
					return ObjectUtils.toString(bean.get("groupname"));
				}
			})
		).collect(Collectors.toList());
		
		return sortRows;

	}
	
	/**
	 * 新增站台
	 * @param batteryGroupVO
	 * @throws Exception
	 */
	public void addBatteryGroup(BatteryGroupVO batteryGroupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO BatteryGroup ( ");
			sql.append(" CompanyCode, ");
			sql.append(" GroupID, ");
			sql.append(" GroupName, ");
			sql.append(" Country,");
			sql.append(" Area, ");
			sql.append(" Address, ");
			sql.append(" Lng, ");
			sql.append(" Lat, ");
			sql.append(" DefaultGroup, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" )VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, batteryGroupVO.getCompanyCode());
			ps.setString(2, batteryGroupVO.getGroupID());
			ps.setString(3, batteryGroupVO.getGroupName());
			ps.setString(4, batteryGroupVO.getCountry());
			ps.setString(5, batteryGroupVO.getArea());
			ps.setString(6, batteryGroupVO.getAddress());
			ps.setString(7, batteryGroupVO.getLng());
			ps.setString(8, batteryGroupVO.getLat());
			ps.setString(9, batteryGroupVO.getDefaultGroup());
			ps.setString(10, batteryGroupVO.getUserName());
			ps.setString(11, batteryGroupVO.getUserName());
			ps.execute();
			
			//修改公司預設座標
			this.updCompanyLngLat(batteryGroupVO.getUserName(), batteryGroupVO.getCompanyCode(), connection, ps2);

			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 修改站台
	 * @param batteryGroupVO
	 * @throws Exception
	 */
	public void updBatteryGroup(BatteryGroupVO batteryGroupVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();		
			sql.append(" update BatteryGroup ");
			sql.append(" set ");
			sql.append(" GroupID = ?, ");
			sql.append(" GroupName = ?, ");
			sql.append(" Country = ?, ");
			sql.append(" Area = ?, ");
			sql.append(" Address = ?, ");
			sql.append(" Lng = ?, ");
			sql.append(" Lat = ?, ");
			sql.append(" UpdateUserName = ? ");
			sql.append(" where SeqNo = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, batteryGroupVO.getGroupID());
			ps.setString(2, batteryGroupVO.getGroupName());
			ps.setString(3, batteryGroupVO.getCountry());
			ps.setString(4, batteryGroupVO.getArea());
			ps.setString(5, batteryGroupVO.getAddress());
			ps.setString(6, batteryGroupVO.getLng());
			ps.setString(7, batteryGroupVO.getLat());
			ps.setString(8, batteryGroupVO.getUserName());
			ps.setString(9, batteryGroupVO.getGroupInternalId());
			ps.execute();
			
			//修改公司預設座標
			this.updCompanyLngLat(batteryGroupVO.getUserName(), batteryGroupVO.getCompanyCode(), connection, ps2);

			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 刪除站台
	 * @param batteryGroupVO
	 * @throws Exception
	 */
	public void delBatteryGroup(BatteryGroupVO batteryGroupVO) throws Exception {	
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			if(batteryGroupVO.getDataList().size()>0) {//將站台通訊序號移至預設群組
				StringBuffer sql = new StringBuffer();
				sql.append(" update NBList ");
				sql.append(" set GroupInternalID = ?,  ");
				sql.append(" UpdateUserName = ? ");
				sql.append(" where GroupInternalID = ? ");
				ps = connection.prepareStatement(sql.toString());
				for(BatteryGroupVO dataVO : batteryGroupVO.getDataList()) {
					ps.setString(1, dataVO.getDefaultInternalId());
					ps.setString(2, batteryGroupVO.getUserName());
					ps.setString(3, dataVO.getGroupInternalId());
		
					ps.addBatch();		
				}
				ps.executeBatch();
			}
						
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" delete from BatteryGroup ");		
			sql2.append(" where SeqNo in ("+batteryGroupVO.getGroupInternalIdArr()+") ");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.execute();
			
			if(batteryGroupVO.getCompanyList().size()>0) {			
				for(String companyCode : batteryGroupVO.getCompanyList()) {
					//修改公司預設座標
					this.updCompanyLngLat(batteryGroupVO.getUserName(), companyCode, connection, ps3);	
				}
			}
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 取得預設群組
	 * 
	 * @param batteryGroupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getDefaultGroup(BatteryGroupVO batteryGroupVO) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select * from ( ");
		sql.append(" select b.SeqNo as id, ");
		sql.append(" d.SeqNo as defaultId, ");
		sql.append(" b.CompanyCode ");
		sql.append(" from BatteryGroup b, BatteryGroup d ");
		sql.append(" where b.CompanyCode = d.CompanyCode ");		
		sql.append(" and d.DefaultGroup = 0 ");
		sql.append(" and b.SeqNo in ("+batteryGroupVO.getGroupInternalIdArr()+"))a ");

		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 刪除站台
	 * @param batteryGroupVO
	 * @throws Exception
	 */
	public void addBatteryGroupBatch(List<BatteryGroupVO> list) throws Exception {	
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		String companyCode = "";
		String userName = "";
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO BatteryGroup ( ");
			sql.append(" CompanyCode, ");
			sql.append(" GroupID, ");
			sql.append(" GroupName, ");
			sql.append(" Country,");
			sql.append(" Area, ");
			sql.append(" Address, ");
			sql.append(" Lng, ");
			sql.append(" Lat, ");
			sql.append(" DefaultGroup, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" )VALUES (?,?,?,?,?,?,?,?,?,?,?) ");

			ps = connection.prepareStatement(sql.toString());
			for(int i=0; i<list.size(); i++) {
				BatteryGroupVO batteryGroupVO = list.get(i);
				ps.setString(1, batteryGroupVO.getCompanyCode());
				ps.setString(2, batteryGroupVO.getGroupID());
				ps.setString(3, batteryGroupVO.getGroupName());
				ps.setString(4, batteryGroupVO.getCountry());
				ps.setString(5, batteryGroupVO.getArea());
				ps.setString(6, batteryGroupVO.getAddress());
				ps.setString(7, batteryGroupVO.getLng());
				ps.setString(8, batteryGroupVO.getLat());
				ps.setString(9, batteryGroupVO.getDefaultGroup());
				ps.setString(10, batteryGroupVO.getUserName());
				ps.setString(11, batteryGroupVO.getUserName());
	
				ps.addBatch();
				if(i==0) {
					companyCode = batteryGroupVO.getCompanyCode();
					userName = batteryGroupVO.getUserName();
				}
			}
			ps.executeBatch();
			
			//修改公司預設座標
			this.updCompanyLngLat(userName, companyCode, connection, ps2);

			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 取得電池群組
	 * 
	 * @param batteryGroupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryGroup(BatteryGroupVO batteryGroupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * from BatteryGroup ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}

		if(StringUtils.isNotBlank(batteryGroupVO.getGroupID())) {
			sql.append(" and GroupID = ? ");
			parameterList.add(batteryGroupVO.getGroupID());
		}
		
		if(StringUtils.isNotBlank(batteryGroupVO.getGroupInternalId())) {
			sql.append(" and SeqNo = ? ");
			parameterList.add(batteryGroupVO.getGroupInternalId());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 參數設定下拉選單
	 * @param batteryGroupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCommandList(BatteryGroupVO batteryGroupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct c.CompanyCode, bg.Country, bg.Area, bg.GroupID, bg.GroupName, bg.SeqNo ");
		sql.append(" from Company c, BatteryGroup bg ");
		sql.append(" inner join ( ");
		sql.append(" select nh.GroupInternalID, count(1) as BatteryCount   ");
		sql.append(" from NBList nb,  ");
		sql.append(" NBGroupHis nh, ");
		sql.append(" Battery b,  ");
		sql.append(" BattMaxRecTime m ");

		sql.append(" where nb.Active = 13  ");
		sql.append(" and nb.NBID = nh.NBID ");
		sql.append(" and nb.NBID = b.NBID   ");
		sql.append(" and b.NBID = m.NBID  ");
		sql.append(" and b.BatteryID = m.BatteryID  ");
		sql.append(" and m.MaxRecTime between nh.Starttime and nh.Endtime ");
		sql.append(" group by nh.GroupInternalID          ");
		sql.append(" ) a ");
		sql.append(" on bg.SeqNo = a.GroupInternalID  ");
		sql.append(" where 1=1 ");	
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+batteryGroupVO.getCompanyCodeArr()+") ");
		}
		sql.append(" and c.CompanyCode = bg.CompanyCode ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {
				return ToolUtil.parseInt(bean.get("companycode"));
			}
		}).thenComparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("country"));
			}
		}).thenComparing(new Function<DynaBean, String>() {
			public String apply(DynaBean bean) {
				return ObjectUtils.toString(bean.get("area"));
			}
		})).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 取得公司預設站台
	 * @param batteryGroupVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCompanyDefaultGroup(String companyCode) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select SeqNo from BatteryGroup ");
		sql.append(" where CompanyCode = ? ");
		parameterList.add(companyCode);
		sql.append(" and DefaultGroup = 0 ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改公司預設座標
	 * @param userName
	 * @param companyCode
	 * @param connection
	 * @param ps
	 * @throws SQLException
	 */
	public void updCompanyLngLat(String userName, String companyCode, Connection connection, PreparedStatement ps) throws SQLException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" update Company c, ");
			sql.append(" (select g.CompanyCode, round(avg(g.Lng),8) as Lng, round(avg(g.Lat),8) as Lat from BatteryGroup g group by g.CompanyCode) a ");
			sql.append(" set c.Lng = a.Lng,  ");
			sql.append(" c.Lat = a.Lat, ");
			sql.append(" c.UpdateUserName = ? ");
			sql.append(" where c.CompanyCode = ? ");
			sql.append(" and a.CompanyCode = c.CompanyCode ");
			
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, userName);
			ps.setString(2, companyCode);
			ps.execute();
		}catch(SQLException e) {
			throw new SQLException(e.toString());		
		}
	}
	
	/**
	 * 檢核站台號碼
	 * 
	 * @param batteryGroupVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> checkGroupID(BatteryGroupVO batteryGroupVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select 1 from BatteryGroup ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(batteryGroupVO.getCompanyCode())) {
			sql.append(" and CompanyCode = ? ");
			parameterList.add(batteryGroupVO.getCompanyCode());
		}

		if(StringUtils.isNotBlank(batteryGroupVO.getGroupID())) {
			sql.append(" and GroupID = ? ");
			parameterList.add(batteryGroupVO.getGroupID());
		}
		
		if(StringUtils.isNotBlank(batteryGroupVO.getGroupInternalId())) {
			sql.append(" and SeqNo <> ? ");
			parameterList.add(batteryGroupVO.getGroupInternalId());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得站台公司資訊
	 * @param nbid
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getGroupCompanyInfo(String groupInternalID) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select c.CompanyName, c.CompanyCode, g.DefaultGroup ");
		sql.append(" from Company c, BatteryGroup g ");
		sql.append(" where c.CompanyCode = g.CompanyCode ");
		sql.append(" and g.SeqNo = ? ");
		parameterList.add(groupInternalID);
		

		return this.executeQuery(sql.toString(), parameterList);
	}
}
