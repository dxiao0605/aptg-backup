package aptg.cathaybkeco.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aptg.cathaybkeco.vo.LogRecordVO;

public class LogRecordDAO extends BaseDAO {

	/**
	 * 新增操作紀錄
	 * @param logRecordVO
	 * @throws Exception
	 */
	public void addLogRecord(LogRecordVO logRecordVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		List<String> parameterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO LogRecord ( ");
		sql.append(" RecTime, ");
		sql.append(" UserName, ");
		sql.append(" ActionCode, ");
		sql.append(" ActionContent ");	
		sql.append(" )VALUES(?,?,?,?) ");
		
		parameterList.add(sdf.format(new Date()));
		parameterList.add(logRecordVO.getUserName());
		parameterList.add(logRecordVO.getActionCode());
		parameterList.add(logRecordVO.getActionContent());
		
		this.executeUpdate(sql.toString(), parameterList);
	}

}
