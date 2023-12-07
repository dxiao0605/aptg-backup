package aptg.handle;

import java.net.Socket;

import aptg.function.FuncID_A;
import aptg.function.FuncID_C;
import aptg.manager.SocketManager;

/**
 * 處理 web to eco5 的查詢or設定
 * 
 * @author austinchen
 *
 */
public class TaskHandle implements Runnable {
	
	private String request;
	
	public TaskHandle(String request) {
		this.request = request;
	}
	
	@Override
	public void run() {
//		int semicolonCount = StringUtils.countMatches(request, ";");

		String[] message = request.split(";");	// *1;S01;00000001;ES990000000000TEST00;abc123;\n
		String funcID = message[1].substring(0, 1);	// ex: S01, A50 => S, A
		String gatewayID = message[3];
		
		Socket socket = SocketManager.getInstance().getSocket(gatewayID);

		try {
			switch(funcID) {
				// 查詢指令 request or response
				case "A":
					FuncID_A func_a = new FuncID_A(socket);
					func_a.queryCmd(message);
					break;
					
				case "C":
					FuncID_C func_c = new FuncID_C(socket);
					func_c.setParameter(message);
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
