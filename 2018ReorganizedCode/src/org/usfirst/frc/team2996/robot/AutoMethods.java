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

public class AutoMethods {

	static final String doNothing = "Do Nothing";
	static final String driveForwardCrossLine = "Drive Forward Cross Line";
	static final String driveForwardDropCubeOrNah = "Drive Forward Drop Cube Or Nah";
	static final String driveForwardTurnDropCubeOrNah = "Drive Forward Turn Drop Cube Or Nah";
	String selectedAuto;
	SendableChooser<String> autoChooser = new SendableChooser<>();

	static final int WHEEL_DIAMETER = 6; // in inches
	static final int ENCODER_TICKS_PER_REV = 360 * 4;
	static final double DISTNACE_PER_ENCODER_TICK = ((WHEEL_DIAMETER * Math.PI) / ENCODER_TICKS_PER_REV); // in inches

	public enum DriveForwardCrossLineStates {
		DELAY, DRIVING_FORWARD
	}
	DriveForwardCrossLineStates currentDriveForwardCrossLineState = DriveForwardCrossLineStates.DELAY;
	
	public enum DriveForwardDropCubeOrNahStates {
		DELAY, DRIVING_FORWARD, DROP_CUBE_OR_NAH
	}
	DriveForwardDropCubeOrNahStates currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DELAY;

	public enum DriveForwardTurnDropCubeOrNahStates {
		DELAY, DRIVING_FORWARD, TURNING, DRIVING_FORWARD_AGAIN, DROP_CUBE_OR_NAH
	}
	DriveForwardTurnDropCubeOrNahStates currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DELAY;
	
	private Timer delayTimer;
	
	private DifferentialDrive robotDrive;

	private SensorCollection frontLeftSensors;
	private SensorCollection frontRightSensors;
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;

	private AHRS navX;
	
	String sides;
	boolean shouldDropCube;

	int frontLeftEncoder;
	int frontRightEncoder;
	int encoderAverage;
	
	public AutoMethods(Robot robot) {
		delayTimer = new Timer();
		
		robotDrive = robot.getDrive().getRobotDrive();
		
		frontLeftSensors = robot.getDrive().getFrontLeftSensors();
		frontRightSensors = robot.getDrive().getFrontRightSensors();
		leftIntakeMotor = robot.getIntake().getLeftIntakeMotor();
		rightIntakeMotor = robot.getIntake().getRightIntakeMotor();
		
		navX = new AHRS(SPI.Port.kMXP);
		
		putAutoOptions();
		autoReset();
	}
	
	public void driveForwardCrossLine() {
		encoderSet();
		switch (currentDriveForwardCrossLineState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverage <= SmartDashboardSettings.driveForwardCrossLineDistance) {
				robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, 0, false);
			} else {
				robotDrive.curvatureDrive(0, 0, false);
			}
			break;
		}
	}

	public void driveForwardDropCubeOrNah() {
		encoderSet();
		switch (currentDriveForwardDropCubeOrNahState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverage <= SmartDashboardSettings.driveForwardDropCubeorNahForwardDistance) {
				robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, 0, false);
			} else {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DROP_CUBE_OR_NAH;
			}
			break;
		case DROP_CUBE_OR_NAH:
			robotDrive.curvatureDrive(0, 0, false);
			if (shouldDropCube == true) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
			}
			break;
		}
	}

	public void driveForwardTurnDropCubeOrNah() {
		encoderSet();
		switch (currentDriveForwardTurnDropCubeOrNahState) {
		case DELAY:
			if (delayTimer.get() >= SmartDashboardSettings.autoDelay) {
				currentDriveForwardDropCubeOrNahState = DriveForwardDropCubeOrNahStates.DRIVING_FORWARD;
			}
			break;
		case DRIVING_FORWARD:
			if (encoderAverage <= SmartDashboardSettings.driveForwardTurnDropCubeorNahForwardDistance1) {
				robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, 0, false);
			} else {
				currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.TURNING;
			}
			break;
		case TURNING:
			if (SmartDashboardSettings.autoStartSide.toLowerCase() == "left") {
				if (navX.getAngle() <= 90) {
					robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, SmartDashboardSettings.autoTurnSpeed, true);
				} else {
					currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DRIVING_FORWARD_AGAIN;
				}
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase() == "right") {
				if (navX.getAngle() <= -90) {
					robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, SmartDashboardSettings.autoTurnSpeed, true);
				} else {
					currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DRIVING_FORWARD_AGAIN;
				}
			}
			break;
		case DRIVING_FORWARD_AGAIN:
			if (encoderAverage <= SmartDashboardSettings.driveForwardTurnDropCubeorNahForwardDistance2) {
				robotDrive.curvatureDrive(SmartDashboardSettings.autoDriveSpeed, 0, false);
			} else {
				currentDriveForwardTurnDropCubeOrNahState = DriveForwardTurnDropCubeOrNahStates.DROP_CUBE_OR_NAH;
			}
			break;
		case DROP_CUBE_OR_NAH:
			robotDrive.curvatureDrive(0, 0, false);
			if (shouldDropCube == true) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
			}
			break;
		}
	}

	public void encoderSet() {
		frontLeftEncoder = frontLeftSensors.getQuadraturePosition();
		frontRightEncoder = frontRightSensors.getQuadraturePosition();
		encoderAverage = (frontLeftEncoder + frontRightEncoder) / 2;
	}

	public void autoReset() {
		delayTimer.reset();
		frontLeftSensors.setQuadraturePosition(0, 10);
		frontRightSensors.setQuadraturePosition(0, 10);
		navX.reset();
	}

	public void putAutoOptions() {
		autoChooser.addObject("Do Nothing", doNothing);
		autoChooser.addObject("Drive Forward Cross Line", driveForwardCrossLine);
		autoChooser.addObject("Drive Forward Drop Cube Or Nah", driveForwardDropCubeOrNah);
		autoChooser.addObject("Drive Forward Turn Drop Cube Or Nah", driveForwardTurnDropCubeOrNah);
		SmartDashboard.putData("Auto choices", autoChooser);
	}

	public void getInfo() {
		sides = DriverStation.getInstance().getGameSpecificMessage();
		if (sides.charAt(0) == 'L') {
			if (SmartDashboardSettings.autoStartSide.toLowerCase() == "left") {
				shouldDropCube = true;
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase() == "right") {
				shouldDropCube = false;
			}
		} else if (sides.charAt(0) == 'R') {
			if (SmartDashboardSettings.autoStartSide.toLowerCase() == "left") {
				shouldDropCube = false;
			} else if (SmartDashboardSettings.autoStartSide.toLowerCase() == "right") {
				shouldDropCube = true;
			}
		}
	}
	
	public void startDelayTimer() {
		delayTimer.start();
	}

	public void pickAuto() {
		selectedAuto = autoChooser.getSelected();
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

	public AHRS getNavX() {
		return navX;
	}
	
}
