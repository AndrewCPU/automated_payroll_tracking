package net.andrewcpu.payroll.dao.impl.stub;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.stub.crud.ClosePayrollStubDAO;
import net.andrewcpu.payroll.dao.impl.stub.crud.CreatePayrollStubDAO;
import net.andrewcpu.payroll.dao.impl.stub.crud.LocatePayrollStubDAO;
import net.andrewcpu.payroll.dao.impl.stub.crud.UpdatePayrollStubDAO;
import net.andrewcpu.payroll.model.PayrollStubModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class PayrollStubDAO extends DAO {
	private static PayrollStubDAO instance = null;

	public static PayrollStubDAO getInstance() {
		if(instance == null) {
			instance = new PayrollStubDAO();
		}
		return instance;
	}

	private PayrollStubDAO() {
		instance = this;
	}

	private void precheck() {
		if(!LocatePayrollStubDAO.getInstance().doesCurrentStubExist())
		{
			CreatePayrollStubDAO.getInstance().createPayrollStub();
			getLogger().info("Creating new payroll stub.");
		}
	}

	public void clockIn() {
		precheck();
		if(LocatePayrollStubDAO.getInstance().isCurrentStubFinished())
		{
			ClosePayrollStubDAO.getInstance().closeCurrentStub();
			CreatePayrollStubDAO.getInstance().createPayrollStub();
		}
		getLogger().info("Clocking in at " + new Date().toString());
		UpdatePayrollStubDAO.getInstance().setStartTime(new Date());
	}

	public void clockOut() {
		precheck();
		if(LocatePayrollStubDAO.getInstance().isCurrentStubFinished()){
			try {
				throw new Exception("Tried to clock out on an already finished stub! Where was clock in?");
			} catch (Exception e) {
				getLogger().error(e);
			}
		}
		else{
			getLogger().info("Clocking out at " + new Date().toString());
		}
		UpdatePayrollStubDAO.getInstance().setEndTime(new Date());
		ClosePayrollStubDAO.getInstance().closeCurrentStub();
	}

	public boolean isClockedIn() {
		if(!LocatePayrollStubDAO.getInstance().doesCurrentStubExist()) {
			return false;
		}
		if(LocatePayrollStubDAO.getInstance().getCurrentStub().getStartTime() != null){
			if(LocatePayrollStubDAO.getInstance().getCurrentStub().getEndTime() == null){
				return true;
			}
		}
 		return false;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}
}
