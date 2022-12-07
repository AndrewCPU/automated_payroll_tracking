package net.andrewcpu.payroll.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class PayrollDayModel {
	@JsonProperty("stubs")
	private List<PayrollStubModel> stubs;

	@JsonProperty("date")
	private Date date;

	@JsonProperty("hours")
	private double totalHoursWorked;
	@JsonProperty("complete")
	private boolean complete;

	public PayrollDayModel() {
	}

	public List<PayrollStubModel> getStubs() {
		return stubs;
	}

	public void setStubs(List<PayrollStubModel> stubs) {
		this.stubs = stubs;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public double getTotalHoursWorked() {
		return totalHoursWorked;
	}

	public void setTotalHoursWorked(double totalHoursWorked) {
		this.totalHoursWorked = totalHoursWorked;
	}

	@Override
	public String toString() {
		return "PayrollDayModel{" +
				"stubs=" + stubs +
				", date=" + date +
				", totalHoursWorked=" + totalHoursWorked +
				", complete=" + complete +
				'}';
	}
}
