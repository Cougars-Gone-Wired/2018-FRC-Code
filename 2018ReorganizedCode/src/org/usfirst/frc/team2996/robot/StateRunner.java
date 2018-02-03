package org.usfirst.frc.team2996.robot;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class StateRunner {

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
	
	public StateRunner(Robot robot) {
		this.armSolenoid = robot.getArm().getArmSolenoid();
		this.changeDriveGearSolenoid = robot.getDrive().getChangeDriveGearSolenoid();
		this.changeElevatorGearSolenoid = robot.getElevator().getChangeElevatorGearSolenoid();
		this.rightIntakeMotor = robot.getIntake().getRightIntakeMotor();
		this.elevatorMasterMotor = robot.getElevator().getElevatorMasterMotor();
		this.elevatorSlaveMotor = robot.getElevator().getElevatorSlaveMotor();
		this.frontLeftMotor = robot.getDrive().getFrontLeftMotor();
		this.rearLeftMotor = robot.getDrive().getRearLeftMotor();
		this.frontRightMotor = robot.getDrive().getFrontRightMotor();
		this.rearRightMotor = robot.getDrive().getRearRightMotor();
	}
	
	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public void run() {
		if (counter < states.size()) {
			State s = states.get(counter);
			
			armSolenoid.set(s.getArmSolenoidState());
			changeDriveGearSolenoid.set(s.isChangeDriveGearSolenoidState());
			changeElevatorGearSolenoid.set(s.isChangeElevatorGearSolenoidState());
			leftIntakeMotor.set(s.getLeftIntakeMotorState());
			rightIntakeMotor.set(s.getRightIntakeMotorState());
			elevatorMasterMotor.set(s.getElevatorMasterMotorState());
			elevatorSlaveMotor.set(s.getElevatorSlaveMotorState());
			frontLeftMotor.set(s.getFrontLeftMotorState());
			rearLeftMotor.set(s.getRearLeftMotorState());
			frontRightMotor.set(s.getFrontRightMotorState());
			rearRightMotor.set(s.getRearRightMotorState());
			
			counter++;
		}
	}
}
