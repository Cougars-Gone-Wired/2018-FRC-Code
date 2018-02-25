package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joysticks {

	// objects for everything associated with the manipulator controller
	private Joystick manipulatorStick;
	private double elevatorAxis;
	private boolean elevatorHighGearButton;
	private boolean elevatorLowGearButton;
	private double intakeTrigger;
	private double outtakeTrigger;
	private Toggle armButton;
	private boolean armButtonOutput;

	// objects and variables for everything associated with the mobility controller
	private Joystick mobilityStick;
	private double driveForwardAxis;
	private double driveTurnAxis;
	private boolean driveHighGearButton;
	private boolean driveLowGearButton;
	private double leftDriveAxis;
	private double rightDriveAxis;

	public Joysticks() {
		// instantiations of all previously declared objects
		manipulatorStick = new Joystick(Constants.MANIPULATOR_STICK_PORT);
		armButton = new Toggle(manipulatorStick, Constants.ARM_BUTTON);

		mobilityStick = new Joystick(Constants.MOBILITY_STICK_PORT);
	}

	// method to set all the previously declared variables to the values being
	// inputted into the controllers
	public void setJoystickInputValues() {
		elevatorAxis = manipulatorStick.getRawAxis(Constants.ELEVATOR_AXIS);
		elevatorHighGearButton = manipulatorStick.getRawButton(Constants.ELEVATOR_HIGH_GEAR_BUTTON);
		elevatorLowGearButton = manipulatorStick.getRawButton(Constants.ELEVATOR_LOW_GEAR_BUTTON);
		intakeTrigger = manipulatorStick.getRawAxis(Constants.INTAKE_TRIGGER);
		outtakeTrigger = manipulatorStick.getRawAxis(Constants.OUTTAKE_TRIGGER);
		armButtonOutput = armButton.toggle();

		driveForwardAxis = mobilityStick.getRawAxis(Constants.DRIVE_FORWARD_AXIS);
		driveTurnAxis = mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS);
		driveHighGearButton = mobilityStick.getRawButton(Constants.DRIVE_HIGH_GEAR_BUTTON);
		driveLowGearButton = mobilityStick.getRawButton(Constants.DRIVE_LOW_GEAR_BUTTON);
		leftDriveAxis = mobilityStick.getRawAxis(Constants.LEFT_DRIVE_AXIS);
		rightDriveAxis = mobilityStick.getRawAxis(Constants.RIGHT_DRIVE_AXIS);
	}

	// getters for all objects and variables declared in this class
	public Joystick getManipulatorStick() {
		return manipulatorStick;
	}

	public double getElevatorAxis() {
		return elevatorAxis;
	}

	public boolean isElevatorHighGearButton() {
		return elevatorHighGearButton;
	}

	public boolean isElevatorLowGearButton() {
		return elevatorLowGearButton;
	}

	public double getIntakeTrigger() {
		return intakeTrigger;
	}

	public double getOuttakeTrigger() {
		return outtakeTrigger;
	}

	public Toggle getArmButton() {
		return armButton;
	}

	public boolean isArmButtonOutput() {
		return armButtonOutput;
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

	public boolean isDriveHighGearButton() {
		return driveHighGearButton;
	}

	public boolean isDriveLowGearButton() {
		return driveLowGearButton;
	}

	public double getLeftDriveAxis() {
		return leftDriveAxis;
	}

	public double getRightDriveAxis() {
		return rightDriveAxis;
	}

}
