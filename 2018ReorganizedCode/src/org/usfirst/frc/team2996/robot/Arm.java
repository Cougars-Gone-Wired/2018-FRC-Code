package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Arm {

	public enum ArmStates {
		UP, DOWN
	}
	ArmStates currentArmState = ArmStates.UP;
	
	private DoubleSolenoid armSolenoid;
	
	public Arm() {
		armSolenoid = new DoubleSolenoid(Constants.ARM_SOLENOID_UP_PORT, Constants.ARM_SOLENOID_DOWN_PORT);
	}
	
	public void armFunctions(boolean armButtonOutput) {
		switch(currentArmState) {
		case UP:
			if (armButtonOutput) {
				armSolenoid.set(DoubleSolenoid.Value.kReverse);
				currentArmState = ArmStates.DOWN;
			}
			break;
		case DOWN:
			if (!armButtonOutput) {
				armSolenoid.set(DoubleSolenoid.Value.kForward);
				currentArmState = ArmStates.UP;
			}
		}
	}

	public DoubleSolenoid getArmSolenoid() {
		return armSolenoid;
	}
}
