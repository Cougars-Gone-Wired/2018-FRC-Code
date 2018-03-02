/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2996.robot;

import java.util.List;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

	// declarations of objects for each class with methods that need to be called in
	// this class
	private Joysticks joysticks;

	private Elevator elevator;
	private Intake intake;
	private Arm arm;

	private Drive drive;

	private ChangeGear elevatorChangeGear;
	private ChangeGear driveChangeGear;

	private AutoMethods autoMethods;
	private AutoPrograms autoPrograms;

//	private StateRecorder recorder;
//	private StateRunner runner;
	
	private RobotLogger robotLogger;

	private static final String MIDDLE = "Middle"; // string that are the values to chose from on the SmartDashboard
	private static final String LEFT = "Left";
	private static final String RIGHT = "Right";

	private static final String SWITCH = "Switch";
	private static final String SCALE = "Scale";
	private static final String EXCHANGE = "Exchange";
	private static final String CROSS_LINE = "Cross Line";

	private static final String LLL = "LLL";
	private static final String RRR = "RRR";
	private static final String LRL = "LRL";
	private static final String RLR = "RLR";

	private SendableChooser<String> position = new SendableChooser<>();
	private SendableChooser<String> priority = new SendableChooser<>();

	String currentPosition; // these get the values chosen on the SmartDashboard
	String currentPriority;
	String fieldConfiguration;

	public enum autoStates {
		LEFT_SCALE, LEFT_SWITCH, LEFT_EXCHANGE, LEFT_CROSS_LINE,
		MIDDLE_SWITCH_LEFT, MIDDLE_SWITCH_RIGHT, MIDDLE_EXCHANGE, MIDDLE_CROSS_LINE, 
		RIGHT_SCALE, RIGHT_SWITCH, RIGHT_CROSS_LINE,
		TURN_LEFT, TURN_RIGHT, TURN
	}

	public autoStates autoChanger;
//	private StateRecorder recorder;
//	private StateRunner runner;

	// Constants constants;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		// instantiations of all previously declared objects
		joysticks = new Joysticks();

		elevator = new Elevator();
		intake = new Intake();
		arm = new Arm();

		drive = new Drive();

		elevatorChangeGear = new ChangeGear(true, elevator.getChangeElevatorGearSolenoid());
		driveChangeGear = new ChangeGear(false, drive.getChangeDriveGearSolenoid());

		// objects that need to be instantiated at the end of robotInit because they use
		// other objects in this class
		autoMethods = new AutoMethods(this);
		autoPrograms = new AutoPrograms(this);

//		recorder = new StateRecorder(this);
//		runner = new StateRunner(this);

		// constants = new Constants();
//		if(robotLogger != null){
//			robotLogger.halt();
//		}
//
//		robotLogger = new RobotLogger(this);
//		new Thread(robotLogger).start();

		// static methods that need to be called in robotInit
		SmartDashboardSettings.displaySettings(); // put things on the SmartDashboard

		position.addDefault("Middle", MIDDLE); // sends auto inputs to the chooser
		position.addObject("Left", LEFT);
		position.addObject("Right", RIGHT);

		priority.addObject("Switch", SWITCH);
		priority.addObject("Scale", SCALE);
		priority.addObject("Exchange", EXCHANGE);
		priority.addDefault("Cross Line", CROSS_LINE);

		SmartDashboard.putData("Robot Position", position); // sends choosers to the SmartDashboard
//		SmartDashboard.putData("Auto Priority", priority);

		autoPrograms.putAutoNumbers();
		
		//CameraServer camera = CameraServer.getInstance();
//		camera.startAutomaticCapture();
		//UsbCamera usbCam1 = camera.startAutomaticCapture("usb1", 0);
		//usbCam1.setResolution(120, 80);
//		VideoMode mode1 = usbCam1.getVideoMode();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		elevatorChangeGear.setGearState(true);
		driveChangeGear.setGearState(true);
		autoPrograms.setAutoNumbers();
//		autoMethods.getInfo(); // get the field color configuration
		autoPrograms.autoReset();
		autoPrograms.turningStage2 = false;
		autoPrograms.startDelayTimer(); // start timer to possibly be used to delay auto

		autoPrograms.resetStates();
//		runner.counterInitialize(); // set counter to 0
//		try {
//			// read the gson file for the selected gson auto
//			List<State> states = StateReader.read(StateLister.gsonChooser.getSelected());
//			runner.setStates(states); // get all the states from the gson file to be used in auto
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		currentPosition = position.getSelected(); // these get the values chosen on the SmartDashboard
//		currentPriority = priority.getSelected();
		fieldConfiguration = DriverStation.getInstance().getGameSpecificMessage();
		System.out.println(fieldConfiguration);
		
		SmartDashboard.putString("Current Position", currentPosition);
		SmartDashboard.putNumber("Current Position Length", currentPosition.length());
		SmartDashboard.putString("Field Config", fieldConfiguration);
		SmartDashboard.putNumber("Field Config Length", fieldConfiguration.length());
