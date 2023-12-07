package aptg.cathaybkeco.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;

import aptg.cathaybkeco.vo.ElectricityPriceVO;

public class ElectricityPriceDAO extends BaseDAO {

	/**
	 * 取得歷年電價表
	 * 
	 * @param electricityPriceVO
	 * @return List<DynaBean>
	 * @throws Exception
	 */
	public List<DynaBean> getElectricityPrice(ElectricityPriceVO electricityPriceVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select ");
		sql.append(" (select RatePlanDesc from RatePlanList l ");
		sql.append(" where l.RatePlanCode = e.RatePlanCode ) as RatePlanDesc, ");
		sql.append(" e.*  ");
		sql.append(" from ElectricityPrice e ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(electricityPriceVO.getRatePlanCode())) {
			sql.append(" and e.RatePlanCode = ? ");
			parameterList.add(electricityPriceVO.getRatePlanCode());
		}
		if (StringUtils.isNotBlank(electricityPriceVO.getYear())) {
			sql.append(" and e.Year = ? ");
			parameterList.add(electricityPriceVO.getYear());
		}
		if (StringUtils.isNotBlank(electricityPriceVO.getMonth())) {
			sql.append(" and e.Month = ? ");
			parameterList.add(electricityPriceVO.getMonth());
		}
	
		sql.append(" order by CAST(Year as UNSIGNED) desc, CAST(Month as UNSIGNED) ");

		return this.executeQuery(sql.toString(), parameterList);
	}

