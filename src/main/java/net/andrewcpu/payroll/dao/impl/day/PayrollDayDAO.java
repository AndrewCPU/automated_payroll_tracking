package net.andrewcpu.payroll.dao.impl.day;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.SpreadsheetDAO;
import net.andrewcpu.payroll.model.PayrollDayModel;
import net.andrewcpu.payroll.model.PayrollStubModel;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PayrollDayDAO extends DAO {

	private static PayrollDayDAO instance = null;

	public static PayrollDayDAO getInstance() {
		if(instance == null){
			new PayrollDayDAO();
		}
		return instance;
	}

	private PayrollDayDAO() {
		instance = this;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}

	public boolean isCurrentDayOpen() {
		return !getCurrentPayrollDay().isComplete();
	}

	public PayrollDayModel getCurrentPayrollDay() {
		if(!doesCurrentModelExist()) {
			PayrollDayModel model = new PayrollDayModel();
			model.setComplete(false);
			model.setDate(new Date());
			model.setStubs(new ArrayList<>());
			FileUtil.writeObjectToPath(model, getCurrentDayFullPath());
		}
		return FileUtil.readObjectFromPath(getCurrentDayFullPath(), PayrollDayModel.class);
	}

	public PayrollDayModel addNewStub(PayrollStubModel payrollStubModel)  {
		PayrollDayModel day = getCurrentPayrollDay();
		if(day.isComplete()) {
			try {
				throw new Exception("Cannot add a pay stub after today has been closed out!");
			} catch (Exception e) {
				getLogger().error(e);
				throw new RuntimeException(e);
			}
		}
		day.getStubs().add(payrollStubModel);
		FileUtil.writeObjectToPath(day, getCurrentDayFullPath());
		return day;
	}

	public PayrollDayModel reopen() {
		PayrollDayModel day = getCurrentPayrollDay();
		if(day.isComplete()) {
			day.setComplete(false);
			day.setTotalHoursWorked(0.0);
			FileUtil.writeObjectToPath(day, getCurrentDayFullPath());
			return day;
		}
		return null;
	}

	public PayrollDayModel endDay() {
		PayrollDayModel day = getCurrentPayrollDay();
		if(day.isComplete()){
			try {
				throw new Exception("You cannot end a day that has already ended!");
			} catch (Exception e) {
				getLogger().error(e);
				throw new RuntimeException(e);
			}
		}

		long totalTime = 0;
		for(PayrollStubModel stub : day.getStubs()) {
			totalTime += (stub.getEndTime().getTime() - stub.getStartTime().getTime());
		}
		//totalTime in milliseconds
		int totalMinutes = ((Long)TimeUnit.MILLISECONDS.toMinutes(totalTime)).intValue();

		int totalHours = totalMinutes / 60;
		int leftOverMinutes = totalMinutes % 60;
		if(leftOverMinutes == 0) {
			leftOverMinutes = 0;
		}
		else if(leftOverMinutes < 15) {
			leftOverMinutes = 15;
		}
		else if(leftOverMinutes < 30) {
			leftOverMinutes = 30;
		}
		else if(leftOverMinutes < 45) {
			leftOverMinutes = 45;
		}
		else if(leftOverMinutes < 60){
			leftOverMinutes = 60;
		}
		double roundedHours = totalHours + (leftOverMinutes / 60.0);
		day.setComplete(true);
		day.setTotalHoursWorked(roundedHours);
		FileUtil.writeObjectToPath(day, getCurrentDayFullPath());
		SpreadsheetDAO.getInstance().logHoursForToday();
		return day;
	}

	private Path getCurrentDayFullPath() {
		return new File(FileUtil.getDaysDirectory().toFile(), getCurrentDayFileName()).toPath();
	}
	private String getCurrentDayFileName() {
		String today = DateUtil.getDateFormat().format(new Date());
		return today + ".json";
	}

	private boolean doesCurrentModelExist() {
		return new File(FileUtil.getDaysDirectory().toFile(), getCurrentDayFileName()).exists();
	}

}
