package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger extends Object implements Runnable {
	public static final String DELI = ", ";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

	Robot robot;

	private volatile boolean running = false;
	boolean autonomousState = false;
	boolean teleopState = false;
	boolean shooterState = false;
	Logger logging;

	public Logger createLogger() throws SecurityException, IOException {
		Logger logger = Logger.getLogger(RobotLogger.class.getName());
		logger.setUseParentHandlers(false);
		Level level = Level.FINE;
		logger.setLevel(level);

		File file = new File("/home/lvuser/" + "RobotLog" + dateFormat.format(new Date()) + ".csv");
		FileHandler fh = new FileHandler(file.getAbsolutePath());
		fh.setFormatter(new RobotLoggerFormatter());
		logger.addHandler(fh);

		// File file = new File(name + format.format(new Date()) + ".csv");
		System.out.println(file.getAbsolutePath());
		fh.setFormatter(new RobotLoggerFormatter());
		logger.addHandler(fh);
		return logger;
	}

	RobotLogger(Robot robot) {
		this.robot = robot;
		running = true;
	}

	public void halt() {
		System.out.println("Logging Halted");
		running = false;
	}

	public void run() {
		System.out.println("Logging Started");
		while (running) {
			boolean loggingActive = SmartDashboard.getBoolean("logging", false);
			boolean enabled = robot.isEnabled();
			if (loggingActive && enabled) {
				try {
					if (logging == null) {
						logging = createLogger();
						writeHeader();
					}
					// PIDlog();
					movementLog();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

			} else {
				autonomousState = false;
				teleopState = false;
			}
			// 20 milliseconds
			Timer.delay(0.02);
		}
	}

	public void writeHeader() throws Throwable {

		logging.fine(", FrontLeftMotor, FrontRightMotor, RearLeftMotor, RearRightMotor");
		logging.fine(", Double, Double, Double, Double");
	}

	public void movementLog() throws Throwable {
		StringBuilder sb = new StringBuilder();
		robot.getDrive().getFrontLeftMotor();
		if (robot.getDrive() != null) {
			if (robot.getDrive().getFrontLeftSensors() != null) {
				sb.append(DELI).append(robot.getDrive().getFrontLeftSensors().getQuadraturePosition());
			} else {
				sb.append(DELI).append("1324");
			}
			if (robot.getDrive().getFrontRightSensors() != null) {
				sb.append(DELI).append(robot.getDrive().getFrontRightSensors().getQuadraturePosition());
			} else {
				sb.append(DELI).append("1324");
			}
		}
		// if (robot.isAutonomous()) {
		// if (!autonomousState) {
		// logging.fine("Auto Begin");
		// autonomousState = true;
		// }
		//
		// if (autonomousState) {
		// logging.fine("");
		// }
		//
		// } else {
		// autonomousState = false;
		// }
		//
		// if (robot.isOperatorControl()) {
		// if (!teleopState) {
		// logging.fine("Teleop Begin");
		// teleopState = true;
		// }
		//
		// if (teleopState) {
		// logging.fine("");
		// }
		//
		// } else {
		// teleopState = false;
		// }
		logging.fine(sb.toString());
	}

	public static String findType(String s) {
		return "String";
	}

	public static String findType(int s) {
		return "Integer";
	}

	public static String findType(float s) {
		return "Float";
	}

	public static String findType(double s) {
		return "Double";
	}

	public static String findType(boolean s) {
		return "Boolean";
	}

	public static String findType(char s) {
		return "Char";
	}
}
