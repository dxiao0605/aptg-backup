package aptg.dao;

import aptg.dao.base.BaseDao2;

public class DeletedPowerAccountDao extends BaseDao2 {

//	public List<Integer> insertDeletedPowerAccount(String powerAccount) throws SQLException {
//		List<String> sqlList = new ArrayList<>();
//
//		String sql = "INSERT INTO DeletedPowerAccount " +
//				   	 "( " +
//				   	 	"PowerAccount, BankCode, CustomerName, AccountDesc, PATypeCode, " +
//				   	 	"PowerPhase, PAAddress, CreateTime, CreateUser, UpdateTime, UpdateUser " +
//				   	 ") " +
//				   	 "select " +
//				   	 	"PowerAccount, BankCode, CustomerName, AccountDesc, PATypeCode, " +
//				   	 	"PowerPhase, PAAddress, CreateTime, CreateUser, UpdateTime, UpdateUser " +
//				   	 "from PowerAccount where PowerAccount= ?";
//
//		Connection connection = initConnection();
//		PreparedStatement ps = initPreparedStatement(connection, sql);
//
//		ps.setString(1, powerAccount);
//		ps.addBatch();
//		sqlList.add(ps.toString());
//
//		return batchInsert(connection, ps, sqlList);
//	}
}
