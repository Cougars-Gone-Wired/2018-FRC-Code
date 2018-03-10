package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class ChangeGear {

	public enum GearStates { // all the states a sonic shifter can be in
		LOW_GEAR, HIGH_GEAR
	}

	private GearStates gearCurrentState;
	
	private Solenoid changeGearSolenoid;

	private boolean lowGearState;
	
	public ChangeGear(boolean lowGearState, Solenoid changeGearSolenoid) { // gear the sonic shifter starts in depends on default state passed into
												// the
												// constuctor for an object of this class
//		if (defualtState.toLowerCase() == "low") {
//			this.gearCurrentState = GearStates.LOW_GEAR;
//		} else if (defualtState.toLowerCase() == "high") {
//			this.gearCurrentState = GearStates.HIGH_GEAR;
//		}
	this.lowGearState = lowGearState;
	this.changeGearSolenoid = changeGearSolenoid;
	this.gearCurrentState = GearStates.LOW_GEAR;
	}

	// method to check if the sonic shifter(s) need to change states
	public void changeGearBumper(boolean highGearButton, boolean lowGearButton) {
		switch (gearCurrentState) {
		case LOW_GEAR:
			if (highGearButton && !lowGearButton) {
				changeGearSolenoid.set(!lowGearState);
				gearCurrentState = GearStates.HIGH_GEAR;
			}
			break;
		case HIGH_GEAR:
			if (!highGearButton && lowGearButton) {
				changeGearSolenoid.set(lowGearState);
				gearCurrentState = GearStates.LOW_GEAR;
			}
			break;
		}
	}
	
	public void changeGearTrigger(boolean highGearButton, double lowGearTrigger) {
		switch (gearCurrentState) {
		case LOW_GEAR:
			if (highGearButton && lowGearTrigger < 0.15) {
				changeGearSolenoid.set(!lowGearState);
				gearCurrentState = GearStates.HIGH_GEAR;
			}
			break;
		case HIGH_GEAR:
			if (!highGearButton && lowGearTrigger >= 0.15) {
				changeGearSolenoid.set(lowGearState);
				gearCurrentState = GearStates.LOW_GEAR;
			}
			break;
		}
	}
	
	public void setGearState(boolean state) {
		changeGearSolenoid.set(state);
		if (state == lowGearState) {
			gearCurrentState = GearStates.LOW_GEAR;
		} else if (state == !lowGearState) {
			gearCurrentState = GearStates.HIGH_GEAR;
		}
	}
}
