package tcpip2;

import java.io.Serializable;
import java.util.ArrayList;

public class Msg implements Serializable{

	private static final long serialVersionUID = 1L;
	String id;
	String txt;
	String tid;
	ArrayList<String> ips;

	public Msg() {
	}

	public Msg(String id, String txt) {
		this.id = id;
		this.txt = txt;
	}

	public Msg(String id, String txt, String tid) {
		this.id = id;
		this.txt = txt;
		this.tid = tid;
	}

	public Msg(String id, String txt, String tid, ArrayList<String> ips) {
		this.id = id;
		this.txt = txt;
		this.tid = tid;
		this.ips = ips;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public ArrayList<String> getIps() {
		return ips;
	}

	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}

}
