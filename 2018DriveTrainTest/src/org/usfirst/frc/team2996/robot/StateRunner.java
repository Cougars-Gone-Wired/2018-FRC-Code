package org.usfirst.frc.team2996.robot;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class StateRunner {

	// declarations for all the solenoids and motors to get the states of
	private WPI_TalonSRX frontLeftMotor;
	private WPI_TalonSRX rearLeftMotor;

	private WPI_TalonSRX frontRightMotor;
	private WPI_TalonSRX rearRightMotor;

	// list for all the states of the solenoids and motors to be listed in
	List<State> states;

	// variable to act as if it were an i in a for loop for the run method
	int counter = 0;

	public StateRunner(Robot robot) {
		// assigns all the previously declared objects to objects instantiated in other
		// classes
		this.frontLeftMotor = robot.getDrive().getFrontLeftMotor();
		this.rearLeftMotor = robot.getDrive().getRearLeftMotor();
		this.frontRightMotor = robot.getDrive().getFrontRightMotor();
		this.rearRightMotor = robot.getDrive().getRearRightMotor();
	}

	public void counterInitialize() { // method to set the counter to 0
		counter = 0;
	}

	// method to set the list declared in this class to a list of states passed in
	public void setStates(List<State> states) {
		this.states = states;
	}

	// method to set the states of the solenoids and motors to the states listed in
	// the list every 20 milliseconds
	public void run() {
		if (counter < states.size()) {
			State s = states.get(counter);

			frontLeftMotor.set(s.getFrontLeftMotorState());
			rearLeftMotor.set(s.getRearLeftMotorState());
			frontRightMotor.set(s.getFrontRightMotorState());
			rearRightMotor.set(s.getRearRightMotorState());

			counter++;
		}
	}
}
