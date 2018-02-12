package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardSettings {

	// declarations for all variables to be assigned to he values on the
	// SmartDashboard
	static double driveSpeed;
	static double autoDriveSpeed;
	static double autoTurnSpeed;
	static String autoStartSide;
	static double autoDelay;

	static double driveForwardCrossLineDistance;
	static double driveForwardDropCubeorNahForwardDistance;
	static double driveForwardTurnDropCubeorNahForwardDistance1;
	static double driveForwardTurnDropCubeorNahForwardDistance2;

	static boolean shouldRecord;
	static String gsonFileName;

	static boolean useDeadReckoningAuto;
	static boolean useRecorderAuto;

	// method to put everything on the SmartDashboard and set variables to
	// everything on the SmartDashboard

	public static void displaySettings() { // method that puts everything needed on the SmartDashboard
		SmartDashboard.putNumber("Drive Speed", 1.0);
		SmartDashboard.putNumber("Auto Drive Speed", 1.0);
		SmartDashboard.putNumber("Auto Turn Speed", 0.25);
		SmartDashboard.putString("Auto Start Side", "");
		SmartDashboard.putNumber("Auto Delay", 0); // in seconds

		SmartDashboard.putNumber("Drive Forward Cross Line Distance", 120); // in inches
		SmartDashboard.putNumber("Drive Forward Drop Cube or Nah Forward Distance", 132); // in inches
		SmartDashboard.putNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 1", 150); // in inches
		SmartDashboard.putNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 2", 24); // in inches

		SmartDashboard.putBoolean("Should Record", false);
		SmartDashboard.putString("Gson File Name", "");

		SmartDashboard.putBoolean("Use Dread Reckoning Auto", false);
		SmartDashboard.putBoolean("Use Recorder Auto", false);
	}

	// method that sets previously declared variables to the values on the
	// SmartDashboard
	public static void setConstantVars() {
		driveSpeed = SmartDashboard.getNumber("Drive Speed", 1.0);
		autoDriveSpeed = Utility.invertDouble(SmartDashboard.getNumber("Auto Drive Speed", 1.0));
		autoTurnSpeed = SmartDashboard.getNumber("Auto Turn Speed", 0.25);
		autoStartSide = SmartDashboard.getString("Auto Start Side", "");
		autoDelay = SmartDashboard.getNumber("Auto Delay", 0);

		driveForwardCrossLineDistance = SmartDashboard.getNumber("Drive Forward Cross Line Distance", 0);
		driveForwardDropCubeorNahForwardDistance = SmartDashboard
				.getNumber("Drive Forward Drop Cube or Nah Forward Distance", 0);
		driveForwardTurnDropCubeorNahForwardDistance1 = SmartDashboard
				.getNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 1", 0);
		driveForwardTurnDropCubeorNahForwardDistance2 = SmartDashboard
				.getNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 2", 0);

		shouldRecord = SmartDashboard.getBoolean("Should Record", false);
		gsonFileName = SmartDashboard.getString("Gson File Name", "notGood");

		useDeadReckoningAuto = SmartDashboard.getBoolean("Use Dread Reckoning Auto", false);
		useRecorderAuto = SmartDashboard.getBoolean("Use Recorder Auto", false);
	}

}
