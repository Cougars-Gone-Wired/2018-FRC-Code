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
	
	//Drive
	double FrontLeftMotor = robot.getDrive().getFrontLeftSensors().getQuadraturePosition();
	double FrontRightMotor = robot.getDrive().getFrontRightSensors().getQuadraturePosition();
	
	//Elevator
	boolean ForwardLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isFwdLimitSwitchClosed();
	boolean ReverseLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isRevLimitSwitchClosed();
	String ElevatorState = robot.getElevator().currentElevatorState.toString();
	boolean ElevatorHighGear = robot.getJoysticks().isElevatorHighGearButton();
	
	//Arm
	String ArmState = robot.getArm().currentArmState.toString();
	
	//Intake
	double rightIntakeMotor = robot.getIntake().getRightIntakeMotor().get();
	double leftIntakeMotor = robot.getIntake().getLeftIntakeMotor().get();
	
	//Manipulator
	boolean ArmButton = robot.getJoysticks().getManipulatorStick().getRawButton(1);
//	boolean buttonB = robot.getJoysticks().getManipulatorStick().getRawButton(2);
//	boolean buttonX = robot.getJoysticks().getManipulatorStick().getRawButton(3);
//	boolean buttonY = robot.getJoysticks().getManipulatorStick().getRawButton(4);
	boolean ElevatorLowGearButton = robot.getJoysticks().getManipulatorStick().getRawButton(5);
	boolean ElevatorHighGearButton = robot.getJoysticks().getManipulatorStick().getRawButton(6);
//	double LeftJoystickX = robot.getJoysticks().getManipulatorStick().getRawAxis(0);
	double LiftAxis = robot.getJoysticks().getManipulatorStick().getRawAxis(1);
//	double RightJoystickX = robot.getJoysticks().getManipulatorStick().getRawAxis(4);
	double RightJoystickY = robot.getJoysticks().getManipulatorStick().getRawAxis(5);
	double IntakeTrigger = robot.getJoysticks().getManipulatorStick().getRawAxis(2);
	double OuttakeTrigger = robot.getJoysticks().getManipulatorStick().getRawAxis(3);
	
	//Mobility
//	boolean buttonA2 = robot.getJoysticks().getMobilityStick().getRawButton(1);
//	boolean buttonB2 = robot.getJoysticks().getMobilityStick().getRawButton(2);
//	boolean buttonX2 = robot.getJoysticks().getMobilityStick().getRawButton(3);
//	boolean buttonY2 = robot.getJoysticks().getMobilityStick().getRawButton(4);
	boolean DriveLowGearButton = robot.getJoysticks().getMobilityStick().getRawButton(5);
	boolean DriveHighGearButton = robot.getJoysticks().getMobilityStick().getRawButton(6);
//	double LeftJoystickX2 = robot.getJoysticks().getMobilityStick().getRawAxis(0);
	double DriveForwardAxis = robot.getJoysticks().getMobilityStick().getRawAxis(1);
	double DriveTurnAxis = robot.getJoysticks().getMobilityStick().getRawAxis(4);
//	double RightJoystickY2 = robot.getJoysticks().getMobilityStick().getRawAxis(5);
//	double LeftTrigger2 = robot.getJoysticks().getMobilityStick().getRawAxis(2);
//	double RightTrigger2 = robot.getJoysticks().getMobilityStick().getRawAxis(3);

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
			StringBuilder names = new StringBuilder();
			StringBuilder types = new StringBuilder();
			
			//DRIVE
			names.append(DELI).append("FrontLeftMotor");
			types.append(DELI).append(findType(FrontLeftMotor));
			
			names.append(DELI).append("FrontRightMotor");
			types.append(DELI).append(findType(FrontRightMotor));
			
			//ELEVATOR
			names.append(DELI).append("ForwardLimitSwitch");
			types.append(DELI).append(findType(ForwardLimitSwitch));
			
			names.append(DELI).append("ReverseLimitSwitch");
			types.append(DELI).append(findType(ReverseLimitSwitch));
			
			names.append(DELI).append("ElevatorState");
			types.append(DELI).append(findType(ElevatorState));
			
			//ARM
			names.append(DELI).append("ArmState");
			types.append(DELI).append(findType(ArmState));
			
			//INTAKE
			names.append(DELI).append("rightIntakeMotor");
			types.append(DELI).append(findType(rightIntakeMotor));
			
			names.append(DELI).append("leftIntakeMotorMotor");
			types.append(DELI).append(findType(leftIntakeMotor));
			//MANIPULATOR
			names.append(DELI).append("ArmButton");
			types.append(DELI).append(findType(ArmButton));
			
