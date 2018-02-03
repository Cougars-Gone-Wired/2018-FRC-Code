package org.usfirst.frc.team2996.robot;

public class Constants {

	//static final boolean compBot = true;
	
	static final int MANIPULATOR_STICK_PORT = 0;
	static final int ELEVATOR_AXIS = 1;
	static final int ELEVATOR_HIGH_GEAR_BUTTON = 6;
	static final int ELEVATOR_LOW_GEAR_BUTTON = 5;
	static final int INTAKE_TRIGGER = 2;
	static final int OUTTAKE_TRIGGER = 3;
	static final int ARM_BUTTON = 1;
	
	static final int MOBILITY_STICK_PORT = 1;
	static final int DRIVE_FORWARD_AXIS = 1;
	static final int DRIVE_TURN_AXIS = 4;
	static final int DRIVE_HIGH_GEAR_BUTTON = 6;
	static final int DRIVE_LOW_GEAR_BUTTON = 5;

	static final int ELEVATOR_MASTER_MOTOR_ID = 99;
	static final int ELEVATOR_SLAVE_MOTOR_ID = 99;
	static final int LEFT_INTAKE_MOTOR_ID = 99;
	static final int RIGHT_INTAKE_MOTOR_ID = 99;
	static final int FRONT_LEFT_MOTOR_ID = 99;
	static final int REAR_LEFT_MOTOR_ID = 99;
	static final int FRONT_RIGHT_MOTOR_ID = 99;
	static final int REAR_RIGHT_MOTOR_ID = 99;
	
	static final int ARM_SOLENOID_UP_PORT = 99;
	static final int ARM_SOLENOID_DOWN_PORT = 99;
	static final int CHANGE_DRIVE_GEAR_SOLENOID_PORT = 99;
	static final int CHANGE_ELEVATOR_GEAR_SOLENOID_PORT = 99;
	
	static final String DRIVE_GEAR_DEFAULT = "low";
	static final String ELEVATOR_GEAR_DEFAULT = "high";
	
}