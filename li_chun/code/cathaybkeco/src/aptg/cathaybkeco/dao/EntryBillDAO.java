package aptg.cathaybkeco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.vo.EntryBillVO;

public class EntryBillDAO extends BaseDAO {

	/**
	 * 取得電費單資訊
	 * 
	 * @param entryBillVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getEntryBill(EntryBillVO entryBillVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * ");
		sql.append(" from EntryBill ");
		sql.append(" where 1=1 ");
		
		if (StringUtils.isNotBlank(entryBillVO.getPowerAccount())) {
			sql.append(" and PowerAccount = ? ");
			parameterList.add(entryBillVO.getPowerAccount());
		}
		if (StringUtils.isNotBlank(entryBillVO.getBillMon())) {
			sql.append(" and BillMon = ? ");
			parameterList.add(entryBillVO.getBillMon());
		}
		if(StringUtils.isNotBlank(entryBillVO.getOldBillMon())) {
			sql.append(" and BillMon <> ? ");
			parameterList.add(entryBillVO.getOldBillMon());
		}
		sql.append(" order by CAST(BillMon as datetime) ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 新增電費單資訊
	 * 
	 * @param entryBillVO
	 * @throws Exception
	 */
	public void addEntryBill(EntryBillVO entryBillVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO EntryBill ( ");
		sql.append(" PowerAccount, ");
		sql.append(" BillMon, ");
		sql.append(" BillStartDay, ");
		sql.append(" BillEndDay, ");
		sql.append(" BaseCharge, ");
		sql.append(" UsageCharge, ");
		sql.append(" OverCharge, ");
		sql.append(" ShareCharge, ");
		sql.append(" PFCharge, ");
		sql.append(" TotalCharge, ");
		sql.append(" MaxDemandPK, ");
		sql.append(" MaxDemandSP, ");
		sql.append(" MaxDemandSatSP, ");
		sql.append(" MaxDemandOP, ");
		sql.append(" CECPK, ");
		sql.append(" CECSP, ");
		sql.append(" CECSatSP, ");
		sql.append(" CECOP, ");
		sql.append(" PF, ");
		sql.append(" StartBillMon, ");
		sql.append(" EndBillMon, ");
		sql.append(" ShowCharge, ");
		sql.append(" ShowCEC ");
		sql.append(" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		parameterList.add(entryBillVO.getPowerAccount());
		parameterList.add(entryBillVO.getBillMon());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getBaseCharge());
		parameterList.add(entryBillVO.getUsageCharge());
		parameterList.add(entryBillVO.getOverCharge());
		parameterList.add(entryBillVO.getShareCharge());
		parameterList.add(entryBillVO.getPfCharge());
		parameterList.add(entryBillVO.getTotalCharge());
		parameterList.add(entryBillVO.getMaxDemandPK());
		parameterList.add(entryBillVO.getMaxDemandSP());
		parameterList.add(entryBillVO.getMaxDemandSatSP());
		parameterList.add(entryBillVO.getMaxDemandOP());
		parameterList.add(entryBillVO.getCecPK());
		parameterList.add(entryBillVO.getCecSP());
		parameterList.add(entryBillVO.getCecSatSP());
		parameterList.add(entryBillVO.getCecOP());
		parameterList.add(entryBillVO.getPf());	
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getShowCharge());
		parameterList.add(entryBillVO.getShowCEC());
		
		this.executeUpdate(sql.toString(), parameterList);
	}

	/**
	 * 修改電費單資訊
	 * 
	 * @param powerAccountVO
	 * @throws Exception
	 */
	public void updEntryBill(EntryBillVO entryBillVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" update EntryBill");
		sql.append(" set ");
		sql.append(" BillMon = ?, ");
		sql.append(" BillStartDay = ?, ");
		sql.append(" BillEndDay = ?, ");
		sql.append(" BaseCharge = ?, ");
		sql.append(" UsageCharge = ?, ");
		sql.append(" OverCharge = ?, ");
		sql.append(" ShareCharge = ?, ");
		sql.append(" PFCharge = ?, ");
		sql.append(" TotalCharge = ?, ");
		sql.append(" MaxDemandPK = ?, ");
		sql.append(" MaxDemandSP = ?, ");
		sql.append(" MaxDemandSatSP = ?, ");
		sql.append(" MaxDemandOP = ?, ");
		sql.append(" CECPK = ?, ");
		sql.append(" CECSP = ?, ");
		sql.append(" CECSatSP = ?, ");
		sql.append(" CECOP = ?, ");
		sql.append(" PF = ?, ");
		sql.append(" StartBillMon = ?, ");
		sql.append(" EndBillMon = ?, ");
		sql.append(" ShowCharge = ?, ");
		sql.append(" ShowCEC = ? ");
		sql.append(" where 1=1 ");
		sql.append(" and PowerAccount = ? "); 
		sql.append(" and BillMon = ? ");
		
