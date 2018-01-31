package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	private Joystick mobilityStick;
	double driveForwardAxis;
	double driveTurnAxis;
	
	private DifferentialDrive robotDrive;
	
	public Drive(Robot robot) {
		this.mobilityStick = robot.getMobilityStick();
		this.robotDrive = robot.getRobotDrive();
	}

	public void arcadeDrive() {
		setDriveSpeed();
		robotDrive.arcadeDrive(driveForwardAxis, driveTurnAxis);
	}
	
	public void setDriveSpeed() {
		driveForwardAxis = Utility.deadZone(mobilityStick.getRawAxis(Robot.DRIVE_FORWARD_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0));
		driveTurnAxis = Utility.deadZone(mobilityStick.getRawAxis(Robot.DRIVE_TURN_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0));
	}
}
