package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Elevator {

	private Joystick manipulatorStick;
	boolean elevatorUpButton;
	boolean elevatorDownButton;
	private SpeedControllerGroup elevatorMotors;

	public enum elevatorStates {NOT_MOVING, GOING_UP, GOING_DOWN};
	elevatorStates elevatorCurrentState = elevatorStates.NOT_MOVING;

	public Elevator(Robot robot) {
		this.manipulatorStick = robot.getManipulatorStick();
		this.elevatorMotors = robot.getElevatorMotors();
	}

	public void elevatorFunctions() {
		elevatorUpButton = manipulatorStick.getRawButton(Robot.ELEVATOR_UP_BUTTON);
		elevatorDownButton = manipulatorStick.getRawButton(Robot.ELEVATOR_DOWN_BUTTON);
		switch (elevatorCurrentState) {
		case NOT_MOVING:
			if (elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(1);
				elevatorCurrentState = elevatorStates.GOING_UP;
			} else if (!elevatorUpButton && elevatorDownButton) {
				elevatorMotors.set(-1);
				elevatorCurrentState = elevatorStates.GOING_DOWN;
			}
			break;
		case GOING_UP:
		case GOING_DOWN:
			if (!elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(0);
				elevatorCurrentState = elevatorStates.NOT_MOVING;
			}
			break;
		}
	}
}
