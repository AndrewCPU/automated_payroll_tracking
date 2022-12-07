package net.andrewcpu.payroll.dao.impl.init;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.SpreadsheetDAO;
import net.andrewcpu.payroll.dao.interfaces.InitializationDAO;
import net.andrewcpu.payroll.registry.InitializedDataRegistry;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;
import net.andrewcpu.payroll.util.SpreadsheetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SpreadsheetInitializationDAO extends DAO implements InitializationDAO {
	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}

	private Path createNewSpreadsheet(String currentFile) {
		getLogger().info("This week's spreadsheet (" + currentFile + ") does not exist. Copying base spreadsheet into directory." );
		Path path = FileUtil.createNewSpreadsheetFile(currentFile);
		getLogger().info("Created " + currentFile + " at " + path.getParent().toFile().getAbsolutePath());
		return path;
	}

	private Workbook loadWorkbook(Path path) {
		Workbook workbook;
		try {
			workbook = SpreadsheetUtil.load(path);
		} catch (IOException e) {
			getLogger().error(e);
			return null;
		}
		return workbook;
	}

	@Override
	public void initialize() {
		String currentFile = getCurrentFileName();
		if(!FileUtil.doesSpreadsheetExist(currentFile)) {
			createNewSpreadsheet(currentFile);
		}
		else{
			getLogger().info("Located preexisting spreadsheet.");
		}
		Path path = FileUtil.getSpreadsheetPath(currentFile);
		Workbook workbook = loadWorkbook(path);
		getLogger().info("Registering workbook and workbookPath in InitializedDataRegistry.");
		InitializedDataRegistry.workbook = workbook;
		InitializedDataRegistry.workbookPath = path;
	}



	public String getCurrentFileName() {
		Date currentMonday = DateUtil.getCurrentMonday();
		return SpreadsheetDAO.getInstance().getFileNameForDate(currentMonday);
	}

}
