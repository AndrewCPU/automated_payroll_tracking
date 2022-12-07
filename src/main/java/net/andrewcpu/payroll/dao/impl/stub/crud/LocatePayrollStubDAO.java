package net.andrewcpu.payroll.dao.impl.stub.crud;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.model.PayrollStubModel;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LocatePayrollStubDAO extends DAO {
	private static LocatePayrollStubDAO instance = null;

	public static LocatePayrollStubDAO getInstance() {
		if(instance == null)
		{
			instance = new LocatePayrollStubDAO();
		}
		return instance;
	}

	private LocatePayrollStubDAO() {
		instance = this;
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}

	public Path getCurrentStubPath() {
		File currentPath = new File(FileUtil.getStubsDirectory().toFile(), "current.json");
		return currentPath.toPath();
	}

	public boolean doesCurrentStubExist() {
		File currentPath = getCurrentStubPath().toFile();
		return currentPath.exists();
	}

	public PayrollStubModel getCurrentStub() {
		File currentPath = getCurrentStubPath().toFile();
		return FileUtil.readObjectFromPath(currentPath.toPath(), PayrollStubModel.class);
	}

	public boolean isCurrentStubFinished() {
		return getCurrentStub().getEndTime() != null && getCurrentStub().getStartTime() != null;
	}

}
