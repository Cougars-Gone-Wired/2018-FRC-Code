package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Toggle {
	// declarations of variables used in this class
	private boolean buttonState;
	private boolean output = false;

	// declarations of things that are assigned in the constructor
	private Joystick stick;
	private int buttonNumber;

	public Toggle(Joystick stick, int buttonNumber) {
		// assigning previously declared things to the things passed into the
		// constructor
		this.stick = stick;
		this.buttonNumber = buttonNumber;
	}

	// method that makes a button output true on one press and false on the next
	// press and so on and so forth
	public boolean toggle() {
		if (stick.getRawButton(buttonNumber)) {
			if (!buttonState) {
				output = !output;
			}
			buttonState = true;
		} else {
			buttonState = false;
		}
		return output;
	}
}
