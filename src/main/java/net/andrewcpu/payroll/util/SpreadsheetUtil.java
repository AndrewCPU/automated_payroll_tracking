package net.andrewcpu.payroll.util;

import net.andrewcpu.payroll.registry.InitializationRegistry;
import net.andrewcpu.payroll.registry.InitializedDataRegistry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class SpreadsheetUtil {

	public static Point getCoordinatesFromLetters(String letters) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String[] split = letters.split("", 2);
		int x = alphabet.indexOf(split[0]);
		int y = Integer.parseInt(split[1]);
		return new Point(x, y-1);
	}

	public static void setValue(Point point, String value) throws IOException {
		InitializedDataRegistry.workbook.getSheetAt(0).getRow(point.y).getCell(point.x, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(value);
		FileOutputStream outputStream = new FileOutputStream(InitializedDataRegistry.workbookPath.toFile());
		InitializedDataRegistry.workbook.write(outputStream);
		// WE DO NOT CLOSE WORKBOOK HERE
	}

	public static void setValue(Workbook workbook, Path path, Point point, String value) throws IOException {
		workbook.getSheetAt(0).getRow(point.y).getCell(point.x, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(value);
		FileOutputStream outputStream = new FileOutputStream(path.toFile());
		workbook.write(outputStream);
		// WE DO NOT CLOSE WORKBOOK HERE
	}

	public static void closeWorkbook() {
		try {
			InitializedDataRegistry.workbook.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Workbook load(Path path) throws FileNotFoundException, IOException {
		FileInputStream file = new FileInputStream(path.toFile());
		Workbook workbook = new XSSFWorkbook(file);
		return workbook;
	}
}
