package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Intake {

	public enum IntakeStates { // all the states the intake can be in
		NOT_MOVING, INTAKING, OUTTAKING
	}
	
	IntakeStates currentIntakeState = IntakeStates.NOT_MOVING; // the state for the intake to start in

	public enum AutoIntakeStates {
		NOT_MOVING, INTAKING
	}
	
	AutoIntakeStates currentAutoIntakeState = AutoIntakeStates.NOT_MOVING;
	
	private boolean intake = false;
	
	// declarations for all objects associated with the intake
	private WPI_TalonSRX leftIntakeMotor;
	private WPI_TalonSRX rightIntakeMotor;

	public Intake() {
		// instantiations of all previously declared objects
		leftIntakeMotor = new WPI_TalonSRX(Constants.LEFT_INTAKE_MOTOR_ID);
		rightIntakeMotor = new WPI_TalonSRX(Constants.RIGHT_INTAKE_MOTOR_ID);
	}

	// method to check if the intake needs to change states
	public void intakeFunctions(double intakeTrigger, double outtakeTrigger) {
		switch (currentIntakeState) {
		case NOT_MOVING:
			if ((intakeTrigger >= 0.15) && (outtakeTrigger < 0.15)) {
				leftIntakeMotor.set(-1);
				rightIntakeMotor.set(1);
				currentIntakeState = IntakeStates.INTAKING;
			} else if ((intakeTrigger < 0.15) && (outtakeTrigger >= 0.15)) {
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

	public void autoIntake() {
		switch(currentAutoIntakeState) {
		case NOT_MOVING:
			leftIntakeMotor.set(0);
			rightIntakeMotor.set(0);
			if (intake) {
				leftIntakeMotor.set(1);
				rightIntakeMotor.set(-1);
				currentAutoIntakeState = AutoIntakeStates.INTAKING;
			}
			break;
		case INTAKING:
			leftIntakeMotor.set(1);
			rightIntakeMotor.set(-1);
			if (!intake) {
				leftIntakeMotor.set(0);
				rightIntakeMotor.set(0);
				currentAutoIntakeState = AutoIntakeStates.NOT_MOVING;
			}
			break;
		}
	}
	
	// getters for all the objects declared in this class
	public WPI_TalonSRX getLeftIntakeMotor() {
		return leftIntakeMotor;
	}

	public WPI_TalonSRX getRightIntakeMotor() {
		return rightIntakeMotor;
	}

	public void setIntake(boolean intake) {
		this.intake = intake;
	}
	
}
