package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.battery.util.JdbcUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;

public class NbListDAO extends BaseDAO {
    static final String endTime = "2038-01-01 00:00:00";

	/**
	 * 取得通訊板
	 * 
	 * @param nbListVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getNBList(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.CompanyName, n.NBID, bg.DefaultGroup   ");
		sql.append(" from Company c, BatteryGroup bg, NBList n ");
		sql.append(" where c.CompanyCode = bg.CompanyCode ");
		if(StringUtils.isNotBlank(nbListVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(nbListVO.getCompanyCode());
		}
		sql.append(" and bg.SeqNo = n.GroupInternalID ");
		if(StringUtils.isNotBlank(nbListVO.getStart()) && StringUtils.isBlank(nbListVO.getEnd())) {
			sql.append(" and n.NBID = ? ");
			parameterList.add(nbListVO.getStart());
		}else if(StringUtils.isNotBlank(nbListVO.getStart()) && StringUtils.isNotBlank(nbListVO.getEnd())) {
			sql.append(" and n.NBID >= ? ");
			parameterList.add(nbListVO.getStart());
		}
		if(StringUtils.isNotBlank(nbListVO.getEnd())) {
			sql.append(" and n.NBID <= ? ");
			parameterList.add(nbListVO.getEnd());
		}
		if(StringUtils.isNotBlank(nbListVO.getAllocate())) {
			sql.append(" and n.Allocate = ? ");
			parameterList.add(nbListVO.getAllocate());
		}
		if(StringUtils.isNotBlank(nbListVO.getActive())) {
			sql.append(" and n.Active = ? ");
			parameterList.add(nbListVO.getActive());
		}else {
			sql.append(" and n.Active <> 15 ");
		}
		
		sql.append(" order by n.NBID ");
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 批量新增通訊序號
	 * @param nbListVO
	 * @throws Exception
	 */
	public void addNBListBatch(NbListVO nbListVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			List<NbListVO> insertList = nbListVO.getInsertList();
			List<NbListVO> updateList = nbListVO.getUpdateList();
			if(insertList!=null && !insertList.isEmpty()) {
				StringBuffer sql = new StringBuffer();
				sql.append(" INSERT INTO NBList ( ");
				sql.append(" NBID, ");
				sql.append(" GroupInternalID, ");
				sql.append(" CreateUserName, ");
				sql.append(" UpdateUserName ");
				sql.append(" ) VALUES (?,?,?,?) ");
				ps = connection.prepareStatement(sql.toString());
				for(NbListVO dataVO : insertList) {
					ps.setString(1, dataVO.getNbId());
					ps.setString(2, dataVO.getGroupInternalId());
					ps.setString(3, dataVO.getUserName());
					ps.setString(4, dataVO.getUserName());		
					ps.addBatch();		
				}
				ps.executeBatch();
			}
			
			if(updateList!=null && !updateList.isEmpty()) {
				StringBuffer sql = new StringBuffer();
				sql.append(" update NBList set ");
				sql.append(" Active = 13, Allocate =17, ");
				sql.append(" GroupInternalID = ?, ");
				sql.append(" UpdateUserName = ? ");
				sql.append(" where NBID = ? ");
				ps4 = connection.prepareStatement(sql.toString());
				for(NbListVO dataVO : updateList) {			
					ps4.setString(1, dataVO.getGroupInternalId());
					ps4.setString(2, dataVO.getUserName());
					ps4.setString(3, dataVO.getNbId());
					ps4.addBatch();		
				}
				ps4.executeBatch();
			}
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" INSERT INTO NBGroupHis ( ");
			sql2.append(" NBID, ");
			sql2.append(" GroupInternalID, ");
			sql2.append(" StartTime, ");
			sql2.append(" EndTime, ");
			sql2.append(" CreateUserName, ");
			sql2.append(" UpdateUserName ");
			sql2.append(" ) VALUES (?,?,?,?,?,?) ");
			ps2 = connection.prepareStatement(sql2.toString());
			for(NbListVO dataVO : nbListVO.getDataList()) {
				ps2.setString(1, dataVO.getNbId());
				ps2.setString(2, dataVO.getGroupInternalId());
				ps2.setString(3, dataVO.getStartTime());
				ps2.setString(4, dataVO.getEndTime());
				ps2.setString(5, dataVO.getUserName());
				ps2.setString(6, dataVO.getUserName());		
				ps2.addBatch();		
			}
			ps2.executeBatch();	
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" INSERT INTO NBHistory ( ");
			sql3.append(" NBID, ");
			sql3.append(" ModifyItem, ");
			sql3.append(" CreateUserName, ");
			sql3.append(" UpdateUserName ");
			sql3.append(" ) VALUES (?,?,?,?) ");
			ps3 = connection.prepareStatement(sql3.toString());
			for(NbListVO dataVO : nbListVO.getDataList()) {
				ps3.setString(1, dataVO.getNbId());
				ps3.setString(2, dataVO.getModifyItem());
				ps3.setString(3, dataVO.getUserName());
				ps3.setString(4, dataVO.getUserName());		
				ps3.addBatch();		
			}
			ps3.executeBatch();
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 通訊板異動紀錄
	 * 
	 * @param nbListVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getNBHistory(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from ( ");
		sql.append(" select  ");
		sql.append(" nh.SeqNo, ");
		sql.append(" c.CompanyName, ");//-- 公司
		sql.append(" nh.NBID, ");//-- 通訊序號
		sql.append(" b.BatteryID, ");//-- 電池組ID(母板)
		sql.append(" b.SeqNo as BattInternalId, ");
		sql.append(" nl.Active, ");
		sql.append(" nh.ModifyItem, ");//-- 異動項目
		sql.append(" c2.CompanyName as AllocateCompany, ");//-- 序號歸屬公司
		sql.append(" nh.CreateTime, ");//-- 異動時間
		sql.append(" nh.CreateUserName, ");//-- 異動人員
		sql.append(" nh.Remark ");//-- 異動備註
		sql.append(" from ");
		sql.append(" NBHistory nh ");
		sql.append(" left join NBList nl ");
		sql.append(" on nl.NBID = nh.NBID ");
		sql.append(" left join Battery b ");
		sql.append(" on b.NBID = nh.NBID ");
		sql.append(" and b.BatteryID = 0, ");
		sql.append(" Company c, ");
		sql.append(" Company c2 ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(nbListVO.getCompanyCode())) {
			sql.append(" and nh.CompanyCode = ? ");
			parameterList.add(nbListVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(nbListVO.getCompanyCodeArr())) {
			sql.append(" and nh.CompanyCode in ("+nbListVO.getCompanyCodeArr()+") ");
		}
		if(StringUtils.isNotBlank(nbListVO.getNbidArr())) {
			sql.append(" and nh.NBID in ("+nbListVO.getNbidArr()+") ");
		}
		if(StringUtils.isNotBlank(nbListVO.getModifyItemArr())) {
			sql.append(" and nh.ModifyItem in ("+nbListVO.getModifyItemArr()+") ");
		}
		if(StringUtils.isNotBlank(nbListVO.getStartDate())) {
			sql.append(" and nh.CreateTime >= STR_TO_DATE(?,'%Y-%m-%d %H:%i') ");
			parameterList.add(nbListVO.getStartDate());
		}
		if(StringUtils.isNotBlank(nbListVO.getEndDate())) {
			sql.append(" and nh.CreateTime <= STR_TO_DATE(?,'%Y-%m-%d %H:%i') ");
			parameterList.add(nbListVO.getEndDate());
		}
		if(StringUtils.isNotBlank(nbListVO.getBatteryGroupIdArr())) {
			sql.append(" and b.SeqNo in ("+nbListVO.getBatteryGroupIdArr()+") ");
		}
				
		sql.append(" and c.CompanyCode = nh.CompanyCode ");
		sql.append(" and c2.CompanyCode = nh.AllocateCompanyCode ");
		sql.append(" ) a ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, Integer>() {
			public Integer apply(DynaBean bean) {				
				return ToolUtil.parseInt(bean.get("seqno"));				
			}
		}).reversed()).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 通訊序號下拉選單
	 * 
	 * @param nbListVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getNBIDList(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct c.CompanyName, n.CompanyCode, n.NBID ");
		sql.append(" from NBHistory n, ");
		sql.append(" Company c ");
		sql.append(" where n.CompanyCode = c.CompanyCode ");
		
		if(StringUtils.isNotBlank(nbListVO.getCompanyCode())) {	//非業主公司只呈現(9:啟用,10:停用)				
			sql.append(" and n.CompanyCode = ? ");			
			sql.append(" and n.ModifyItem in (9,10) ");
			parameterList.add(nbListVO.getCompanyCode());
		}
		sql.append(" order by c.CompanyName ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核通訊序號
	 * 
	 * @param nbListVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> checkNBID(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select NBID from NBList  ");
		sql.append(" where NBID = ? ");
		sql.append(" and Active <> 15 ");
		parameterList.add(nbListVO.getNbId());
				
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核刪除通訊序號
	 * 
	 * @param nbListVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> checkDelNBID(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select NBID from NBList  ");
		sql.append(" where NBID = ? ");
		sql.append(" and Active = 15 ");
		parameterList.add(nbListVO.getNbId());
				
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 修改分配情況
	 * @param nbListVO
	 * @throws Exception
	 */
	public void updNBListAllocate(NbListVO nbListVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" INSERT INTO NBHistory ( ");
			sql1.append(" NBID, ");
			sql1.append(" ModifyItem, ");
			sql1.append(" AllocateCompanyCode, ");
			sql1.append(" Remark, ");
			sql1.append(" CreateUserName, ");
			sql1.append(" UpdateUserName ");
			sql1.append(" ) VALUES (?,?,?,?,?,?) ");
			ps = connection.prepareStatement(sql1.toString());
			for(String nbid : nbListVO.getNbList()) {
				ps.setString(1, nbid);
				ps.setString(2, nbListVO.getModifyItem());
				ps.setString(3, nbListVO.getCompanyCode());
				ps.setString(4, nbListVO.getRemark());
				ps.setString(5, nbListVO.getUserName());
				ps.setString(6, nbListVO.getUserName());		
				ps.addBatch();		
			}
			ps.executeBatch();
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" UPDATE NBList SET ");
			sql2.append(" GroupInternalID = ?, ");
			sql2.append(" Allocate = ?, ");
			sql2.append(" ContinuousSeqNo = null, ");
			sql2.append(" UpdateUserName = ? ");
			sql2.append(" WHERE NBID in ("+nbListVO.getNbidArr()+")");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, nbListVO.getGroupInternalId());
			ps2.setString(2, nbListVO.getAllocate());
			ps2.setString(3, nbListVO.getUserName());
			ps2.execute();

			String sysdate = sdf.format(new Date());
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" UPDATE NBGroupHis SET ");
			sql3.append(" EndTime = ?, ");
			sql3.append(" UpdateUserName = ? ");
			sql3.append(" WHERE NBID in ("+nbListVO.getNbidArr()+")");
			sql3.append(" and EndTime = str_to_date(?,'%Y-%m-%d %H:%i:%S') ");
			ps3 = connection.prepareStatement(sql3.toString());
			ps3.setString(1, sysdate);
			ps3.setString(2, nbListVO.getUserName());
			ps3.setString(3, endTime);
			ps3.execute();
	        
	        StringBuffer sql4 = new StringBuffer();
			sql4.append(" INSERT INTO NBGroupHis ( ");
			sql4.append(" NBID, ");
			sql4.append(" GroupInternalID, ");
			sql4.append(" StartTime, ");
			sql4.append(" EndTime, ");
			sql4.append(" CreateUserName, ");
			sql4.append(" UpdateUserName ");
			sql4.append(" ) VALUES (?,?,?,?,?,?) ");
			ps4 = connection.prepareStatement(sql4.toString());
			for(String nbid : nbListVO.getNbList()) {
				ps4.setString(1, nbid);
				ps4.setString(2, nbListVO.getGroupInternalId());
				ps4.setString(3, sysdate);
				ps4.setString(4, endTime);
				ps4.setString(5, nbListVO.getUserName());
				ps4.setString(6, nbListVO.getUserName());		
				ps4.addBatch();		
			}
			ps4.executeBatch();
			
			if("16".equals(nbListVO.getAllocate())) {//分配時，清空電池型號、安裝日期
				StringBuffer sql5 = new StringBuffer();
				sql5.append(" update Battery ");
				sql5.append(" set InstallDate = null, ");
				sql5.append(" BatteryTypeCode = null, ");
				sql5.append(" UpdateUserName = ? ");
				sql5.append(" where NBID = ? ");
				ps5 = connection.prepareStatement(sql5.toString());
				
				for(String nbid : nbListVO.getNbList()) {					
					ps5.setString(1, nbListVO.getUserName());
					ps5.setString(2, nbid);
					ps5.addBatch();		
				}
				ps5.executeBatch();
			}
			
			String closeContent = "";
			if("16".equals(nbListVO.getAllocate())) {//16分配,17 未分配
				closeContent = "System resolves this alert automatically when the Comm. ID is allocated.";
			}else {
				closeContent = "System resolves this alert automatically when the Comm. ID is unallocated.";
			}
			StringBuffer sql6 = new StringBuffer();
			sql6.append(" update Event set  ");
			sql6.append(" CloseTime = ?,  ");
			sql6.append(" CloseUser = 'System', ");
			sql6.append(" CloseContent = ?,  ");
			sql6.append(" EventStatus = 6  ");
			sql6.append(" where NBID = ? ");
			sql6.append(" and EventStatus = 5 ");
			ps6 = connection.prepareStatement(sql6.toString());
			
			for(String nbid : nbListVO.getNbList()) {					
				ps6.setString(1, sysdate);
				ps6.setString(2, closeContent);
				ps6.setString(3, nbid);
				ps6.addBatch();		
			}
			ps6.executeBatch();
			
								

			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(ps2);
			JdbcUtil.close(ps3);
			JdbcUtil.close(ps4);
			JdbcUtil.close(ps5);
			JdbcUtil.close(ps6);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 修改啟用狀態
	 * @param nbListVO
	 * @throws Exception
	 */
	public void updNBListActive(NbListVO nbListVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE NBList SET ");
			sql.append(" Active = ?, ");
			sql.append(" UpdateUserName = ? ");
			sql.append(" WHERE NBID in ("+nbListVO.getNbidArr()+")");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, nbListVO.getActive());
			ps.setString(2, nbListVO.getUserName());
			ps.execute();			
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" INSERT INTO NBHistory ( ");
			sql2.append(" CompanyCode, ");
			sql2.append(" NBID, ");
			sql2.append(" ModifyItem, ");
			sql2.append(" AllocateCompanyCode, ");
			sql2.append(" Remark, ");
			sql2.append(" CreateUserName, ");
			sql2.append(" UpdateUserName ");
			sql2.append(" ) VALUES (?,?,?,?,?,?,?) ");
			ps2 = connection.prepareStatement(sql2.toString());
			for(String[] nbid : nbListVO.getNbInfoList()) {
				ps2.setString(1, nbListVO.getCompanyCode());
				ps2.setString(2, nbid[1]);
				ps2.setString(3, nbListVO.getModifyItem());
				ps2.setString(4, nbid[0]);
				ps2.setString(5, nbListVO.getRemark());
				ps2.setString(6, nbListVO.getUserName());
				ps2.setString(7, nbListVO.getUserName());		
				ps2.addBatch();	
			}
			ps2.executeBatch();
			
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
	 * 站台設定
	 * @param nbListVO
	 * @throws Exception
	 */
	public void updGroupSetup(NbListVO nbListVO) throws Exception {	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;
		PreparedStatement ps8 = null;
		PreparedStatement ps9 = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
			
			String closeContent = "Due to the group of this Comm. ID is changed, system resolves this alert automatically.";
			String sysdate = sdf.format(new Date());
			
			int i = 1;
			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE NBList SET ");
			sql.append(" GroupInternalID = ?, ");
			if(StringUtils.isNotBlank(nbListVO.getContinuousSeqNo())) {
				sql.append(" ContinuousSeqNo = ?, ");
			}else {
				sql.append(" ContinuousSeqNo = null, ");
			}
			sql.append(" UpdateUserName = ? ");
			sql.append(" WHERE NBID = ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(i++, nbListVO.getGroupInternalId());
			if(StringUtils.isNotBlank(nbListVO.getContinuousSeqNo())) {	
				ps.setString(i++, nbListVO.getContinuousSeqNo());
			}
			ps.setString(i++, nbListVO.getUserName());
			ps.setString(i++, nbListVO.getNbId());
			ps.execute();		
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" UPDATE NBGroupHis SET ");
			sql2.append(" ContinuousSeqNo = ?, ");
			sql2.append(" UpdateUserName = ? ");
			sql2.append(" WHERE SeqNo = ? ");
			sql2.append(" and ContinuousSeqNo is null ");
			ps2 = connection.prepareStatement(sql2.toString());
			ps2.setString(1, nbListVO.getContinuousSeqNo());
			ps2.setString(2, nbListVO.getUserName());
			ps2.setString(3, nbListVO.getContinuousSeqNo());
			ps2.execute();
			
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append(" UPDATE NBGroupHis SET ");
			sql3.append(" EndTime = ?, ");
			sql3.append(" UpdateUserName = ? ");
			sql3.append(" WHERE NBID = ? ");
			sql3.append(" and EndTime = str_to_date(?,'%Y-%m-%d %H:%i:%S') ");
			ps3 = connection.prepareStatement(sql3.toString());
			ps3.setString(1, sysdate);
			ps3.setString(2, nbListVO.getUserName());
			ps3.setString(3, nbListVO.getNbId());
			ps3.setString(4, endTime);
			ps3.execute();
			
			i = 1;
			String parameter = "";
			StringBuffer sql4 = new StringBuffer();
			sql4.append(" INSERT INTO NBGroupHis ( ");
			sql4.append(" NBID, ");
			sql4.append(" GroupInternalID, ");
			sql4.append(" StartTime, ");
			sql4.append(" EndTime, ");
			if(StringUtils.isNotBlank(nbListVO.getPreviousNBID())) {
				sql4.append(" PreviousNBID, ");
				parameter += ",?";
			}
			if(StringUtils.isNotBlank(nbListVO.getContinuousSeqNo())) {
				sql4.append(" ContinuousSeqNo, ");
				parameter += ",?";
			}
			sql4.append(" CreateUserName, ");
			sql4.append(" UpdateUserName ");
			sql4.append(" ) VALUES (?,?,?,?,?,?"+parameter+") ");
			ps4 = connection.prepareStatement(sql4.toString());
			ps4.setString(i++, nbListVO.getNbId());
			ps4.setString(i++, nbListVO.getGroupInternalId());
			ps4.setString(i++, sysdate);
			ps4.setString(i++, endTime);
			if(StringUtils.isNotBlank(nbListVO.getPreviousNBID())) {
				ps4.setString(i++, nbListVO.getPreviousNBID());	
			}
			if(StringUtils.isNotBlank(nbListVO.getContinuousSeqNo())) {
				ps4.setString(i++, nbListVO.getContinuousSeqNo());
			}
			ps4.setString(i++, nbListVO.getUserName());
			ps4.setString(i++, nbListVO.getUserName());					
			ps4.execute();
			
			StringBuffer sql8 = new StringBuffer();
			sql8.append(" update Event set  ");
			sql8.append(" CloseTime = ?,  ");
			sql8.append(" CloseUser = 'System', ");
			sql8.append(" CloseContent = ?,  ");
			sql8.append(" EventStatus = 6  ");
			sql8.append(" where NBID = ? ");
			sql8.append(" and EventStatus = 5 ");
			ps8 = connection.prepareStatement(sql8.toString());
			ps8.setString(1, sysdate);
			ps8.setString(2, closeContent);
			ps8.setString(3, nbListVO.getNbId());					
			ps8.execute();
			
			//將被接續NBID移回預設站台
			if(StringUtils.isNotBlank(nbListVO.getPreviousNBID())) {
				StringBuffer sql5 = new StringBuffer();
				sql5.append(" UPDATE NBList SET ");
				sql5.append(" GroupInternalID = ?, ");				
				sql5.append(" ContinuousSeqNo = null, ");				
				sql5.append(" UpdateUserName = ? ");
				sql5.append(" WHERE NBID = ? ");
				ps5 = connection.prepareStatement(sql5.toString());
				ps5.setString(1, nbListVO.getDefaultGroupInternalId());
				ps5.setString(2, nbListVO.getUserName());
				ps5.setString(3, nbListVO.getPreviousNBID());
				ps5.execute();		
				
				
				StringBuffer sql6 = new StringBuffer();
				sql6.append(" UPDATE NBGroupHis SET ");
				sql6.append(" EndTime = ?, ");
				sql6.append(" UpdateUserName = ? ");
				sql6.append(" WHERE NBID = ? ");
				sql6.append(" and EndTime = str_to_date(?,'%Y-%m-%d %H:%i:%S') ");
				ps6 = connection.prepareStatement(sql6.toString());
				ps6.setString(1, sysdate);
				ps6.setString(2, nbListVO.getUserName());
				ps6.setString(3, nbListVO.getPreviousNBID());
				ps6.setString(4, endTime);
				ps6.execute();
				
				StringBuffer sql7 = new StringBuffer();
				sql7.append(" INSERT INTO NBGroupHis ( ");
				sql7.append(" NBID, ");
				sql7.append(" GroupInternalID, ");
				sql7.append(" StartTime, ");
				sql7.append(" EndTime, ");				
				sql7.append(" CreateUserName, ");
				sql7.append(" UpdateUserName ");
				sql7.append(" ) VALUES (?,?,?,?,?,?) ");
				ps7 = connection.prepareStatement(sql7.toString());
				ps7.setString(1, nbListVO.getPreviousNBID());
				ps7.setString(2, nbListVO.getDefaultGroupInternalId());
				ps7.setString(3, sysdate);
				ps7.setString(4, endTime);
				ps7.setString(5, nbListVO.getUserName());
				ps7.setString(6, nbListVO.getUserName());					
				ps7.execute();
				
				StringBuffer sql9 = new StringBuffer();
				sql9.append(" update Event set  ");
				sql9.append(" CloseTime = ?,  ");
				sql9.append(" CloseUser = 'System', ");
				sql9.append(" CloseContent = ?,  ");
				sql9.append(" EventStatus = 6  ");
				sql9.append(" where NBID = ? ");
				sql9.append(" and EventStatus = 5 ");
				ps9 = connection.prepareStatement(sql9.toString());
				ps9.setString(1, sysdate);
				ps9.setString(2, closeContent);
				ps9.setString(3, nbListVO.getPreviousNBID());					
				ps9.execute();
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
			JdbcUtil.close(ps4);
			JdbcUtil.close(ps5);
			JdbcUtil.close(ps6);
			JdbcUtil.close(ps7);
			JdbcUtil.close(ps8);
			JdbcUtil.close(ps9);
			JdbcUtil.close(connection);
		}
	}
	
	public List<DynaBean> getContinuousSeqNo(String nbid) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select (case when n.ContinuousSeqNo is null then h.SeqNo else n.ContinuousSeqNo end) as ContinuousSeqNo  ");
		sql.append(" from NBList n, NBGroupHis h  ");
		sql.append(" where n.NBID = h.NBID  ");
		sql.append(" and n.NBID = ? ");
		sql.append(" and h.EndTime = str_to_date(?,'%Y-%m-%d %H:%i:%S') ");
		parameterList.add(nbid);
		parameterList.add(endTime);
				
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 取得通訊序號的預設站台
	 * @param nbid
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNBIDDefaultGroup(String nbid) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select b.CompanyCode, c.CompanyName, d.SeqNo ");
		sql.append(" from NBList n, BatteryGroup b, BatteryGroup d, Company c ");
		sql.append(" where n.NBID = ? ");
		parameterList.add(nbid);
		sql.append(" and n.GroupInternalID = b.SeqNo ");
		sql.append(" and b.CompanyCode = d.CompanyCode  ");
		sql.append(" and d.DefaultGroup = 0 ");
		sql.append(" and b.CompanyCode = c.CompanyCode ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 站台接續通訊序號
	 * @param nbListVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getGroupNBList(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct n.NBID ");
		sql.append(" from BatteryGroup b, NBList n  ");
		sql.append(" where b.SeqNo = n.GroupInternalID  ");
		sql.append(" and b.SeqNo = ?  ");
		parameterList.add(nbListVO.getGroupInternalId());
		sql.append(" and n.Active = 13 ");
		sql.append("  order by n.NBID ");

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 查詢通訊板歸屬站台歷史
	 * @param nbListVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNBGroupHis(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * ");
		sql.append(" from NBGroupHis h  ");
		sql.append(" where 1=1 ");

		if(StringUtils.isNotBlank(nbListVO.getContinuousSeqNo())) {//有接續
			sql.append(" and ContinuousSeqNo = ? ");
			parameterList.add(nbListVO.getContinuousSeqNo());
		}else {//沒有接續則顯示該通訊序號以前收的歷史數據
			sql.append(" and NBID = ? ");
			parameterList.add(nbListVO.getNbId());
		}
		
		sql.append(" order by StartTime desc ");
				
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 接續序號歷史篩選
	 * @param nbListVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNBGroupHisList(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct c.CompanyName, ");
		sql.append(" c.CompanyCode, ");
		sql.append(" g.Country, ");
		sql.append(" g.Area, ");
		sql.append(" g.GroupName, ");
		sql.append(" g.GroupId, ");
		sql.append(" g.SeqNo, ");
		sql.append(" nh.NBID ");		
		sql.append(" from Company c, ");
		sql.append(" BatteryGroup g,  ");
		sql.append(" NBGroupHis nh ");
		sql.append(" where c.CompanyCode = g.CompanyCode ");
		if(StringUtils.isNotBlank(nbListVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(nbListVO.getCompanyCode());
		}
		sql.append(" and g.SeqNo = nh.GroupInternalID  ");
		sql.append(" and nh.ContinuousSeqNo is not null ");
		sql.append(" order by nh.NBID ");
			
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	
	/**
	 * 查詢通訊板歸屬站台歷史
	 * @param nbListVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNBGroupHistory(NbListVO nbListVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.CompanyName,  ");
		sql.append(" g.Country,  ");
		sql.append(" g.Area,  ");
		sql.append(" g.GroupId,  ");
		sql.append(" g.GroupName, 		  ");
		sql.append(" nh.NBID, ");
		sql.append(" nh.PreviousNBID, ");
		sql.append(" nh.Starttime ");
		sql.append(" from Company c, ");
		sql.append(" BatteryGroup g,   ");
		sql.append(" NBGroupHis nh		  ");
		sql.append(" where c.CompanyCode = g.CompanyCode ");
		if(StringUtils.isNotBlank(nbListVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(nbListVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(nbListVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+nbListVO.getCompanyCodeArr()+") ");
		}

		if(StringUtils.isNotBlank(nbListVO.getGroupInternalIdArr())) {
			sql.append(" and g.SeqNo in ("+nbListVO.getGroupInternalIdArr()+") ");
		}
		if(StringUtils.isNotBlank(nbListVO.getCountryArr())) {
			sql.append(" and g.Country in ("+nbListVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(nbListVO.getAreaArr())) {
			sql.append(" and g.Area in ("+nbListVO.getAreaArr()+") ");
		}
		
		sql.append(" and g.SeqNo = nh.GroupInternalID  ");
		sql.append(" and nh.PreviousNBID is not null         ");
		if(StringUtils.isNotBlank(nbListVO.getStartDate())) {
			sql.append(" and nh.StartTime >= STR_TO_DATE(?,'%Y-%m-%d %H:%i') ");
			parameterList.add(nbListVO.getStartDate());
		}
		if(StringUtils.isNotBlank(nbListVO.getEndDate())) {
			sql.append(" and nh.StartTime <= STR_TO_DATE(?,'%Y-%m-%d %H:%i') ");
			parameterList.add(nbListVO.getEndDate());
		}
		sql.append(" order by nh.StartTime desc, g.GroupName ");
				
		return this.executeQuery(sql.toString(), parameterList);
	}
}
