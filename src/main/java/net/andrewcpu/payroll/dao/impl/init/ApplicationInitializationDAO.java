package net.andrewcpu.payroll.dao.impl.init;

import net.andrewcpu.payroll.dao.DAO;
import net.andrewcpu.payroll.dao.interfaces.InitializationDAO;
import net.andrewcpu.payroll.model.config.ConfigurationModel;
import net.andrewcpu.payroll.registry.InitializationRegistry;
import net.andrewcpu.payroll.registry.InitializedDataRegistry;
import net.andrewcpu.payroll.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static net.andrewcpu.payroll.util.FileUtil.getDefaultSpreadsheetFile;
import static net.andrewcpu.payroll.util.FileUtil.getSpreadsheetPath;

public class ApplicationInitializationDAO extends DAO implements InitializationDAO {
	@Override
	public void initialize() {
		directoryCheck();
		initConfig();
		for(InitializationDAO dao : InitializationRegistry.initializers){
			getLogger().info("Initializing " + dao.getClass().getSimpleName());
			dao.initialize();
		}
	}

	private void initConfig() {
		File configDirectory = FileUtil.getConfigDirectory().toFile();
		File configFile = new File(configDirectory, "config.json");
		if(!configFile.exists()) {
			getLogger().info("Config does not exist. Copying default config.");
			try {
				Files.copy(FileUtil.getDefaultConfigurationFile(),configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(getDefaultSpreadsheetFile(), new File(FileUtil.getConfigDirectory().toFile(),"sheet.xlsx").toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		InitializedDataRegistry.configuration = FileUtil.readObjectFromPath(configFile.toPath(), ConfigurationModel.class);
		getLogger().info("Loaded config.");
	}

	private void directoryCheck() {
		if(!FileUtil.doesPayrollDirectoryExist()) {
			getLogger().info("Payroll directory does not exist.");
			getLogger().info("Creating payroll directory.");
			FileUtil.createPayrollDirectory();
			getLogger().info("Payroll directory has been created.");
		}
		else{
			getLogger().info("Located existing payroll directory (" + FileUtil.getPayrollDirectory().toString() + ")");
		}

		if(!FileUtil.doesDaysDirectoryExist()) {
			getLogger().info("Days directory does not exist.");
			getLogger().info("Creating days directory.");
			FileUtil.createDaysDirectory();
		}
		else{
			getLogger().info("Located day history directory.");
		}

		if(!FileUtil.doesStubsDirectoryExist()) {
			getLogger().info("Stubs directory does not exist.");
			getLogger().info("Creating stubs directory.");
			FileUtil.createStubsDirectory();
		}
		else{
			getLogger().info("Located stubs directory.");
		}
		if(!FileUtil.doesConfigDirectoryExist()) {
			getLogger().info("Configuration directory not found....");
			getLogger().info("Creating configuration directory...");
			FileUtil.createConfigDirectory();
		}
		else{
			getLogger().info("Located configuration directory.");
		}
	}

	@Override
	protected Logger getLogger() {
		return LogManager.getLogger();
	}
}
