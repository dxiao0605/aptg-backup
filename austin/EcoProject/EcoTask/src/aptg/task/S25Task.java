package aptg.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.ProcInfoBean;
import aptg.dao.PowerRecordDao2;
import aptg.function.FuncID_S;
import aptg.manager.SysConfigManager;
import aptg.models.PowerRecordModel;
import aptg.utils.EmailUtil;
import aptg.utils.ToolUtil;

public class S25Task {

	private static final String CLASS_NAME = S25Task.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private static final String SPLIT_SYMBOL 		= "@@@";
	private static final String S25_DIRPATH 		= ".s25.filepath";
	private String LOG_DIRPATH;			// path: /opt/poc_cubeco/logs/EcoSocket/S25
	private String EXECUTING_DIRPATH;	// path: /opt/poc_cubeco/logs/EcoSocket/S25/executing
	private String FINISHED_DIRPATH;	// path: /opt/poc_cubeco/logs/EcoSocket/S25/finished

	private static final BigDecimal UP_PF						= new BigDecimal(1);
	private static final BigDecimal DOWN_PF						= new BigDecimal(-1);
	
	private static final int SECOND_OF_A_DAY 					= 86400;	// sec (24*60*60)
	private static final String Sysconfig_S25LogDeleteFlag 		= "S25LogDeleteFlag";
	private static final String Sysconfig_S25LogKeepDays		= "S25LogKeepDays";
	private int S25LogDeleteFlag;
	private int S25LogKeepDays;

	private Map<String, PowerRecordModel> pfMap = new HashMap<>();
	
	private static String ReExecuteFile;
	
	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		if (args.length!=0) {
			// 從executing file指定單一file重做
			ReExecuteFile = args[0];
			logger.info(CLASS_NAME+" 指定檔案 S25/executing/" + ReExecuteFile);
		}

		S25Task task = new S25Task();
		task.process();

		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void process() {
		// 取得S25 Log的存放資料夾設定
		getPathSetting();
		
		// 取得S25資料夾底下所有檔案
		List<File> s25Files = (StringUtils.isBlank(ReExecuteFile)) ? getS25Files() :		// 每3分鐘排程 S25底下所有log
																	 getReExecutingFile();	// 指定executing dir單一file
		
		// mv S25 files to executing DIR
		List<File> executingFiles = mvFiles(s25Files, EXECUTING_DIRPATH);

		Map<String, PowerRecordModel> records = new HashMap<>();
		for (File file: executingFiles) {
			List<File> files = new ArrayList<>();
			files.add(file);
			// 讀取欲寫入的 S25 log
			List<String> logs = getS25Log(files);

			// 解析S25 log
			Map<String, PowerRecordModel> temp = analysisS25(logs);
			records.putAll(temp);
			
			// pf值異常，發送告警信
			if (pfMap.size()!=0) {
				sendMail(file, pfMap);
				pfMap.clear();
			}
		}
		
//		// 讀取欲寫入的 S25 log
//		List<String> logs = getS25Log(executingFiles);
//		
//		// 解析S25 log
//		Map<String, PowerRecordModel> records = analysisS25(logs);
		
		// 寫入DB & 排除Exception的Duplicate entry
		boolean isSuccess = insertS25(records);

		// mv executing files to finished DIR
		if (isSuccess) {
			// 先搬檔至finished
			List<File> finishedFiles = mvFiles(executingFiles, FINISHED_DIRPATH);
			if (S25LogDeleteFlag==1) {
				// 再刪檔 => 針對資料夾finished底下所有檔案 (非搬移的而已)
				deleteS25(null);
			}
		}
		
//		// pf值異常，發送告警信
//		if (pfMap.size()!=0)
//			sendMail(pfMap);
	}
	
