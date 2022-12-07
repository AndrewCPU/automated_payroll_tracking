package net.andrewcpu.payroll.registry;

import net.andrewcpu.payroll.model.config.ConfigurationModel;
import org.apache.poi.ss.usermodel.Workbook;

import java.nio.file.Path;

public class InitializedDataRegistry {

	public static ConfigurationModel configuration = null;
	public static Workbook workbook = null;
	public static Path workbookPath = null;
}
