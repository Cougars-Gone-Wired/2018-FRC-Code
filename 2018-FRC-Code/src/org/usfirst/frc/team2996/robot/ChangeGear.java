package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class ChangeGear {

	public enum GearStates {
		LOW_GEAR, HIGH_GEAR
	}
	private GearStates gearCurrentState;
	
	private Joystick stick;
	int highGearButtonNumber;
	int lowGearButtonNumber;
	boolean highGearButton;
	boolean lowGearButton;
	private Solenoid changeGearSolenoid;
	
	public ChangeGear(Joystick stick, int highGearButtonNumber, int lowGearButtonNumber, Solenoid changeGearSolenoid, String defualtState) {
		this.stick = stick;
		this.highGearButtonNumber = highGearButtonNumber;
		this.lowGearButtonNumber = lowGearButtonNumber;
		this.changeGearSolenoid = changeGearSolenoid;
		if (defualtState.toLowerCase() == "low") {
			this.gearCurrentState = GearStates.LOW_GEAR;
		} else if (defualtState.toLowerCase() == "high") {
			this.gearCurrentState = GearStates.HIGH_GEAR;
		}
	}
	
	public void changeGear() {
		highGearButton = stick.getRawButton(highGearButtonNumber);
		lowGearButton = stick.getRawButton(lowGearButtonNumber);
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
