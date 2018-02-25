package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {

	public enum ElevatorStates { // all the states the elevator can be in
		NOT_MOVING, GOING_UP, GOING_DOWN
	}

	ElevatorStates currentElevatorState = ElevatorStates.NOT_MOVING; // the state for the elevator to start in

	// declarations for all objects associated with the elevator
	private WPI_TalonSRX elevatorMasterMotor; // the only motor we will actually be controlling
	private WPI_TalonSRX elevatorSlaveMotor; // will do anything its master does
	private SensorCollection elevatorMasterMotorSensors;

	private Solenoid changeElevatorGearSolenoid;

	double elevatorAxisValue;
	
	public Elevator() {
		// instantiations of all previously declared objects
		elevatorMasterMotor = new WPI_TalonSRX(Constants.ELEVATOR_MASTER_MOTOR_ID);
		elevatorSlaveMotor = new WPI_TalonSRX(Constants.ELEVATOR_SLAVE_MOTOR_ID);
		elevatorSlaveMotor.follow(elevatorMasterMotor); // setting the slave motor to do everything the master motor
														// does
		elevatorMasterMotorSensors = new SensorCollection(elevatorMasterMotor); // for limit switches

		changeElevatorGearSolenoid = new Solenoid(Constants.CHANGE_ELEVATOR_GEAR_SOLENOID_PORT);
	}

	public void elevatorFunctions(double elevatorAxis) { // method to check if the elevator needs to change states
		SmartDashboard.putBoolean("fwd limit switch", elevatorMasterMotorSensors.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("rev limit switch", elevatorMasterMotorSensors.isRevLimitSwitchClosed());
		elevatorAxisValue = Utility.deadZone(elevatorAxis * -1);
		switch (currentElevatorState) {
		case NOT_MOVING:
			if (elevatorAxisValue > 0 && !elevatorMasterMotorSensors.isFwdLimitSwitchClosed()) {
				elevatorMasterMotor.set(elevatorAxisValue);
				currentElevatorState = ElevatorStates.GOING_UP;
			} else if (elevatorAxisValue < 0 && !elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
				elevatorMasterMotor.set(elevatorAxisValue);
				currentElevatorState = ElevatorStates.GOING_DOWN;
			}
			break;
		case GOING_UP:
			if (elevatorMasterMotorSensors.isFwdLimitSwitchClosed()) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			} else if (elevatorAxisValue == 0) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			} else if (elevatorAxisValue > 0) {
				elevatorMasterMotor.set(elevatorAxisValue);
			} 
			break;
		case GOING_DOWN:
			if (elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			} else if (elevatorAxisValue == 0) {
				elevatorMasterMotor.set(0);
				currentElevatorState = ElevatorStates.NOT_MOVING;
			} else if (elevatorAxisValue < 0) {
				elevatorMasterMotor.set(elevatorAxisValue * .5);
			}
			break;
		}
	}

	
	// getters for all the objects declared in this class
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
