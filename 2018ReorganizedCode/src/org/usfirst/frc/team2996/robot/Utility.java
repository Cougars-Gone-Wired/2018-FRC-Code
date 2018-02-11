package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Utility {

	// method for putting a threshold on stick axes that must be passed before they
	// can output their actual value
	public static double deadZone(double axisOutputValue) {
		if (Math.abs(axisOutputValue) > 0.3) {
			return axisOutputValue;
		} else {
			return 0;
		}
	}
	
	public static double invertAxis(double axisValue) {
		return -axisValue;
	}

	public static void invertTalon(WPI_TalonSRX talon) {
		talon.setInverted(true);
	}
}
