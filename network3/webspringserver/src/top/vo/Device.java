package top.vo;

public class Device {
	String ip;
	String id;

	public Device() {
		super();
	}

	public Device(String ip, String id) {
		super();
		this.ip = ip;
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "loginInfo [ip=" + ip + ", id=" + id + "]";
	}

}