	/**
	 * 初始Log & Executing DIR位置
	 */
	public void getPathSetting() {
		try {
			ResourceBundle dbConfig = new PropertyResourceBundle(Files.newInputStream(Paths.get("db.properties")));
			String mode = dbConfig.getString("db.mode");
			
			ResourceBundle config = new PropertyResourceBundle(Files.newInputStream(Paths.get("config.properties")));
			this.LOG_DIRPATH = config.getString(mode + S25_DIRPATH);
			this.EXECUTING_DIRPATH = LOG_DIRPATH +"/executing";
			this.FINISHED_DIRPATH = LOG_DIRPATH +"/finished";
			
			this.S25LogDeleteFlag = Integer.valueOf(SysConfigManager.getInstance().getSysconfig(Sysconfig_S25LogDeleteFlag));
			this.S25LogKeepDays = Integer.valueOf(SysConfigManager.getInstance().getSysconfig(Sysconfig_S25LogKeepDays));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得S25資料夾底下所有檔案
	 * 
	 * @return
	 */
	public List<File> getS25Files() {
		List<File> s25Files = new ArrayList<>();
		
		File dir = new File(LOG_DIRPATH);	// S25
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(File file: files) {
				if (file.isFile()) {
					s25Files.add(file);
				}
			}
		}
		return s25Files;
	}
	/**
	 * 取得S25/executing資料夾底下指定的檔案
	 * 
	 * @return
	 */
	public List<File> getReExecutingFile() {
		List<File> s25Files = new ArrayList<>();
		String filePath = EXECUTING_DIRPATH +"/"+ ReExecuteFile;
		File file = new File(filePath);
		if (file.isFile()) {
			s25Files.add(file);
		}
		return s25Files;
	}
	
	/**
	 * S25 DIR => executing DIR
	 * executing DIR => finished DIR
	 * 
	 * @param srcFiles
	 * @param destDir
	 */
	public List<File> mvFiles(List<File> srcFiles, String destDir) {
		List<File> destFiles = new ArrayList<>();
		
		for (File file: srcFiles) {
			if (file.isFile()) {
				String filename = file.getName();
				String newAbsolutePath = (filename.contains("_")) ? 
											destDir +"/"+ filename : 
											destDir +"/"+ filename +"_"+ToolUtil.getInstance().getUUIDTailString();

				try {
					Path temp = Files.move(Paths.get(file.getAbsolutePath()), Paths.get(newAbsolutePath), StandardCopyOption.REPLACE_EXISTING);
					if(temp!=null) {
						destFiles.add(new File(newAbsolutePath));
						logger.info("Move file: ["+file.getAbsolutePath()+"] to ["+newAbsolutePath+"]");
					} else { 
						logger.error("Move file: ["+file.getAbsolutePath()+"] to ["+newAbsolutePath+"] failed");
					}
				} catch(Exception e) {
					e.printStackTrace();
					logger.error("Move S25 file: ["+file.getName()+"] to '"+destDir+"' Dir failed: "+e.getMessage());
				}
			}
		}
		return destFiles;
	}
	
	/**
	 * find S25 log file: 讀取愈寫入的 S25 log
	 * 
	 * @return
	 */
	public List<String> getS25Log(List<File> executingFiles) {
		List<String> logs = new ArrayList<>();

		for(File file: executingFiles) {
			if (file.isFile()) {
				logger.info("================== Parsing S25 Log file: ["+ file.getName()+"]");

				// 對檔案做解析
				FileReader fr = null;
				BufferedReader br = null;
				try {
					String absolutePath = file.getAbsolutePath();
					fr = new FileReader(absolutePath);
					br = new BufferedReader(fr);

					while (br.ready()) {
						String content = br.readLine();
						String connectTime = content.substring(content.indexOf("[INFO ] ")+8, content.indexOf("FuncID_S25")-1);
						String payload = content.substring(content.indexOf("[S25] ")+6);
						
						String log = connectTime +SPLIT_SYMBOL+ payload;
						logs.add(log);
//						logger.info("============== log size: "+logs.size());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Parsing S25 Log file: ["+file.getName()+"] failed: "+e.getMessage());
				} finally {
					try {
						br.close();
						fr.close();
					} catch(Exception e) {
						e.printStackTrace();
					}
				} 
			}
		}
		return logs;
	}
	
	/**
	 * analysis S25:  解析S25 log
	 * 
	 * @param logs
	 * @return
	 */
	public Map<String, PowerRecordModel> analysisS25(List<String> logs) {
		Map<String, PowerRecordModel> records = new HashMap<>();
		
		FuncID_S s25 = new FuncID_S();
		
		logger.info("================== Analysis S25 Log, log size:"+ logs.size());
		for (String log: logs) {
			try {
				String[] content = log.split(SPLIT_SYMBOL);
				String connectTime = content[0];	// 2021/10/05 08:37:47.425
				String payload = content[1];		// *1;S25;xxxx
				
				String[] message = payload.split(";");
				PowerRecordModel power = s25.updateS25(message);
				power.setConnectTime(connectTime.substring(0, connectTime.indexOf(".")));
				
				if (isValidPF(power)) {
					String key = power.getDeviceID() +"-"+ power.getRecTime();
					records.put(key, power);
				} else {
					log = log.replace(SPLIT_SYMBOL, " - ");
					pfMap.put(log, power);
				}
				
//				logger.info("=== power: "+JsonUtil.getInstance().convertObjectToJsonstring(power));
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Analysis S25 Log failed: "+e.getMessage() +"\n ("+log+")");
			}
		}
		return records;
	}
	private boolean isValidPF(PowerRecordModel power) {
		// -1<=PF<=1
		if (power.getPF().compareTo(DOWN_PF)>=0 && power.getPF().compareTo(UP_PF)<=0)
			return true;
		else
			return false;
	}
	
	/**
	 * insert S25 PowerRecord to DB: 寫入DB
	 * 
	 * @param records
	 * @return
	 */
	public boolean insertS25(Map<String, PowerRecordModel> records) {
		boolean isSuccess = false;
		String result = null;

		logger.info("================== Insert S25 Log, record size:"+ records.size());
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			if (records.size()!=0)
				result = dao.insertPowerRecordWithPartition(records);
			
			if (StringUtils.isBlank(result)) {
				// result=null => 無Exception message
				isSuccess = true;
			} else {
				// batch failed => 排除失敗的，寫入DB => 若成功排除, isSuccess=true
				isSuccess = handleException(records, result);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Insert S25 record failed: "+e.getMessage());
		}
		return isSuccess;
	}
	private boolean handleException(Map<String, PowerRecordModel> records, String result) {
		boolean isSuccess = false;

		// ex: result=Duplicate entry 'IN11CATHAYBK--006209-2021-08-16 11:29:00' for key 'DeviceID'
		if (result.contains("Duplicate entry")) {
			String key = result.substring("Duplicate entry ".length()+1, result.indexOf(" for key")-1);	// IN11CATHAYBK--006209-2021-08-16 11:29:00
			
			if (records.containsKey(key)) {
				PowerRecordModel power = records.get(key);
				logger.info("Skip Duplicate entry, DeviceID=["+power.getDeviceID()+"], RecTime=["+power.getRecTime()+"]");
				records.remove(key);
			}
			
			// retry insert
			if (records.size()!=0)
				isSuccess = insertS25(records);
		} else {
			logger.error("Insert PowerRecord & PowerRecordCreateTime Failed: "+ result);
			
//			// 如發生"Deadlock found when trying to get lock; try restarting transaction"錯誤，單一檔案處理
//			if (result.contains("Deadlock found when trying to get lock; try restarting transaction")) {
//				handleDeadlockExcpetion();
//			}
		}
		return isSuccess;
	}
	
	/**
	 * delete S25 log
	 */
	public void deleteS25(List<File> files) {
		logger.info("================== SysConfig delete S25 setting, S25LogDeleteFlag=["+S25LogDeleteFlag+"], S25LogKeepDays=["+S25LogKeepDays+"]天");
		long keepstamp = S25LogKeepDays * SECOND_OF_A_DAY;
		
		Date now = new Date();
		long nowstamp = now.getTime() / 1000;
		
		/*
		 * 	如有傳指定資料夾內檔案列表至deleteS25 func
		 * => true: 使用傳來的files list
		 * => false: 抓取finished資料夾底下的檔案列表
		 */
		files = (files!=null) ? files : getFinishedListFiles();
		
		for (File file: files) {
			if (!file.isFile())
				continue;
			
			// 判斷檔案修改時間
			long timestamp = file.lastModified() / 1000;
			
			if ( (nowstamp-timestamp)>=keepstamp) {
				boolean isDelete = file.delete();
				if (isDelete)
					logger.info("================== delete S25 Log file: ["+file.getName()+"]");
				else
					logger.error("delete S25 log file: ["+file.getName()+"] failed");
			}
		}
	}
	private List<File> getFinishedListFiles() {
		List<File> files = new ArrayList<>();
		
		File dir = new File(FINISHED_DIRPATH);	// finished
		if (dir.isDirectory()) {
			File[] finishedFiles = dir.listFiles();
			for (File file: finishedFiles) {
				if (file.isFile()) {
					files.add(file);
				}
			}
		}
		return files;
	}
	
	
	
	
	/*
	 * 功能未開發完成
	 */
	private void handleDeadlockExcpetion() {
		// kill S25Task process
		killProcess();
		
		// mv S25 log to failed directory
		
		// retry S25Task one by one
		
	}
	private void killProcess() {
		List<ProcInfoBean> procLists = new ArrayList<>();
		
		List<String> cmds = new ArrayList<>();
		cmds.add("/bin/sh");
		cmds.add("-c");
		cmds.add("ps -eo pid,args | grep S25Task");
		
		// ps aux process
		try {
			Process p = Runtime.getRuntime().exec(cmds.toArray(new String[0]));
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = input.readLine()) != null) {
				line = line.trim();
				String spLine[] = line.trim().split(" ");
				for(int i = 0 ; i < spLine.length ; i++) {
					if(spLine[i].indexOf("S25Task") > -1) {
						String pid = spLine[0];
						String cmd = line.substring(line.indexOf(pid) + 1);
						procLists.add(new ProcInfoBean(pid, cmd.trim()));
					}
				}	
			}
			input.close();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// kill process
		String killCmd = "";
		for(int i = 0 ; i < procLists.size() ; i++) {
			ProcInfoBean info = procLists.get(i);
			String pid = info.getPid();
			killCmd = "kill -9 "+pid;
			
			Process pp;
			try {
				pp = Runtime.getRuntime().exec(killCmd);
				int exitValue = pp.waitFor();
				System.out.println("Exec cmd: "+killCmd+", Status: "+exitValue);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 發送mail通知
	 */
	private void sendMail(Map<String, PowerRecordModel> map) {
		String subject = "ECO S25 PF值異常, 異常通知 !!! ";
		String body = "\n";
		
		for (String log: map.keySet()) {
			PowerRecordModel record = map.get(log);
			
			body += log +"\n"+
					"( "+record.getDeviceID() +", "+ record.getRecTime() +", PF=["+record.getPF()+"] )" +"\n\n";
		}
		
		EmailUtil email = new EmailUtil();
		email.setSubject(subject);
		email.setBody(body);
		try {
			logger.info("主旨: "+subject + ", 內容: \n"+body);
			email.sendMail();
		} catch (Exception e) {
//			logger.error("Send Email Failed..: "+body);
			e.printStackTrace();
		}
	}

	/**
	 * 發送mail通知
	 */
	private void sendMail(File file, Map<String, PowerRecordModel> map) {
		String subject = "ECO S25 PF值異常, 異常通知 !!! ";
		String body = "\n";
		
		for (String log: map.keySet()) {
			PowerRecordModel record = map.get(log);
			
			body += "[檔名]: " + file.getName() +"\n"+
					"[LOG]: " + log +"\n"+
					"( "+record.getDeviceID() +", "+ record.getRecTime() +", PF=["+record.getPF()+"] )" +"\n\n";
		}
		
		EmailUtil email = new EmailUtil();
		email.setSubject(subject);
		email.setBody(body);
		try {
			logger.info("主旨: "+subject + ", 內容: \n"+body);
			email.sendMail();
		} catch (Exception e) {
//			logger.error("Send Email Failed..: "+body);
			e.printStackTrace();
		}
	}
}
