package top.vo;

public class Device {
	String ip;
	String id;
	String carId;

	public Device() {
		super();
	}

	public Device(String ip, String id, String carId) {
		super();
		this.ip = ip;
		this.id = id;
		this.carId = carId;
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

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Override
	public String toString() {
		return "Device [ip=" + ip + ", id=" + id + ", carId=" + carId + "]";
	}

}
