package net.andrewcpu.payroll.gui;

import net.andrewcpu.payroll.dao.impl.day.PayrollDayDAO;
import net.andrewcpu.payroll.dao.impl.stub.PayrollStubDAO;
import net.andrewcpu.payroll.dao.impl.stub.crud.LocatePayrollStubDAO;
import net.andrewcpu.payroll.dao.impl.week.FinalizePayrollDAO;
import net.andrewcpu.payroll.model.PayrollDayModel;
import net.andrewcpu.payroll.util.DateUtil;
import net.andrewcpu.payroll.util.FileUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PayrollGUI extends JFrame {

	private MenuItem clockIn = new MenuItem("Clock in");
	private MenuItem clockOut = new MenuItem("Clock out");
	private MenuItem clockOutAndEndDay = new MenuItem("Clock out and end day");
	private MenuItem endDay = new MenuItem("End day");
	private MenuItem reopenDay = new MenuItem("Reopen day");
	private Menu open = new Menu("Open");
	private MenuItem payrollDirectory = new MenuItem("Payroll Directory");
	private MenuItem dayDirectory = new MenuItem("Day Directory");
	private MenuItem stubDirectory = new MenuItem("Stub Directory");
	private MenuItem exitItem = new MenuItem("Exit");
	private MenuItem finalizeWeek = new MenuItem("Finalize week");
	private MenuItem currentDuration = new MenuItem("");
	final TrayIcon trayIcon =
			new TrayIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("icon.png")), "Payroll");
	final PopupMenu popup = new PopupMenu();

	public PayrollGUI() throws IOException {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();
		open.add(payrollDirectory);
		open.add(dayDirectory);
		open.add(stubDirectory);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
		setupEventListeners();
		setState();
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			setState();
		}, 30, 30, TimeUnit.SECONDS);
	}

	public void setState() {
		popup.removeAll();

		MenuItem menuItem = new MenuItem("Payroll Tracking");
		menuItem.setEnabled(false);

		popup.add(menuItem);

		if(PayrollStubDAO.getInstance().isClockedIn()) {
			popup.add(currentDuration);
			double start = LocatePayrollStubDAO.getInstance().getCurrentStub().getStartTime().getTime();
			double now = System.currentTimeMillis();
			double diff = (now - start) / 1000.0 / 60.0 / 60.0;
			currentDuration.setLabel("Clocked in for: " + new DecimalFormat("0.00").format(diff) + "hrs");
			currentDuration.setEnabled(false);
			popup.addSeparator();
		}
		if(PayrollDayDAO.getInstance().isCurrentDayOpen()){
			if(PayrollStubDAO.getInstance().isClockedIn()) {
				popup.add(clockOut);
				popup.addSeparator();
				popup.add(clockOutAndEndDay);
			}
			else{
				popup.add(clockIn);
			}
			popup.addSeparator();
			popup.add(endDay);
		}
		else{
			popup.add(reopenDay);
			popup.addSeparator();
			popup.add(finalizeWeek);
		}

		popup.addSeparator();
		popup.add(open);
		popup.addSeparator();
		popup.add(exitItem);
	}

	private void setupEventListeners() {
		clockIn.addActionListener(e -> {
			PayrollStubDAO.getInstance().clockIn();
			setState();
		});
		clockOut.addActionListener(e -> {
			PayrollStubDAO.getInstance().clockOut();
			setState();
		});
		clockOutAndEndDay.addActionListener(e -> {
			PayrollStubDAO.getInstance().clockOut();
			PayrollDayModel day = PayrollDayDAO.getInstance().endDay();
			JOptionPane.showMessageDialog(null,"Successfully logged " + day.getTotalHoursWorked() +  " hours for " + DateUtil.getDateFormat().format(new Date()), "Payroll", JOptionPane.PLAIN_MESSAGE);
			setState();
		});
		endDay.addActionListener(e -> {
			PayrollDayModel day = PayrollDayDAO.getInstance().endDay();
			JOptionPane.showMessageDialog(null,"Successfully logged " + day.getTotalHoursWorked() +  " hours for " + DateUtil.getDateFormat().format(new Date()), "Payroll", JOptionPane.PLAIN_MESSAGE);
			setState();
		});

		reopenDay.addActionListener(e -> {
			PayrollDayDAO.getInstance().reopen();
			setState();
		});

		exitItem.addActionListener(e -> System.exit(0));
		payrollDirectory.addActionListener(e -> openDirectory(FileUtil.getPayrollDirectory().toFile()));
		stubDirectory.addActionListener(e -> openDirectory(FileUtil.getStubsDirectory().toFile()));
		dayDirectory.addActionListener(e -> openDirectory(FileUtil.getDaysDirectory().toFile()));
		finalizeWeek.addActionListener(e -> {
			try {
				Path result = FinalizePayrollDAO.getInstance().finalizeWeek();
				result = result.getParent();
				openDirectory(result.toFile());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	private void openDirectory(File file) {
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
