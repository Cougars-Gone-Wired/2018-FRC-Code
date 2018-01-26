package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;

public class Arm {

	Joystick manipulatorStick;
	Toggle armButton;
	boolean armButtonOutput;
	
	DoubleSolenoid armSolenoid;
	
	public enum ArmStates {
		UP, DOWN
	}
	ArmStates currentArmState = ArmStates.UP;
	
	public Arm(Robot robot) {
		this.manipulatorStick = robot.getManipulatorStick();
		this.armButton = robot.getArmButton();
		this.armSolenoid = robot.getArmSolenoid();
	}
	
	public void arm() {
		armButtonOutput = armButton.toggle();
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
}
