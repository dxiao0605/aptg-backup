package aptg.bean;

import java.io.Serializable;

public class PublishBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mqttid;
	private String topic;
	private String payload;
	private String uuid;
	
	public String getMqttid() {
		return mqttid;
	}
	public void setMqttid(String mqttid) {
		this.mqttid = mqttid;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
