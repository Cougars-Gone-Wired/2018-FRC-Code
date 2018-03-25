package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoProgramsRevised {
		
	private static final String MIDDLE = "Middle";
	private static final String LEFT = "Left";
	private static final String RIGHT = "Right";

	private static final String SWITCH = "Switch";
	private static final String SCALE = "Scale";

	private static final String LLL = "LLL";
	private static final String RRR = "RRR";
	private static final String LRL = "LRL";
	private static final String RLR = "RLR";
	
	private SendableChooser<String> position = new SendableChooser<>();
	private SendableChooser<String> priority = new SendableChooser<>();
	
	String currentPosition;
	String currentPriority;
	String fieldConfiguration;
	
	public enum AutoStates {
		MIDDLE_CROSS_LINE, MIDDLE_SWITCH_RIGHT, MIDDLE_SWITCH_LEFT,
		LEFT_CROSS_LINE, LEFT_SWITCH, LEFT_SCALE_LEFT, LEFT_SCALE_RIGHT,
		RIGHT_CROSS_LINE, RIGHT_SWITCH, RIGHT_SCALE_RIGHT, RIGHT_SCALE_LEFT,
		LEFT_TURN, RIGHT_TURN,
		ELEVATOR,
		INTAKE_CHECK,
		DO_NOTHING
	}
	
	public AutoStates autoChanger;
	
	static final int WHEEL_DIAMETER = 6; // in inches
	static final int ENCODER_TICKS_PER_REV = 360 * 4;
	static final double DISTANCE_PER_ENCODER_TICK = ((WHEEL_DIAMETER * Math.PI) / ENCODER_TICKS_PER_REV); // in inches
	
	static final double GYRO_CONSTANT = 0.03;
	
	static final double TURNING_GYRO_OFFSET = 70;//good
	static final double TURNING_GYRO_OFFSET2 = 10;//good
	static final double TURNING_GYRO_OFFSET3 = 0;//good
	
	private DifferentialDrive robotDrive;

	private SensorCollection frontLeftSensors;
	private SensorCollection frontRightSensors;
	
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;
	
	private Intake intake;
	private Arm arm;
	private Elevator elevator;
	
	private AHRS navX;
	
	private Timer delayTimer;
	
	int frontLeftEncoder;
	int frontRightEncoder;
	int encoderAverage;
	double encoderAverageInches;
	
	private double turnAngle = 90;
	private boolean doneTurning = false;
	
	private int pauseCounter = 0;
	private int turnPause = 40;
	private int cubePause = 60;
	private int outtakePause = 100;
	private int outtakeDropArmPause = 100;
	private int elevatorPause = 100;
	private int drivePause = 40;
	
	private int turnLoopCounter = 0;
	private int turnLoopLimit = 40;
	
	private double autoDelay;
	
	private double autoDriveSpeed = Utility.invertDouble(1);
	private double autoTurnSpeedHigh = .75;
	private double autoTurnSpeedLow = .4;
	private double autoTurnSpeedBack = .2;
	
	private int autoElevatorDesiredTime = 20;
	
	//practice to comp +14
	static final double COMP_BOT_CONSTANT = 14; //14
	
	private double middleCrossLineForwardDistance = 81 + COMP_BOT_CONSTANT;
	
	private double middleSwitchRightForwardDistance = 81 + COMP_BOT_CONSTANT; //81
	
	private double middleSwitchRightBackForwardDistance = 81 + COMP_BOT_CONSTANT;
	private double middleSwitchRightBackBackwardDistance = -55 - COMP_BOT_CONSTANT;
	
	private double middleSwitchRight2CubeForwardDistance1 = 81 + COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeBackwardDistance1 = -57 - COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeForwardDistance2 = 42 + COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeForwardDistance3 = 20 + COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeBackwardDistance2 = -20 - COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeForwardDistance4 = 42 + COMP_BOT_CONSTANT;
	private double middleSwitchRight2CubeForwardDistance5 = 57 + COMP_BOT_CONSTANT;
	
	private double middleSwitchLeftForwardDistance1 = 21 + COMP_BOT_CONSTANT;
	private double middleSwitchLeftForwardDistance2 = 95 + COMP_BOT_CONSTANT;
	private double middleSwitchLeftForwardDistance3 = 55 + COMP_BOT_CONSTANT; 
	
	private double middleSwitchLeftBackForwardDistance1 = 21 + COMP_BOT_CONSTANT;
	private double middleSwitchLeftBackForwardDistance2 = 95 + COMP_BOT_CONSTANT;
	private double middleSwitchLeftBackForwardDistance3 = 55 + COMP_BOT_CONSTANT;
	private double middleSwitchLeftBackBackwardDistance = -55 - COMP_BOT_CONSTANT;
	
	private double middleSwitchLeft2CubeForwardDistance1 = 21 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance2 = 85 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance3 = 57 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeBackwardDistance1 = -57 - COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance4 = 42 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance5 = 20 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeBackwardDistance2 = -20 - COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance6 = 42 + COMP_BOT_CONSTANT;
	private double middleSwitchLeft2CubeForwardDistance7 = 57 + COMP_BOT_CONSTANT;
	
	private double leftCrossLineForwardDistance = 180 + COMP_BOT_CONSTANT;
	
	private double leftSwitchForwardDistance1 = 130 + COMP_BOT_CONSTANT;
	private double leftSwitchForwardDistance2 = 15 + COMP_BOT_CONSTANT;
	
	private double leftScaleLeftReadyForwardDistance = 230 + COMP_BOT_CONSTANT;
	
	private double leftScaleLeftForwardDistance1 = 280 + COMP_BOT_CONSTANT;
	private double leftScaleLeftForwardDistance2 = 17 + COMP_BOT_CONSTANT;
	private double leftScaleLeftForwardDistance3 = 15 + COMP_BOT_CONSTANT;
	
	private double leftScaleRightReadyForwardDistance1 = 185 + COMP_BOT_CONSTANT;
	private double leftScaleRightReadyForwardDistance2 = 190 + COMP_BOT_CONSTANT;
	
	private double leftScaleRightForwardDistance1 = 195 + COMP_BOT_CONSTANT;
	private double leftScaleRightForwardDistance2 = 180 + COMP_BOT_CONSTANT;
	private double leftScaleRightForwardDistance3 = 30 + COMP_BOT_CONSTANT;
	
	private double rightCrossLineForwardDistance = 180 + COMP_BOT_CONSTANT;
	
	private double rightSwitchForwardDistance1 = 130 + COMP_BOT_CONSTANT;
	private double rightSwitchForwardDistance2 = 15 + COMP_BOT_CONSTANT;
	
	private double rightScaleRightReadyForwardDistance = 230 + COMP_BOT_CONSTANT;
	
	private double rightScaleRightForwardDistance1 = 200 + COMP_BOT_CONSTANT;
	private double rightScaleRightForwardDistance2 = 40 + COMP_BOT_CONSTANT;
	private double rightScaleRightForwardDistance3 = 30 + COMP_BOT_CONSTANT;
	
	private double rightScaleLeftReadyForwardDistance1 = 185 + COMP_BOT_CONSTANT;
	private double rightScaleLeftReadyForwardDistance2 = 190 + COMP_BOT_CONSTANT;
	
	private double rightScaleLeftForwardDistance1 = 195 + COMP_BOT_CONSTANT;
	private double rightScaleLeftForwardDistance2 = 180 + COMP_BOT_CONSTANT;
	private double rightScaleLeftForwardDistance3 = 30 + COMP_BOT_CONSTANT;
	
	private double intakeCheckForwardDistance = 40 + COMP_BOT_CONSTANT;
	
	public AutoProgramsRevised(Robot robot) {
		position.addDefault("Middle", MIDDLE); // sends auto inputs to the chooser
		position.addObject("Left", LEFT);
		position.addObject("Right", RIGHT);

		priority.addObject("Switch", SWITCH);
		priority.addObject("Scale", SCALE);
		
		SmartDashboard.putData("Robot Position", position);
		SmartDashboard.putData("Auto Priority", priority);
		
		robotDrive = robot.getDrive().getRobotDrive();

		frontLeftSensors = robot.getDrive().getFrontLeftSensors();
		frontRightSensors = robot.getDrive().getFrontRightSensors();
		
		leftIntakeMotor = robot.getIntake().getLeftIntakeMotor();
		rightIntakeMotor = robot.getIntake().getRightIntakeMotor();
		
		intake = robot.getIntake();
		arm = robot.getArm();
		elevator = robot.getElevator();
		
		delayTimer = new Timer();
		putDelayTimer();
		
		navX = new AHRS(SPI.Port.kMXP);
	}
	
	public void pickAuto() {
		reset();
		resetStates();
		delayReset();
		setDelayTimer();
		
		currentPosition = position.getSelected();
		currentPriority = priority.getSelected();
		fieldConfiguration = DriverStation.getInstance().getGameSpecificMessage();
		
		SmartDashboard.putString("Field Config", fieldConfiguration);
		
		switch(currentPosition) {
		case MIDDLE:
			switch(fieldConfiguration) {
			case RRR:
				autoChanger = AutoStates.MIDDLE_SWITCH_RIGHT;
				break;
			case RLR:
				autoChanger = AutoStates.MIDDLE_SWITCH_RIGHT;
				break;
			case LLL:
				autoChanger = AutoStates.MIDDLE_SWITCH_LEFT;
				break;
			case LRL:
				autoChanger = AutoStates.MIDDLE_SWITCH_LEFT;
				break;
			default:
				autoChanger = AutoStates.MIDDLE_CROSS_LINE;
				break;
			}
			break;
		case LEFT:
			switch(currentPriority) {
			case SWITCH:
				switch(fieldConfiguration) {
				case RRR:
					autoChanger = AutoStates.LEFT_CROSS_LINE;
					break;
				case RLR:
					autoChanger = AutoStates.LEFT_CROSS_LINE;
					break;
				case LLL:
					autoChanger = AutoStates.LEFT_SWITCH;
					break;
				case LRL:
					autoChanger = AutoStates.LEFT_SWITCH;
					break;
				default:
					autoChanger = AutoStates.MIDDLE_CROSS_LINE;
					break;
				}
				break;
			case SCALE:
				switch(fieldConfiguration) {
				case RRR:
					autoChanger = AutoStates.LEFT_SCALE_RIGHT;
					break;
				case RLR:
					autoChanger = AutoStates.LEFT_SCALE_LEFT;
					break;
				case LLL:
					autoChanger = AutoStates.LEFT_SCALE_LEFT;
					break;
				case LRL:
					autoChanger = AutoStates.LEFT_SCALE_RIGHT;
					break;
				default:
					autoChanger = AutoStates.MIDDLE_CROSS_LINE;
					break;
				}
				break;
			default:
				autoChanger = AutoStates.MIDDLE_CROSS_LINE;
				break;
			}
			break;
		case RIGHT:
			switch(currentPriority) {
			case SWITCH:
				switch(fieldConfiguration) {
				case RRR:
					autoChanger = AutoStates.RIGHT_SWITCH;
					break;
				case RLR:
					autoChanger = AutoStates.RIGHT_SWITCH;
					break;
				case LLL:
					autoChanger = AutoStates.RIGHT_CROSS_LINE;
					break;
				case LRL:
					autoChanger = AutoStates.RIGHT_CROSS_LINE;
					break;
				default:
					autoChanger = AutoStates.MIDDLE_CROSS_LINE;
					break;
				}
				break;
			case SCALE:
				switch(fieldConfiguration) {
				case RRR:
					autoChanger = AutoStates.RIGHT_SCALE_RIGHT;
					break;
				case RLR:
					autoChanger = AutoStates.RIGHT_SCALE_LEFT;
					break;
				case LLL:
					autoChanger = AutoStates.RIGHT_SCALE_LEFT;
					break;
				case LRL:
					autoChanger = AutoStates.RIGHT_SCALE_RIGHT;
					break;
				default:
					autoChanger = AutoStates.MIDDLE_CROSS_LINE;
					break;
				}
				break;
			default:
				autoChanger = AutoStates.MIDDLE_CROSS_LINE;
				break;
			}
			break;
		default:
			autoChanger = AutoStates.MIDDLE_CROSS_LINE;
			break;
		}
//		autoChanger = AutoStates.ELEVATOR;
	}
	
	public void runAuto() {
		setEncoderValues();
		showSensorValues();
		
		switch(autoChanger) {
		case MIDDLE_CROSS_LINE:
			middleCrossLine();
			break;
		case MIDDLE_SWITCH_RIGHT:
//			middleSwitchRight();
			middleSwitchRightBack();
//			middleSwitchRight2Cube(); 
			break;
		case MIDDLE_SWITCH_LEFT:
//			middleSwitchLeft();
			middleSwitchLeftBack();
//			middleSwitchLeft2Cube();
			break;
		case LEFT_CROSS_LINE:
			leftCrossLine();
			break;
		case LEFT_SWITCH:
			leftSwitch();
			break;
		case LEFT_SCALE_LEFT:
//			leftScaleLeftReady();
			leftScaleLeft();
			break;
		case LEFT_SCALE_RIGHT:
			leftScaleRightReady();
//			leftScaleRight();
			break;
		case RIGHT_CROSS_LINE:
			rightCrossLine();
			break;
		case RIGHT_SWITCH:
			rightSwitch();
			break;
		case RIGHT_SCALE_RIGHT:
			rightScaleRightReady();
//			rightScaleRight();
			break;
		case RIGHT_SCALE_LEFT:
			rightScaleLeftReady();
//			rightScaleLeft();
			break;
		case LEFT_TURN:
			leftTurnCheck();
			break;
		case RIGHT_TURN:
			rightTurnCheck();
			break;
		case ELEVATOR:
//			elevator.setElevatorAutoDesiredHieght(9);
			elevator.setStartMoving(true);
			elevator.realElevatorAuto();
			break;
		case INTAKE_CHECK:
			intakeCheck();
			break;
		case DO_NOTHING:
			break;
		}
	}
	
	public void setEncoderValues() {
		frontLeftEncoder = frontLeftSensors.getQuadraturePosition();
		frontRightEncoder = Utility.invertInt(frontRightSensors.getQuadraturePosition());
		encoderAverage = (frontLeftEncoder + frontRightEncoder) / 2;
		encoderAverageInches = encoderAverage * DISTANCE_PER_ENCODER_TICK;
	}
	
	public void showSensorValues() {
		SmartDashboard.putNumber("Front Left Encoder", frontLeftEncoder);
		SmartDashboard.putNumber("Front Right Encoder", frontRightEncoder);
		SmartDashboard.putNumber("Encoder Average", encoderAverage);
		SmartDashboard.putNumber("Encoder Average Inches", encoderAverageInches);
		SmartDashboard.putNumber("Gyro", navX.getAngle());
	}
	
	public void gyroCorrect() {
		double angle = navX.getAngle();
		System.out.println("Gyro: " + angle);
		robotDrive.curvatureDrive(autoDriveSpeed, angle * GYRO_CONSTANT, false);
	}
	
	public void backwardGyroCorrect() {
		double angle = navX.getAngle();
		System.out.println("Gyro: " + angle);
		robotDrive.curvatureDrive(-autoDriveSpeed, angle * GYRO_CONSTANT, false);
	}
	
	public void reset() {
		robotDrive.curvatureDrive(0, 0, false);
		currentTurningState = TurningStates.HIGH_SPEED;
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		navX.reset();
		doneTurning = false;
		pauseCounter = 0;
		turnLoopCounter = 0;
		intake.setIntake(false);
		elevator.autoElevatorReset();
	}
	
	public void putDelayTimer() {
		SmartDashboard.putNumber("Auto Delay", 0);
	}
	
	public void setDelayTimer() {
		autoDelay = SmartDashboard.getNumber("Auto Delay", 0);
	}
	
	public void startDelayTimer() {
		delayTimer.start();
	}
	
	public void delayReset() {
		delayTimer.reset();
	}
	
	public void resetStates() {
		currentTurningState = TurningStates.HIGH_SPEED;
		
		currentIntakeCheckState = IntakeCheckStates.DROP_ARM;
		
		currentMiddleCrossLineState = MiddleCrossLineStates.DELAY;
		currentMiddleSwitchRightState = MiddleSwitchRightStates.DELAY;
		currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.DELAY;
		currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DELAY;
		currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DELAY;
		currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DELAY;
		currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DELAY;
		
		currentLeftCrossLineState = LeftCrossLineStates.DELAY;
		currentLeftSwitchState = LeftSwitchStates.DELAY;
		currentLeftScaleLeftReadyState = LeftScaleLeftReadyStates.DELAY;
		currentLeftScaleLeftState = LeftScaleLeftStates.DELAY;
		currentLeftScaleRightReadyState = LeftScaleRightReadyStates.DELAY;
		currentLeftScaleRightState = LeftScaleRightStates.DELAY;
		
		currentRightCrossLineState = RightCrossLineStates.DELAY;
		currentRightSwitchState = RightSwitchStates.DELAY;
		currentRightScaleRightReadyState = RightScaleRightReadyStates.DELAY;
		currentRightScaleRightState = RightScaleRightStates.DELAY;
		currentRightScaleLeftReadyState = RightScaleLeftReadyStates.DELAY;
		currentRightScaleLeftState = RightScaleLeftStates.DELAY;
	}
	
	
	public enum TurningStates{
		HIGH_SPEED, LOW_SPEED, BACK
	}
	
	TurningStates currentTurningState = TurningStates.HIGH_SPEED;
	
	public void leftTurnCheck() {
		if (!doneTurning){
			leftTurn();
		} else {
			robotDrive.curvatureDrive(0, 0, false);
			frontLeftSensors.setQuadraturePosition(0, 10);
			frontRightSensors.setQuadraturePosition(0, 10);
			navX.reset();
		}
	}
	
	public void leftTurn() {
		switch(currentTurningState) {
		case HIGH_SPEED:
			if (navX.getAngle() > (-turnAngle) + TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, autoTurnSpeedHigh, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.LOW_SPEED;
			}
			break;
		case LOW_SPEED:
			if (navX.getAngle() > (-turnAngle) + TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, autoTurnSpeedLow, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.BACK;
			}
			break;
		case BACK:
			System.out.println("Back");
			if (turnLoopCounter < turnLoopLimit){
				if (navX.getAngle() < (-turnAngle) - TURNING_GYRO_OFFSET3) {
					robotDrive.curvatureDrive(0, -autoTurnSpeedBack, true);
				} else if (navX.getAngle() > (-turnAngle) + TURNING_GYRO_OFFSET3) {
					robotDrive.curvatureDrive(0, autoTurnSpeedBack, true);
				}
				turnLoopCounter++;
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				doneTurning = true;
			}
			break;
		}
	}
	
	public void rightTurnCheck() {
		if (!doneTurning){
			rightTurn();
		} else {
			robotDrive.curvatureDrive(0, 0, false);
			frontLeftSensors.setQuadraturePosition(0, 10);
			frontRightSensors.setQuadraturePosition(0, 10);
			navX.reset();
		}
	}
	
	public void rightTurn() {
		System.out.println(navX.getAngle());
		switch(currentTurningState) {
		case HIGH_SPEED:
			System.out.println("High");
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, -autoTurnSpeedHigh, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.LOW_SPEED;
			}
			break;
		case LOW_SPEED:
			System.out.println("Low");
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, -autoTurnSpeedLow, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.BACK;
			}
			break;
		case BACK:
//			System.out.println("Back");
			if (turnLoopCounter < turnLoopLimit){
				if (navX.getAngle() > (turnAngle) + TURNING_GYRO_OFFSET3) {
					System.out.println("Back1");
					robotDrive.curvatureDrive(0, autoTurnSpeedBack, true);
				} else if (navX.getAngle() < (turnAngle) - TURNING_GYRO_OFFSET3) {
					System.out.println("Back2");
					robotDrive.curvatureDrive(0, -autoTurnSpeedBack, true);
				}
				turnLoopCounter++;
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				doneTurning = true;
			}
			break;
		}
	}
	
	
	public enum IntakeCheckStates {
		DROP_ARM, INTAKE, DRIVE_FORWARD
	}
	
	IntakeCheckStates currentIntakeCheckState = IntakeCheckStates.DROP_ARM;
	
	public void intakeCheck() {
		intake.autoIntake();
		switch(currentIntakeCheckState) {
		case DROP_ARM:
			arm.setArmState(true);
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentIntakeCheckState = IntakeCheckStates.INTAKE;
			}
			break;
		case INTAKE:
			intake.setIntake(true);
			currentIntakeCheckState = IntakeCheckStates.DRIVE_FORWARD;
			break;
		case DRIVE_FORWARD:
			if (encoderAverageInches <= intakeCheckForwardDistance) {
				gyroCorrect();
			} else {
				intake.setIntake(false);
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum MiddleCrossLineStates {
		DELAY, DRIVING_FORWARD
	}

	MiddleCrossLineStates currentMiddleCrossLineState = MiddleCrossLineStates.DELAY;
	
	public void middleCrossLine() {
		switch (currentMiddleCrossLineState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentMiddleCrossLineState = MiddleCrossLineStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= middleCrossLineForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchRightStates {
		DELAY, DRIVING_FORWARD, CUBE_PAUSE, DROP_CUBE
	}
	
	MiddleSwitchRightStates currentMiddleSwitchRightState = MiddleSwitchRightStates.DELAY;
	
	public void middleSwitchRight() {
		switch (currentMiddleSwitchRightState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchRightState = MiddleSwitchRightStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			System.out.println("encoderAverageInches");
			if (encoderAverageInches <= middleSwitchRightForwardDistance) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRightState = MiddleSwitchRightStates.CUBE_PAUSE;
			}
			break;
		case CUBE_PAUSE:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchRightState = MiddleSwitchRightStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchRightBackStates {
		DELAY, DRIVING_FORWARD, CUBE_PAUSE, DROP_CUBE, DRIVING_BACKWARD
	}
	
	MiddleSwitchRightBackStates currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.DELAY;
	
	public void middleSwitchRightBack() {
		intake.autoIntake();
		switch (currentMiddleSwitchRightBackState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= middleSwitchRightBackForwardDistance) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.CUBE_PAUSE;
			}
			break;
		case CUBE_PAUSE:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				currentMiddleSwitchRightBackState = MiddleSwitchRightBackStates.DRIVING_BACKWARD;
			}
			break;
		case DRIVING_BACKWARD:
			if (encoderAverageInches >= middleSwitchRightBackBackwardDistance) {
				backwardGyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				arm.setArmState(true);
			}
			break;
		}
	}
	
	public enum MiddleSwitchRight2CubeStates {
		DELAY, DRIVING_FORWARD1, CUBE_PAUSE1, DROP_CUBE1, 
		DRIVING_BACKWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, 
		DROP_ARM, INTAKE, DRIVING_FORWARD3, DRIVE_PAUSE, 
		DRIVING_BACKWARD2, TURN_PAUSE5, TURNING3, TURN_PAUSE6, DRIVING_FORWARD4, TURN_PAUSE7, TURNING4, TURN_PAUSE8, 
		DRIVING_FORWARD5, CUBE_PAUSE2, DROP_CUBE2 
	}
	
	MiddleSwitchRight2CubeStates currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DELAY;
	
	public void middleSwitchRight2Cube() {
		intake.autoIntake();
		switch (currentMiddleSwitchRight2CubeState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= middleSwitchRight2CubeForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.CUBE_PAUSE1;
			}
			break;
		case CUBE_PAUSE1:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DROP_CUBE1;
			}
			break;
		case DROP_CUBE1:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_BACKWARD1;
			}
			break;
		case DRIVING_BACKWARD1:
			if (encoderAverageInches >= middleSwitchRight2CubeBackwardDistance1) {
				backwardGyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= middleSwitchRight2CubeForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			arm.setArmState(false);
			currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.INTAKE;
			break;
		case INTAKE:
			intake.setIntake(true);
			currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_FORWARD3;
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= middleSwitchRight2CubeForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVE_PAUSE;
			}
			break;
		case DRIVE_PAUSE:
			if (pauseCounter < drivePause){
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_BACKWARD2;
			}
			break;
		case DRIVING_BACKWARD2:
			if (encoderAverageInches >= middleSwitchRight2CubeBackwardDistance2) {
				backwardGyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE5;
			}
			break;
		case TURN_PAUSE5:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURNING3;
			}
			break;
		case TURNING3:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE6;
			}
			break;
		case TURN_PAUSE6:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_FORWARD4;
			}
			break;
		case DRIVING_FORWARD4:
			if (encoderAverageInches <= middleSwitchRight2CubeForwardDistance4) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE7;
			}
			break;
		case TURN_PAUSE7:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURNING4;
			}
			break;
		case TURNING4:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.TURN_PAUSE8;
			}
			break;
		case TURN_PAUSE8:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DRIVING_FORWARD5;
			}
			break;
		case DRIVING_FORWARD5:
			if (encoderAverageInches <= middleSwitchRight2CubeForwardDistance5) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.CUBE_PAUSE2;
			}
			break;
		case CUBE_PAUSE2:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchRight2CubeState = MiddleSwitchRight2CubeStates.DROP_CUBE2;
			}
			break;
		case DROP_CUBE2:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchLeftStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, CUBE_PAUSE, DROP_CUBE
	}
	
	MiddleSwitchLeftStates currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DELAY;
	
	public void middleSwitchLeft() {
		switch (currentMiddleSwitchLeftState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= middleSwitchLeftForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
//			System.out.println("Encoders: " + encoderAverageInches);
			if (encoderAverageInches <= middleSwitchLeftForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= middleSwitchLeftForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.CUBE_PAUSE;
			}
			break;
		case CUBE_PAUSE:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchLeftBackStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, 
		TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, CUBE_PAUSE1, DROP_CUBE1, DRIVING_BACKWARD
	}
	
	MiddleSwitchLeftBackStates currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DELAY;
	
	public void middleSwitchLeftBack() {
		switch (currentMiddleSwitchLeftBackState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= middleSwitchLeftBackForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= middleSwitchLeftBackForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= middleSwitchLeftBackForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.CUBE_PAUSE1;
			}
			break;
		case CUBE_PAUSE1:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DROP_CUBE1;
			}
			break;
		case DROP_CUBE1:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				reset();
				currentMiddleSwitchLeftBackState = MiddleSwitchLeftBackStates.DRIVING_BACKWARD;
			}
			break;
		case DRIVING_BACKWARD:
			if (encoderAverageInches >= middleSwitchLeftBackBackwardDistance) {
				backwardGyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				arm.setArmState(true);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchLeft2CubeStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, 
		TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, CUBE_PAUSE1, DROP_CUBE1, DRIVING_BACKWARD1, 
		TURN_PAUSE5, TURNING3, TURN_PAUSE6, DRIVING_FORWARD4, TURN_PAUSE7, TURNING4, TURN_PAUSE8, 
		DROP_ARM, INTAKE, DRIVING_FORWARD5, DRIVE_PAUSE, 
		DRIVING_BACKWARD2, TURN_PAUSE9, TURNING5, TURN_PAUSE10, DRIVING_FORWARD6, TURN_PAUSE11, TURNING6, TURN_PAUSE12, 
		DRIVING_FORWARD7, CUBE_PAUSE2, DROP_CUBE2 
	}
	
	MiddleSwitchLeft2CubeStates currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DELAY;
	
	public void middleSwitchLeft2Cube() {
		switch (currentMiddleSwitchLeft2CubeState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.CUBE_PAUSE1;
			}
			break;
		case CUBE_PAUSE1:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DROP_CUBE1;
			}
			break;
		case DROP_CUBE1:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_BACKWARD1;
			}
			break;
		case DRIVING_BACKWARD1:
			if (encoderAverageInches >= middleSwitchLeft2CubeBackwardDistance1) {
				backwardGyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE5;
			}
			break;
		case TURN_PAUSE5:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING3;
			}
			break;
		case TURNING3:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE6;
			}
			break;
		case TURN_PAUSE6:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD4;
			}
			break;
		case DRIVING_FORWARD4:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance4) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE7;
			}
			break;
		case TURN_PAUSE7:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING4;
			}
			break;
		case TURNING4:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE8;
			}
			break;
		case TURN_PAUSE8:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			arm.setArmState(false);
			currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.INTAKE;
			break;
		case INTAKE:
			intake.setIntake(true);
			currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD5;
			break;
		case DRIVING_FORWARD5:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance5) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVE_PAUSE;
			}
			break;
		case DRIVE_PAUSE:
			if (pauseCounter < drivePause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_BACKWARD2;
			}
			break;
		case DRIVING_BACKWARD2:
			if (encoderAverageInches >= middleSwitchLeft2CubeBackwardDistance2) {
				backwardGyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE9;
			}
			break;
		case TURN_PAUSE9:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING5;
			}
			break;
		case TURNING5:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE10;
			}
			break;
		case TURN_PAUSE10:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD6;
			}
			break;
		case DRIVING_FORWARD6:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance6) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE11;
			}
			break;
		case TURN_PAUSE11:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURNING6;
			}
			break;
		case TURNING6:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.TURN_PAUSE12;
			}
			break;
		case TURN_PAUSE12:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DRIVING_FORWARD7;
			}
			break;
		case DRIVING_FORWARD7:
			if (encoderAverageInches <= middleSwitchLeft2CubeForwardDistance7) {
				gyroCorrect();
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.CUBE_PAUSE2;
			}
			break;
		case CUBE_PAUSE2:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentMiddleSwitchLeft2CubeState = MiddleSwitchLeft2CubeStates.DROP_CUBE2;
			}
			break;
		case DROP_CUBE2:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum LeftCrossLineStates {
		DELAY, DRIVING_FORWARD
	}

	LeftCrossLineStates currentLeftCrossLineState = LeftCrossLineStates.DELAY;
	
	public void leftCrossLine() {
		switch (currentLeftCrossLineState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentLeftCrossLineState = LeftCrossLineStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= leftCrossLineForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum LeftSwitchStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN, CUBE_PAUSE, DROP_CUBE
	}
	
	LeftSwitchStates currentLeftSwitchState = LeftSwitchStates.DELAY;
	
	public void leftSwitch() {
		switch (currentLeftSwitchState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftSwitchState = LeftSwitchStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= leftSwitchForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentLeftSwitchState = LeftSwitchStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftSwitchState = LeftSwitchStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentLeftSwitchState = LeftSwitchStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentLeftSwitchState = LeftSwitchStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches <= leftSwitchForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentLeftSwitchState = LeftSwitchStates.CUBE_PAUSE;
			}
			break;
		case CUBE_PAUSE:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentLeftSwitchState = LeftSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum LeftScaleLeftReadyStates {
		DELAY, DRIVING_FORWARD
	}

	LeftScaleLeftReadyStates currentLeftScaleLeftReadyState = LeftScaleLeftReadyStates.DELAY;
	
	public void leftScaleLeftReady() {
		switch (currentLeftScaleLeftReadyState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentLeftScaleLeftReadyState = LeftScaleLeftReadyStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= leftScaleLeftReadyForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum LeftScaleLeftStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, ELEVATOR_PAUSE, ELEVATOR_UP, DROP_ARM, OUTTAKE
	}
	
	LeftScaleLeftStates currentLeftScaleLeftState = LeftScaleLeftStates.DELAY;
	
	public void leftScaleLeft() {
		elevator.realElevatorAuto();
		switch (currentLeftScaleLeftState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftScaleLeftState = LeftScaleLeftStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= leftScaleLeftForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleLeftState = LeftScaleLeftStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftScaleLeftState = LeftScaleLeftStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentLeftScaleLeftState = LeftScaleLeftStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentLeftScaleLeftState = LeftScaleLeftStates.ELEVATOR_PAUSE;
			}
			break;
//		case DRIVING_FORWARD2:
//			if (encoderAverageInches <= leftScaleLeftForwardDistance2) {
//				gyroCorrect();
//			} else {
//				reset();
//				currentLeftScaleLeftState = LeftScaleLeftStates.TURN_PAUSE3;
//			}
//			break;
//		case TURN_PAUSE3:
//			if (pauseCounter < turnPause){
//				pauseCounter++;
//			} else {
//				currentLeftScaleLeftState = LeftScaleLeftStates.TURNING2;
//			}
//			break;
//		case TURNING2:
//			if (!doneTurning) {
//				leftTurn();
//			} else {
//				reset();
//				currentLeftScaleLeftState = LeftScaleLeftStates.TURN_PAUSE4;
//			}
//			break;
//		case TURN_PAUSE4:
//			if (pauseCounter < turnPause){
//				pauseCounter++;
//			} else {
//				currentLeftScaleLeftState = LeftScaleLeftStates.DRIVING_FORWARD3;
//			}
//			break;
//		case DRIVING_FORWARD3:
//			if (encoderAverageInches <= leftScaleLeftForwardDistance3) {
//				gyroCorrect();
//			} else {
//				reset();
//				currentLeftScaleLeftState = LeftScaleLeftStates.ELEVATOR_PAUSE;
//			}
//			break;
		case ELEVATOR_PAUSE:
			if (pauseCounter < elevatorPause){
				elevator.setStartMoving(true);
				pauseCounter++;
			} else {
				reset();
				currentLeftScaleLeftState = LeftScaleLeftStates.DROP_ARM;
			}
			break;
//		case ELEVATOR_UP:
////			elevator.setElevatorAutoDesiredHieght(30);
//			if (elevator.liftDone) {
//				reset();
//				currentLeftScaleLeftState = LeftScaleLeftStates.DROP_ARM;
//			}
//			break;
		case DROP_ARM:
			arm.setArmState(false);
			if (pauseCounter < outtakeDropArmPause){
				pauseCounter++;
			} else {
				reset();
				currentLeftScaleLeftState = LeftScaleLeftStates.OUTTAKE;
			}
			break;
		case OUTTAKE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				elevator.setStartMoving(false);
			}
			break;
		}
	}
	
	public enum LeftScaleRightReadyStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN, TURN_PAUSE3, TURNING2, TURN_PAUSE4
	}
	
	LeftScaleRightReadyStates currentLeftScaleRightReadyState = LeftScaleRightReadyStates.DELAY;
	
	public void leftScaleRightReady() {
		switch (currentLeftScaleRightReadyState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= leftScaleRightReadyForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches <= leftScaleRightReadyForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURNING2;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				reset();
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentLeftScaleRightReadyState = LeftScaleRightReadyStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}	
	
	
	public enum LeftScaleRightStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, ELEVATOR_PAUSE, ELEVATOR_UP, DROP_ARM, OUTTAKE
	}
	
	LeftScaleRightStates currentLeftScaleRightState = LeftScaleRightStates.DELAY;
	
	public void leftScaleRight() {
		elevator.autoElevator(autoElevatorDesiredTime);
		switch (currentLeftScaleRightState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftScaleRightState = LeftScaleRightStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= leftScaleRightForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftScaleRightState = LeftScaleRightStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= leftScaleRightForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftScaleRightState = LeftScaleRightStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentLeftScaleRightState = LeftScaleRightStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= leftScaleRightForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.ELEVATOR_PAUSE;
			}
			break;
		case ELEVATOR_PAUSE:
			if (pauseCounter < elevatorPause){
				pauseCounter++;
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.ELEVATOR_UP;
			}
			break;
		case ELEVATOR_UP:
//			elevator.setElevatorAutoDesiredHieght(30);
			if (elevator.liftDone) {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			arm.setArmState(false);
			if (pauseCounter < outtakeDropArmPause){
				pauseCounter++;
			} else {
				reset();
				currentLeftScaleRightState = LeftScaleRightStates.OUTTAKE;
			}
			break;
		case OUTTAKE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum RightCrossLineStates {
		DELAY, DRIVING_FORWARD
	}

	RightCrossLineStates currentRightCrossLineState = RightCrossLineStates.DELAY;
	
	public void rightCrossLine() {
		switch (currentRightCrossLineState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentRightCrossLineState = RightCrossLineStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= rightCrossLineForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum RightSwitchStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN, CUBE_PAUSE, DROP_CUBE
	}
	
	RightSwitchStates currentRightSwitchState = RightSwitchStates.DELAY;
	
	public void rightSwitch() {
		switch (currentRightSwitchState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentRightSwitchState = RightSwitchStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= rightSwitchForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentRightSwitchState = RightSwitchStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightSwitchState = RightSwitchStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentRightSwitchState = RightSwitchStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentRightSwitchState = RightSwitchStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches < rightSwitchForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentRightSwitchState = RightSwitchStates.CUBE_PAUSE;
			}
			break;
		case CUBE_PAUSE:
			if (pauseCounter < cubePause){
				pauseCounter++;
			} else {
				reset();
				currentRightSwitchState = RightSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum RightScaleRightReadyStates {
		DELAY, DRIVING_FORWARD
	}

	RightScaleRightReadyStates currentRightScaleRightReadyState = RightScaleRightReadyStates.DELAY;
	
	public void rightScaleRightReady() {
		switch (currentRightScaleRightReadyState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentRightScaleRightReadyState = RightScaleRightReadyStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= rightScaleRightReadyForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum RightScaleRightStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, ELEVATOR_PAUSE, ELEVATOR_UP, DROP_ARM, OUTTAKE
	}
	
	RightScaleRightStates currentRightScaleRightState = RightScaleRightStates.DELAY;
	
	public void rightScaleRight() {
		elevator.autoElevator(autoElevatorDesiredTime);
		switch (currentRightScaleRightState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentRightScaleRightState = RightScaleRightStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= rightScaleRightForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleRightState = RightScaleRightStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= rightScaleRightForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleRightState = RightScaleRightStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleRightState = RightScaleRightStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= rightScaleRightForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.ELEVATOR_PAUSE;
			}
			break;
		case ELEVATOR_PAUSE:
			if (pauseCounter < elevatorPause){
				pauseCounter++;
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.ELEVATOR_UP;
			}
			break;
		case ELEVATOR_UP:
//			elevator.setElevatorAutoDesiredHieght(30);
			if (elevator.liftDone) {
				reset();
				currentRightScaleRightState = RightScaleRightStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			arm.setArmState(false);
			if (pauseCounter < outtakeDropArmPause){
				pauseCounter++;
			} else {
				reset();
				currentRightScaleRightState = RightScaleRightStates.OUTTAKE;
			}
			break;
		case OUTTAKE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum RightScaleLeftReadyStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN, TURN_PAUSE3, TURNING2, TURN_PAUSE4,
	}
	
	RightScaleLeftReadyStates currentRightScaleLeftReadyState = RightScaleLeftReadyStates.DELAY;
	
	public void rightScaleLeftReady() {
		switch (currentRightScaleLeftReadyState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= rightScaleLeftReadyForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;
			} else {
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches <= rightScaleLeftReadyForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				reset();
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentRightScaleLeftReadyState = RightScaleLeftReadyStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum RightScaleLeftStates {
		DELAY, DRIVING_FORWARD1, TURN_PAUSE1, TURNING1, TURN_PAUSE2, DRIVING_FORWARD2, TURN_PAUSE3, TURNING2, TURN_PAUSE4, DRIVING_FORWARD3, ELEVATOR_PAUSE, ELEVATOR_UP, DROP_ARM, OUTTAKE
	}
	
	RightScaleLeftStates currentRightScaleLeftState = RightScaleLeftStates.DELAY;
	
	public void rightScaleLeft() {
		elevator.autoElevator(autoElevatorDesiredTime);
		switch (currentRightScaleLeftState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentRightScaleLeftState = RightScaleLeftStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= rightScaleLeftForwardDistance1) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.TURN_PAUSE1;
			}
			break;
		case TURN_PAUSE1:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleLeftState = RightScaleLeftStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!doneTurning) {
				leftTurn();
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.TURN_PAUSE2;
			}
			break;
		case TURN_PAUSE2:
			if (pauseCounter < turnPause){
				if (pauseCounter < turnPause - 1){
					navX.reset();
				}
				pauseCounter++;	
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= rightScaleLeftForwardDistance2) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.TURN_PAUSE3;
			}
			break;
		case TURN_PAUSE3:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleLeftState = RightScaleLeftStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!doneTurning) {
				rightTurn();
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.TURN_PAUSE4;
			}
			break;
		case TURN_PAUSE4:
			if (pauseCounter < turnPause){
				pauseCounter++;
			} else {
				currentRightScaleLeftState = RightScaleLeftStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= rightScaleLeftForwardDistance3) {
				gyroCorrect();
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.ELEVATOR_PAUSE;
			}
			break;
		case ELEVATOR_PAUSE:
			if (pauseCounter < elevatorPause){
				pauseCounter++;
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.ELEVATOR_UP;
			}
			break;
		case ELEVATOR_UP:
//			elevator.setElevatorAutoDesiredHieght(30);
			if (elevator.liftDone) {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			arm.setArmState(false);
			if (pauseCounter < outtakeDropArmPause){
				pauseCounter++;
			} else {
				reset();
				currentRightScaleLeftState = RightScaleLeftStates.OUTTAKE;
			}
			break;
		case OUTTAKE:
			if (pauseCounter < outtakePause){
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				pauseCounter++;
			} else {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
}
