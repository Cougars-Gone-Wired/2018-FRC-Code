package org.usfirst.frc.team2996.robot;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class StateRecorder {

	DoubleSolenoid armSolenoid;
	Solenoid changeDriveGearSolenoid;
	Solenoid changeElevatorGearSolenoid;

	WPI_TalonSRX leftIntakeMotor;
	WPI_TalonSRX rightIntakeMotor;

	WPI_TalonSRX elevatorMasterMotor;
	WPI_TalonSRX elevatorSlaveMotor;

	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	
	List<State> states;
	
	int counter = 0;
	
	public StateRecorder(Robot robot) {
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
	
	public void initialize() {
		states = new ArrayList<>();
	}
	
	public void counterInitialize() {
		counter = 0;
	}
	
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
	
	public List<State> getStates() {
		return states;
	}
}
