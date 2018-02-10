package org.usfirst.frc.team2996.robot;

public class State {

	// declarations of variables to hold the states of all the solenoids and motors
	private double frontLeftMotorState;
	private double rearLeftMotorState;

	private double frontRightMotorState;
	private double rearRightMotorState;

	// getters and setters for all the previously declared variables
	public double getFrontLeftMotorState() {
		return frontLeftMotorState;
	}

	public void setFrontLeftMotorState(double frontLeftMotorState) {
		this.frontLeftMotorState = frontLeftMotorState;
	}

	public double getRearLeftMotorState() {
		return rearLeftMotorState;
	}

	public void setRearLeftMotorState(double rearLeftMotorState) {
		this.rearLeftMotorState = rearLeftMotorState;
	}

	public double getFrontRightMotorState() {
		return frontRightMotorState;
	}

	public void setFrontRightMotorState(double frontRightMotorState) {
		this.frontRightMotorState = frontRightMotorState;
	}

	public double getRearRightMotorState() {
		return rearRightMotorState;
	}

	public void setRearRightMotorState(double rearRightMotorState) {
		this.rearRightMotorState = rearRightMotorState;
	}

}
