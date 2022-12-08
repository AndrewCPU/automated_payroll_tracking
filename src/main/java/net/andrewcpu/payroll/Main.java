package net.andrewcpu.payroll;

import net.andrewcpu.payroll.dao.impl.day.PayrollDayDAO;
import net.andrewcpu.payroll.dao.impl.init.ApplicationInitializationDAO;
import net.andrewcpu.payroll.dao.impl.stub.PayrollStubDAO;
import net.andrewcpu.payroll.gui.PayrollGUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	public static void main(String[] args) {
		new Main();
	}

	private ApplicationInitializationDAO applicationInitializationDAO;

	private static final Logger loggers = LogManager.getLogger();

	public Main() {
		applicationInitializationDAO = new ApplicationInitializationDAO();
		try {
			applicationInitializationDAO.initialize();
			new PayrollGUI();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