		parameterList.add(entryBillVO.getBillMon());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getBaseCharge());
		parameterList.add(entryBillVO.getUsageCharge());
		parameterList.add(entryBillVO.getOverCharge());
		parameterList.add(entryBillVO.getShareCharge());
		parameterList.add(entryBillVO.getPfCharge());
		parameterList.add(entryBillVO.getTotalCharge());
		parameterList.add(entryBillVO.getMaxDemandPK());
		parameterList.add(entryBillVO.getMaxDemandSP());
		parameterList.add(entryBillVO.getMaxDemandSatSP());
		parameterList.add(entryBillVO.getMaxDemandOP());
		parameterList.add(entryBillVO.getCecPK());
		parameterList.add(entryBillVO.getCecSP());
		parameterList.add(entryBillVO.getCecSatSP());
		parameterList.add(entryBillVO.getCecOP());
		parameterList.add(entryBillVO.getPf());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getShowCharge());
		parameterList.add(entryBillVO.getShowCEC());
		parameterList.add(entryBillVO.getPowerAccount());
		parameterList.add(entryBillVO.getOldBillMon());
	
		this.executeUpdate(sql.toString(), parameterList);
	}
	
	/**
	 * 取得帳單月數
	 * 
	 * @param entryBillVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getBillMonths(String powerAccount) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select r.BillMonths ");
		sql.append(" from PowerAccountHistory pa,");
		sql.append(" RatePlanList r ");
		sql.append(" where pa.RatePlanCode = r.RatePlanCode ");		
		sql.append(" and PowerAccount = ? ");
		sql.append(" and pa.ApplyDate = (select max(a.ApplyDate) from PowerAccountHistory a where a.PowerAccount = pa.PowerAccount and a.ApplyDate < now()) ");
		parameterList.add(powerAccount);

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 檢核計費日期起迄期間是否有重疊
	 * 
	 * @param entryBillVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> checkBillPeriod(EntryBillVO entryBillVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select * from EntryBill where PowerAccount = ? ");
		sql.append(" and ((BillStartDay >= ? and BillStartDay < ?) ");
		sql.append(" or (BillEndDay > ? and BillEndDay < ?) ");
		sql.append(" or (BillStartDay <= ? and BillEndDay > ?)) ");	
		parameterList.add(entryBillVO.getPowerAccount());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		parameterList.add(entryBillVO.getBillStartDay());
		parameterList.add(entryBillVO.getBillEndDay());
		if(StringUtils.isNotBlank(entryBillVO.getOldBillMon())) {
			sql.append(" and BillMon <> ? ");
			parameterList.add(entryBillVO.getOldBillMon());
		}

		return this.executeQuery(sql.toString(), parameterList);
	}
	
	/**
	 * 批量新增電費單資訊
	 * @param entryBillVO
	 * @throws Exception
	 */
	public void addEntryBillBatch(EntryBillVO entryBillVO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);

			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO EntryBill ( ");
			sql.append(" PowerAccount, ");
			sql.append(" BillMon, ");
			sql.append(" BillStartDay, ");
			sql.append(" BillEndDay, ");
			sql.append(" BaseCharge, ");
			sql.append(" UsageCharge, ");
			sql.append(" OverCharge, ");
			sql.append(" ShareCharge, ");
			sql.append(" PFCharge, ");
			sql.append(" TotalCharge, ");
			sql.append(" MaxDemandPK, ");
			sql.append(" MaxDemandSP, ");
			sql.append(" MaxDemandSatSP, ");
			sql.append(" MaxDemandOP, ");
			sql.append(" CECPK, ");
			sql.append(" CECSP, ");
			sql.append(" CECSatSP, ");
			sql.append(" CECOP, ");
			sql.append(" PF, ");
			sql.append(" StartBillMon, ");
			sql.append(" EndBillMon, ");
			sql.append(" ShowCharge, ");
			sql.append(" ShowCEC ");
			sql.append(" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps = connection.prepareStatement(sql.toString());
			for(EntryBillVO dataVO : entryBillVO.getDataList()) {
				ps.setString(1, dataVO.getPowerAccount());
				ps.setString(2, dataVO.getBillMon());
				ps.setString(3, dataVO.getBillStartDay());
				ps.setString(4, dataVO.getBillEndDay());
				ps.setString(5, dataVO.getBaseCharge());
				ps.setString(6, dataVO.getUsageCharge());
				ps.setString(7, dataVO.getOverCharge());
				ps.setString(8, dataVO.getShareCharge());
				ps.setString(9, dataVO.getPfCharge());
				ps.setString(10, dataVO.getTotalCharge());
				ps.setString(11, dataVO.getMaxDemandPK());
				ps.setString(12, dataVO.getMaxDemandSP());
				ps.setString(13, dataVO.getMaxDemandSatSP());
				ps.setString(14, dataVO.getMaxDemandOP());
				ps.setString(15, dataVO.getCecPK());
				ps.setString(16, dataVO.getCecSP());
				ps.setString(17, dataVO.getCecSatSP());
				ps.setString(18, dataVO.getCecOP());
				ps.setString(19, dataVO.getPf());	
				ps.setString(20, dataVO.getBillStartDay());
				ps.setString(21, dataVO.getBillEndDay());
				ps.setString(22, dataVO.getShowCharge());
				ps.setString(23, dataVO.getShowCEC());

				ps.addBatch();		
			}
			ps.executeBatch();					
			
			connection.commit();				
		}catch(SQLException e) {
			connection.rollback();
			throw new SQLException(e.toString());		
		}finally {
			connection.setAutoCommit(true);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
	}
}
