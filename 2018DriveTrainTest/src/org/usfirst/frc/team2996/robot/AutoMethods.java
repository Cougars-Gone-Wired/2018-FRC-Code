package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMethods {

	// variables to be added as objects to the sendable chooser
	static final String doNothing = "Do Nothing";
	static final String driveForwardCrossLine = "Drive Forward Cross Line";
	static final String driveForwardDropCubeOrNah = "Drive Forward Drop Cube Or Nah";
	static final String driveForwardTurnDropCubeOrNah = "Drive Forward Turn Drop Cube Or Nah";
	String selectedAuto; // variable to hold the auto selected on the SmartDasboard
	SendableChooser<String> autoChooser = new SendableChooser<>(); // sendable chooser to hold dead reckoning auto
																	// options
	// variables to calculate how far we should move per each encoder tick
	static final int WHEEL_DIAMETER = 6; // in inches
	static final int ENCODER_TICKS_PER_REV = 360 * 4;
	static final double DISTANCE_PER_ENCODER_TICK = ((WHEEL_DIAMETER * Math.PI) / ENCODER_TICKS_PER_REV); // in inches
	
	static final double GYRO_CONSTANT = 0.03;

	public enum DriveForwardCrossLineStates { // states the robot can be in in this auto
		DELAY, DRIVING_FORWARD
	}

	// default state for this auto
	DriveForwardCrossLineStates currentDriveForwardCrossLineState = DriveForwardCrossLineStates.DELAY;

	public enum DriveForwardDropCubeOrNahStates { // states the robot can be in in this auto
		DELAY, DRIVING_FORWARD, DROP_CUBE_OR_NAH
	}

	// default state for this auto
	DriveForwardDropCubeOrNahStates currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DELAY;

	public enum DriveForwardTurnDropCubeOrNahStates { // states the robot can be in in this auto
		DELAY, DRIVING_FORWARD, TURNING, DRIVING_FORWARD_AGAIN, DROP_CUBE_OR_NAH
	}

	// default state for this auto
	DriveForwardTurnDropCubeOrNahStates currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DELAY;

	// declarations of objects used in this class
	private Timer delayTimer;

	private DifferentialDrive robotDrive;

	private SensorCollection frontLeftSensors;
	private SensorCollection frontRightSensors;
//	private WPI_TalonSRX leftIntakeMotor;
//	private WPI_TalonSRX rightIntakeMotor;

	private AHRS navX;

	// declarations of variables used in this class
	String sides; // to hold our starting position
	boolean shouldDropCube; // to hold whether we should drop the cube

	int frontLeftEncoder; // to hold the encoder ticks for the left side
	int frontRightEncoder; // to hold the encoder ticks for the right side
	int encoderAverage; // to hold the average encoder ticks between both sides
	double encoderAverageInches; // to hold the average distance that each side has traveled 
	
	double angle;

	public AutoMethods(Robot robot) {
		// instantiation for timer
		delayTimer = new Timer();

		// assigning all other previously declared objects to objects instantiated in
		// other classes
		robotDrive = robot.getDrive().getRobotDrive();

		frontLeftSensors = robot.getDrive().getFrontLeftSensors();
		frontRightSensors = robot.getDrive().getFrontRightSensors();

		// instantiation for navX
		navX = new AHRS(SPI.Port.kMXP);

		// methods to be called in robotInit
		putAutoOptions();
		autoReset();
	}

	public void driveForwardCrossLine() { // program to drive forward enough to cross the base line
		encoderSet();
		switch (currentDriveForwardCrossLineState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				SmartDashboard.putNumber("Delay Timer", delayTimer.get());
				currentDriveForwardCrossLineState = DriveForwardCrossLineStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= SmartDashboardSettings.driveForwardCrossLineDistance) {
				gyroCorrect();
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}

	// method to drive forward, go up against the switch, and drop the cube or not
	public void driveForwardDropCubeOrNah() {
		encoderSet();
		switch (currentDriveForwardDropCubeOrNahState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= SmartDashboardSettings.driveForwardDropCubeorNahForwardDistance) {
				gyroCorrect();
			} else {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DROP_CUBE_OR_NAH;
			}
			break;
		case DROP_CUBE_OR_NAH:
			robotDrive.curvatureDrive(0, 0, false);
			if (shouldDropCube == true) {
//				leftIntakeMotor.set(1);
//				rightIntakeMotor.set(-1);
			}
			break;
		}
	}

	// method to drive forward, turn towards the switch, drive forward again, and
	// drop the cube or not
	// intended for a far left or far right starting position
	public void driveForwardTurnDropCubeOrNah() {
		encoderSet();
		switch (currentDriveForwardTurnDropCubeOrNahState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverageInches <= SmartDashboardSettings.driveForwardTurnDropCubeorNahForwardDistance1) {
				gyroCorrect();
			} else {
				currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.TURNING;
			}
			break;
		case TURNING:
			if (SmartDashboardSettings.autoStartSide.toLowerCase() == "left") {
				if (navX.getAngle() <= 90) {
					robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed,
							SmartDashboardSettings.autoTurnSpeed, true);
				} else {
					currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DRIVING_FORWARD_AGAIN;
				}
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase() == "right") {
				if (navX.getAngle() <= -90) {
					robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed,
							SmartDashboardSettings.autoTurnSpeed, true);
				} else {
					currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DRIVING_FORWARD_AGAIN;
				}
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverageInches <= SmartDashboardSettings.driveForwardTurnDropCubeorNahForwardDistance2) {
				gyroCorrect();
			} else {
				currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DROP_CUBE_OR_NAH;
			}
			break;
		case DROP_CUBE_OR_NAH:
			robotDrive.curvatureDrive(0, 0, false);
			if (shouldDropCube == true) {
//				leftIntakeMotor.set(1);
//				rightIntakeMotor.set(-1);
			}
			break;
		}
	}

	// method to set the encoder variables to the actual outputs of the encoders and
	// average them
	public void encoderSet() {
		frontLeftEncoder = frontLeftSensors.getQuadraturePosition();
		frontRightEncoder = Utility.invertInt(frontRightSensors.getQuadraturePosition());
		encoderAverage = (frontLeftEncoder + frontRightEncoder) / 2;
		encoderAverageInches = encoderAverage * DISTANCE_PER_ENCODER_TICK;
	}
	
	public void gyroCorrect() {
		angle = navX.getAngle();
		robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, angle * GYRO_CONSTANT, false);
		
	}
	
	public void showEncoderValues() {
		SmartDashboard.putNumber("Front Left Encoder", frontLeftEncoder);
		SmartDashboard.putNumber("Front Right Encoder", frontRightEncoder);
		SmartDashboard.putNumber("Encoder Average", encoderAverage);
		SmartDashboard.putNumber("Encoder Average Inches", encoderAverageInches);
	}

	// method to reset the timer, encoders, and gyro before auto
	public void autoReset() {
		delayTimer.reset();
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		encoderAverage = 0;
		encoderAverageInches = 0;
		navX.reset();
		currentDriveForwardCrossLineState = DriveForwardCrossLineStates.DELAY;
	}

	// methods to put options of autonomous programs on SmartDashboard
	public void putAutoOptions() {
		autoChooser.addObject("Do Nothing", doNothing);
		autoChooser.addObject("Drive Forward Cross Line", driveForwardCrossLine);
		autoChooser.addObject("Drive Forward Drop Cube Or Nah", driveForwardDropCubeOrNah);
		autoChooser.addObject("Drive Forward Turn Drop Cube Or Nah", driveForwardTurnDropCubeOrNah);
		SmartDashboard.putData("Auto choices", autoChooser);
	}

	// method to get the game data from the FMS for which sides the colors are on
	// for the switches and scale
	public void getInfo() {
		selectedAuto = autoChooser.getSelected();
		sides = DriverStation.getInstance().getGameSpecificMessage();
		if (sides.charAt(0) == 'L') {
			if (SmartDashboardSettings.autoStartSide.toLowerCase().charAt(0) == 'l') {
				shouldDropCube = true;
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase().charAt(0) == 'r') {
				shouldDropCube = false;
			}
		} else if (sides.charAt(0) == 'R') {
			if (SmartDashboardSettings.autoStartSide.toLowerCase().charAt(0) == 'l') {
				shouldDropCube = false;
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase().charAt(0) == 'r') {
				shouldDropCube = true;
			}
		}
	}

	// method to start the delay timer
	public void startDelayTimer() {
		delayTimer.start();
	}

	// method to run the auto program that was picked on the SmartDashboard
	public void pickAuto() {
		selectedAuto = autoChooser.getSelected();
		showEncoderValues();
		switch (selectedAuto) {
		case doNothing:

			break;
		case driveForwardCrossLine:
			driveForwardCrossLine();
			break;
		case driveForwardDropCubeOrNah:
			driveForwardDropCubeOrNah();
			break;
		case driveForwardTurnDropCubeOrNah:
			driveForwardTurnDropCubeOrNah();
			break;
		}
	}

	// getters for the objects instantiated in this class
	public Timer getDelayTimer() {
		return delayTimer;
	}

	public AHRS getNavX() {
		return navX;
	}

	public int getFrontLeftEncoder() {
		return frontLeftEncoder;
	}

	public int getFrontRightEncoder() {
		return frontRightEncoder;
	}

	public int getEncoderAverage() {
		return encoderAverage;
	}

	public double getEncoderAverageInches() {
		return encoderAverageInches;
	}

}
