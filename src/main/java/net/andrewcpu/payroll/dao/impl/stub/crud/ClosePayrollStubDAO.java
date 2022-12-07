package net.andrewcpu.payroll.dao.impl.stub.crud;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.day.PayrollDayDAO;
import net.andrewcpu.payroll.model.PayrollDayModel;
import net.andrewcpu.payroll.model.PayrollStubModel;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Date;

public class ClosePayrollStubDAO extends DAO {

	private static ClosePayrollStubDAO instance = null;

	public static ClosePayrollStubDAO getInstance() {
		if(instance == null){
			instance = new ClosePayrollStubDAO();
		}
		return instance;
	}

	private ClosePayrollStubDAO(){
		instance = this;
	}

	public PayrollStubModel closeCurrentStub() {
		String fileName = FileUtil.getNextAvailableFileName(FileUtil.getStubsDirectory(), DateUtil.getDateFormat().format(new Date()), "json");
		PayrollStubModel stubModel = LocatePayrollStubDAO.getInstance().getCurrentStub();
		getLogger().info("Writing closed stub to " + fileName);
		FileUtil.writeObjectToPath(stubModel, new File(FileUtil.getStubsDirectory().toFile(), fileName).toPath());
		getLogger().info("Deleting old current.json");
		LocatePayrollStubDAO.getInstance().getCurrentStubPath().toFile().delete();
		getLogger().info("Adding " + fileName + " to current day.");
		PayrollDayDAO.getInstance().addNewStub(stubModel);
		return stubModel;
	}



	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}
}
