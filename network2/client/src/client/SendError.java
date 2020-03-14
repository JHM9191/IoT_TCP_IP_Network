package client;

import msg.Msg;

public class SendError extends Thread {

	Sender sender;

	public SendError(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void run() {
		Msg msg = new Msg("", "Error", Client.CAN_ID);
		sender.setMsg(msg);
		
		new Thread(sender).start();
	}
}
