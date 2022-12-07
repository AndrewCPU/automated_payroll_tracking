package net.andrewcpu.payroll.dao.impl.stub.crud;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.model.PayrollStubModel;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class UpdatePayrollStubDAO extends DAO {
	private static UpdatePayrollStubDAO instance = null;

	public static UpdatePayrollStubDAO getInstance() {
		if(instance == null){
			instance = new UpdatePayrollStubDAO();
		}
		return instance;
	}

	private UpdatePayrollStubDAO() {
		instance = this;
	}

	public PayrollStubModel setStartTime(Date date) {
		getLogger().info("Setting start time to " + date.toString());
		PayrollStubModel stubModel = LocatePayrollStubDAO.getInstance().getCurrentStub();
		stubModel.setStartTime(date);
		FileUtil.writeObjectToPath(stubModel,LocatePayrollStubDAO.getInstance().getCurrentStubPath());
		return stubModel;
	}

	public PayrollStubModel setEndTime(Date date) {
		getLogger().info("Setting end time to " + date.toString());
		PayrollStubModel stubModel = LocatePayrollStubDAO.getInstance().getCurrentStub();
		stubModel.setEndTime(date);
		FileUtil.writeObjectToPath(stubModel,LocatePayrollStubDAO.getInstance().getCurrentStubPath());
		return stubModel;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}
}
