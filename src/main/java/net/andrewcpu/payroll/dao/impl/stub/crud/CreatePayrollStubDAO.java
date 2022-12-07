package net.andrewcpu.payroll.dao.impl.stub.crud;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.model.PayrollStubModel;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class CreatePayrollStubDAO extends DAO {
	private static CreatePayrollStubDAO instance = null;

	public static CreatePayrollStubDAO getInstance() {
		if(instance == null){
			instance = new CreatePayrollStubDAO();
		}
		return instance;
	}

	private CreatePayrollStubDAO() {
		instance = this;
	}

	public PayrollStubModel createPayrollStub() {
		Path currentLocation = LocatePayrollStubDAO.getInstance().getCurrentStubPath();
		if(currentLocation.toFile().exists()){
			try {
				throw new Exception("Current payroll stub already exists.");
			} catch (Exception e) {
				getLogger().error(e);
				return null;
			}
		}
		else{
			getLogger().info("No stub already existing. Creating a new one.");
		}

		PayrollStubModel stubModel = new PayrollStubModel();
		stubModel.setEndTime(null);
		stubModel.setStartTime(null);

		FileUtil.writeObjectToPath(stubModel, currentLocation);

		return stubModel;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}

}
