/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	static int MOBILITY_STICK_PORT;
	static int DRIVE_FORWARD_AXIS;
	static int DRIVE_TURN_AXIS;
	static int HIGH_GEAR_BUTTON;
	static int LOW_GEAR_BUTTON;

	static int LEFT_DRIVE_SOLENOID_ID;
	static int RIGHT_DRIVE_SOLENOID_ID;

	static int FRONT_LEFT_MOTOR_ID;
	static int REAR_LEFT_MOTOR_ID;
	static int FRONT_RIGHT_MOTOR_ID;
	static int REAR_RIGHT_MOTOR_ID;

	Joystick mobilityStick;
	double driveForwardAxis;
	double driveTurnAxis;
	
	Solenoid leftDriveSolenoid;
	Solenoid rightDriveSolenoid;

	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;
	SpeedControllerGroup leftMotors;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	SpeedControllerGroup rightMotors;

	DifferentialDrive robotDrive;

	Drive drive;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		mobilityStick = new Joystick(MOBILITY_STICK_PORT);

		leftDriveSolenoid = new Solenoid(LEFT_DRIVE_SOLENOID_ID);
		rightDriveSolenoid = new Solenoid(RIGHT_DRIVE_SOLENOID_ID);

		frontLeftMotor = new WPI_TalonSRX(FRONT_LEFT_MOTOR_ID);
		rearLeftMotor = new WPI_TalonSRX(REAR_LEFT_MOTOR_ID);
		leftMotors = new SpeedControllerGroup(frontLeftMotor, rearRightMotor);

		frontRightMotor = new WPI_TalonSRX(FRONT_RIGHT_MOTOR_ID);
		rearRightMotor = new WPI_TalonSRX(REAR_RIGHT_MOTOR_ID);
		rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);

		robotDrive = new DifferentialDrive(leftMotors, rightMotors);

		drive = new Drive(this);
		
		drive.setSolenoids(false);
		
		setInverts();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		drive.arcadeDrive();
		drive.changeGear();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void displaySettings() {
		SmartDashboard.putNumber("Drive Speed", 1.0);
	}
	
	public void setInverts() {
		driveForwardAxis *= 1;
		driveTurnAxis *= 1;
		
		frontLeftMotor.setInverted(false);
		rearLeftMotor.setInverted(false);
		frontRightMotor.setInverted(false);
		rearRightMotor.setInverted(false);
	}
	
	public Joystick getMobilityStick() {
		return mobilityStick;
	}

	public double getDriveForwardAxis() {
		return driveForwardAxis;
	}

	public double getDriveTurnAxis() {
		return driveTurnAxis;
	}

	public DifferentialDrive getRobotDrive() {
		return robotDrive;
	}

	public Solenoid getLeftDriveSolenoid() {
		return leftDriveSolenoid;
	}

	public Solenoid getRightDriveSolenoid() {
		return rightDriveSolenoid;
	}
}
