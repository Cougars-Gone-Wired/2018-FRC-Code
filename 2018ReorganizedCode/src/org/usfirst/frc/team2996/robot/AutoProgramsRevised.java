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
		LEFT_TURN, RIGHT_TURN
	}
	
	public AutoStates autoChanger;
	
	static final int WHEEL_DIAMETER = 6; // in inches
	static final int ENCODER_TICKS_PER_REV = 360 * 4;
	static final double DISTANCE_PER_ENCODER_TICK = ((WHEEL_DIAMETER * Math.PI) / ENCODER_TICKS_PER_REV); // in inches
	
	static final double GYRO_CONSTANT = 0.03;
	
	static final double TURNING_GYRO_OFFSET = 70;//good
	static final double TURNING_GYRO_OFFSET2 = 10;//good
	static final double TURNING_GYRO_OFFSET3 = 2;//good
	
	private DifferentialDrive robotDrive;

	private SensorCollection frontLeftSensors;
	private SensorCollection frontRightSensors;
	
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;
	
	private AHRS navX;
	
	private Timer delayTimer;
	
	int frontLeftEncoder;
	int frontRightEncoder;
	int encoderAverage;
	double encoderAverageInches;
	
	private double turnAngle = 90;
	private double turnPause = 40;
	private double cubePause = 60;
	
	private double autoDelay;
	
	private boolean doneTurning = false;
	int pauseCounter = 0;
	
	private double autoDriveSpeed = Utility.invertDouble(1);
	private double autoTurnSpeedHigh = .75;
	private double autoTurnSpeedLow = .4;
	private double autoTurnSpeedBack = .2;
	
	private double middleCrossLineForwardDistance = 81;
	
	private double middleSwitchRightForwardDistance = 81;
	
	private double middleSwitchLeftForwardDistance1 = 21;
	private double middleSwitchLeftForwardDistance2 = 85;
	private double middleSwitchLeftForwardDistance3 = 57;
	
	private double leftCrossLineForwardDistance = 180;
	
	private double leftSwitchForwardDistance1 = 130;
	private double leftSwitchForwardDistance2 = 15;
	
	private double leftScaleLeftReadyForwardDistance = 230;
	
	private double leftScaleRightReadyForwardDistance1 = 195;
	private double leftScaleRightReadyForwardDistance2 = 180;
	
	private double rightCrossLineForwardDistance = 180;
	
	private double rightSwitchForwardDistance1 = 130;
	private double rightSwitchForwardDistance2 = 15;
	
	private double rightScaleRightReadyForwardDistance = 230;
	
	private double rightScaleLeftReadyForwardDistance1 = 195;
	private double rightScaleLeftReadyForwardDistance2 = 180;
	
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
		autoChanger = AutoStates.RIGHT_SWITCH;
	}
	
	public void runAuto() {
		setEncoderValues();
		showSensorValues();
		
		switch(autoChanger) {
		case MIDDLE_CROSS_LINE:
			middleCrossLine();
			break;
		case MIDDLE_SWITCH_RIGHT:
			middleSwitchRight();
			break;
		case MIDDLE_SWITCH_LEFT:
			middleSwitchLeft();
			break;
		case LEFT_CROSS_LINE:
			leftCrossLine();
			break;
		case LEFT_SWITCH:
			leftSwitch();
			break;
		case LEFT_SCALE_LEFT:
			leftScaleLeftReady();
			break;
		case LEFT_SCALE_RIGHT:
			leftScaleRightReady();
			break;
		case RIGHT_CROSS_LINE:
			rightCrossLine();
			break;
		case RIGHT_SWITCH:
			rightSwitch();
			break;
		case RIGHT_SCALE_RIGHT:
			rightScaleRightReady();
			break;
		case RIGHT_SCALE_LEFT:
			rightScaleLeftReady();
			break;
		case LEFT_TURN:
			leftTurn();
			break;
		case RIGHT_TURN:
			rightTurn();
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
	
	public void reset() {
		robotDrive.curvatureDrive(0, 0, false);
		currentTurningState = TurningStates.HIGH_SPEED;
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		navX.reset();
		doneTurning = false;
		pauseCounter = 0;
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
		
		currentMiddleCrossLineState = MiddleCrossLineStates.DELAY;
		currentMiddleSwitchRightState = MiddleSwitchRightStates.DELAY;
		currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DELAY;
		
		currentLeftCrossLineState = LeftCrossLineStates.DELAY;
		currentLeftSwitchState = LeftSwitchStates.DELAY;
		currentLeftScaleLeftReadyState = LeftScaleLeftReadyStates.DELAY;
		currentLeftScaleRightReadyState = LeftScaleRightReadyStates.DELAY;
		
		currentRightCrossLineState = RightCrossLineStates.DELAY;
		currentRightSwitchState = RightSwitchStates.DELAY;
		currentRightScaleRightReadyState = RightScaleRightReadyStates.DELAY;
		currentRightScaleLeftReadyState = RightScaleLeftReadyStates.DELAY;
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
			if (navX.getAngle() < (-turnAngle) - TURNING_GYRO_OFFSET3) {
				robotDrive.curvatureDrive(0, -autoTurnSpeedBack, true);
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
		switch(currentTurningState) {
		case HIGH_SPEED:
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, -autoTurnSpeedHigh, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.LOW_SPEED;
			}
			break;
		case LOW_SPEED:
			System.out.println("Low Speed");
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, -autoTurnSpeedLow, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.BACK;
			}
			break;
		case BACK:
			System.out.println("Back");
			if (navX.getAngle() > (turnAngle) + TURNING_GYRO_OFFSET3) {
				robotDrive.curvatureDrive(0, autoTurnSpeedBack, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				doneTurning = true;
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
				currentMiddleSwitchRightState = MiddleSwitchRightStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
				pauseCounter++;
			} else {
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
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
				currentLeftSwitchState = LeftSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
	
	
	public enum LeftScaleRightReadyStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN
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
				robotDrive.curvatureDrive(0, 0, false);
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
				currentRightSwitchState = RightSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
	
	
	public enum RightScaleLeftReadyStates {
		DELAY, DRIVING_FORWARD, TURN_PAUSE1, TURNING, TURN_PAUSE2, DRIVING_FORWARD_AGAIN
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
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
}
