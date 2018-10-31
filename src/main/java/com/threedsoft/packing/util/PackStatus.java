package com.threedsoft.packing.util;

public enum PackStatus {
	CREATED("Created"), RELEASED("Released"), PACKED("Packed"), SHORTED("Shorted"), CANCELLED("Cancelled");
	PackStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}
}