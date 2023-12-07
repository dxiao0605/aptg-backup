package aptg.battery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
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
import aptg.battery.vo.CommandTaskVO;

public class CommandTaskDAO extends BaseDAO {
	
	/**
	 * 新增命令任務
	 * @param commandTaskVO
	 * @throws Exception
	 */
	public void addCommandTask(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" INSERT INTO CommandTask ( ");
		sql.append(" TaskID,  ");
		sql.append(" GroupInternalID, ");
		sql.append(" NBID, ");	
		sql.append(" BatteryID, ");	
		sql.append(" CommandID, ");		
		sql.append(" Config, ");	
		sql.append(" HexConfig, ");
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		sql.append(" ) VALUES (?,?,?,?,?,? ,?,?,?) ");
		
		parameterList.add(commandTaskVO.getTaskId());
		parameterList.add(commandTaskVO.getGroupInternalId());
		parameterList.add(commandTaskVO.getNbId());
		parameterList.add(commandTaskVO.getBatteryId());
		parameterList.add(commandTaskVO.getCommandId());
		parameterList.add(commandTaskVO.getConfig());
		parameterList.add(commandTaskVO.getHexConfig());
		parameterList.add(commandTaskVO.getUserName());
		parameterList.add(commandTaskVO.getUserName());

		this.executeUpdate(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getCommandTask(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from CommandTask where TaskID = ? ");
		parameterList.add(commandTaskVO.getTaskId());
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	public List<DynaBean> getCommandTaskListPOC(CommandTaskVO commandTaskVO) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ct.TaskID, ");
		sql.append(" (case when c.NBID is null then ct.NBID else c.NBID end) as NBID , ");
		sql.append(" (case when c.BatteryID is null then c.BatteryID else ct.BatteryID end) as BatteryID ,");
		sql.append(" 	   ct.CommandID, ");
		sql.append("        ct.CreateTime, ");
		sql.append("        c.TransactionID, ");
		sql.append("        c.PublishTime, ");
		sql.append(" 	   c.AckTime, ");
		sql.append("        c.ResponseTime, ");
		sql.append(" 	   c.ResponseContent, ");
		sql.append("        ct.Config, ");
		sql.append("        ct.HexConfig ");
		sql.append(" from CommandTask ct  ");
		sql.append("      left join Command c  ");
		sql.append("      on c.TaskID = ct.TaskID ");
		sql.append(" order by ct.CreateTime desc ");

		return this.executeQuery(sql.toString(), null);
	}
	
	/**
	 * 批量新增命令任務
	 * @param taskList
	 * @throws Exception
	 */
	public void addCommandTaskBatch(List<CommandTaskVO> taskList) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();

			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO CommandTask ( ");
			sql.append(" TaskID,  ");
			sql.append(" GroupInternalID, ");
			sql.append(" NBID, ");	
			sql.append(" BatteryID, ");	
			sql.append(" CommandID, ");		
			sql.append(" Config, ");	
			sql.append(" HexConfig, ");
			sql.append(" CreateUserName, ");
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?,?,? ,?,?,?) ");
						
			ps = connection.prepareStatement(sql.toString());
			for(CommandTaskVO commandTaskVO : taskList) {
				ps.setString(1, commandTaskVO.getTaskId());
				ps.setString(2, commandTaskVO.getGroupInternalId());
				ps.setString(3, commandTaskVO.getNbId());
				ps.setString(4, commandTaskVO.getBatteryId());
				ps.setString(5, commandTaskVO.getCommandId());
				ps.setString(6, commandTaskVO.getConfig());
				ps.setString(7, commandTaskVO.getHexConfig());
				ps.setString(8, commandTaskVO.getUserName());
				ps.setString(9, commandTaskVO.getUserName());
		
				ps.addBatch();		
			}
			ps.executeBatch();
		}catch(SQLException e) {
			throw new SQLException(e.toString());		
		}finally {
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 查詢電池明細設定內阻/電壓組數
	 * @param commandTaskVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBatteryDetailCount(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select count(1) as count from BatteryDetail ");
		sql.append(" where NBID = ? ");
		sql.append(" and BatteryID = ? ");
		sql.append(" and Category = ? ");
		parameterList.add(commandTaskVO.getNbId());
		parameterList.add(commandTaskVO.getBatteryId());
		parameterList.add(commandTaskVO.getCategory());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 刪除電池明細設定內阻/電壓
	 * @param commandTaskVO
	 * @throws Exception
	 */
	public void delBatteryDetail(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" delete from BatteryDetail ");
		sql.append(" where NBID = ? ");
		sql.append(" and BatteryID = ? ");
		sql.append(" and Category = ? ");
		parameterList.add(commandTaskVO.getNbId());
		parameterList.add(commandTaskVO.getBatteryId());
		parameterList.add(commandTaskVO.getCategory());

		this.executeUpdate(sql.toString(), parameterList);
	}
	
	public void addBatteryDetail(CommandTaskVO commandTaskVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();

			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO BatteryDetail ( ");
			sql.append(" NBID, ");
			sql.append(" BatteryID, ");
			sql.append(" Category, ");	
			sql.append(" OrderNo, ");
			sql.append(" CreateUserName, ");	
			sql.append(" UpdateUserName ");
			sql.append(" ) VALUES (?,?,?,?,?,?) ");
						
			ps = connection.prepareStatement(sql.toString());
			for(int i=0; i<commandTaskVO.getRecords(); i++) {
				ps.setString(1, commandTaskVO.getNbId());
				ps.setString(2, commandTaskVO.getBatteryId());
				ps.setString(3, commandTaskVO.getCategory());
				ps.setString(4, String.valueOf(i+1));
				ps.setString(5, commandTaskVO.getUserName());
				ps.setString(6, commandTaskVO.getUserName());
				ps.addBatch();		
			}
			ps.executeBatch();
		}catch(SQLException e) {
			throw new SQLException(e.toString());		
		}finally {
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
	
	/**
	 * 參數設定歷史
	 * @param commandTaskVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getCommandTaskList(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  ");
		sql.append(" c.CompanyName, ");
		sql.append(" bg.Country, ");//-- 國家
		sql.append(" bg.Area, ");//-- 地域
		sql.append(" bg.GroupID, ");//-- 站台編號
		sql.append(" bg.GroupName,  ");//-- 站台名稱
		sql.append(" b.NBID, ");
		sql.append(" b.BatteryID, ");
		sql.append(" bt.BatteryTypeName, ");// -- 電池型號
		sql.append(" ct.CommandID, ");//-- 指令
		sql.append(" ct.CreateTime, ");//-- 傳送時間
		sql.append(" cd.PublishTime, ");//-- 發佈時間
		sql.append(" cd.AckTime, ");//-- Ack時間
		sql.append(" cd.ResponseTime, ");//-- Resp時間
		sql.append(" cd.ResponseCode, ");
		sql.append(" cd.ResponseContent, ");//-- 回應訊息
		sql.append(" ct.Config,  ");
		sql.append(" ct.HexConfig  ");
		sql.append(" from BatteryGroup bg,  ");
//		sql.append("      NBList nb,  ");	// -- 註解 by Austin (2022/02/18)
		sql.append("      NBGroupHis nh, ");
		sql.append("      Company c, ");
		sql.append("      Battery b ");
		sql.append("      left join BatteryTypeList bt on bt.BatteryTypeCode = b.BatteryTypeCode, ");
		sql.append(" 	  CommandTask ct   ");
		sql.append("      left join Command cd on ct.TaskID = cd.TaskID");
		sql.append(" where 1=1 ");
		
		if(StringUtils.isNotBlank(commandTaskVO.getCountryArr())) {
			sql.append(" and bg.Country in ("+commandTaskVO.getCountryArr()+") ");
		}
		if(StringUtils.isNotBlank(commandTaskVO.getAreaArr())) {
			sql.append(" and bg.Area in ("+commandTaskVO.getAreaArr()+") ");
		}
		
		if(StringUtils.isNotBlank(commandTaskVO.getGroupInternalIdArr())) {
			sql.append(" and bg.SeqNo in ("+commandTaskVO.getGroupInternalIdArr()+") ");
		}
//		sql.append(" and bg.SeqNo = nb.GroupInternalID ");	// -- 註解 by Austin (2022/02/18)
		sql.append(" and bg.SeqNo = nh.GroupInternalID ");
		sql.append(" and bg.CompanyCode = c.CompanyCode ");		
		if(StringUtils.isNotBlank(commandTaskVO.getCompanyCode())) {
			sql.append(" and c.CompanyCode = ? ");
			parameterList.add(commandTaskVO.getCompanyCode());
		}
		if(StringUtils.isNotBlank(commandTaskVO.getCompanyCodeArr())) {
			sql.append(" and c.CompanyCode in ("+commandTaskVO.getCompanyCodeArr()+") ");
		}
//		sql.append(" and nb.NBID = nh.NBID ");	// -- 註解 by Austin (2022/02/18)
//		sql.append(" and nb.NBID = b.NBID ");	// -- 註解 by Austin (2022/02/18)
		sql.append(" and nh.NBID = ct.NBID ");	// -- and by Austin (2022/02/18)
		
		sql.append(" and b.NBID = ct.NBID ");
		sql.append(" and b.BatteryID = ct.BatteryID ");
		if(StringUtils.isNotBlank(commandTaskVO.getCommandIdArr())) {
			sql.append(" and ct.CommandID in ("+commandTaskVO.getCommandIdArr()+") ");
		}
		if(StringUtils.isNotBlank(commandTaskVO.getTaskIDArr())) {
			sql.append(" and ct.TaskID in ("+commandTaskVO.getTaskIDArr()+") ");
		}
		if(StringUtils.isNotBlank(commandTaskVO.getResponseArr()) || 
				"1".equals(commandTaskVO.getResponseNull()) ) {
			boolean orFlag = false;
			sql.append(" and ( ");
			if(StringUtils.isNotBlank(commandTaskVO.getResponseArr())) {
				sql.append(" cd.ResponseCode in ("+commandTaskVO.getResponseArr()+") ");
				orFlag = true;
			}
			
			if("1".equals(commandTaskVO.getResponseNull())) {
				if(orFlag) 
					sql.append(" or ");
				sql.append(" cd.ResponseCode is null ");
			}
			sql.append(" ) ");
		}
		
		if(StringUtils.isNotBlank(commandTaskVO.getStartDate())) {
			sql.append(" and ct.CreateTime >= STR_TO_DATE(?,'%Y-%m-%d %H:%i')  ");	
			parameterList.add(commandTaskVO.getStartDate());
		}
		
		if(StringUtils.isNotBlank(commandTaskVO.getEndDate())) {
			sql.append(" and ct.CreateTime <= STR_TO_DATE(?,'%Y-%m-%d %H:%i')  ");	
			parameterList.add(commandTaskVO.getEndDate());
		}
		sql.append(" and ct.CreateTime between nh.Starttime and nh.Endtime ");
		
		List<DynaBean> rows = this.executeQuery(sql.toString(), parameterList);
		
		//排序
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<DynaBean> sortRows = rows.stream().sorted(Comparator.comparing(new Function<DynaBean, Date>() {
			public Date apply(DynaBean bean) {
				try {
					return sdf.parse(sdf.format(bean.get("createtime")));
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			}
		}).reversed()).collect(Collectors.toList());
		
		return sortRows;
	}
	
	/**
	 * 查詢電池組指令限制
	 * @param commandTaskVO
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getBattCommandSetup(CommandTaskVO commandTaskVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.B3  ");
		sql.append(" from Company c, ");
		sql.append(" BatteryGroup g, ");
		sql.append(" NBList n, ");
		sql.append(" Battery b ");
		sql.append(" where c.CompanyCode = g.CompanyCode ");
		sql.append(" and g.SeqNo = n.GroupInternalID ");
		sql.append(" and n.NBID = b.NBID ");
		sql.append(" and b.NBID = ? ");
		sql.append(" and b.BatteryID = ? ");
		parameterList.add(commandTaskVO.getNbId());
		parameterList.add(commandTaskVO.getBatteryId());
		
		return this.executeQuery(sql.toString(), parameterList);
	}
}
