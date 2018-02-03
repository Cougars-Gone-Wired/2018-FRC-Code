package org.usfirst.frc.team2996.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardSettings {

	static double driveSpeed;
	static double autoDriveSpeed;
	static double autoTurnSpeed;
	static String autoStartSide;
	
	static double driveForwardCrossLineDistance;
	static double driveForwardDropCubeorNahForwardDistance;
	static double driveForwardTurnDropCubeorNahForwardDistance1;
	static double driveForwardTurnDropCubeorNahForwardDistance2;
	
	static boolean shouldRecord;
	
	static boolean useDeadReckoningAuto;
	static boolean useRecorderAuto;
	
	public static void initialize() {
		displaySettings();
		setConstantVars();
	}
	
	public static void displaySettings() {
		SmartDashboard.putNumber("Drive Speed", 1.0);
		SmartDashboard.putNumber("Auto Drive Speed", 1.0);
		SmartDashboard.putNumber("Auto Turn Speed", 0.25);
		SmartDashboard.putString("Auto Start Side", "");

		SmartDashboard.putNumber("Drive Forward Cross Line Distance", 120); // in inches
		SmartDashboard.putNumber("Drive Forward Drop Cube or Nah Forward Distance", 132); // in inches
		SmartDashboard.putNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 1", 150); // in inches
		SmartDashboard.putNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 2", 24); // in inches

		SmartDashboard.putBoolean("Should Record", false);
		
		SmartDashboard.putBoolean("Use Dread Reckoning Auto", false);
		SmartDashboard.putBoolean("Use Recorder Auto", false);
	}
	
	public static void setConstantVars() {
		driveSpeed = SmartDashboard.getNumber("Drive Speed", 1.0);
		autoDriveSpeed = SmartDashboard.getNumber("Auto Drive Speed", 1.0);
		autoTurnSpeed = SmartDashboard.getNumber("Auto Turn Speed", 0.25);
		autoStartSide = SmartDashboard.getString("Auto Start Side", "");
		
		driveForwardCrossLineDistance = SmartDashboard.getNumber("Drive Forward Cross Line Distance", 0);
		driveForwardDropCubeorNahForwardDistance = SmartDashboard.getNumber("Drive Forward Drop Cube or Nah Forward Distance", 0);
		driveForwardTurnDropCubeorNahForwardDistance1 = SmartDashboard.getNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 1", 0);
		driveForwardTurnDropCubeorNahForwardDistance2 = SmartDashboard.getNumber("Drive Forward Turn Drop Cube or Nah Forward Distance 2", 0);
		
		shouldRecord = SmartDashboard.getBoolean("Should Record", false);
		
		useDeadReckoningAuto = SmartDashboard.getBoolean("Use Dread Reckoning Auto", false);
		useRecorderAuto = SmartDashboard.getBoolean("Use Recorder Auto", false);
	}

}
