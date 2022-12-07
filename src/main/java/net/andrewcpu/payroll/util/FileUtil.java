package net.andrewcpu.payroll.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtil {
	private static final String DIRECTORY_NAME = "payroll";
	private static final String STUBS_DIRECTORY = "stubs";
	private static final String DAYS_DIRECTORY = "days";
	private static final String CONFIG_DIRECTORY = "config";

	public static Path getStubsDirectory() {
		return new File(getPayrollDirectory().toFile(), STUBS_DIRECTORY).toPath();
	}

	public static Path getDaysDirectory() {
		return new File(getPayrollDirectory().toFile(), DAYS_DIRECTORY).toPath();
	}
	public static Path getConfigDirectory() {
		return new File(getPayrollDirectory().toFile(), CONFIG_DIRECTORY).toPath();
	}


	public static <T> T readObjectFromPath(Path path, Class<T> type) {
		try {
			return (T)(new ObjectMapper().readValue(path.toFile(), type));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File writeObjectToPath(Object o, Path path) {
		if(path.toFile().exists()){
			path.toFile().delete();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(path.toFile(), o);
			return path.toFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static Path getPayrollDirectory () {
		String basePath = System.getProperty("user.home");
		return new File(basePath, DIRECTORY_NAME).toPath();
	}

	public static boolean doesPayrollDirectoryExist() {
		return getPayrollDirectory().toFile().exists();
	}
	public static boolean doesSpreadsheetExist(String fileName) {
		return new File(getPayrollDirectory().toFile(), fileName).exists();
	}

	public static boolean doesStubsDirectoryExist() {
		return getStubsDirectory().toFile().exists();
	}

	public static boolean doesDaysDirectoryExist() {
		return getDaysDirectory().toFile().exists();
	}
	public static boolean doesConfigDirectoryExist() {
		return getConfigDirectory().toFile().exists();
	}

	public static Path createStubsDirectory() {
		getStubsDirectory().toFile().mkdir();
		return getStubsDirectory();
	}

	public static Path createDaysDirectory() {
		getDaysDirectory().toFile().mkdir();
		return getDaysDirectory();
	}

	public static Path createConfigDirectory() {
		getConfigDirectory().toFile().mkdir();
		return getConfigDirectory();
	}


	public static Path createPayrollDirectory () {
		File directory = getPayrollDirectory().toFile();
		directory.mkdir();
		return directory.toPath();
	}


	public static Path getSpreadsheetPath(String fileName) {
		return new File(getPayrollDirectory().toFile(), fileName).toPath();
	}

	public static File getDefaultSpreadsheetFile() {
		return new File(FileUtil.class.getClassLoader().getResource("PAYROLL.xlsx").getFile());
	}

	public static File getDefaultConfigurationFile() {
		return new File(FileUtil.class.getClassLoader().getResource("default.json").getFile());
	}

	public static Path getSpreadsheetExtractedPath() {
		return new File(getConfigDirectory().toFile(), ("sheet.xlsx")).toPath();
	}

	public static Path createNewSpreadsheetFile(String fileName) {
		try {
			Files.copy(getSpreadsheetExtractedPath(), getSpreadsheetPath(fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return getSpreadsheetPath(fileName);
	}

	public static String getNextAvailableFileName(Path directory, String prefix, String extension) {
		File file;
		int n = 1;
		do {
			file = new File(directory.toFile(), prefix + "-" + n + "." + extension);
			n++;
		} while (file.exists());
		return file.getName();
	}



}
