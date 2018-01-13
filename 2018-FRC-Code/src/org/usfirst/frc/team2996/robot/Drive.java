package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {

	private Joystick mobilityStick;

	private DifferentialDrive robotDrive;

	private Solenoid leftDriveSolenoid;
	private Solenoid rightDriveSolenoid;

	public Drive(Robot robot) {
		this.mobilityStick = robot.getMobilityStick();
		this.robotDrive = robot.getRobotDrive();
		this.leftDriveSolenoid = robot.getLeftDriveSolenoid();
		this.rightDriveSolenoid = robot.getRightDriveSolenoid();
	}

	public void arcadeDrive() {
		robotDrive.arcadeDrive(mobilityStick.getRawAxis(Robot.DRIVE_FORWARD_AXIS),
				mobilityStick.getRawAxis(Robot.DRIVE_TURN_AXIS));
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

	public void setSolenoids(boolean state) {
		leftDriveSolenoid.set(state);
		rightDriveSolenoid.set(state);
	}
}
