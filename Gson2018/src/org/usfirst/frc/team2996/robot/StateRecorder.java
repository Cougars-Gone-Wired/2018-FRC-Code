package org.usfirst.frc.team2996.robot;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class StateRecorder {

	DoubleSolenoid armSolenoid;
	Solenoid changeGearSolenoid;

	WPI_TalonSRX leftIntakeMotor;
	WPI_TalonSRX rightIntakeMotor;

	WPI_TalonSRX elevatorMotor1;
	WPI_TalonSRX elevatorMotor2;

	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	
	List<State> states;
	
	int counter = 0;
	
	public StateRecorder(Robot robot) {
		this.armSolenoid = robot.getArmSolenoid();
		this.changeGearSolenoid = robot.getChangeGearSolenoid();
		this.leftIntakeMotor = robot.getLeftIntakeMotor();
		this.rightIntakeMotor = robot.getRightIntakeMotor();
		this.elevatorMotor1 = robot.getElevatorMotor1();
		this.elevatorMotor2 = robot.getElevatorMotor2();
		this.frontLeftMotor = robot.getFrontLeftMotor();
		this.rearLeftMotor = robot.getRearLeftMotor();
		this.frontRightMotor = robot.getFrontRightMotor();
		this.rearRightMotor = robot.getRearRightMotor();
	}
	
	public void initialize() {
		states = new ArrayList<>();
	}
	
	public void counterInitialize() {
		counter = 0;
	}
	
	public void record() {
		State s = new State();
		
		s.setArmSolenoidState(armSolenoid.get());
		s.setChangeGearState(changeGearSolenoid.get());
		s.setLeftIntakeMotorState(leftIntakeMotor.get());
		s.setRightIntakeMotorState(rightIntakeMotor.get());
		s.setElevatorMotor1State(elevatorMotor1.get());
		s.setElevatorMotor2State(elevatorMotor2.get());
		s.setFrontLeftMotorState(frontLeftMotor.get());
		s.setRearLeftMotorState(rearLeftMotor.get());
		s.setFrontRightMotorState(frontRightMotor.get());
		s.setRearRightMotorState(rearRightMotor.get());
		
		states.add(s);
	}
	
	public List<State> getStates() {
		return states;
	}
}