//			names.append(DELI).append("ManipulatorButtonB");
//			types.append(DELI).append(findType(buttonB));
			
//			names.append(DELI).append("ManipulatorButtonX");
//			types.append(DELI).append(findType(buttonX));
			
//			names.append(DELI).append("ManipulatorButtonY");
//			types.append(DELI).append(findType(buttonY));
			
			names.append(DELI).append("ElevatorLowGearButton");
			types.append(DELI).append(findType(ElevatorLowGearButton));
			
			names.append(DELI).append("ElevatorHighGearButton");
			types.append(DELI).append(findType(ElevatorHighGearButton));
			
			//names.append(DELI).append("ManipulatorLeftJoystickX");
			//types.append(DELI).append(findType(LeftJoystickX));
			
			names.append(DELI).append("LiftAxis");
			types.append(DELI).append(findType(LiftAxis));
			
//			names.append(DELI).append("ManipulatorRightJoystickX");
//			types.append(DELI).append(findType(RightJoystickX));
			
//			names.append(DELI).append("ManipulatorRightJoystickY");
//			types.append(DELI).append(findType(RightJoystickY));
			
			names.append(DELI).append("IntakeTrigger");
			types.append(DELI).append(findType(IntakeTrigger));
			
			names.append(DELI).append("OuttakeTrigger");
			types.append(DELI).append(findType(OuttakeTrigger));
			
			//MOBILITY
//			names.append(DELI).append("MobilityButtonA");
//			types.append(DELI).append(findType(buttonA2));
//			
//			names.append(DELI).append("MobilityButtonB");
//			types.append(DELI).append(findType(buttonB2));
//			
//			names.append(DELI).append("MobilityButtonX");
//			types.append(DELI).append(findType(buttonX2));
//			
//			names.append(DELI).append("MobilityButtonY");
//			types.append(DELI).append(findType(buttonY2));
			
			names.append(DELI).append("MobilityLeftButton");
			types.append(DELI).append(findType(DriveLowGearButton));
			
			names.append(DELI).append("MobilityFrontRightMotor");
			types.append(DELI).append(findType(DriveHighGearButton));
			
			//names.append(DELI).append("MobilityLeftJoystickX");
			//types.append(DELI).append(findType(LeftJoystickX));
			
			names.append(DELI).append("DriveForwardAxis");
			types.append(DELI).append(findType(DriveForwardAxis));
			
			names.append(DELI).append("DriveTurnAxis");
			types.append(DELI).append(findType(DriveTurnAxis));
			
