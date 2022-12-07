package net.andrewcpu.payroll.dao.impl;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.day.PayrollDayDAO;
import net.andrewcpu.payroll.model.PayrollDayModel;
import net.andrewcpu.payroll.registry.InitializedDataRegistry;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.SpreadsheetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class SpreadsheetDAO extends DAO {

	private static SpreadsheetDAO instance = null;

	public static SpreadsheetDAO getInstance() {
		if(instance == null) {
			instance = new SpreadsheetDAO();
		}
		return instance;
	}

	private SpreadsheetDAO() {
		instance = this;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}

	public String getFileNameForDate(Date date) {
		String formattedDate = DateUtil.getDateFormat().format(date);
		return formattedDate + ".xlsx";
	}
	public void logHoursForToday() {
		String day = LocalDate.now().getDayOfWeek().name().toUpperCase();
		String position = InitializedDataRegistry.configuration.getLocation().valueOf(day);
		Point point = SpreadsheetUtil.getCoordinatesFromLetters(position);

		PayrollDayModel today = PayrollDayDAO.getInstance().getCurrentPayrollDay();
		if(today.isComplete()){
			getLogger().info("Today is complete. Logging total hours for the day (" + today.getTotalHoursWorked() + "hrs)");
			try {
				SpreadsheetUtil.setValue(point, String.valueOf(today.getTotalHoursWorked()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else{
			try {
				throw new Exception("Cannot log hours for day, until day is closed out.");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
