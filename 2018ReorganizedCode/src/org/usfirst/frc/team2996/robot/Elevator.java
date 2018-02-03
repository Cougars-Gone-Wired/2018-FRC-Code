package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Elevator {
	
	public enum ElevatorStates {
		NOT_MOVING, GOING_UP, GOING_DOWN, AT_TOP, AT_BOTTOM
	}
	ElevatorStates currentElevatorState = ElevatorStates.NOT_MOVING;
	
	private WPI_TalonSRX elevatorMasterMotor;
	private WPI_TalonSRX elevatorSlaveMotor;
	private SensorCollection elevatorMasterMotorSensors;
	
	private Solenoid changeElevatorGearSolenoid;
	
	public Elevator() {
		elevatorMasterMotor = new WPI_TalonSRX(Constants.ELEVATOR_MASTER_MOTOR_ID);
		elevatorSlaveMotor = new WPI_TalonSRX(Constants.ELEVATOR_SLAVE_MOTOR_ID);
		elevatorSlaveMotor.follow(elevatorMasterMotor);
		elevatorMasterMotorSensors = new SensorCollection(elevatorMasterMotor);
		
		changeElevatorGearSolenoid = new Solenoid(Constants.CHANGE_ELEVATOR_GEAR_SOLENOID_PORT);
	}
	
	public void elevatorFunctions(double elevatorAxis) {
		elevatorAxis = Utility.deadZone(elevatorAxis);
		switch (currentElevatorState) {
		case NOT_MOVING:
			if (elevatorAxis > 0.15) {
				elevatorMasterMotor.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_UP;
			} else if (elevatorAxis < -0.15) {
				elevatorMasterMotor.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_DOWN;
			}
			break;
		case GOING_UP:
			if (elevatorMasterMotorSensors.isFwdLimitSwitchClosed()) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.AT_TOP;
			} else if (elevatorAxis == 0) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			}
			break;
		case GOING_DOWN:
			if (elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.AT_BOTTOM;
			} else if (elevatorAxis == 0) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			}
			break;
		case AT_TOP:
			if (elevatorAxis < 0.15) {
				elevatorMasterMotor.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_DOWN;
			}
			break;
		case AT_BOTTOM:
			if (elevatorAxis > 0.15) {
				elevatorMasterMotor.set(elevatorAxis);
				currentElevatorState = ElevatorStates.GOING_UP;
			}
			break;
		}
	}

	public WPI_TalonSRX getElevatorMasterMotor() {
		return elevatorMasterMotor;
	}

	public WPI_TalonSRX getElevatorSlaveMotor() {
		return elevatorSlaveMotor;
	}

	public SensorCollection getElevatorMasterMotorSensors() {
		return elevatorMasterMotorSensors;
	}

	public Solenoid getChangeElevatorGearSolenoid() {
		return changeElevatorGearSolenoid;
	}
	
}