//			names.append(DELI).append("MobilityRightJoystickY");
//			types.append(DELI).append(findType(RightJoystickY2));
//			
//			names.append(DELI).append("MobilityLeftTrigger");
//			types.append(DELI).append(findType(LeftTrigger2));
//			
//			names.append(DELI).append("MobilityRightTrigger");
//			types.append(DELI).append(findType(RightTrigger2));
			logging.fine(names.toString());
			logging.fine(types.toString());
	}

	public void mainLog() throws Throwable {
		//Drive
		FrontLeftMotor = robot.getDrive().getFrontLeftSensors().getQuadraturePosition();
		FrontRightMotor = robot.getDrive().getFrontRightSensors().getQuadraturePosition();
		//Elevator
		ForwardLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isFwdLimitSwitchClosed();
		ReverseLimitSwitch = robot.getElevator().getElevatorMasterMotorSensors().isRevLimitSwitchClosed();
		ElevatorState = robot.getElevator().currentElevatorState.toString();
		//Arm
		ArmState = robot.getArm().currentArmState.toString();
		//Intake
		rightIntakeMotor = robot.getIntake().getRightIntakeMotor().get();
		leftIntakeMotor = robot.getIntake().getLeftIntakeMotor().get();
		//Manipulator
		ArmButton = robot.getJoysticks().getManipulatorStick().getRawButton(1);
//		buttonB = robot.getJoysticks().getManipulatorStick().getRawButton(2);
//		buttonX = robot.getJoysticks().getManipulatorStick().getRawButton(3);
//		buttonY = robot.getJoysticks().getManipulatorStick().getRawButton(4);
		ElevatorLowGearButton = robot.getJoysticks().getManipulatorStick().getRawButton(5);
		ElevatorHighGearButton = robot.getJoysticks().getManipulatorStick().getRawButton(6);
		LiftAxis = robot.getJoysticks().getManipulatorStick().getRawAxis(1);
//		RightJoystickX = robot.getJoysticks().getManipulatorStick().getRawAxis(4);
//		RightJoystickY = robot.getJoysticks().getManipulatorStick().getRawAxis(5);
		IntakeTrigger = robot.getJoysticks().getManipulatorStick().getRawAxis(2);
		OuttakeTrigger = robot.getJoysticks().getManipulatorStick().getRawAxis(3);
		//Mobility
//		buttonA2 = robot.getJoysticks().getMobilityStick().getRawButton(1);
//		buttonB2 = robot.getJoysticks().getMobilityStick().getRawButton(2);
//		buttonX2 = robot.getJoysticks().getMobilityStick().getRawButton(3);
//		buttonY2 = robot.getJoysticks().getMobilityStick().getRawButton(4);
		DriveLowGearButton = robot.getJoysticks().getMobilityStick().getRawButton(5);
		DriveHighGearButton = robot.getJoysticks().getMobilityStick().getRawButton(6);
//		LeftJoystickX2 = robot.getJoysticks().getMobilityStick().getRawAxis(0);
		DriveForwardAxis = robot.getJoysticks().getMobilityStick().getRawAxis(1);
		DriveTurnAxis = robot.getJoysticks().getMobilityStick().getRawAxis(4);
//		RightJoystickY2 = robot.getJoysticks().getMobilityStick().getRawAxis(5);
//		LeftTrigger2 = robot.getJoysticks().getMobilityStick().getRawAxis(2);
//		RightTrigger2 = robot.getJoysticks().getMobilityStick().getRawAxis(3);

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
			//Manipulator
			sb.append(DELI).append(ArmButton);
//			sb.append(DELI).append(buttonB);
//			sb.append(DELI).append(buttonX);
//			sb.append(DELI).append(buttonY);
			sb.append(DELI).append(ElevatorLowGearButton);
			sb.append(DELI).append(ElevatorHighGearButton);
			//sb.append(DELI).append(LeftJoystickX);
			sb.append(DELI).append(LiftAxis);
//			sb.append(DELI).append(RightJoystickX);
//			sb.append(DELI).append(RightJoystickY);
			sb.append(DELI).append(IntakeTrigger);
			sb.append(DELI).append(OuttakeTrigger);
			//Mobility
//			sb.append(DELI).append(buttonA2);
//			sb.append(DELI).append(buttonB2);
//			sb.append(DELI).append(buttonX2);
//			sb.append(DELI).append(buttonY2);
			sb.append(DELI).append(DriveLowGearButton);
			sb.append(DELI).append(DriveHighGearButton);
//			sb.append(DELI).append(LeftJoystickX2);
			sb.append(DELI).append(DriveForwardAxis);
			sb.append(DELI).append(DriveTurnAxis);
//			sb.append(DELI).append(RightJoystickY2);
//			sb.append(DELI).append(LeftTrigger2);
//			sb.append(DELI).append(RightTrigger2);
			
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