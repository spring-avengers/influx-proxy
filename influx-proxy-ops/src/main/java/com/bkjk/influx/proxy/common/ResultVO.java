package com.bkjk.influx.proxy.common;

import java.io.Serializable;

public class ResultVO implements Serializable {

	private static final long serialVersionUID = 3191995929954552019L;

	private boolean success;

	private String message;

	public ResultVO(boolean success) {
		this.success = success;
	}

	public ResultVO(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
