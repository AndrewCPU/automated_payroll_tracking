package net.andrewcpu.payroll.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.poi.ss.formula.functions.WeekNum;

public class SpreadsheetConfigurationModel {
	@JsonProperty("MONDAY")
	private String monday;

	@JsonProperty("TUESDAY")
	private String tuesday;

	@JsonProperty("WEDNESDAY")
	private String wednesday;

	@JsonProperty("THURSDAY")
	private String thursday;

	@JsonProperty("FRIDAY")
	private String friday;

	@JsonProperty("SATURDAY")
	private String saturday;

	@JsonProperty("SUNDAY")
	private String sunday;

	@JsonProperty("TOTAL")
	private String total;

	@JsonProperty("DATE")
	private String date;


	public String valueOf(String str) {
		switch (str){
			case "MONDAY":
				return monday;
			case "TUESDAY":
				return tuesday;
			case "WEDNESDAY":
				return wednesday;
			case "THURSDAY":
				return thursday;
			case "FRIDAY":
				return friday;
			case "SATURDAY":
				return saturday;
			case "SUNDAY":
				return sunday;
			case "TOTAL":
				return total;
			case "DATE":
				return date;
		}
		return null;
	}


	public SpreadsheetConfigurationModel() {
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SpreadsheetConfigurationModel{" +
				"monday='" + monday + '\'' +
				", tuesday='" + tuesday + '\'' +
				", wednesday='" + wednesday + '\'' +
				", thursday='" + thursday + '\'' +
				", friday='" + friday + '\'' +
				", saturday='" + saturday + '\'' +
				", sunday='" + sunday + '\'' +
				", total='" + total + '\'' +
				", date='" + date + '\'' +
				'}';
	}
}
