package aptg.battery.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.CommandTaskDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CommandTaskVO;



/**
 * Servlet implementation class getCommandTask 下行命令POC
 */
@WebServlet("/getCommandTask")
public class getCommandTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCommandTask.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCommandTask() {
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
		logger.debug("getCommandTask start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					ResourceBundle resource = ToolUtil.getLanguage("");
//					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
					
					CommandTaskDAO commandTaskDAO = new CommandTaskDAO();
					List<DynaBean> list = commandTaskDAO.getCommandTaskListPOC(new CommandTaskVO());				
					if (list != null && !list.isEmpty()) {						
						
						rspJson.put("msg", convertToJson(list));
						
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));//查無資料
					}
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);		
		ToolUtil.response(rspJson.toString(), response);
		logger.debug("getCommandTask end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject data = new JSONObject();
		try {	
			JSONArray taskArr = new JSONArray();
			for(int i=0; i<rows.size(); i++) {							
				DynaBean bean = rows.get(i);				
				JSONObject task = new JSONObject();
				task.put("Seq", i+1);
				task.put("TaskID", bean.get("taskid"));//任務ID
				task.put("NBID", ObjectUtils.toString(bean.get("nbid")));//通訊板序號
				task.put("BatteryID", ObjectUtils.toString(bean.get("batteryid")));//電池序號
				task.put("CommandID", bean.get("commandid"));//命令代碼
				task.put("CreateTime", ToolUtil.dateFormat(bean.get("createtime"), sdf));//新增任務時間
				task.put("TransactionID", ObjectUtils.toString(bean.get("transactionid")));//交易序號
				task.put("ReqTime", ToolUtil.dateFormat(bean.get("publishtime"), sdf));//發布時間
				task.put("AckTime", ToolUtil.dateFormat(bean.get("acktime"), sdf));//ACK時間
				task.put("ResponseTime", ToolUtil.dateFormat(bean.get("responsetime"), sdf));//Resp時間
				task.put("ResponseContent", ObjectUtils.toString(bean.get("responsecontent")));//回應訊息
				task.put("Config", ObjectUtils.toString(bean.get("config")));//Json設定值
				task.put("HexConfig", ObjectUtils.toString(bean.get("hexconfig")));//Hex設定值
				
				taskArr.put(task);
			}			
			data.put("Task", taskArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