//		switch (currentPosition) {
//		case MIDDLE:
//			switch (currentPriority) {
//			case SCALE: //Middle position, Scale or Switch priority
//			case SWITCH:
//				switch (fieldConfiguration) {
//				case LLL:
//					autoChanger = autoStates.LEFT_SWITCH;
//					break;
//				case RRR:
//					autoChanger = autoStates.RIGHT_SWITCH;
//					break;
//				case LRL:
//					autoChanger = autoStates.LEFT_SWITCH;
//					break;
//				case RLR:
//					autoChanger = autoStates.RIGHT_SWITCH;
//					break;
//				default:
//					autoChanger = autoStates.MIDDLE_CROSS_LINE;
//					break;
//				}
//				break;
//			case EXCHANGE: //Middle position, Exchange priority
//				autoChanger = autoStates.MIDDLE_EXCHANGE;
//				break;
//			case CROSS_LINE:
//			default: //Middle position, unknown priority
//				autoChanger = autoStates.MIDDLE_CROSS_LINE;
//				break;
//			}
//			break;
//		case RIGHT:
//			switch (currentPriority) {
//			case SCALE: //Right position, Scale priority
//				switch (fieldConfiguration) {
//				case LLL:
//					autoChanger = autoStates.RIGHT_CROSS_LINE;
//					break;
//				case RRR:
//					autoChanger = autoStates.RIGHT_SCALE;
//					break;
//				case LRL:
//					autoChanger = autoStates.RIGHT_SCALE;
//					break;
//				case RLR:
//					autoChanger = autoStates.RIGHT_SWITCH;
//					break;
//				default:
//					autoChanger = autoStates.RIGHT_CROSS_LINE;
//					break;
//				}
//				break;
//			case SWITCH: //Right position, Switch priority 
//				switch (fieldConfiguration) {
//				case LLL:
//					autoChanger = autoStates.RIGHT_CROSS_LINE;
//					break;
//				case RRR:
//					autoChanger = autoStates.RIGHT_SWITCH;
//					break;
//				case LRL:
//					autoChanger = autoStates.RIGHT_SCALE;
//					break;
//				case RLR:
//					autoChanger = autoStates.RIGHT_SWITCH;
//					break;
//				default:
//					autoChanger = autoStates.RIGHT_CROSS_LINE;
//					break;
//				}
//				break;
//			case EXCHANGE: //Right position, Exchange or unknown priority
//			case CROSS_LINE:
//			default:
//				autoChanger = autoStates.RIGHT_CROSS_LINE;
//				break;
//			}
//			break;
//		case LEFT:
//			switch (currentPriority) {
//			case SCALE: //Left position, Scale priority 
//				switch (fieldConfiguration) {
//				case LLL:
//					autoChanger = autoStates.LEFT_SCALE;
//					break;
//				case RRR:
//					autoChanger = autoStates.LEFT_EXCHANGE;
//					break;
//				case LRL:
//					autoChanger = autoStates.LEFT_SWITCH;
//					break;
//				case RLR:
//					autoChanger = autoStates.LEFT_SCALE;
//					break;
//				default:
//					autoChanger = autoStates.LEFT_CROSS_LINE;
//					break;
//				}
//				break;
//			case SWITCH: //Left position, Switch priority 
//				switch (fieldConfiguration) {
//				case LLL:
//					autoChanger = autoStates.LEFT_SWITCH;
//					break;
//				case RRR:
//					autoChanger = autoStates.LEFT_EXCHANGE;
//					break;
//				case LRL:
//					autoChanger = autoStates.LEFT_SWITCH;
//					break;
//				case RLR:
//					autoChanger = autoStates.LEFT_SCALE;
//					break;
//				default:
//					autoChanger = autoStates.LEFT_CROSS_LINE;
//					break;
//				}
//				break;
//			case EXCHANGE: //Left position, Exchange priority
//				autoChanger = autoStates.LEFT_EXCHANGE;
//				break;
//			case CROSS_LINE:
//			default: //Left position, unknown priority
//				autoChanger = autoStates.LEFT_CROSS_LINE;
//				break;
//			}
//			break;
//		default: //Unknown position
//			autoChanger = autoStates.MIDDLE_CROSS_LINE;
//			break;
//		}
		
		switch (currentPosition) {
		case MIDDLE:
			System.out.println("middle");
			switch (fieldConfiguration) {
			case LLL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			case RRR:
				autoChanger = autoStates.MIDDLE_SWITCH_RIGHT;
				break;
			case LRL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			case RLR:
				autoChanger = autoStates.MIDDLE_SWITCH_RIGHT;
				break;
			default:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			}
		case RIGHT:
			System.out.println("right");
			switch (fieldConfiguration) {
			case LLL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
//				autoPrograms.setMiddleCrossLineForwardDistance(100);
				break;
			case RRR:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			case LRL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
//				autoPrograms.setMiddleCrossLineForwardDistance(100);
				break;
			case RLR:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			default:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			}
		case LEFT:
			System.out.println("left");
			switch (fieldConfiguration) {
			case LLL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
//				autoPrograms.setMiddleCrossLineForwardDistance(100);
				break;
			case RRR:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			case LRL:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
//				autoPrograms.setMiddleCrossLineForwardDistance(100);
				break;
			case RLR:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			default:
				autoChanger = autoStates.MIDDLE_CROSS_LINE;
				break;
			}
		default: //Unknown position
			System.out.println("default");
			autoChanger = autoStates.MIDDLE_CROSS_LINE;
			autoPrograms.setMiddleCrossLineForwardDistance(40);
			break;
		}
		autoChanger = autoStates.MIDDLE_SWITCH_RIGHT; // temporary testing for specific auto programs
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
//		if (SmartDashboardSettings.useDeadReckoningAuto) {
//			autoMethods.pickAuto(); // run selected dead reckoning auto
//		} else if (SmartDashboardSettings.useRecorderAuto) {
//			runner.run(); // run selected gson auto
//		}
		autoPrograms.setEncoderValues();
		autoPrograms.showEncoderValues();
		switch(autoChanger) {
		case LEFT_SCALE:
			autoPrograms.leftScale();
			break;
		case LEFT_SWITCH:
			autoPrograms.leftSwitch();
			break;
		case LEFT_EXCHANGE:
			autoPrograms.leftExchange();
			break;
		case LEFT_CROSS_LINE:
			autoPrograms.leftCrossLine();
			break;
		case MIDDLE_SWITCH_LEFT:
			autoPrograms.middleSwitchLeft();
			break;
		case MIDDLE_SWITCH_RIGHT:
			autoPrograms.middleSwitchRight();
			break;
		case MIDDLE_EXCHANGE:
			autoPrograms.middleExchange();
			break;
		case MIDDLE_CROSS_LINE:
			autoPrograms.middleCrossLine();
			break;
		case RIGHT_SCALE:
			autoPrograms.rightScale();
			break;
		case RIGHT_SWITCH:
			autoPrograms.rightSwitch();
			break;
		case RIGHT_CROSS_LINE:
			autoPrograms.rightCrossLine();
			break;
		case TURN_LEFT:
			autoPrograms.leftTurn();
			break;
		case TURN_RIGHT:
			autoPrograms.rightTurn();
			break;
		case TURN:
			autoPrograms.turningTest();
			break;
		}
	}
	

	@Override
	public void teleopInit() {
		elevatorChangeGear.setGearState(true);
		autoMethods.getNavX().reset();
		
		intake.getLeftIntakeMotor().set(0);
		intake.getRightIntakeMotor().set(0);
//		recorder.initialize(); // create new array list
//		runner.counterInitialize(); // set counter to 0
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		SmartDashboard.putNumber("Teleop Gyro", autoMethods.getNavX().getAngle());
		
		joysticks.setJoystickInputValues(); // checks all values being inputted into the controllers

		elevator.elevatorFunctions(joysticks.getElevatorAxis()); // run the elevator
		intake.intakeFunctions(joysticks.getIntakeTrigger(), joysticks.getOuttakeTrigger()); // run the intake
		arm.armFunctions(joysticks.isArmButtonOutput()); // run the arm

//		if (SmartDashboardSettings.tankDrive) {
//			drive.tankDrive(joysticks.getLeftDriveAxis(), joysticks.getRightDriveAxis());
//		} else {
			drive.arcadeDrive(joysticks.getDriveForwardAxis(), joysticks.getDriveTurnAxis()); // run the drive train
//		}
		
//		SmartDashboard.putBoolean("Elavator Solenoid", elevator.getChangeElevatorGearSolenoid().get());
		elevatorChangeGear.changeGear(joysticks.isElevatorHighGearButton(), joysticks.isElevatorLowGearButton()); // method for changing gears on the elevator
		driveChangeGear.changeGear(joysticks.isDriveLowGearButton(), joysticks.isDriveHighGearButton()); // method for changing gears on the drive train
		
//		robotLogger.run();
//		recorder.record(); // record the states of the motors and solenoids every 20 milliseconds
	}

	@Override
	public void disabledInit() {
//		if (SmartDashboardSettings.shouldRecord) {
//			List<State> states = recorder.getStates(); // get the states recorded in teleop
//			try {
//				// write the states to a gson file
//				StatesWriter.writeStates(states, SmartDashboardSettings.gsonFileName);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

//		StateLister.getStateNames(); // list all available gson files on the smartDahsboard
	}

	@Override
	public void disabledPeriodic() {
		SmartDashboardSettings.setConstantVars();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	// getters for all the objects declared in this class
	public Joysticks getJoysticks() {
		return joysticks;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public Intake getIntake() {
		return intake;
	}

	public Arm getArm() {
		return arm;
	}

	public Drive getDrive() {
		return drive;
	}

	public ChangeGear getElevatorChangeGear() {
		return elevatorChangeGear;
	}

	public ChangeGear getDriveChangeGear() {
		return driveChangeGear;
	}

//	public StateRecorder getRecorder() {
//		return recorder;
//	}
//
//	public StateRunner getRunner() {
//		return runner;
//	}

	public autoStates getAutoChanger() {
		return autoChanger;
	}

}
