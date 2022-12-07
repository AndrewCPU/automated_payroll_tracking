package net.andrewcpu.payroll.dao.impl.week;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.impl.SpreadsheetDAO;
import net.andrewcpu.payroll.dao.impl.init.SpreadsheetInitializationDAO;
import net.andrewcpu.payroll.registry.InitializedDataRegistry;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;
import net.andrewcpu.payroll.util.SpreadsheetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class FinalizePayrollDAO extends DAO {
	private static FinalizePayrollDAO instance = null;

	public static FinalizePayrollDAO getInstance() {
		if(instance == null){
			instance = new FinalizePayrollDAO();
		}
		return instance;
	}

	private FinalizePayrollDAO() {
		instance = this;
	}

	public Path finalizeWeek() throws IOException {
		// execute on monday?
		Calendar calendar = Calendar.getInstance();
		Date startDate;
		if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.add(Calendar.DATE, -7);
			startDate = calendar.getTime();
		}
		else{
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			// if today is not monday, assume its like a friday and go back to the current monday.
			startDate = calendar.getTime();
		}

		String fileName = SpreadsheetDAO.getInstance().getFileNameForDate(calendar.getTime());
		Workbook workbook = SpreadsheetUtil.load(FileUtil.getSpreadsheetPath(fileName));
		Sheet sheet = workbook.getSheetAt(0);
		double total = 0.0;
		do {
			String DAY_OF_WEEK = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault()).getDayOfWeek().name().toUpperCase();
			Point spreadsheetPosition = SpreadsheetUtil.getCoordinatesFromLetters(InitializedDataRegistry.configuration.getLocation().valueOf(DAY_OF_WEEK));
			String value = sheet.getRow(spreadsheetPosition.y).getCell(spreadsheetPosition.x, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
			if(value.length() == 0){
				getLogger().info("No hours recorded for " + DAY_OF_WEEK + ". Defaulting to 0.0");
				SpreadsheetUtil.setValue(workbook,FileUtil.getSpreadsheetPath(fileName), spreadsheetPosition, "0.0");
			}
			else{
				total += Double.parseDouble(value);
			}
			calendar.add(Calendar.DATE, 1);
		} while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);
		Point totalLocation = SpreadsheetUtil.getCoordinatesFromLetters(InitializedDataRegistry.configuration.getLocation().getTotal());
		Point dateLocation = SpreadsheetUtil.getCoordinatesFromLetters(InitializedDataRegistry.configuration.getLocation().getDate());
		SpreadsheetUtil.setValue(workbook, FileUtil.getSpreadsheetPath(fileName), dateLocation, DateUtil.getInDocumentDateFormat().format(startDate));
		SpreadsheetUtil.setValue(workbook, FileUtil.getSpreadsheetPath(fileName), totalLocation, Double.toString(total));
		return FileUtil.getSpreadsheetPath(fileName);
	}


	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}
}
