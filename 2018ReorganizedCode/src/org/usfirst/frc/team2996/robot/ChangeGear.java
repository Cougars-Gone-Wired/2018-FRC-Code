package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class ChangeGear {

	public enum GearStates {
		LOW_GEAR, HIGH_GEAR
	}
	private GearStates gearCurrentState;
	
	public ChangeGear(String defualtState) {
		if (defualtState.toLowerCase() == "low") {
			this.gearCurrentState = GearStates.LOW_GEAR;
		} else if (defualtState.toLowerCase() == "high") {
			this.gearCurrentState = GearStates.HIGH_GEAR;
		}
	}
	
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
