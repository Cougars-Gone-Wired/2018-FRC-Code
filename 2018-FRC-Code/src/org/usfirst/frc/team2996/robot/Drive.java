package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	private Joystick mobilityStick;
	private double driveForwardAxis;
	private double driveTurnAxis;
	
	private DifferentialDrive robotDrive;

	private Solenoid leftDriveSolenoid;
	private Solenoid rightDriveSolenoid;

	public Drive(Robot robot) {
		this.mobilityStick = robot.getMobilityStick();
		this.driveForwardAxis = robot.getDriveForwardAxis();
		this.driveTurnAxis = robot.getDriveTurnAxis();
		this.robotDrive = robot.getRobotDrive();
		this.leftDriveSolenoid = robot.getLeftDriveSolenoid();
		this.rightDriveSolenoid = robot.getRightDriveSolenoid();
	}

	public void arcadeDrive() {
		setDriveSpeed();
		robotDrive.arcadeDrive(driveForwardAxis, driveTurnAxis);
	}

	public void changeGear() {
		if (mobilityStick.getRawButton(Robot.HIGH_GEAR_BUTTON) 
				&& !mobilityStick.getRawButton(Robot.LOW_GEAR_BUTTON)) {
			setSolenoids(true);
		} else if (mobilityStick.getRawButton(Robot.LOW_GEAR_BUTTON)
				&& !mobilityStick.getRawButton(Robot.HIGH_GEAR_BUTTON)) {
			setSolenoids(false);
		}
	}

	public void setDriveSpeed() {
		driveForwardAxis = mobilityStick.getRawAxis(Robot.DRIVE_FORWARD_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0);
		driveTurnAxis = mobilityStick.getRawAxis(Robot.DRIVE_TURN_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0);
	}
	
	public void setSolenoids(boolean state) {
		leftDriveSolenoid.set(state);
		rightDriveSolenoid.set(state);
	}
}
