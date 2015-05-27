package com.avy.cflag.game.utils;

public class HelpMsg {
	private int x;
	private int y;
	private String msg;
	
	HelpMsg(){
		this.x = -1;
		this.y = -1;
		this.msg = "";
	}
	
	public HelpMsg(int x, int y, String inMsg) {
		this.x = x;
		this.y = y;
		this.msg = inMsg;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
