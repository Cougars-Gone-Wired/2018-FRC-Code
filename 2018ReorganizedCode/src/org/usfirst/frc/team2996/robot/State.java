package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class State {

	// declarations of variables to hold the states of all the solenoids and motors
	private DoubleSolenoid.Value armSolenoidState;
	private boolean changeDriveGearSolenoidState;
	private boolean changeElevatorGearSolenoidState;

	private double leftIntakeMotorState;
	private double rightIntakeMotorState;

	private double elevatorMasterMotorState;
	private double elevatorSlaveMotorState;

	private double frontLeftMotorState;
	private double rearLeftMotorState;

	private double frontRightMotorState;
	private double rearRightMotorState;

	// getters and setters for all the previously declared variables
	public DoubleSolenoid.Value getArmSolenoidState() {
		return armSolenoidState;
	}

	public void setArmSolenoidState(DoubleSolenoid.Value armSolenoidState) {
		this.armSolenoidState = armSolenoidState;
	}

	public boolean isChangeDriveGearSolenoidState() {
		return changeDriveGearSolenoidState;
	}

	public void setChangeDriveGearSolenoidState(boolean changeDriveGearSolenoidState) {
		this.changeDriveGearSolenoidState = changeDriveGearSolenoidState;
	}

	public boolean isChangeElevatorGearSolenoidState() {
		return changeElevatorGearSolenoidState;
	}

	public void setChangeElevatorGearSolenoidState(boolean changeElevatorGearSolenoidState) {
		this.changeElevatorGearSolenoidState = changeElevatorGearSolenoidState;
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

	public double getElevatorMasterMotorState() {
		return elevatorMasterMotorState;
	}

	public void setElevatorMasterMotorState(double elevatorMasterMotorState) {
		this.elevatorMasterMotorState = elevatorMasterMotorState;
	}

	public double getElevatorSlaveMotorState() {
		return elevatorSlaveMotorState;
	}

	public void setElevatorSlaveMotorState(double elevatorSlaveMotorState) {
		this.elevatorSlaveMotorState = elevatorSlaveMotorState;
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
