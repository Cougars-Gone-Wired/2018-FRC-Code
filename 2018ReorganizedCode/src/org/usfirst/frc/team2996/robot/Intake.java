package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Intake {

	public enum IntakeStates{
		NOT_MOVING, INTAKING, OUTTAKING
	}
	IntakeStates currentIntakeState = IntakeStates.NOT_MOVING;
	
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;
	
	public Intake() {
		leftIntakeMotor = new WPI_TalonSRX(Constants.LEFT_INTAKE_MOTOR_ID);
		rightIntakeMotor = new WPI_TalonSRX(Constants.RIGHT_INTAKE_MOTOR_ID);
	}
	
	public void intakeFunctions(double intakeTrigger, double outtakeTrigger) {
		switch (currentIntakeState) {
		case NOT_MOVING:
			if ((intakeTrigger >= 0.15) && (outtakeTrigger <= 0.15)) {
				leftIntakeMotor.set(-1);
				rightIntakeMotor.set(1);
				currentIntakeState = IntakeStates.INTAKING;
			} else if ((intakeTrigger <= 0.15) && (outtakeTrigger >= 0.15)) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				currentIntakeState = IntakeStates.OUTTAKING;
			}
			break;
		case INTAKING:
		case OUTTAKING:
			if ((intakeTrigger <= 0.15) && (outtakeTrigger <= 0.15)) {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				currentIntakeState = IntakeStates.NOT_MOVING;
			}
			break;
		}
	}

	public WPI_TalonSRX getLeftIntakeMotor() {
		return leftIntakeMotor;
	}

	public WPI_TalonSRX getRightIntakeMotor() {
		return rightIntakeMotor;
	}
}
