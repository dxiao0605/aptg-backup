import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DateFormat;
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


public class smsQuerySm{

	private static final Logger logger = LogManager.getFormatterLogger(smsQuerySm.class.getName());
	
	
	//private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2 = 134;
	private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2 = 126;
	private static final int MAX_SINGLE_MSG_SEGMENT_SIZE_UCS2 = 70;
	private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_7BIT = 153;
	private static final int MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT = 160;
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

	public smsQuerySm() throws IOException {
		//getConfig();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SmppObject.setDebug(debug);
		// SmppObject.setEvent(event);
		logger.info("Initialising...------------------------------------------------------------------");
		smsQuerySm startap = null;
		int loop_count=0;
		
		try {
			startap = new smsQuerySm();

			if (startap != null) {

				//while (true)
				//{
				
				 
				 
				 startap.getConfig();	
				 
				 
				startap.bind();

				// Thread.sleep(2000);
				startap.QuerySM();

				// loop();
				// startap.submit();

		        startap.unbind();
				//}

			}

		} catch (Exception e) {

			startap.unbind();
			logger.error("exception initialising SMPPTest " + e);

		}

	}

	
	

	
	
	
	private void QuerySM() throws Exception{
		
		    logger.info("QuerySM...Start");
		    logger.info("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver");

			conn2 = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt2 = conn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			String sql = "select mid_seq,a_num,msg_id,ret_string,ret_code,ret_state,ret_finaldate from msgweb.sms_msg_id b where ret_string ='I' and a_num is not null ";

			// logger.info((sql);
            String ret_str ="";
		   rs2 = stmt2.executeQuery(sql);
			
			while (rs2.next())  {
				
				String mid_seq = Integer.toString(rs2.getInt("mid_seq"));
				
				String msg_id = rs2.getString("msg_id");
				String a_num = rs2.getString("a_num");
				
				//String a_num ="0973291018";
				
				logger.info("mid_seq=" + mid_seq +",msg_id=" + msg_id +",a_num=" + a_num );
				

                ret_str ="";
				
				String Q_ret = querySM(a_num,msg_id);
				String[] retarray = Q_ret.split("-"); 
				
				//rs2.updateString("ret_string", ret_str);
				//rs2.updateString("ret_code", retarray[3]);
				//rs2.updateString("ret_state", retarray[2]);
				//rs2.updateString("ret_finaldate", retarray[1]);
				//rs2.updateRow();
				
				/*
				for (String MsgId : MsgIdarray) {
                  logger.info("SMS_SEQ=" + SMS_SEQ +",A_num=" + A_num +",Msg_Id=" + MsgId);
				  String Q_ret = querySM(A_num,MsgId);
				  //logger.info("Q_ret="+ Q_ret);
				  ret_str=ret_str+Q_ret+",";
				  //logger.info("ret_str="+ ret_str);
				  
                }*/
				
				//logger.info("ret_string="+ret_str);
				//rs1.updateString("ret_string", ret_str);
				//rs1.updateRow();
			}
			
			
			stmt2.close();
			rs2.close();
			conn2.close();
		
		
		logger.info("QuerySM...End");
	}
	
	
	
	private String querySM(String aNum,String msgId) {
		logger.info("SMPPTest.query()");
		String ret_str ="";
		
		try {
			
			QuerySM request = new QuerySM();
			QuerySMResp response;

			// input values
			//messageId = getParam("Message id", messageId);
			//sourceAddress = getAddress("Source", sourceAddress);
            sourceAddress.setAddress("20100001886" + aNum.substring(1));
			String mId = msgId;
			
			
			
			// set values
			request.setMessageId(mId);
			request.setSourceAddr(sourceAddress);

			// send the request
			logger.info("Query request " + request.debugString());
			if (asynchronous) {
				session.query(request);
			} else {
				response = session.query(request);
				logger.info("Query response " + response.debugString());
				String f_date = response.getFinalDate();
				String MsgState =Byte.toString(response.getMessageState());
				String ErrCode = Byte.toString(response.getErrorCode());
				
				ret_str = "-"+f_date +"-"+ MsgState +"-"+ ErrCode;
				
				logger.info(ret_str);
				//messageId = response.getMessageId();
				//logger.info("messageId =" + messageId);
				
			}

		} catch (Exception e) {
			//event.write(e, "");
			//logger.info("Query operation failed. " + e);
			logger.error("Query operation failed. " + e);
			logger.error("System Exit " );
			System.exit(1); 
		} 
		
		    //logger.info(ret_str);
			logger.info("query finish..");
			//logger.info(ret_str);
			return ret_str;
			
		
		
		
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
			
			queryTimesLog = Integer.parseInt(properties.getProperty("queryTimesLog"));
			rebindSmscTime = Integer.parseInt(properties.getProperty("rebindSmscTime"));
			stopProcess =Integer.parseInt(properties.getProperty("stopProcess"));
			enquery_link_Time =Integer.parseInt(properties.getProperty("enquery_link_Time"));

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

			if (bindOption.compareToIgnoreCase("t") == 0) {
				request = new BindTransmitter();
			} else if (bindOption.compareToIgnoreCase("r") == 0) {
				request = new BindReceiver();
			} else if (bindOption.compareToIgnoreCase("tr") == 0) {
				request = new BindTransciever();
			} else {
				logger.info("Invalid bind mode, expected t, r or tr, got " + bindOption + ". Operation canceled.");
				return;
			}

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
