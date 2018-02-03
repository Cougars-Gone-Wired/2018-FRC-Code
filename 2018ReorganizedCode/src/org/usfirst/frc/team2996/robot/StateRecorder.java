package org.usfirst.frc.team2996.robot;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class StateRecorder {

	// declarations for all the solenoids and motors to get the states of
	private DoubleSolenoid armSolenoid;
	private Solenoid changeDriveGearSolenoid;
	private Solenoid changeElevatorGearSolenoid;

	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;

	private WPI_TalonSRX elevatorMasterMotor;
	private WPI_TalonSRX elevatorSlaveMotor;

	private WPI_TalonSRX frontLeftMotor;
	private WPI_TalonSRX rearLeftMotor;

	private WPI_TalonSRX frontRightMotor;
	private WPI_TalonSRX rearRightMotor;

	// list for all the states of the solenoids and motors to be listed in
	List<State> states;

	public StateRecorder(Robot robot) {
		// assigns all the previously declared objects to objects instantiated in other
		// classes
		this.armSolenoid = robot.getArm().getArmSolenoid();
		this.changeDriveGearSolenoid = robot.getDrive().getChangeDriveGearSolenoid();
		this.changeElevatorGearSolenoid = robot.getElevator().getChangeElevatorGearSolenoid();
		this.leftIntakeMotor = robot.getIntake().getLeftIntakeMotor();
		this.rightIntakeMotor = robot.getIntake().getRightIntakeMotor();
		this.elevatorMasterMotor = robot.getElevator().getElevatorMasterMotor();
		this.elevatorSlaveMotor = robot.getElevator().getElevatorSlaveMotor();
		this.frontLeftMotor = robot.getDrive().getFrontLeftMotor();
		this.rearLeftMotor = robot.getDrive().getRearLeftMotor();
		this.frontRightMotor = robot.getDrive().getFrontRightMotor();
		this.rearRightMotor = robot.getDrive().getRearRightMotor();
	}

	public void initialize() { // method that instantiates the list previously declared
		states = new ArrayList<>();
	}

	// method to save the states of all the solenoids and motors every 20
	// milliseconds and compile them into a list
	public void record() {
		State s = new State();

		s.setArmSolenoidState(armSolenoid.get());
		s.setChangeDriveGearSolenoidState(changeDriveGearSolenoid.get());
		s.setChangeElevatorGearSolenoidState(changeElevatorGearSolenoid.get());
		s.setLeftIntakeMotorState(leftIntakeMotor.get());
		s.setRightIntakeMotorState(rightIntakeMotor.get());
		s.setElevatorMasterMotorState(elevatorMasterMotor.get());
		s.setElevatorSlaveMotorState(elevatorSlaveMotor.get());
		s.setFrontLeftMotorState(frontLeftMotor.get());
		s.setRearLeftMotorState(rearLeftMotor.get());
		s.setFrontRightMotorState(frontRightMotor.get());
		s.setRearRightMotorState(rearRightMotor.get());

		states.add(s);
	}

	// getter for the list filled in the record method
	public List<State> getStates() {
		return states;
	}
}
