package aptg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;


public class SocketClient {
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(5);
	
    private String address = "127.0.0.1";// 連線的ip
    private int port = 7500;// 連線的port
    private List<Long> threadList = new ArrayList<>();

	public SocketClient() {
//        try {
//            // 送出字串
//            String request = "*1;S01;00000001;EDGH-ECO-TEST-0066--;IN11-ECO-TEST-006602;";
////            String request = "#1;A50;00000004;gatewayID;deviceID;";
//            
//        	Socket socket = SocketFactory.getDefault().createSocket(address, port);
//        	
//        	int count = 0;
//        	while(true) {
//            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            	out.println(request);
//            	System.out.println("client send: "+request);
//                
//            	count++;
//            	if (count==3)
//            		socket = null;
//            	
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String response = reader.readLine();
//            	System.out.println("client get: "+response);
//
//            	try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//        	}        	
////            socket.close();
////            socket = null;
//            
//        } catch (java.io.IOException e) {
//            System.out.println("Socket連線有問題 !");
//            System.out.println("IOException :" + e.getMessage());
//        }
    }
	private void multi() {
		int size = 3;
		for (int index=1 ; index<=size ; index++) {
	    	threadPool.execute(new MultiClient(index));
		}

    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("\n-------------------------------------------\n"+"STOP ThreadID: "+threadList.get(0)+" \n"+ "-------------------------------------------\n");
	}
    public static void main(String args[]) {
        SocketClient client = new SocketClient();
    	client.multi();
    }
    
    
    public class MultiClient implements Runnable {
    	
    	private int count;
    	
    	public MultiClient(int count) {
    		this.count = count;
    	}

		@Override
		public void run() {
	        try {
	            // 送出字串
	            String request = "*1;S01;00000001;EDGH-ECO-TEST-0066--;IN11-ECO-TEST-"+String.format("%06d", count)+";";
//	            String request = "#1;A50;00000004;gatewayID;deviceID;";
	            
	        	Socket socket = SocketFactory.getDefault().createSocket(address, port);

	            long threadId = Thread.currentThread().getId();
	        	synchronized (threadList) {
		            threadList.add(threadId);
				}
	        	System.out.println("######## threadId=["+threadId+"], count=["+count+"], socket: "+socket + ", thread size:"+threadList.size());
	        	
	        	int count = 0;
	        	while(true) {
	            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            	out.println(request);
	            	System.out.println(" threadId=["+threadId+"], client["+count+"] send: "+request);
	                
	            	count++;
	            	if (count==5 && threadId==threadList.get(0)) {
	            		socket = null;
	            	}
	            	
	                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                String response = reader.readLine();
	            	System.out.println("client["+count+"] get: "+response);

	            	try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}        	
//	            socket.close();
//	            socket = null;
	            
	        } catch (java.io.IOException e) {
	            System.out.println("Socket連線有問題 !");
	            System.out.println("IOException :" + e.getMessage());
	        }
		}
    }
}
