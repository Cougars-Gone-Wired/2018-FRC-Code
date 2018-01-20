package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Elevator {

	private Joystick manipulatorStick;
	boolean elevatorUpButton;
	boolean elevatorDownButton;
	private SpeedControllerGroup elevatorMotors;
	private DigitalInput topElevatorLimitSwitch;
	private DigitalInput bottomElevatorLimitSwitch;

	public enum liftStates {NOT_MOVING, GOING_UP, GOING_DOWN, AT_TOP, AT_BOTTOM};
	liftStates liftState = liftStates.NOT_MOVING;

	public Elevator(Robot robot) {
		this.manipulatorStick = robot.getManipulatorStick();
		this.elevatorMotors = robot.getElevatorMotors();
		this.topElevatorLimitSwitch = robot.getTopElevatorLimitSwitch();
		this.bottomElevatorLimitSwitch = robot.getBottomElevatorLimitSwitch();
	}

	public void elevatorFunctions() {
		elevatorUpButton = manipulatorStick.getRawButton(Robot.ELEVATOR_UP_BUTTON);
		elevatorDownButton = manipulatorStick.getRawButton(Robot.ELEVATOR_DOWN_BUTTON);
		switch (liftState) {
		case NOT_MOVING:
			if (elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(1);
				liftState = liftStates.GOING_UP;
			} else if (!elevatorUpButton && elevatorDownButton) {
				elevatorMotors.set(-1);
				liftState = liftStates.GOING_DOWN;
			}
			break;
		case GOING_UP:
			if (topElevatorLimitSwitch.get()) {
				elevatorMotors.set(0);
				liftState = liftStates.AT_TOP;
			} else if (!elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(0);
				liftState = liftStates.NOT_MOVING;
			}
			break;
		case GOING_DOWN:
			if (bottomElevatorLimitSwitch.get()) {
				elevatorMotors.set(0);
				liftState = liftStates.AT_BOTTOM;
			} else if (!elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(0);
				liftState = liftStates.NOT_MOVING;
			}
			break;
		case AT_TOP:
			if (!elevatorUpButton && elevatorDownButton) {
				elevatorMotors.set(-1);
				liftState = liftStates.GOING_DOWN;
			}
			break;
		case AT_BOTTOM:
			if (elevatorUpButton && !elevatorDownButton) {
				elevatorMotors.set(1);
				liftState = liftStates.GOING_UP;
			}
			break;
		}
	}
}
