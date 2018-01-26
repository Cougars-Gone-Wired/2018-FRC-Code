package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	private Joystick mobilityStick;
	double driveForwardAxis;
	double driveTurnAxis;
	boolean highGearButton;
	boolean lowGearButton;
	
	private DifferentialDrive robotDrive;

	private Solenoid changeGearSolenoid;

	public enum GearStates {
		LOW_GEAR, HIGH_GEAR
	}
	GearStates gearCurrentState = GearStates.LOW_GEAR;
	
	public Drive(Robot robot) {
		this.mobilityStick = robot.getMobilityStick();
		this.robotDrive = robot.getRobotDrive();
		this.changeGearSolenoid = robot.getChangeGearSolenoid();
	}

	public void arcadeDrive() {
		setDriveSpeed();
		robotDrive.arcadeDrive(driveForwardAxis, driveTurnAxis);
	}

	public void changeGear() {
		highGearButton = mobilityStick.getRawButton(Robot.HIGH_GEAR_BUTTON);
		lowGearButton = mobilityStick.getRawButton(Robot.LOW_GEAR_BUTTON);
		switch (gearCurrentState) {
		case LOW_GEAR:
			if (highGearButton && !lowGearButton) {
				changeGearSolenoid.set(true);
				gearCurrentState = GearStates.HIGH_GEAR;
			}
			break;
		case HIGH_GEAR:
			if (!highGearButton && lowGearButton) {
				changeGearSolenoid.set(false);
				gearCurrentState = GearStates.LOW_GEAR;
			}
			break;
		}
	}

	public void setDriveSpeed() {
		driveForwardAxis = Utility.deadZone(mobilityStick.getRawAxis(Robot.DRIVE_FORWARD_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0));
		driveTurnAxis = Utility.deadZone(mobilityStick.getRawAxis(Robot.DRIVE_TURN_AXIS) * SmartDashboard.getNumber("Drive Speed", 1.0));
	}
}
