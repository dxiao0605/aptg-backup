package aptg.bean;

import java.io.Serializable;

public class MqttConfigBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String topic;
	private String mqttid;
	private String qos;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMqttid() {
		return mqttid;
	}
	public void setMqttid(String mqttid) {
		this.mqttid = mqttid;
	}
	public String getQos() {
		return qos;
	}
	public void setQos(String qos) {
		this.qos = qos;
	}
}