	/**
	 * 新增歷年電價表
	 * 
	 * @param electricityPriceVO
	 * @throws Exception
	 */
	public void addElectricityPrice(ElectricityPriceVO electricityPriceVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ElectricityPrice ( ");
		if(StringUtils.isNotBlank(electricityPriceVO.getYear())) {
			sql.append(" Year, ");
			parameterList.add(electricityPriceVO.getYear());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getMonth())) {
			sql.append(" Month, ");
			parameterList.add(electricityPriceVO.getMonth());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getRatePlanCode())) {
			sql.append(" RatePlanCode, ");
			parameterList.add(electricityPriceVO.getRatePlanCode());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice1())) {
			sql.append(" LampPrice1, ");
			parameterList.add(electricityPriceVO.getLampPrice1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice2())) {
			sql.append(" LampPrice2, ");
			parameterList.add(electricityPriceVO.getLampPrice2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice3())) {
			sql.append(" LampPrice3, ");
			parameterList.add(electricityPriceVO.getLampPrice3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice4())) {
			sql.append(" LampPrice4, ");
			parameterList.add(electricityPriceVO.getLampPrice4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice5())) {
			sql.append(" LampPrice5, ");
			parameterList.add(electricityPriceVO.getLampPrice5());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice6())) {
			sql.append(" LampPrice6, ");
			parameterList.add(electricityPriceVO.getLampPrice6());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice1S())) {
			sql.append(" LampPrice1S, ");
			parameterList.add(electricityPriceVO.getLampPrice1S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice2S())) {
			sql.append(" LampPrice2S, ");
			parameterList.add(electricityPriceVO.getLampPrice2S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice3S())) {
			sql.append(" LampPrice3S, ");
			parameterList.add(electricityPriceVO.getLampPrice3S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice4S())) {
			sql.append(" LampPrice4S, ");
			parameterList.add(electricityPriceVO.getLampPrice4S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice5S())) {
			sql.append(" LampPrice5S, ");
			parameterList.add(electricityPriceVO.getLampPrice5S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice6S())) {
			sql.append(" LampPrice6S, ");
			parameterList.add(electricityPriceVO.getLampPrice6S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep1())) {
			sql.append(" LampStep1, ");
			parameterList.add(electricityPriceVO.getLampStep1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep2())) {
			sql.append(" LampStep2, ");
			parameterList.add(electricityPriceVO.getLampStep2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep3())) {
			sql.append(" LampStep3, ");
			parameterList.add(electricityPriceVO.getLampStep3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep4())) {
			sql.append(" LampStep4, ");
			parameterList.add(electricityPriceVO.getLampStep4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep5())) {
			sql.append(" LampStep5, ");
			parameterList.add(electricityPriceVO.getLampStep5());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice1())) {
			sql.append(" LampBPrice1, ");
			parameterList.add(electricityPriceVO.getLampBPrice1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice2())) {
			sql.append(" LampBPrice2, ");
			parameterList.add(electricityPriceVO.getLampBPrice2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice3())) {
			sql.append(" LampBPrice3, ");
			parameterList.add(electricityPriceVO.getLampBPrice3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice4())) {
			sql.append(" LampBPrice4, ");
			parameterList.add(electricityPriceVO.getLampBPrice4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice1S())) {
			sql.append(" LampBPrice1S, ");
			parameterList.add(electricityPriceVO.getLampBPrice1S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice2S())) {
			sql.append(" LampBPrice2S, ");
			parameterList.add(electricityPriceVO.getLampBPrice2S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice3S())) {
			sql.append(" LampBPrice3S, ");
			parameterList.add(electricityPriceVO.getLampBPrice3S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice4S())) {
			sql.append(" LampBPrice4S, ");
			parameterList.add(electricityPriceVO.getLampBPrice4S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep1())) {
			sql.append(" LampBStep1, ");
			parameterList.add(electricityPriceVO.getLampBStep1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep2())) {
			sql.append(" LampBStep2, ");
			parameterList.add(electricityPriceVO.getLampBStep2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep3())) {
			sql.append(" LampBStep3, ");
			parameterList.add(electricityPriceVO.getLampBStep3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseCharge1phase())) {
			sql.append(" BaseCharge1phase, ");
			parameterList.add(electricityPriceVO.getBaseCharge1phase());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseCharge3phase())) {
			sql.append(" BaseCharge3phase, ");
			parameterList.add(electricityPriceVO.getBaseCharge3phase());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeUsually())) {
			sql.append(" BaseChargeUsually, ");
			parameterList.add(electricityPriceVO.getBaseChargeUsually());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSP())) {
			sql.append(" BaseChargeSP, ");
			parameterList.add(electricityPriceVO.getBaseChargeSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSatSP())) {
			sql.append(" BaseChargeSatSP, ");
			parameterList.add(electricityPriceVO.getBaseChargeSatSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeOP())) {
			sql.append(" BaseChargeOP, ");
			parameterList.add(electricityPriceVO.getBaseChargeOP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeUsuallyS())) {
			sql.append(" BaseChargeUsuallyS, ");
			parameterList.add(electricityPriceVO.getBaseChargeUsuallyS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSPS())) {
			sql.append(" BaseChargeSPS, ");
			parameterList.add(electricityPriceVO.getBaseChargeSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSatSPS())) {
			sql.append(" BaseChargeSatSPS, ");
			parameterList.add(electricityPriceVO.getBaseChargeSatSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeOPS())) {
			sql.append(" BaseChargeOPS, ");
			parameterList.add(electricityPriceVO.getBaseChargeOPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeCharge())) {
			sql.append(" TimeCharge, ");
			parameterList.add(electricityPriceVO.getTimeCharge());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSP())) {
			sql.append(" TimeChargeSP, ");
			parameterList.add(electricityPriceVO.getTimeChargeSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSatSP())) {
			sql.append(" TimeChargeSatSP, ");
			parameterList.add(electricityPriceVO.getTimeChargeSatSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeOP())) {
			sql.append(" TimeChargeOP, ");
			parameterList.add(electricityPriceVO.getTimeChargeOP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeS())) {
			sql.append(" TimeChargeS, ");
			parameterList.add(electricityPriceVO.getTimeChargeS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSPS())) {
			sql.append(" TimeChargeSPS, ");
			parameterList.add(electricityPriceVO.getTimeChargeSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSatSPS())) {
			sql.append(" TimeChargeSatSPS, ");
			parameterList.add(electricityPriceVO.getTimeChargeSatSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeOPS())) {
			sql.append(" TimeChargeOPS, ");
			parameterList.add(electricityPriceVO.getTimeChargeOPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getOver2KPrice())) {
			sql.append(" Over2KPrice, ");
			parameterList.add(electricityPriceVO.getOver2KPrice());
		}
		sql.append(" CreateUserName, ");
		sql.append(" UpdateUserName ");
		parameterList.add(electricityPriceVO.getUserName());
		parameterList.add(electricityPriceVO.getUserName());
		
		String q = "";
		for(int i=0; i<parameterList.size(); i++){
			if(i==0) {
				q="?";
			}else {
				q+=",?";
			}
		}
		sql.append(" )VALUES( "+q+" ) ");
		
		this.executeUpdate(sql.toString(), parameterList);
	}

	/**
	 * 修改歷年電價表
	 * @param electricityPriceVO
	 * @throws Exception
	 */
	public void updElectricityPrice(ElectricityPriceVO electricityPriceVO) throws Exception {
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ElectricityPrice ");
		sql.append(" SET ");
		if(StringUtils.isNotBlank(electricityPriceVO.getNewYear())) {
			sql.append(" Year = ?, ");			
			parameterList.add(electricityPriceVO.getNewYear());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getNewMonth())) {
			sql.append(" Month = ?, ");
			parameterList.add(electricityPriceVO.getNewMonth());
		}
	
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice1())) {
			sql.append(" LampPrice1 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice2())) {
			sql.append(" LampPrice2 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice3())) {
			sql.append(" LampPrice3 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice4())) {
			sql.append(" LampPrice4 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice5())) {
			sql.append(" LampPrice5 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice5());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice6())) {
			sql.append(" LampPrice6 = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice6());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice1S())) {
			sql.append(" LampPrice1S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice1S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice2S())) {
			sql.append(" LampPrice2S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice2S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice3S())) {
			sql.append(" LampPrice3S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice3S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice4S())) {
			sql.append(" LampPrice4S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice4S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice5S())) {
			sql.append(" LampPrice5S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice5S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampPrice6S())) {
			sql.append(" LampPrice6S = ?, ");
			parameterList.add(electricityPriceVO.getLampPrice6S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep1())) {
			sql.append(" LampStep1 = ?, ");
			parameterList.add(electricityPriceVO.getLampStep1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep2())) {
			sql.append(" LampStep2 = ?, ");
			parameterList.add(electricityPriceVO.getLampStep2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep3())) {
			sql.append(" LampStep3 = ?, ");
			parameterList.add(electricityPriceVO.getLampStep3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep4())) {
			sql.append(" LampStep4 = ?, ");
			parameterList.add(electricityPriceVO.getLampStep4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampStep5())) {
			sql.append(" LampStep5 = ?, ");
			parameterList.add(electricityPriceVO.getLampStep5());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice1())) {
			sql.append(" LampBPrice1 = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice2())) {
			sql.append(" LampBPrice2 = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice3())) {
			sql.append(" LampBPrice3 = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice4())) {
			sql.append(" LampBPrice4 = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice4());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice1S())) {
			sql.append(" LampBPrice1S = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice1S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice2S())) {
			sql.append(" LampBPrice2S = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice2S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice3S())) {
			sql.append(" LampBPrice3S = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice3S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBPrice4S())) {
			sql.append(" LampBPrice4S = ?, ");
			parameterList.add(electricityPriceVO.getLampBPrice4S());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep1())) {
			sql.append(" LampBStep1 = ?, ");
			parameterList.add(electricityPriceVO.getLampBStep1());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep2())) {
			sql.append(" LampBStep2 = ?, ");
			parameterList.add(electricityPriceVO.getLampBStep2());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getLampBStep3())) {
			sql.append(" LampBStep3 = ?, ");
			parameterList.add(electricityPriceVO.getLampBStep3());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseCharge1phase())) {
			sql.append(" BaseCharge1phase = ?, ");
			parameterList.add(electricityPriceVO.getBaseCharge1phase());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseCharge3phase())) {
			sql.append(" BaseCharge3phase = ?, ");
			parameterList.add(electricityPriceVO.getBaseCharge3phase());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeUsually())) {
			sql.append(" BaseChargeUsually = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeUsually());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSP())) {
			sql.append(" BaseChargeSP = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSatSP())) {
			sql.append(" BaseChargeSatSP = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeSatSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeOP())) {
			sql.append(" BaseChargeOP = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeOP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeUsuallyS())) {
			sql.append(" BaseChargeUsuallyS = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeUsuallyS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSPS())) {
			sql.append(" BaseChargeSPS = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeSatSPS())) {
			sql.append(" BaseChargeSatSPS = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeSatSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getBaseChargeOPS())) {
			sql.append(" BaseChargeOPS = ?, ");
			parameterList.add(electricityPriceVO.getBaseChargeOPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeCharge())) {
			sql.append(" TimeCharge = ?, ");
			parameterList.add(electricityPriceVO.getTimeCharge());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSP())) {
			sql.append(" TimeChargeSP = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSatSP())) {
			sql.append(" TimeChargeSatSP = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeSatSP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeOP())) {
			sql.append(" TimeChargeOP = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeOP());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeS())) {
			sql.append(" TimeChargeS = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSPS())) {
			sql.append(" TimeChargeSPS = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeSatSPS())) {
			sql.append(" TimeChargeSatSPS = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeSatSPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getTimeChargeOPS())) {
			sql.append(" TimeChargeOPS = ?, ");
			parameterList.add(electricityPriceVO.getTimeChargeOPS());
		}
		if(StringUtils.isNotBlank(electricityPriceVO.getOver2KPrice())) {
			sql.append(" Over2KPrice = ?, ");
			parameterList.add(electricityPriceVO.getOver2KPrice());
		}
		sql.append(" UpdateUserName = ? ");		
		sql.append(" WHERE RatePlanCode = ? ");
		sql.append(" and Year = ? ");
		sql.append(" and Month = ? ");

		parameterList.add(electricityPriceVO.getUserName());
		parameterList.add(electricityPriceVO.getRatePlanCode());
		parameterList.add(electricityPriceVO.getYear());
		parameterList.add(electricityPriceVO.getMonth());
		
		this.executeUpdate(sql.toString(), parameterList);
	}
}
