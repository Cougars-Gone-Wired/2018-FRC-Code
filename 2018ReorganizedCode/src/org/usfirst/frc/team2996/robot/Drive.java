package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {

	private WPI_TalonSRX frontLeftMotor;
	private WPI_TalonSRX rearLeftMotor;
	private SpeedControllerGroup leftMotors;
	private SensorCollection frontLeftSensors;
	
	private WPI_TalonSRX frontRightMotor;
	private WPI_TalonSRX rearRightMotor;
	private SpeedControllerGroup rightMotors;
	private SensorCollection frontRightSensors;

	private DifferentialDrive robotDrive;
	
	private Solenoid changeDriveGearSolenoid;

	public Drive() {
		frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
		rearLeftMotor = new WPI_TalonSRX(Constants.REAR_LEFT_MOTOR_ID);
		leftMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
		frontLeftSensors = new SensorCollection(frontLeftMotor);

		frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_ID);
		rearRightMotor = new WPI_TalonSRX(Constants.REAR_RIGHT_MOTOR_ID);
		rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
		frontRightSensors = new SensorCollection(frontRightMotor);

		robotDrive = new DifferentialDrive(leftMotors, rightMotors);
		
		changeDriveGearSolenoid = new Solenoid(Constants.CHANGE_DRIVE_GEAR_SOLENOID_PORT);
	}

	public void arcadeDrive(double driveForwardAxis, double driveTurnAxis) {
		setDriveSpeed(driveForwardAxis, driveForwardAxis);
		robotDrive.arcadeDrive(driveForwardAxis, driveTurnAxis);
	}

	public void setDriveSpeed(double driveForwardAxis, double driveTurnAxis) {
		driveForwardAxis = Utility.deadZone(driveForwardAxis * SmartDashboardSettings.driveSpeed);
		driveTurnAxis = Utility.deadZone(driveTurnAxis * SmartDashboardSettings.driveSpeed);
	}

	public WPI_TalonSRX getFrontLeftMotor() {
		return frontLeftMotor;
	}

	public WPI_TalonSRX getRearLeftMotor() {
		return rearLeftMotor;
	}

	public SpeedControllerGroup getLeftMotors() {
		return leftMotors;
	}

	public SensorCollection getFrontLeftSensors() {
		return frontLeftSensors;
	}

	public WPI_TalonSRX getFrontRightMotor() {
		return frontRightMotor;
	}

	public WPI_TalonSRX getRearRightMotor() {
		return rearRightMotor;
	}

	public SpeedControllerGroup getRightMotors() {
		return rightMotors;
	}

	public SensorCollection getFrontRightSensors() {
		return frontRightSensors;
	}

	public DifferentialDrive getRobotDrive() {
		return robotDrive;
	}

	public Solenoid getChangeDriveGearSolenoid() {
		return changeDriveGearSolenoid;
	}

}
