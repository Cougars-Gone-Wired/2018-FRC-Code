package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class State {

	private DoubleSolenoid.Value armSolenoidState;
	private boolean leftDriveSolenoidState;
	private boolean rightDriveSolenoidState;

	private double intakeMotorLeftState;
	private double intakeMotorRightState;

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
	public boolean getLeftDriveSolenoidState() {
		return leftDriveSolenoidState;
	}
	public void setLeftDriveSolenoidState(boolean leftDriveSolenoidState) {
		this.leftDriveSolenoidState = leftDriveSolenoidState;
	}
	public boolean getRightDriveSolenoidState() {
		return rightDriveSolenoidState;
	}
	public void setRightDriveSolenoidState(boolean rightDriveSolenoidState) {
		this.rightDriveSolenoidState = rightDriveSolenoidState;
	}
	public double getIntakeMotorLeftState() {
		return intakeMotorLeftState;
	}
	public void setIntakeMotorLeftState(double intakeMotorLeftState) {
		this.intakeMotorLeftState = intakeMotorLeftState;
	}
	public double getIntakeMotorRightState() {
		return intakeMotorRightState;
	}
	public void setIntakeMotorRightState(double intakeMotorRightState) {
		this.intakeMotorRightState = intakeMotorRightState;
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
