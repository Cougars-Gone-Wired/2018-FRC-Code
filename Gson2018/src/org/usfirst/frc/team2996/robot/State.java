package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class State {

	private DoubleSolenoid.Value armSolenoidState;
	private boolean changeGearSolenoidState;

	private double leftIntakeMotorState;
	private double rightIntakeMotorState;

	private double elevatorMotor1State;
	private double elevatorMotor2State;

	private double frontLeftMotorState;
	private double rearLeftMotorState;

	private double frontRightMotorState;
	private double rearRightMotorState;
	
	
	public DoubleSolenoid.Value getArmSolenoidState() {
		return armSolenoidState;
	}
	public void setArmSolenoidState(DoubleSolenoid.Value armSolenoidState) {
		this.armSolenoidState = armSolenoidState;
	}
	public boolean getChangeGearSolenoidState() {
		return changeGearSolenoidState;
	}
	public void setChangeGearState(boolean changeGearSolenoidState) {
		this.changeGearSolenoidState = changeGearSolenoidState;
	}
	public double getLeftIntakeMotorState() {
		return leftIntakeMotorState;
	}
	public void setLeftIntakeMotorState(double leftIntakeMotorState) {
		this.leftIntakeMotorState = leftIntakeMotorState;
	}
	public double getRightIntakeMotorState() {
		return rightIntakeMotorState;
	}
	public void setRightIntakeMotorState(double rightIntakeMotorState) {
		this.rightIntakeMotorState = rightIntakeMotorState;
	}
	public double getElevatorMotor1State() {
		return elevatorMotor1State;
	}
	public void setElevatorMotor1State(double elevatorMotor1State) {
		this.elevatorMotor1State = elevatorMotor1State;
	}
	public double getElevatorMotor2State() {
		return elevatorMotor2State;
	}
	public void setElevatorMotor2State(double elevatorMotor2State) {
		this.elevatorMotor2State = elevatorMotor2State;
	}
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
