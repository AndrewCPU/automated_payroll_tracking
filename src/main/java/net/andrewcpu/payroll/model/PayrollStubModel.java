package net.andrewcpu.payroll.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PayrollStubModel {
	@JsonProperty("start")
	private Date startTime;

	@JsonProperty("end")
	private Date endTime;

	public PayrollStubModel() {
	}

	public PayrollStubModel(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "PayrollStubModel{" +
				"startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}
}
