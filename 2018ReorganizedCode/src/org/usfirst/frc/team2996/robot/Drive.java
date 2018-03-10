package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	// declarations for all objects associated with the drive train
	private WPI_TalonSRX frontLeftMotor;
	private WPI_TalonSRX rearLeftMotor;
	private SpeedControllerGroup leftMotors;
	private SensorCollection frontLeftSensors; // for encoders (not used in this class)
	
	private WPI_TalonSRX frontRightMotor;
	private WPI_TalonSRX rearRightMotor;
	private SpeedControllerGroup rightMotors;
	private SensorCollection frontRightSensors; // for encoders (not used in this class)

	private DifferentialDrive robotDrive;
	
	private Solenoid changeDriveGearSolenoid;
	
	double driveForwardAxisValue;
	double driveTurnAxisValue;
	double leftDriveAxisValue;
	double rightDriveAxisValue;

	public Drive() {
		// instantiations of all previously declared objects
		frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
		rearLeftMotor = new WPI_TalonSRX(Constants.REAR_LEFT_MOTOR_ID);
		leftMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
		frontLeftSensors = new SensorCollection(frontLeftMotor);
//		frontLeftMotor.configOpenloopRamp(.25, 0);
//		rearLeftMotor.configOpenloopRamp(.25, 0);
		
		frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_ID);
		rearRightMotor = new WPI_TalonSRX(Constants.REAR_RIGHT_MOTOR_ID);
		rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
		frontRightSensors = new SensorCollection(frontRightMotor);
//		frontRightMotor.configOpenloopRamp(.25, 0);
//		rearRightMotor.configOpenloopRamp(.25, 0);

		robotDrive = new DifferentialDrive(leftMotors, rightMotors);
		
		changeDriveGearSolenoid = new Solenoid(Constants.CHANGE_DRIVE_GEAR_SOLENOID_PORT);
	}

	public void arcadeDrive(double driveForwardAxis, double driveTurnAxis) { // method for driving in arcade configuration 
		setArcadeDriveSpeed(driveForwardAxis, driveTurnAxis);
		robotDrive.arcadeDrive(driveForwardAxisValue, driveTurnAxisValue * .75);
		
//		SmartDashboard.putNumber("Left Encoder", frontLeftSensors.getQuadraturePosition());
//		SmartDashboard.putNumber("Right Encoder", frontRightSensors.getQuadraturePosition());
	}

	public void tankDrive(double leftDriveAxis, double rightDriveAxis) {
		setTankDriveSpeed(leftDriveAxis, rightDriveAxis);
		robotDrive.tankDrive(leftDriveAxisValue, rightDriveAxisValue);
	}
	
	public void setArcadeDriveSpeed(double driveForwardAxis, double driveTurnAxis) { // method for changing the drive speed if specified on the SmartDashboard
		driveForwardAxisValue = Utility.deadZone(driveForwardAxis * SmartDashboardSettings.driveSpeed);
		driveTurnAxisValue = Utility.invertDouble(Utility.deadZone(driveTurnAxis * SmartDashboardSettings.driveSpeed));
	}
	public void setTankDriveSpeed(double leftDriveAxis, double rightDriveAxis) { // method for changing the drive speed if specified on the SmartDashboard
		leftDriveAxisValue = Utility.deadZone(leftDriveAxis * SmartDashboardSettings.driveSpeed);
		rightDriveAxisValue = Utility.deadZone(rightDriveAxis * SmartDashboardSettings.driveSpeed);
	}

	
	// getters for all the objects declared in this class
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
