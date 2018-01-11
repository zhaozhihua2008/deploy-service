package com.boco.deploy.data;

public class InstallLogData {
	private String logId;
	private String data;
	private int endIndex;
	private boolean eof;
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public boolean isEof() {
		return eof;
	}
	public void setEof(boolean eof) {
		this.eof = eof;
	}
	
	
}
