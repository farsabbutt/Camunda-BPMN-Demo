package org.automationlab.api.services;

public class ServiceResponse<T> {
	public boolean success;
	public T data;
	public String message;
	
	public ServiceResponse() {
		this.success = true;
	}
	
	public ServiceResponse(boolean success, T data, String message) {
		this.success = success;
		this.data = data;
		this.message = message;
	}
	
	public ServiceResponse(boolean success, T data) {
		this.success = success;
		this.data = data;
	}
	
	public ServiceResponse(String message) {
		success = false;
		this.message = message;
	}	
}
