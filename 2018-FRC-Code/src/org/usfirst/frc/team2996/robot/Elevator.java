package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Elevator {

	private Joystick manipulatorStick;
	double elevatorAxis;
	
	private SensorCollection elevatorMotor1Sensors;
	private SensorCollection elevatorMotor2Sensors;
	
	private SpeedControllerGroup elevatorMotors;

	public enum ElevatorStates {
		NOT_MOVING, GOING_UP, GOING_DOWN, AT_TOP, AT_BOTTOM
	}
	ElevatorStates currentElevatorState = ElevatorStates.NOT_MOVING;

	public Elevator(Robot robot) {
		this.manipulatorStick = robot.getManipulatorStick();
		this.elevatorMotor1Sensors = robot.getElevatorMotor1Sensors();
		this.elevatorMotor2Sensors = robot.getElevatorMotor2Sensors();
		this.elevatorMotors = robot.getElevatorMotors();
	}

	public void elevatorFunctions() {
		elevatorAxis = Utility.deadZone(manipulatorStick.getRawAxis(Robot.ELEVATOR_AXIS));
		switch (currentElevatorState) {
		case NOT_MOVING:
			if (elevatorAxis > 0.15) {
				elevatorMotors.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_UP;
			} else if (elevatorAxis < -0.15) {
				elevatorMotors.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_DOWN;
			}
			break;
		case GOING_UP:
			if (elevatorMotor1Sensors.isFwdLimitSwitchClosed()) {
				elevatorMotors.set(0);
				currentElevatorState = ElevatorStates.AT_TOP;
			} else if (elevatorAxis == 0) {
				elevatorMotors.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			}
			break;
		case GOING_DOWN:
			if (elevatorMotor1Sensors.isRevLimitSwitchClosed()) {
				elevatorMotors.set(0);
				currentElevatorState = ElevatorStates.AT_BOTTOM;
			} else if (elevatorAxis == 0) {
				elevatorMotors.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			}
			break;
		case AT_TOP:
			if (elevatorAxis < 0.15) {
				elevatorMotors.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_DOWN;
			}
			break;
		case AT_BOTTOM:
			if (elevatorAxis > 0.15) {
				elevatorMotors.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_UP;
			}
		}
	}
}
