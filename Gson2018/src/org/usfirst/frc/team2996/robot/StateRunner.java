package org.usfirst.frc.team2996.robot;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class StateRunner {

	DoubleSolenoid armSolenoid;
	Solenoid leftDriveSolenoid;
	Solenoid rightDriveSolenoid;

	WPI_TalonSRX intakeMotorLeft;
	WPI_TalonSRX intakeMotorRight;

	WPI_TalonSRX elevatorMotor1;
	WPI_TalonSRX elevatorMotor2;

	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	
	List<State> states;
	
	int counter = 0;
	
	public StateRunner(Robot robot) {
		this.armSolenoid = robot.getArmSolenoid();
		this.leftDriveSolenoid = robot.getLeftDriveSolenoid();
		this.rightDriveSolenoid = robot.getRightDriveSolenoid();
		this.intakeMotorLeft = robot.getIntakeMotorLeft();
		this.intakeMotorRight = robot.getIntakeMotorRight();
		this.elevatorMotor1 = robot.getElevatorMotor1();
		this.elevatorMotor2 = robot.getElevatorMotor2();
		this.frontLeftMotor = robot.getFrontLeftMotor();
		this.rearLeftMotor = robot.getRearLeftMotor();
		this.frontRightMotor = robot.getFrontRightMotor();
		this.rearRightMotor = robot.getRearRightMotor();
	}
	
	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public void run() {
		if (counter < states.size()) {
			State s = states.get(counter);
			
			armSolenoid.set(s.getArmSolenoidState());
			leftDriveSolenoid.set(s.getLeftDriveSolenoidState());
			rightDriveSolenoid.set(s.getRightDriveSolenoidState());
			intakeMotorLeft.set(s.getIntakeMotorLeftState());
			intakeMotorRight.set(s.getIntakeMotorRightState());
			elevatorMotor1.set(s.getElevatorMotor1State());
			elevatorMotor2.set(s.getElevatorMotor2State());
			frontLeftMotor.set(s.getFrontLeftMotorState());
			rearLeftMotor.set(s.getRearLeftMotorState());
			frontRightMotor.set(s.getFrontRightMotorState());
			rearRightMotor.set(s.getRearRightMotorState());
			
			counter++;
		}
	}
}
