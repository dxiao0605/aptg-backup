package aptg.cathaybkeco.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import aptg.cathaybkeco.util.JdbcUtil;
import aptg.cathaybkeco.util.ToolUtil;



/**
 * Servlet implementation class querySQL
 */
@WebServlet("/aaa")
public class querySQL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public querySQL() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));	
			StringBuilder sb = new StringBuilder();
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
				while ((line = br.readLine()) != null) {
					sb.append(line+" ");
			}

			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					response.setContentType("text/html;charset=utf-8");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(executeQuery(sb.toString()));
				}
			} else {
				throw new Exception("缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(rspJson.toString());
		}
		
	}

	protected String executeQuery(String sql) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer rsp = new StringBuffer();
		try {			
			connection = JdbcUtil.getConnection();
			ps = connection.prepareStatement(sql.toString());			
			rs = ps.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			int columnCount = data.getColumnCount();
			int count = 0;
			for (int i = 1; i <= columnCount; i++) {
				rsp.append("|").append("\t").append(data.getColumnName(i)).append("\t");
				if(i == columnCount) {
					rsp.append("|").append("\n");
				}
			}	
			rsp.append("--------------------------------------------------").append("\n");
			while(rs.next()){
				count++;
				for (int i = 1; i <= columnCount; i++) {
					rsp.append("|").append("\t").append(rs.getString(i)).append("\t");
					if(i == columnCount) {
						rsp.append("|").append("\n");
					}
				}
				rsp.append("--------------------------------------------------").append("\n");
			}
			rsp.append("Count:").append(count);
		} catch (SQLException e) {
			throw new SQLException(e.toString());
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(ps);
			JdbcUtil.close(connection);
		}
		return rsp.toString();
	}
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
