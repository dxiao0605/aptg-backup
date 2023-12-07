package aptg.monitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.manager.SysConfigManager;
import aptg.utils.EmailUtil;
import aptg.utils.ToolUtil;

public class S25FailedMonitor {

	private static final String CLASS_NAME = S25FailedMonitor.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final String S25_DIRPATH 		= ".s25.filepath";
	private String LOG_DIRPATH;			// path: /opt/poc_cubeco/logs/EcoSocket/S25
	private String EXECUTING_DIRPATH;	// path: /opt/poc_cubeco/logs/EcoSocket/S25/executing

	private static final String Mail_To_List_1					= "austinchen@aptg.com.tw";
	private static final String Sysconfig_ExecutingLogMin		= "ExecutingLogMin";
	private int ExecutingLogMin;
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		S25FailedMonitor task = new S25FailedMonitor();
		task.process();
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void process() {
		// 取得S25 Log的存放資料夾設定
		getPathSetting();
		
		List<File> files = failedCheck();
		
		if (files.size()!=0)
			sendMail(files);
	}
	
	/**
	 * 初始Log & Executing DIR位置
	 */
	private void getPathSetting() {
		try {
			ResourceBundle dbConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get("db.properties")));
			String mode = dbConfig.getString("db.mode");

			ResourceBundle config = new PropertyResourceBundle(Files.newInputStream(Paths.get("config.properties")));
			this.LOG_DIRPATH = config.getString(mode + S25_DIRPATH);
			this.EXECUTING_DIRPATH = LOG_DIRPATH +"/executing";
			
			this.ExecutingLogMin = Integer.valueOf(SysConfigManager.getInstance().getSysconfig(Sysconfig_ExecutingLogMin));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 檢查時間過久的檔案
	 * 
	 * @return
	 */
	private List<File> failedCheck() {
		List<File> executingFiles = new ArrayList<>();

		long keepstamp = ExecutingLogMin * 60;	// min
		Date now = new Date();
		long nowstamp = now.getTime() / 1000;
		
		File dir = new File(EXECUTING_DIRPATH);	// S25/executing
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(File file: files) {
				if (file.isFile()) {
					// 判斷檔案修改時間
					long timestamp = file.lastModified() / 1000;
					if ( (nowstamp-timestamp)>=keepstamp) {
						executingFiles.add(file);
					}
				}
			}
		}
		return executingFiles;
	}
	
	/**
	 * 發送mail通知
	 */
	private void sendMail(List<File> files) {
		String subject = "ECO S25 Executing dir files 執行過久, 異常通知 !!! ";
		String body = "\n";
		
		for (File file: files) {
			body += file.getAbsolutePath() +" \t "+ ToolUtil.getInstance().convertDateToString(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss") +" \n";
		}
		// ========== 目前hardcode我自己 ==========
		List<String> toList = new ArrayList<>();
		toList.add(Mail_To_List_1);
		// =========================================
		
		EmailUtil email = new EmailUtil();
		email.setSubject(subject);
		email.setBody(body);
		email.setToList(toList);
		try {
			logger.info("########### Send Mail Start ############");
			logger.info("subject: "+subject);
			logger.info("body: "+body);
			logger.info("########### Send Mail End ############");
			email.sendMail();
		} catch (Exception e) {
//			logger.error("Send Email Failed..: "+body);
			e.printStackTrace();
		}
	}
}
