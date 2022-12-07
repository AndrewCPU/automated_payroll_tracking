package net.andrewcpu.payroll.registry;

import net.andrewcpu.payroll.dao.impl.init.SpreadsheetInitializationDAO;
import net.andrewcpu.payroll.dao.interfaces.InitializationDAO;

import java.util.ArrayList;
import java.util.List;

public class InitializationRegistry {
	public static List<InitializationDAO> initializers = new ArrayList<>();
	static {
		initializers.add(new SpreadsheetInitializationDAO());
	}
}
