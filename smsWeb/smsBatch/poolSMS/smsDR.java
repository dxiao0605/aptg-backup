import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import com.cloudhopper.commons.charset.CharsetUtil;
import org.smpp.*;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.*;
import org.smpp.debug.Debug;
import org.smpp.debug.Event;
import org.smpp.debug.FileDebug;
import org.smpp.debug.FileEvent;
import org.smpp.util.Queue;
import org.smpp.util.ByteBuffer;


public class smsDR {

	private static final Logger logger = LogManager.getFormatterLogger(smsDR.class.getName());
	
	
	
	static int isLongMsg =0;

	String DB_URL = "";
	String USER = "";
	String PASS = "";

	Connection conn = null,conn1 = null,conn2 = null;
	Statement stmt = null,stmt1 = null,stmt2 = null;
	Statement p_stmt = null;
	ResultSet rs, p_rs,rs1,rs2;

	Properties properties = new Properties();
	static Session session = null;
	static int queryTimesLog = 300;
	static int rebindSmscTime = 3000;
	static  int enquery_link_Time =30;
	static int stopProcess = 0;
	static int smsSeqStart =100000000;
	
	boolean bound = false;
	String ipAddress = null;
	int port = 0;
	String systemId = null;
	String password = null;
	String bindOption = "t";

	boolean asynchronous = false;
	SMPPTestPDUEventListener pduListener = null;

	AddressRange addressRange = new AddressRange();

	String systemType = "";
	String serviceType = "";
	Address sourceAddress = new Address();
	Address destAddress = new Address();
	String scheduleDeliveryTime = "";
	String validityPeriod = "";
	String shortMessage = "";
	int numberOfDestination = 1;
	String messageId = "";
	byte esmClass = 0;
	byte protocolId = 0;
	byte priorityFlag = 0;
	byte registeredDelivery = 0;
	byte replaceIfPresentFlag = 0;
	byte dataCoding = 0;
	byte smDefaultMsgId = 0;
	long receiveTimeout = Data.RECEIVE_BLOCKING;

	String callBackUrl = "";
	String callBackNumStr = "";
	ByteBuffer callBackNumber = new ByteBuffer();
	String chineseCharacter = "UnicodeBigUnmarked"; // big5

	// String A_NUM , B_NUM , MSG ,SMS_SEQ;
	String psSeq = "";
	String smSeq = "";
	String  A_NUM ="";
	String  B_NUM="";
	String  MSG="";
	String  SMS_SEQ="";
    
	int r_msgcount = 0;
	int t_msgcount = 0, t_finish = 0;

	public smsDR() throws IOException {
		//getConfig();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SmppObject.setDebug(debug);
		// SmppObject.setEvent(event);
		logger.info("Initialising...------------------------------------------------------------------");
		smsDR startap = null;
		int loop_count=0;
		
		try {
			startap = new smsDR();

			if (startap != null) {

				while (true)
				{
				
				 
				 
				 startap.getConfig();	
				 if(stopProcess > 0) {
					 logger.info("Stop Process----------------------------------------------------------------- ");
					 break;
				 }
				
				startap.bind();

				startap.loop();
				
				// Thread.sleep(60000);
				//startap.DrEvent();

				// loop();
				// startap.submit();

		        startap.unbind();
				}

			}

		} catch (Exception e) {

			startap.unbind();
			logger.error("exception initialising SMPPTest " + e);

		}

	}
	
	
	
	
	
	private void loop() {

		try {

			

			// logger.info((sql);

			int loop_count=0;
			int total_count=0;
			int enquery_link =0;
			
			while (true) {

				Thread.sleep(1000);
				loop_count++;
				total_count++;
				enquery_link++;
				
				int rec_count = 0;
				t_finish = 0;
				
				if(enquery_link > enquery_link_Time){
					logger.info("enquery_link "+ enquery_link_Time+ " secs");
					enquireLink();
					enquery_link =0;
					getConfig();
					
					
					 if(stopProcess > 0) {
					 logger.info("Stop Process =1");
					 break;
				    }
					
					
				}
				
				
				
				
				if(total_count > rebindSmscTime) {
					
					logger.info("rebindSmscTime "+ rebindSmscTime+ " secs");
					break;
				}
					


				
			}

		} catch (Exception e) {
			// conn.close();
			logger.error("Got an exception! " + e);
			logger.info(e.getMessage());
			e.printStackTrace();
			logger.error("System Exit " );
			System.exit(1); 

		}

	}
	
	
	
	
	
	
	
	

