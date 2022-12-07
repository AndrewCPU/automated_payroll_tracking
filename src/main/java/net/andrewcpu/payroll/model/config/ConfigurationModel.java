package net.andrewcpu.payroll.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationModel {
	@JsonProperty("location")
	private SpreadsheetConfigurationModel location;

	public SpreadsheetConfigurationModel getLocation() {
		return location;
	}

	public void setLocation(SpreadsheetConfigurationModel location) {
		this.location = location;
	}

	public ConfigurationModel() {
	}

	@Override
	public String toString() {
		return "ConfigurationModel{" +
				"location=" + location +
				'}';
	}
}
