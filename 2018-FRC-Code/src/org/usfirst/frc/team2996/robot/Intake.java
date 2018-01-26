package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;

public class Intake {

	private Joystick manipulatorStick;
	boolean intakeButton;
	boolean outtakeButton;
	
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;
	
	public enum IntakeStates{
		NOT_MOVING, INTAKING, OUTTAKING
	}
	IntakeStates currentIntakeState = IntakeStates.NOT_MOVING;
	
	public Intake(Robot robot) {
		this.manipulatorStick = robot.getManipulatorStick();
		this.leftIntakeMotor = robot.getLeftIntakeMotor();
		this.rightIntakeMotor = robot.getRightIntakeMotor();
	}
	
	public void intakeFunctions() {
		intakeButton = manipulatorStick.getRawButton(Robot.INTAKE_BUTTON);
		outtakeButton = manipulatorStick.getRawButton(Robot.OUTTAKE_BUTTON);
		switch (currentIntakeState) {
		case NOT_MOVING:
			if (intakeButton && !outtakeButton) {
				leftIntakeMotor.set(-1);
				rightIntakeMotor.set(1);
				currentIntakeState = IntakeStates.INTAKING;
			} else if (!intakeButton && outtakeButton) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				currentIntakeState = IntakeStates.OUTTAKING;
			}
			break;
		case INTAKING:
		case OUTTAKING:
			if (!intakeButton && !outtakeButton) {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				currentIntakeState = IntakeStates.NOT_MOVING;
			}
			break;
		}
	}
}
