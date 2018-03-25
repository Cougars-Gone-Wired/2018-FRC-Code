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

	public enum AutoElevatorStates {
		NOT_MOVING, UP_FAST, UP_SLOW
	}
	
	AutoElevatorStates currentAutoElevatorState = AutoElevatorStates.UP_FAST;
	
	// declarations for all objects associated with the elevator
	private WPI_TalonSRX elevatorMasterMotor; // the only motor we will actually be controlling
	private WPI_TalonSRX elevatorSlaveMotor; // will do anything its master does
	private SensorCollection elevatorMasterMotorSensors;
	private SensorCollection elevatorSlaveMotorSensors;
	private double elevatorEncoder = 0;
	private double encoderConstant = AutoProgramsRevised.DISTANCE_PER_ENCODER_TICK;
	
	private double elevatorAutoDesiredTime = 0;
	
	private boolean startMoving = false;
	
	private double distanceDifference = 0;
	private double autoElevatorCounter = 0;
	static final int UP_FAST_TOLERANCE = 10;
	static final int UP_SLOW_TOLERANCE = 20;
	
	static final double UP_FAST_SPEED = .75;
	static final double UP_SLOW_SPEED = .4;
	
	boolean liftDone = false;
	
	private int liftLoopCounter = 0;
	private int liftLoopLimit = 20;

	private Solenoid changeElevatorGearSolenoid;

	double elevatorAxisValue;
	
	public Elevator() {
		// instantiations of all previously declared objects
		elevatorMasterMotor = new WPI_TalonSRX(Constants.ELEVATOR_MASTER_MOTOR_ID);
		elevatorSlaveMotor = new WPI_TalonSRX(Constants.ELEVATOR_SLAVE_MOTOR_ID);
		elevatorSlaveMotor.follow(elevatorMasterMotor); // setting the slave motor to do everything the master motor
														// does
		elevatorMasterMotorSensors = new SensorCollection(elevatorMasterMotor); // for limit switches
		elevatorSlaveMotorSensors = new SensorCollection(elevatorSlaveMotor);

		changeElevatorGearSolenoid = new Solenoid(Constants.CHANGE_ELEVATOR_GEAR_SOLENOID_PORT);
	}

	public void elevatorFunctions(double elevatorAxis) { // method to check if the elevator needs to change states
		SmartDashboard.putBoolean("fwd limit switch", elevatorMasterMotorSensors.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("rev limit switch", elevatorMasterMotorSensors.isRevLimitSwitchClosed());
		elevatorEncoder = elevatorSlaveMotorSensors.getQuadraturePosition() * encoderConstant;
		SmartDashboard.putNumber("Elevator Encoder", elevatorEncoder);
		if (elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
			elevatorMasterMotorSensors.setQuadraturePosition(0, 10);
		}
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

	
	public void autoElevator(double elevatorAutoDesiredHieght) {
		elevatorEncoder = elevatorSlaveMotorSensors.getQuadraturePosition() * encoderConstant;
//		System.out.println(elevatorEncoder);
		distanceDifference = elevatorAutoDesiredHieght - elevatorEncoder;
		SmartDashboard.putNumber("Elevator Encoder", elevatorEncoder);
		SmartDashboard.putNumber("Elevator Desired Hieght", elevatorAutoDesiredHieght);
		if (elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
			elevatorMasterMotorSensors.setQuadraturePosition(0, 10);
			currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
		} else if (elevatorMasterMotorSensors.isFwdLimitSwitchClosed()) {
			currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
		}
		switch(currentAutoElevatorState) {
		case NOT_MOVING:
			elevatorMasterMotor.set(0);
			if (distanceDifference > UP_FAST_TOLERANCE) {
				elevatorMasterMotor.set(UP_FAST_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_FAST;
			} else if (distanceDifference > UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(UP_SLOW_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_SLOW;
			}
			break;		
		case UP_FAST:
			System.out.println("elevator");
			if (distanceDifference > UP_FAST_TOLERANCE) {
				elevatorMasterMotor.set(UP_FAST_SPEED);
			} else if (distanceDifference > UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(UP_SLOW_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_SLOW;
			} else if (distanceDifference < UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(0);
				currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
			}
			break;
		case UP_SLOW:
			System.out.println("elevator");
			if (liftLoopCounter < liftLoopLimit) {
				if (distanceDifference > UP_SLOW_TOLERANCE) {
					elevatorMasterMotor.set(UP_SLOW_SPEED);
					currentAutoElevatorState = AutoElevatorStates.UP_SLOW;
				}
				liftLoopCounter++;
			} else if (distanceDifference < UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(0);
				liftDone = true;
				currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
			}
			break;	
		}
	}
	
	public void autoTimeElevator() {
		SmartDashboard.putNumber("Elevator Desired Time", elevatorAutoDesiredTime);
		System.out.println(autoElevatorCounter);
		if (elevatorMasterMotorSensors.isRevLimitSwitchClosed()) {
			autoElevatorCounter = 0;
			currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
		} else if (elevatorMasterMotorSensors.isFwdLimitSwitchClosed()) {
			currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
		}
		switch(currentAutoElevatorState) {
		case NOT_MOVING:
			elevatorMasterMotor.set(0);
			if (elevatorAutoDesiredTime > UP_FAST_TOLERANCE) {
				elevatorMasterMotor.set(UP_FAST_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_FAST;
			} else if (elevatorAutoDesiredTime > UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(UP_SLOW_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_SLOW;
			}
			break;		
		case UP_FAST:
//			System.out.println("elevator");
			if (autoElevatorCounter > elevatorAutoDesiredTime) {
				elevatorMasterMotor.set(0);
				liftDone = true;
				currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
			} else if (autoElevatorCounter < UP_FAST_TOLERANCE) {
				elevatorMasterMotor.set(UP_FAST_SPEED);
			} else if (autoElevatorCounter < UP_SLOW_TOLERANCE) {
				elevatorMasterMotor.set(UP_SLOW_SPEED);
				currentAutoElevatorState = AutoElevatorStates.UP_SLOW;
			}
			autoElevatorCounter ++;
			break;
		case UP_SLOW:
//			System.out.println("elevator");
			if (autoElevatorCounter > elevatorAutoDesiredTime){
				elevatorMasterMotor.set(0);
				liftDone = true;
				currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
//			} else if (liftLoopCounter < liftLoopLimit) {
			} else if (autoElevatorCounter < UP_SLOW_TOLERANCE) {
					elevatorMasterMotor.set(UP_SLOW_SPEED);
			}
//				liftLoopCounter++;
//			} 
			autoElevatorCounter ++;
			break;	
		}
	}
	
	public void realElevatorAuto(){
		if(startMoving){
			elevatorMasterMotor.set(UP_FAST_SPEED);
		} else {
			elevatorMasterMotor.set(0);
		}
	}
	
	public void autoElevatorReset() {
		elevatorMasterMotor.set(0);
		elevatorMasterMotorSensors.setQuadraturePosition(0, 10);
		liftDone = false;
//		startMoving = false;
		currentAutoElevatorState = AutoElevatorStates.NOT_MOVING;
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

	public void setElevatorAutoDesiredTime(double elevatorAutoDesiredTime) {
		this.elevatorAutoDesiredTime = elevatorAutoDesiredTime;
	}

	public void setStartMoving(boolean startMoving) {
		this.startMoving = startMoving;
	}

//	public void setElevatorAutoDesiredHieght(int elevatorAutoDesiredHieght) {
//		this.elevatorAutoDesiredHieght = elevatorAutoDesiredHieght;
//	}

	
}
