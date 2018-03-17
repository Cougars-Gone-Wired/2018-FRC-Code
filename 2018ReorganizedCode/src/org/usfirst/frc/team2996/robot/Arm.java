package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Arm {

	public enum ArmStates { // all the states the arm can be in
		UP, DOWN
	}

	ArmStates currentArmState = ArmStates.UP; // the state for the arm to start in

	// declaration for the object associated with the arm
	private DoubleSolenoid armSolenoid;

	public Arm() {
		// instantiation of previously declared object
		armSolenoid = new DoubleSolenoid(Constants.ARM_SOLENOID_UP_PORT, Constants.ARM_SOLENOID_DOWN_PORT);
	}

	public void armFunctions(boolean armButtonOutput) { // method to check if the arm needs to change states
		switch (currentArmState) {
		case UP:
			if (!armButtonOutput) {
				armSolenoid.set(DoubleSolenoid.Value.kReverse);
				currentArmState = ArmStates.DOWN;
			}
			break;
		case DOWN:
			if (armButtonOutput) {
				armSolenoid.set(DoubleSolenoid.Value.kForward);
				currentArmState = ArmStates.UP;
			}
		}
	}

	public void setArmState(boolean up) {
		if (up) {
			armSolenoid.set(DoubleSolenoid.Value.kForward);
			currentArmState = ArmStates.UP;
		} else if (!up) {
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
			currentArmState = ArmStates.DOWN;
		}
	}
	
	// getter for the object declared in this class
	public DoubleSolenoid getArmSolenoid() {
		return armSolenoid;
	}
}
