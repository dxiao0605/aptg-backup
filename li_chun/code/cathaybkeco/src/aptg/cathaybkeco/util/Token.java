package aptg.cathaybkeco.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import aptg.cathaybkeco.dao.AuthorizationDAO;
import aptg.cathaybkeco.vo.AuthorizationVO;


public class Token{
	
	/**
	 * 取得Token
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String token = new String();
		try {
			 token = EncryptUtil.encryptSHA256(str + sdf.format(new Date()));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return token;
	}
	
	/**
	 * 檢核Token
	 * @param systemId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static boolean checkToken(String systemId, String token) throws Exception {
		boolean check = false;
		try {
			AuthorizationVO authorizationVO = new AuthorizationVO();
			authorizationVO.setToken(token);
			authorizationVO.setSystemId(systemId);
			AuthorizationDAO authorizationDAO = new AuthorizationDAO();
			check = authorizationDAO.checkToken(authorizationVO);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return check;
	}

}
