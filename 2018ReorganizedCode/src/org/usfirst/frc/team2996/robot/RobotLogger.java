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
	boolean loggingActive;
	boolean enabled;
	boolean titleRun = false;

	
	
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
		System.out.println("Logging Started");
		SmartDashboard.putBoolean("logging", true);
	}

	public void halt() {
		System.out.println("Logging Halted");
		running = false;
	}

	public void run() {
		while (running) {
			loggingActive = SmartDashboard.getBoolean("logging", false);
			enabled = robot.isEnabled();
			if (loggingActive && enabled) {
				try {
					if (logging == null) {
						logging = createLogger();
						writeHeader();
					}
					// PIDlog();
					mainLog();
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
		//Titles
//		StringBuilder types = new StringBuilder();
//		// Writes Values that are eventually used by Excel Logging Code
//		types.append(DELI).append(findType());
//		types.append(DELI).append(findType(FrontRightMotor));
//		types.append(DELI).append(findType(FrontLeftMotor));
//		types.append(DELI).append(findType(FrontLeftMotor));
//		
//		logging.fine(types.toString());
	}

	public void mainLog() throws Throwable {
		//Drive
		double FrontLeftMotor = robot.getDrive().getFrontLeftSensors().getQuadraturePosition();
		double FrontRightMotor = robot.getDrive().getFrontRightSensors().getQuadraturePosition();
		//Elevator
		boolean ForwardLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isFwdLimitSwitchClosed();
		boolean ReverseLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isRevLimitSwitchClosed();
		String ElevatorState = robot.getElevator().currentElevatorState.toString();
		//Arm
		String ArmState = robot.getArm().currentArmState.toString();
		//Intake
		double rightIntakeMotor = robot.getIntake().getRightIntakeMotor().get();
		double leftIntakeMotor = robot.getIntake().getLeftIntakeMotor().get();
		
		if(titleRun =! true) {
			titleRun = true;
			StringBuilder types = new StringBuilder();
			//Types
			
			//Drive
			types.append(DELI).append(findType(FrontLeftMotor));
			types.append(DELI).append(findType(FrontRightMotor));
			//Elevator
			types.append(DELI).append(findType(ForwardLimitSwitch));
			types.append(DELI).append(findType(ReverseLimitSwitch));
			types.append(DELI).append(findType(ElevatorState));
			//Arm
			types.append(DELI).append(findType(ArmState));
			//Intake
			types.append(DELI).append(findType(rightIntakeMotor));
			types.append(DELI).append(findType(leftIntakeMotor));
			
			logging.fine(types.toString());
		}
		StringBuilder sb = new StringBuilder();
		// Writing everything to Logger
		if (robot.getDrive() != null) {
			// Drive
			sb.append(DELI).append(FrontLeftMotor);
			sb.append(DELI).append(FrontRightMotor);
			//Elevator
			sb.append(DELI).append(ForwardLimitSwitch);
			sb.append(DELI).append(ReverseLimitSwitch);
			sb.append(DELI).append(ElevatorState);
			//Arm
			sb.append(DELI).append(ArmState);
			//Intake
			sb.append(DELI).append(rightIntakeMotor);
			sb.append(DELI).append(leftIntakeMotor);
			
//			//No null logger
//			// Drive
//			if (robot.getDrive().getFrontLeftSensors() != null) {
//
//			} else {
//				//This is the default value if something goes wrong
//				sb.append(DELI).append(" ");
//			}
//			if (robot.getDrive().getFrontRightSensors() != null) {
//
//			} else {
//				sb.append(DELI).append(" ");
//			}
//			
//			// Elevator
//			if (robot.getElevator().getElevatorMasterMotorSensors() != null) {
//
//			} else {
//				sb.append(DELI).append(" ");
//			}
//			if (robot.getElevator().getElevatorMasterMotorSensors() != null) {
//
//			} else {
//				sb.append(DELI).append(" ");
//			}
//			if (ElevatorState != null) {
//
//			} else {
//				sb.append(DELI).append(" ");
//			}
//			
//			// Arm
//			if (ArmState != null) {
//
//			} else {
//				sb.append(DELI).append(" ");
//			}
//			
//			//Intake
//			if(rightIntakeMotor != null) {
//				
//			}
			
		}
		logging.fine(sb.toString());
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
