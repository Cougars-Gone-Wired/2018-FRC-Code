package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class ChangeGear {

	public enum GearStates { // all the states a sonic shifter can be in
		LOW_GEAR, HIGH_GEAR
	}

	private GearStates gearCurrentState;

	public ChangeGear(String defualtState) { // gear the sonic shifter starts in depends on default state passed into
												// the
												// constuctor for an object of this class
		if (defualtState.toLowerCase() == "low") {
			this.gearCurrentState = GearStates.LOW_GEAR;
		} else if (defualtState.toLowerCase() == "high") {
			this.gearCurrentState = GearStates.HIGH_GEAR;
		}
	}

	// method to check if the sonic shifter(s) need to change states
	public void changeGear(boolean highGearButton, boolean lowGearButton, Solenoid changeGearSolenoid) {
		switch (gearCurrentState) {
		case LOW_GEAR:
			if (highGearButton && !lowGearButton) {
				changeGearSolenoid.set(true);
				gearCurrentState = GearStates.HIGH_GEAR;
			}
			break;
		case HIGH_GEAR:
			if (!highGearButton && lowGearButton) {
				changeGearSolenoid.set(false);
				gearCurrentState = GearStates.LOW_GEAR;
			}
			break;
		}
	}
}
