package com.avy.cflag.game.utils;

public class HelpMsg {
	private int row;
	private int col;
	private String msg;
	
	HelpMsg(){
		this.row = -1;
		this.col = -1;
		this.msg = "";
	}
	
	public HelpMsg(int inRow, int inCol, String inMsg) {
		this.row = inRow;
		this.col = inCol;
		this.msg = inMsg;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
