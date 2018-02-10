package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joysticks {

	// objects and variables for everything associated with the mobility controller
	private Joystick mobilityStick;
	private double driveForwardAxis;
	private double driveTurnAxis;

	public Joysticks() {
		mobilityStick = new Joystick(Constants.MOBILITY_STICK_PORT);
	}

	// method to set all the previously declared variables to the values being
	// inputted into the controllers
	public void setJoystickInputValues() {
		driveForwardAxis = mobilityStick.getRawAxis(Constants.DRIVE_FORWARD_AXIS);
		driveTurnAxis = mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS);
	}

	// getters for all objects and variables declared in this class
	public Joystick getMobilityStick() {
		return mobilityStick;
	}

	public double getDriveForwardAxis() {
		return driveForwardAxis;
	}

	public double getDriveTurnAxis() {
		return driveTurnAxis;
	}


}