	private void DrEvent() {
		
		
		logger.info("DrEvent Start...");
		
		
		logger.info("DrEvent End...");
		
	}
	
	
	
	/**
	 * Gets a property and converts it into byte.
	 */
	private byte getByteProperty(String propName, byte defaultValue) {
		return Byte.parseByte(properties.getProperty(propName, Byte.toString(defaultValue)));
	}

	/**
	 * Gets a property and converts it into integer.
	 */
	private int getIntProperty(String propName, int defaultValue) {
		return Integer.parseInt(properties.getProperty(propName, Integer.toString(defaultValue)));
	}

	private void setAddressParameter(String descr, Address address, byte ton, byte npi, String addr) {
		address.setTon(ton);
		address.setNpi(npi);
		try {
			address.setAddress(addr);
		} catch (WrongLengthOfStringException e) {
			logger.error("The length of " + descr + " parameter is wrong.");
		}
	}

	private void getConfig() {

		String Config_f = "config.properties";

		byte ton;
		byte npi;
		String addr;

		String bindMode;
		int rcvTimeout;
		String syncMode;

		try {
			
			
			logger.info("Load Config ......");
			properties.load(new FileInputStream(Config_f));

			
			
			DB_URL = properties.getProperty("MySqlUrl");
			USER = properties.getProperty("username");
			PASS = properties.getProperty("password");
			
			queryTimesLog = Integer.parseInt(properties.getProperty("DqueryTimesLog"));
			rebindSmscTime = Integer.parseInt(properties.getProperty("DrebindSmscTime"));
			stopProcess =Integer.parseInt(properties.getProperty("DstopProcess"));
			enquery_link_Time =Integer.parseInt(properties.getProperty("Denquery_link_Time"));

			ipAddress = properties.getProperty("ip-address");
			port = getIntProperty("port", port);
			systemId = properties.getProperty("system-id");
			password = properties.getProperty("smsc_password");

			ton = getByteProperty("addr-ton", addressRange.getTon());
			npi = getByteProperty("addr-npi", addressRange.getNpi());
			addr = properties.getProperty("address-range", addressRange.getAddressRange());

			addressRange.setTon(ton);
			addressRange.setNpi(npi);

			try {
				addressRange.setAddressRange(addr);
			} catch (WrongLengthOfStringException e) {
				logger.error("The length of address-range parameter is wrong.");
			}

			ton = getByteProperty("source-ton", sourceAddress.getTon());
			npi = getByteProperty("source-npi", sourceAddress.getNpi());
			addr = properties.getProperty("source-address", sourceAddress.getAddress());
			setAddressParameter("source-address", sourceAddress, ton, npi, addr);

			ton = getByteProperty("destination-ton", destAddress.getTon());
			npi = getByteProperty("destination-npi", destAddress.getNpi());
			addr = properties.getProperty("destination-address", destAddress.getAddress());
			setAddressParameter("destination-address", destAddress, ton, npi, addr);

			serviceType = properties.getProperty("service-type", serviceType);
			systemType = properties.getProperty("system-type", systemType);
			bindMode = properties.getProperty("bind-mode", bindOption);
			bindOption = bindMode;

			if (receiveTimeout == Data.RECEIVE_BLOCKING) {
				rcvTimeout = -1;
			} else {
				rcvTimeout = ((int) receiveTimeout) / 1000;
			}
			rcvTimeout = getIntProperty("receive-timeout", rcvTimeout);
			if (rcvTimeout == -1) {
				receiveTimeout = Data.RECEIVE_BLOCKING;
			} else {
				receiveTimeout = rcvTimeout * 1000;
			}

			syncMode = properties.getProperty("sync-mode", (asynchronous ? "async" : "sync"));
			if (syncMode.equalsIgnoreCase("sync")) {
				asynchronous = false;
			} else if (syncMode.equalsIgnoreCase("async")) {
				asynchronous = true;
			} else {
				asynchronous = false;
			}

			shortMessage = properties.getProperty("the-short-message", shortMessage);
			dataCoding = getByteProperty("data-encoding", dataCoding);
			callBackUrl = properties.getProperty("callback-url", callBackUrl);
			callBackNumStr = properties.getProperty("callback-number", callBackNumStr);

			registeredDelivery = getByteProperty("registeredDelivery", registeredDelivery);

			Date f_Date = new Date();
			DateFormat f_DateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			psSeq = "Ps-Seq-" + f_DateFormat.format(f_Date);

			logger.info("psSeq=" + psSeq);

		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	private void bind() {
		logger.info("SMPPTest.bind()");
		try {

			if (bound) {
				logger.info("Already bound, unbind first.");
				return;
			}

			BindRequest request = null;
			BindResponse response = null;
			// String syncMode = (asynchronous ? "a" : "s");

			// type of the session
			// syncMode = getParam("Asynchronous/Synchronnous Session? (a/s)", syncMode);
			/*
			 * if (syncMode.compareToIgnoreCase("a") == 0) { asynchronous = true; } else if
			 * (syncMode.compareToIgnoreCase("s") == 0) { asynchronous = false; } else {
			 * log("Invalid mode async/sync, expected a or s, got " + syncMode +
			 * ". Operation canceled."); return; }
			 */
			// input values
			// bindOption = getParam("Transmitter/Receiver/Transciever (t/r/tr)",
			// bindOption);

			
				request = new BindReceiver();
			

			// ipAddress = getParam("IP address of SMSC", ipAddress);
			// port = getParam("Port number", port);

			TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
			connection.setReceiveTimeout(20 * 1000);
			session = new Session(connection);

			// systemId = getParam("Your system ID", systemId);
			// password = getParam("Your password", password);

			// set values
			request.setSystemId(systemId);
			request.setPassword(password);
			request.setSystemType(systemType);
			request.setInterfaceVersion((byte) 0x34);
			request.setAddressRange(addressRange);

			// send the request
			asynchronous = true;
			logger.info("Bind request " + request.debugString());
			if (asynchronous) {
				pduListener = new SMPPTestPDUEventListener(session, psSeq);
				response = session.bind(request, pduListener);
			} else {
				response = session.bind(request);
			}
			logger.info("Bind response " + response.debugString());
			if (response.getCommandStatus() == Data.ESME_ROK) {
				bound = true;
			}

		} catch (Exception e) {
			// event.write(e, "");
			// debug.write("Bind operation failed. " + e);
			logger.info("Bind operation failed. " + e);
		} finally {
			// debug.exit(this);
			logger.info("bind finish");
		}
	}

	
	
	private void enquireLink() {
		logger.info("SMPPTest.enquireLink()");
		try {

			EnquireLink request = new EnquireLink();
			EnquireLinkResp response;
			logger.info("Enquire Link request " + request.debugString());
			
			asynchronous = true;
			if (asynchronous) {
				session.enquireLink(request);
			} else {
				response = session.enquireLink(request);
				logger.info("Enquire Link response " + response.debugString());
				
			}

		} catch (Exception e) {
			//event.write(e, "");
			//debug.write("Enquire Link operation failed. " + e);
			logger.error("Enquire Link operation failed. " + e);
			
			logger.error("System Exit " );
			System.exit(1); 
		} finally {
			//debug.exit(this);
			logger.info("enquireLink finish");
		}
	}
	
	
	
	
	
	
	private void unbind() {
		logger.info("SMPPTest.unbind()");
		try {

			if (!bound) {
				logger.info("Not bound, cannot unbind.");
				return;
			}

			// send the request
			logger.info("Going to unbind.");
			if (session.getReceiver().isReceiver()) {
				logger.info("It can take a while to stop the receiver.");
			}
			UnbindResp response = session.unbind();
			logger.info("Unbind response " + response.debugString());
			bound = false;

		} catch (Exception e) {
			// event.write(e, "");
			// debug.write("Unbind operation failed. " + e);
			logger.info("Unbind operation failed. " + e);
		} finally {
			// debug.exit(this);
			logger.info("unbind finish");
		}
	}

	
	
	private boolean isAllASCII(String input) {
	    boolean isASCII = true;
	    for (int i = 0; i < input.length(); i++) {
	        int c = input.charAt(i);
	        if (c > 0x7F) {
	            isASCII = false;
	            break;
	        }
	    }
	    return isASCII;
	}

	
	public static String printbyte(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[ ");
	    for (byte b : bytes) {
	        sb.append(String.format("0x%02X ", b));
	    }
	    sb.append("]");
	    return sb.toString();
	}
	
	
	
	public  void printPdu(String smsMsg) {
        	String[] toppings = {"id:", "submit date:", "done date:" ,"stat:","err:"};
    		String[] pduresult = {"", "", "" ,"",""};
			
    		int size = toppings.length;
            for (int i=0; i<size; i++)
            {
    		String key=toppings[i];
    		int p = smsMsg.indexOf(key);
    		String right = smsMsg.substring(p + key.length());
    		String[] keyValue = right.split(" ");
			pduresult[i] = keyValue[0];
    		System.out.println(key +""+keyValue[0]);
    		
            }
        	InsertDr(pduresult[0],pduresult[1],pduresult[2],pduresult[3],pduresult[4]);
        }
	
	
    private void InsertDr(String msgid,String dodate,String donedate,String stat,String code) {
		
		PreparedStatement preparedStatement = null;
		
		try{
		    logger.info("InsertMsgId...Start");
			logger.info("msgid"+msgid+",dodate="+dodate+",donedate="+donedate+",stat="+stat+",code="+code);
		    logger.info("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver");

			
			
			conn1 = DriverManager.getConnection(DB_URL, USER, PASS);

			
			
			
			//stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
			String sql = "INSERT INTO sms_dr" + "(msg_id,ret_do_date,ret_done_date,ret_state,ret_code) VALUES" + "(?,?,?,?,?)";
			
			
			preparedStatement = conn1.prepareStatement(sql);

			preparedStatement.setString(1, msgid);
			preparedStatement.setString(2, dodate);
			preparedStatement.setString(3, donedate);
			preparedStatement.setString(4, stat);
			preparedStatement.setString(5, code);
			
			
			preparedStatement.executeUpdate();
			
			
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (conn1 != null) {
				conn1.close();
			}
			
			logger.info("Close database...");
		}catch(Exception e){
			
			logger.error("InsertDr failed. " + e);
			//logger.error("System Exit " );
			//System.exit(1); 
			
		}
		
            
		
		
		logger.info("InsertMsgId...End");
	}
	
	
	
	
	private class SMPPTestPDUEventListener extends SmppObject implements ServerPDUEventListener {
		Session session;
		Queue requestEvents = new Queue();
		String listen_psseq = "";

		public SMPPTestPDUEventListener(Session session, String lsn_psseq) {
			this.session = session;
			this.listen_psseq = lsn_psseq;
		}

		public void handleEvent(ServerPDUEvent event) {
			
			
			
			PDU pdu = event.getPDU();
			if (pdu.isRequest()) {
				logger.info("async request received, enqueuing " + pdu.debugString());
				
				//if(pdu.getCommandId() ==5){
				//logger.info("deliver_sm " + (DeliverSM)pdu.getShortMessage());	
				//}
				
				if (pdu instanceof EnquireLink) {
					try{
				 EnquireLinkResp response = new EnquireLinkResp();	
                 response.setSequenceNumber(pdu.getSequenceNumber());
				 logger.info("EnquireLinkResp = " + response.debugString());
			     session.respond(response);
					}catch (Exception e){
				
			    	logger.error("EnquireLinkResp failed. " + e);
			
			       }
				}
				
				if (pdu instanceof DeliverSM) {
                 DeliverSM deliver = (DeliverSM)pdu;
				 String retMsg = deliver.getShortMessage();
				 logger.info("deliver_sm =" + retMsg);
				 printPdu(retMsg);
				}
				
				
				synchronized (requestEvents) {
					requestEvents.enqueue(event);
					requestEvents.notify();
				}
			} else if (pdu.isResponse()) {
				r_msgcount++;
				logger.info("t_msgcount=" + t_msgcount + "," + "r_msgcount=" + r_msgcount + ",listen_psseq="
						+ listen_psseq + "seq=" + Integer.toString(pdu.getSequenceNumber()));
				logger.info("async response received " + pdu.debugString());
				// if (t_msgcount == r_msgcount)
				// unbind();
			} else {
				logger.info("pdu of unknown class (not request nor " + "response) received, discarding "
						+ pdu.debugString());
			}
			
		}

	}

}
