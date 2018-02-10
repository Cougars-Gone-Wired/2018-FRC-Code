package org.usfirst.frc.team2996.robot;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class StateRecorder {

	// declarations for all the solenoids and motors to get the states of
	private WPI_TalonSRX frontLeftMotor;
	private WPI_TalonSRX rearLeftMotor;

	private WPI_TalonSRX frontRightMotor;
	private WPI_TalonSRX rearRightMotor;

	// list for all the states of the solenoids and motors to be listed in
	List<State> states;

	public StateRecorder(Robot robot) {
		// assigns all the previously declared objects to objects instantiated in other
		// classes
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
