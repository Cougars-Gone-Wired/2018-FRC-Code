package org.usfirst.frc.team2996.robot;

public class Utility {

	// method for putting a threshold on stick axes that must be passed before they
	// can output their actual value
	public static double deadZone(double axisOutputValue) {
		if (Math.abs(axisOutputValue) > 0.15) {
			return axisOutputValue;
		} else {
			return 0;
		}
	}
}
