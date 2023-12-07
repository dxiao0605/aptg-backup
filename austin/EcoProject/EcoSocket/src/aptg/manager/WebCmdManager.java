package aptg.manager;

import java.util.ArrayList;
import java.util.List;

public class WebCmdManager {
	
//	private static LinkedHashMap<String, String> cmdMap = new LinkedHashMap<>();
	private List<String> cmdList = new ArrayList<>();
	
	private static WebCmdManager instances;
	private WebCmdManager() {}
	
	public static WebCmdManager getInstance() {
		if (instances==null) {
			synchronized (WebCmdManager.class) {
				if (instances==null) {
					instances = new WebCmdManager();
				}
			}
		}
		return instances;
	}

	public String getWebCmd() {
		if (cmdList.size()!=0) {
			String cmd = cmdList.get(0);
			removeWebCmd();
			return cmd;
		}
		return null;
	}
	
	public void addWebCmd(String cmd) {
		synchronized (cmdList) {
			cmdList.add(cmd);	
		}
	}
	
	public void removeWebCmd() {
		synchronized (cmdList) {
			cmdList.remove(0);
		}
	}
}
