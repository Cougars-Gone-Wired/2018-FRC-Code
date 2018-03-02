package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoPrograms extends AutoMethods{

	private double autoDriveSpeed;
	private double autoTurnSpeed;
	private double autoDelay;
	
	private double leftSwitchForwardDistance1;
	private double leftSwitchTurnAngle;
	private double leftSwitchForwardDistance2;
	
	private double leftScaleForwardDistance;
	private double leftScaleLiftTime;
	
	private double leftExchangeForwardDistance1;
	private double leftExchangeTurnAngle1;
	private double leftExchangeForwardDistance2;
	private double leftExchangeTurnAngle2;
	private double leftExchangeForwardDistance3;
	
	private double leftCrossLineForwardDistance;
	
	private double middleSwitchLeftForwardDistance1;
	private double middleSwitchLeftTurnAngle1;
	private double middleSwitchLeftForwardDistance2;
	private double middleSwitchLeftTurnAngle2;
	private double middleSwitchLeftForwardDistance3;
	
	private double middleSwitchRightForwardDistance;
	
	private double middleExchangeForwardDistance1;
	private double middleExchangeTurnAngle1;
	private double middleExchangeForwardDistance2;
	private double middleExchangeTurnAngle2;
	private double middleExchangeForwardDistance3;
	
	private double middleCrossLineForwardDistance;
	
	private double rightSwitchForwardDistance1;
	private double rightSwitchTurnAngle;
	private double rightSwitchForwardDistance2;
	
	private double rightScaleForwardDistance;
	private double rightScaleLiftTime;
	
	private double rightCrossLineForwardDistance;
	
	private double turnAngle;
	private boolean doneTurning = false;
	
	private double turnTestAngle;
	
	static final double GYRO_CONSTANT = 0.03;
	
	static final double TURNING_GYRO_OFFSET = 70;
	static final double TURNING_GYRO_OFFSET2 = 10;
	static final double TURNING_GYRO_OFFSET3 = 2;
	
	boolean turningStage2 = false;
	
	public AutoPrograms(Robot robot) {
		super(robot);
	}
	
	public void putAutoNumbers() {
		SmartDashboard.putNumber("Auto Drive Speed", 1);
		SmartDashboard.putNumber("Auto Turn Speed", .75);
		SmartDashboard.putNumber("Auto Delay", 0);
		
		SmartDashboard.putNumber("Left Switch Forward Distance 1", 100);
//		SmartDashboard.putNumber("Left Switch Turn Angle", 90);
		SmartDashboard.putNumber("Left Switch Forward Distance 2", 10);
//		
//		SmartDashboard.putNumber("Left Scale Forward Distance", 200);
//		SmartDashboard.putNumber("Left Scale Lift Time", 1);
//		
//		SmartDashboard.putNumber("Left Exchange Forward Distance 1", 48);
//		SmartDashboard.putNumber("Left Exchnage Turn Angle 1", -90);
//		SmartDashboard.putNumber("Left Exchange Forward Distance 2", 60);
//		SmartDashboard.putNumber("Left Exchange Turn Angle 2", 90);
//		SmartDashboard.putNumber("Left Exchange Forward Distance 3", 72);
//		
		SmartDashboard.putNumber("Left Cross Line Forward Distance", 100);

		SmartDashboard.putNumber("Middle Switch Left Forward Distance 1", 25);
		SmartDashboard.putNumber("Middle Switch Left Turn Angle 1", -90);
		SmartDashboard.putNumber("Middle Switch Left Forward Distance 2", 40);
		SmartDashboard.putNumber("Middle Switch Left Turn Angle 2", 90);
		SmartDashboard.putNumber("Middle Switch Left Forward Distance 3", 30);
		
		SmartDashboard.putNumber("Middle Switch Right Forward Distance", 42);
		
//		SmartDashboard.putNumber("Middle Exchange Forward Distance 1", 48);
//		SmartDashboard.putNumber("Middle Exchnage Turn Angle 1", -90);
//		SmartDashboard.putNumber("Middle Exchange Forward Distance 2", 60);
//		SmartDashboard.putNumber("Middle Exchange Turn Angle 2", 90);
//		SmartDashboard.putNumber("Middle Exchange Forward Distance 3", 72);
		
		SmartDashboard.putNumber("Middle Cross Line Forward Distance", 40); //85 practice
		
		SmartDashboard.putNumber("Right Switch Forward Distance 1", 100);
//		SmartDashboard.putNumber("Right Switch Turn Angle", -90);
		SmartDashboard.putNumber("Right Switch Forward Distance 2", 10);
//		
//		SmartDashboard.putNumber("Right Scale Forward Distance", 200);
//		SmartDashboard.putNumber("Right Scale Lift Time", 1);
//		
		SmartDashboard.putNumber("Right Cross Line Forward Distance", 100);
//		
		SmartDashboard.putNumber("Turn Angle", 90);
		
		SmartDashboard.putNumber("Turn Test Angle", 90);
	}
	
	public void setAutoNumbers() {
		autoDriveSpeed = Utility.invertDouble(SmartDashboard.getNumber("Auto Drive Speed", 1));
		autoTurnSpeed = SmartDashboard.getNumber("Auto Turn Speed", .75);
		autoDelay = SmartDashboard.getNumber("Auto Delay", 0);
		
		leftSwitchForwardDistance1 = SmartDashboard.getNumber("Left Switch Forward Distance 1", 150);
		leftSwitchTurnAngle = SmartDashboard.getNumber("Left Switch Turn Angle", 90);
		leftSwitchForwardDistance2 = SmartDashboard.getNumber("Left Switch Forward Distance 2", 24);
		
		leftScaleForwardDistance = SmartDashboard.getNumber("Left Scale Forward Distance", 200);
		leftScaleLiftTime = SmartDashboard.getNumber("Left Scale Lift Time", 1);
		
		leftExchangeForwardDistance1 = SmartDashboard.getNumber("Left Exchange Forward Distance 1", 36);
		leftExchangeTurnAngle1 = SmartDashboard.getNumber("Left Exchange Turn Angle 1", -90);
		leftExchangeForwardDistance2 = SmartDashboard.getNumber("Left Exchange Forward Distance 2", 36);
		leftExchangeTurnAngle2 = SmartDashboard.getNumber("Left Exchange Turn Angle 2", -90);
		leftExchangeForwardDistance3 = SmartDashboard.getNumber("Left Exchange Forward Distance 3", 36);
		
		leftCrossLineForwardDistance = SmartDashboard.getNumber("Left Cross Line Forward Distance", 130);
		
		middleSwitchLeftForwardDistance1 = SmartDashboard.getNumber("Middle Switch Left Forward Distance 1", 25);
		middleSwitchLeftTurnAngle1 = SmartDashboard.getNumber("Middle Switch Left Turn Angle 1", -90);
		middleSwitchLeftForwardDistance2 = SmartDashboard.getNumber("Middle Switch Left Forward Distance 2", 40);
		middleSwitchLeftTurnAngle2 = SmartDashboard.getNumber("Middle Switch Left Turn Angle 2", 90);
		middleSwitchLeftForwardDistance3 = SmartDashboard.getNumber("Middle Switch Left Forward Distance 3", 30);
		
		middleSwitchRightForwardDistance = SmartDashboard.getNumber("Middle Switch Right Forward Distance", 42);
		
		middleExchangeForwardDistance1 = SmartDashboard.getNumber("Middle Exchange Forward Distance 1", 36);
		middleExchangeTurnAngle1 = SmartDashboard.getNumber("Middle Exchange Turn Angle 1", 90);
		middleExchangeForwardDistance2 = SmartDashboard.getNumber("Middle Exchange Forward Distance 2", 36);
		middleExchangeTurnAngle2 = SmartDashboard.getNumber("Middle Exchange Turn Angle 2", 90);
		middleExchangeForwardDistance3 = SmartDashboard.getNumber("Middle Exchange Forward Distance 3", 36);
		
		middleCrossLineForwardDistance = SmartDashboard.getNumber("Middle Cross Line Forward Distance", 40); //85 practice
		rightSwitchForwardDistance1 = SmartDashboard.getNumber("Right Switch Forward Distance 1", 150);
		rightSwitchTurnAngle = SmartDashboard.getNumber("Right Switch Turn Angle", -90);
		rightSwitchForwardDistance2 = SmartDashboard.getNumber("Right Switch Forward Distance 2", 24);
		
		rightScaleForwardDistance = SmartDashboard.getNumber("Right Scale Forward Distance", 200);
		rightScaleLiftTime = SmartDashboard.getNumber("Right Scale Lift Time", 1);
		
		rightCrossLineForwardDistance = SmartDashboard.getNumber("Right Cross Line Forward Distance", 130);
		
		turnAngle = SmartDashboard.getNumber("Turn Angle", 90);
		
		turnTestAngle = SmartDashboard.getNumber("Turn Test Angle", 90);
	}
	
	public void gyroCorrect() {
		double angle = navX.getAngle();
		System.out.println("Gyro: " + angle);
		robotDrive.curvatureDrive(autoDriveSpeed, angle * GYRO_CONSTANT, false);
	}
	
	public void setEncoderValues() {
		frontLeftEncoder = frontLeftSensors.getQuadraturePosition();
		frontRightEncoder = Utility.invertInt(frontRightSensors.getQuadraturePosition());
		encoderAverage = (frontLeftEncoder + frontRightEncoder) / 2;
		encoderAverageInches = encoderAverage * DISTANCE_PER_ENCODER_TICK;
	}
	
	public void showEncoderValues() {
		SmartDashboard.putNumber("Front Left Encoder", frontLeftEncoder);
		SmartDashboard.putNumber("Front Right Encoder", frontRightEncoder);
		SmartDashboard.putNumber("Encoder Average", encoderAverage);
		SmartDashboard.putNumber("Encoder Average Inches", encoderAverageInches);
		SmartDashboard.putNumber("Gyro", navX.getAngle());
	}
	
	public void autoReset() {
		delayTimer.reset();
		liftTimer.reset();
		outtakeTimer.reset();
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		navX.reset();
	}
	
	public void startDelayTimer() {
		delayTimer.start();
	}
	
	public void resetEncoder() {
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		encoderAverage = 0;
		encoderAverageInches = 0;
	}
	
	public void resetStates() {
		currentLeftSwitchState = LeftSwitchStates.DELAY;
		currentLeftScaleState = LeftScaleStates.DELAY;
		currentLeftExchangeState = LeftExchangeStates.DELAY;
		currentLeftCrossLineState = LeftCrossLineStates.DELAY;
		
		currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DELAY;
		currentMiddleSwitchRightState = MiddleSwitchRightStates.DELAY;
		currentMiddleExchangeState = MiddleExchangeStates.DELAY;
		currentMiddleCrossLineState = MiddleCrossLineStates.DELAY;
		
		currentRightSwitchState = RightSwitchStates.DELAY;
		currentRightScaleState = RightScaleStates.DELAY;
		currentRightCrossLineState = RightCrossLineStates.DELAY;
	}
	
	
	public enum LeftSwitchStates {
		DELAY, DRIVING_FORWARD, TURNING, DRIVING_FORWARD_AGAIN, DROP_CUBE
	}
	
	LeftSwitchStates currentLeftSwitchState = LeftSwitchStates.DELAY;
	
	public void leftSwitch() {
		switch (currentLeftSwitchState) {
		case DELAY:
			if (delayTimer.get() > autoDelay) {
				currentLeftSwitchState = LeftSwitchStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches < leftSwitchForwardDistance1) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				navX.reset();
				doneTurning = false;
				currentLeftSwitchState = LeftSwitchStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				rightTurn();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentLeftSwitchState = LeftSwitchStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches < leftSwitchForwardDistance2) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				outtakeTimer.start();
				currentLeftSwitchState = LeftSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (outtakeTimer.get() < 5) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
			} else {
				outtakeTimer.stop();
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum LeftScaleStates {
		DELAY, DRIVING_FORWARD, LIFTING, DROP_ARM, DROP_CUBE
	}
	
	LeftScaleStates currentLeftScaleState = LeftScaleStates.DELAY;
	
	public void leftScale() {
		switch(currentLeftScaleState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftScaleState = LeftScaleStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= leftScaleForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				liftTimer.start();
				currentLeftScaleState = LeftScaleStates.LIFTING;
			}
			break;
		case LIFTING:
			if (liftTimer.get() <= leftScaleLiftTime) {
				elevatorMasterMotor.set(.5);
			} else {
				elevatorMasterMotor.set(0);
				currentLeftScaleState = LeftScaleStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
			currentLeftScaleState = LeftScaleStates.DROP_CUBE;
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
			break;
		}
	}
	
	
	public enum LeftExchangeStates {
		DELAY, DRIVING_FORWARD1, TURNING1, DRIVING_FORWARD2, TURNING2, DRIVING_FORWARD3, DROP_ARM, RELEASE_CUBE
	}
	
	LeftExchangeStates currentLeftExchangeState = LeftExchangeStates.DELAY;
	
	public void leftExchange() {
		switch (currentLeftExchangeState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentLeftExchangeState = LeftExchangeStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= leftExchangeForwardDistance1) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentLeftExchangeState = LeftExchangeStates.TURNING1;
			}
			break;
		case TURNING1:
			if (navX.getAngle() <= leftExchangeTurnAngle1) {
				robotDrive.curvatureDrive(autoDriveSpeed, -autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentLeftExchangeState = LeftExchangeStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= leftExchangeForwardDistance2) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentLeftExchangeState = LeftExchangeStates.TURNING2;
			}
			break;
		case TURNING2:
			if (navX.getAngle() <= leftExchangeTurnAngle2) {
				robotDrive.curvatureDrive(autoDriveSpeed, -autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentLeftExchangeState = LeftExchangeStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= leftExchangeForwardDistance3) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentLeftExchangeState = LeftExchangeStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
			currentLeftExchangeState = LeftExchangeStates.RELEASE_CUBE;
			break;
		case RELEASE_CUBE:
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
	
	
	public enum MiddleSwitchLeftStates {
		DELAY, DRIVING_FORWARD1, TURNING1, DRIVING_FORWARD2, TURNING2, DRIVING_FORWARD3, DROP_CUBE
	}
	
	MiddleSwitchLeftStates currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DELAY;
	
	public void middleSwitchLeft() {
		switch (currentMiddleSwitchLeftState) {
		case DELAY:
			if (delayTimer.get() > autoDelay) {
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches < middleSwitchLeftForwardDistance1) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				navX.reset();
				turningStage2 = false;
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURNING1;
			}
			break;
		case TURNING1:
			if (!turningStage2) {
				if (navX.getAngle() > middleSwitchLeftTurnAngle1 + TURNING_GYRO_OFFSET) {
					robotDrive.curvatureDrive(0, autoTurnSpeed, true);
				} else if (navX.getAngle() > middleSwitchLeftTurnAngle1 + TURNING_GYRO_OFFSET2) {
					robotDrive.curvatureDrive(0, .4, true);
				} else {
					turningStage2 = true;
					robotDrive.curvatureDrive(0, 0, false);
				} 
			} else {
				if (navX.getAngle() < (middleSwitchLeftTurnAngle1) - TURNING_GYRO_OFFSET3) {
					robotDrive.curvatureDrive(0, -.2, true);
				} else {
					robotDrive.curvatureDrive(0, 0, false);
					resetEncoder();
					currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD2;
				}
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches < middleSwitchLeftForwardDistance2) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				navX.reset();
				turningStage2 = false;
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.TURNING2;
			}
			break;
		case TURNING2:
			if (!turningStage2) {
				if (navX.getAngle() < middleSwitchLeftTurnAngle2 - TURNING_GYRO_OFFSET) {
					robotDrive.curvatureDrive(0, -autoTurnSpeed, true);
				} else if (navX.getAngle() < middleSwitchLeftTurnAngle2 - TURNING_GYRO_OFFSET2) {
					robotDrive.curvatureDrive(0, -.4, true);
				} else {
					turningStage2 = true;
					robotDrive.curvatureDrive(0, 0, false);
				} 
			} else {
				if (navX.getAngle() > (middleSwitchLeftTurnAngle2) + TURNING_GYRO_OFFSET3) {
					robotDrive.curvatureDrive(0, .2, true);
				} else {
					robotDrive.curvatureDrive(0, 0, false);
					resetEncoder();
					currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DRIVING_FORWARD3;
				}
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches < middleSwitchLeftForwardDistance3) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				outtakeTimer.start();
				currentMiddleSwitchLeftState = MiddleSwitchLeftStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (outtakeTimer.get() < 5) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
			} else {
				outtakeTimer.stop();
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum MiddleSwitchRightStates {
		DELAY, DRIVING_FORWARD, DROP_CUBE
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
				robotDrive.curvatureDrive(0, 0, false);
				outtakeTimer.start();
				currentMiddleSwitchRightState = MiddleSwitchRightStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
//			if (outtakeTimer.get() < 5) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
//			} else {
//				outtakeTimer.stop();
//				leftIntakeMotor.set(0);
//				rightIntakeMotor.set(0);
//			}
			break;
		}
	}
	
	
	public enum MiddleExchangeStates {
		DELAY, DRIVING_FORWARD1, TURNING1, DRIVING_FORWARD2, TURNING2, DRIVING_FORWARD3, DROP_ARM, RELEASE_CUBE
	}
	
	MiddleExchangeStates currentMiddleExchangeState = MiddleExchangeStates.DELAY;
	
	public void middleExchange() {
		switch (currentMiddleExchangeState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentMiddleExchangeState = MiddleExchangeStates.DRIVING_FORWARD1;
			}
			break;
		case DRIVING_FORWARD1:
			if (encoderAverageInches <= middleExchangeForwardDistance1) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentMiddleExchangeState = MiddleExchangeStates.TURNING1;
			}
			break;
		case TURNING1:
			if (navX.getAngle() >= middleExchangeTurnAngle1) {
				robotDrive.curvatureDrive(autoDriveSpeed, autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentMiddleExchangeState = MiddleExchangeStates.DRIVING_FORWARD2;
			}
			break;
		case DRIVING_FORWARD2:
			if (encoderAverageInches <= middleExchangeForwardDistance2) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentMiddleExchangeState = MiddleExchangeStates.TURNING2;
			}
			break;
		case TURNING2:
			if (navX.getAngle() >= middleExchangeTurnAngle2) {
				robotDrive.curvatureDrive(autoDriveSpeed, autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentMiddleExchangeState = MiddleExchangeStates.DRIVING_FORWARD3;
			}
			break;
		case DRIVING_FORWARD3:
			if (encoderAverageInches <= middleExchangeForwardDistance3) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentMiddleExchangeState = MiddleExchangeStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
			currentMiddleExchangeState = MiddleExchangeStates.RELEASE_CUBE;
			break;
		case RELEASE_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
//			System.out.println(SmartDashboard.getNumber("Encoder Average Inches", encoderAverageInches));
			System.out.println("Encoders: " + encoderAverageInches);
//			System.out.println("Distance" + middleCrossLineForwardDistance);
			if (encoderAverageInches <= middleCrossLineForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}
	
	
	public enum RightSwitchStates {
		DELAY, DRIVING_FORWARD, TURNING, DRIVING_FORWARD_AGAIN, DROP_CUBE
	}
	
	RightSwitchStates currentRightSwitchState = RightSwitchStates.DELAY;
	
	public void rightSwitch() {
		switch (currentRightSwitchState) {
		case DELAY:
			if (delayTimer.get() > autoDelay) {
				currentRightSwitchState = RightSwitchStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches < rightSwitchForwardDistance1) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				navX.reset();
				doneTurning = false;
				currentRightSwitchState = RightSwitchStates.TURNING;
			}
			break;
		case TURNING:
			if (!doneTurning) {
				leftTurn();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				resetEncoder();
				currentRightSwitchState = RightSwitchStates.DRIVING_FORWARD_AGAIN;
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches < rightSwitchForwardDistance2) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				outtakeTimer.start();
				currentRightSwitchState = RightSwitchStates.DROP_CUBE;
			}
			break;
		case DROP_CUBE:
			if (outtakeTimer.get() < 5) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
			} else {
				outtakeTimer.stop();
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
			}
			break;
		}
	}
	
	
	public enum RightScaleStates {
		DELAY, DRIVING_FORWARD, LIFTING, DROP_ARM, DROP_CUBE
	}
	
	RightScaleStates currentRightScaleState = RightScaleStates.DELAY;
	
	public void rightScale() {
		switch(currentRightScaleState) {
		case DELAY:
			if (delayTimer.get() >= autoDelay) {
				currentRightScaleState = RightScaleStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= rightScaleForwardDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				liftTimer.start();
				currentRightScaleState = RightScaleStates.LIFTING;
			}
			break;
		case LIFTING:
			if (liftTimer.get() <= rightScaleLiftTime) {
				elevatorMasterMotor.set(.5);
			} else {
				elevatorMasterMotor.set(0);
				currentRightScaleState = RightScaleStates.DROP_ARM;
			}
			break;
		case DROP_ARM:
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
			currentLeftScaleState = LeftScaleStates.DROP_CUBE;
			break;
		case DROP_CUBE:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
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
	
	
	public enum TurningStates{
		HIGH_SPEED, LOW_SPEED, BACK
	}
	
	TurningStates currentTurningState = TurningStates.HIGH_SPEED;
	
	public void leftTurn() {
		switch(currentTurningState) {
		case HIGH_SPEED:
			if (navX.getAngle() > (-turnAngle) + TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.LOW_SPEED;
			}
			break;
		case LOW_SPEED:
			if (navX.getAngle() > (-turnAngle) + TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, .4, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.BACK;
			}
			break;
		case BACK:
			if (navX.getAngle() < (-turnAngle) - TURNING_GYRO_OFFSET3) {
				robotDrive.curvatureDrive(0, -.2, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				doneTurning = true;
			}
			break;
		}
	}
	
	public void rightTurn() {
		switch(currentTurningState) {
		case HIGH_SPEED:
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, -autoTurnSpeed, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.LOW_SPEED;
			}
			break;
		case LOW_SPEED:
			if (navX.getAngle() < turnAngle - TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, -.4, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				currentTurningState = TurningStates.BACK;
			}
			break;
		case BACK:
			if (navX.getAngle() > (turnAngle) + TURNING_GYRO_OFFSET3) {
				robotDrive.curvatureDrive(0, .2, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
				doneTurning = true;
			}
			break;
		}
	}
	
	
	public void turningTest() {
//		System.out.println("Gyro: " + navX.getAngle());
//		System.out.println("angle: " + turnTestAngle);
		//left
		if (!turningStage2) {
			if (navX.getAngle() > (-turnTestAngle) + TURNING_GYRO_OFFSET) {
				robotDrive.curvatureDrive(0, autoTurnSpeed, true);
			} else if (navX.getAngle() > (-turnTestAngle) + TURNING_GYRO_OFFSET2) {
				robotDrive.curvatureDrive(0, .4, true);
			} else {
				turningStage2 = true;
				robotDrive.curvatureDrive(0, 0, false);
			}
			//right
	//		if (navX.getAngle() < turnTestAngle - TURNING_GYRO_OFFSET) {
	//			robotDrive.curvatureDrive(0, -autoTurnSpeed, true);
	//		} else if (navX.getAngle() < turnTestAngle - TURNING_GYRO_OFFSET2) {
	//			robotDrive.curvatureDrive(0, -.4, true);
	//		} else {
	//			turningStage2 = true;
	//			robotDrive.curvatureDrive(0, 0, false);
	//		}
		} else {
			//left
			if (navX.getAngle() < (-turnTestAngle) - TURNING_GYRO_OFFSET3) {
				robotDrive.curvatureDrive(0, -.2, true);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			
			//right
//			if (navX.getAngle() > (turnTestAngle) + TURNING_GYRO_OFFSET3) {
//				robotDrive.curvatureDrive(0, .2, true);
//			} else {
//				robotDrive.curvatureDrive(0, 0, false);
//			}
		}
	}

	public double getMiddleCrossLineForwardDistance() {
		return middleCrossLineForwardDistance;
	}

	public void setMiddleCrossLineForwardDistance(double middleCrossLineForwardDistance) {
		this.middleCrossLineForwardDistance = middleCrossLineForwardDistance;
	}
	
	
}
