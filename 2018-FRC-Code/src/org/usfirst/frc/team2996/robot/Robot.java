/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	static int MANIPULATOR_STICK_PORT = 0;
	static int INTAKE_BUTTON;
	static int OUTTAKE_BUTTON;
	static int ARM_BUTTON;
	static int ELEVATOR_AXIS = 1;
	
	static int MOBILITY_STICK_PORT = 1;
	static int DRIVE_FORWARD_AXIS = 1;
	static int DRIVE_TURN_AXIS = 4;
	static int HIGH_GEAR_BUTTON;
	static int LOW_GEAR_BUTTON;
	
	static int ARM_SOLENOID_PORT1;
	static int ARM_SOLENOID_PORT2;
	static int CHANGE_GEAR_SOLENOID_PORT;

	static int LEFT_INTAKE_MOTOR;
	static int RIGHT_INTAKE_MOTOR;
	static int ELEVATOR_MOTOR_1_ID;
	static int ELEVATOR_MOTOR_2_ID;
	static int FRONT_LEFT_MOTOR_ID;
	static int REAR_LEFT_MOTOR_ID;
	static int FRONT_RIGHT_MOTOR_ID;
	static int REAR_RIGHT_MOTOR_ID;

	Joystick manipulatorStick;
	Toggle armButton;
	
	Joystick mobilityStick;
	
	DoubleSolenoid armSolenoid;
	Solenoid changeGearSolenoid;

	WPI_TalonSRX leftIntakeMotor;
	WPI_TalonSRX rightIntakeMotor;
	
	WPI_TalonSRX elevatorMotor1;
	WPI_TalonSRX elevatorMotor2;
	SensorCollection elevatorMotor1Sensors;
	SensorCollection elevatorMotor2Sensors;
	SpeedControllerGroup elevatorMotors;
	
	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;
	SpeedControllerGroup leftMotors;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	SpeedControllerGroup rightMotors;

	DifferentialDrive robotDrive;

	Intake intake;
	Arm arm;
	Elevator elevator;
	Drive drive;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		manipulatorStick = new Joystick(MANIPULATOR_STICK_PORT);
		armButton = new Toggle(manipulatorStick, ARM_BUTTON);
		
		mobilityStick = new Joystick(MOBILITY_STICK_PORT);
	
		armSolenoid = new DoubleSolenoid(ARM_SOLENOID_PORT1, ARM_SOLENOID_PORT2);
		changeGearSolenoid = new Solenoid(CHANGE_GEAR_SOLENOID_PORT);

		leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE_MOTOR);
		rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE_MOTOR);
		
		elevatorMotor1 = new WPI_TalonSRX(ELEVATOR_MOTOR_1_ID);
		elevatorMotor2 = new WPI_TalonSRX(ELEVATOR_MOTOR_2_ID);
		elevatorMotor1Sensors = new SensorCollection(elevatorMotor1);
		elevatorMotor2Sensors = new SensorCollection(elevatorMotor2);
		elevatorMotors = new SpeedControllerGroup(elevatorMotor1, elevatorMotor2);
		
		frontLeftMotor = new WPI_TalonSRX(FRONT_LEFT_MOTOR_ID);
		rearLeftMotor = new WPI_TalonSRX(REAR_LEFT_MOTOR_ID);
		leftMotors = new SpeedControllerGroup(frontLeftMotor, rearRightMotor);

		frontRightMotor = new WPI_TalonSRX(FRONT_RIGHT_MOTOR_ID);
		rearRightMotor = new WPI_TalonSRX(REAR_RIGHT_MOTOR_ID);
		rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);

		robotDrive = new DifferentialDrive(leftMotors, rightMotors);

		intake = new Intake(this);
		arm = new Arm(this);
		elevator = new Elevator(this);
		drive = new Drive(this);
		
		setInverts();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	@Override
	public void teleopInit() {
	}
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		intake.intakeFunctions();
		arm.arm();
		elevator.elevatorFunctions();
		drive.arcadeDrive();
		drive.changeGear();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void displaySettings() {
		SmartDashboard.putNumber("Drive Speed", 1.0);
	}
	
	public void setInverts() {
		leftIntakeMotor.setInverted(false);
		rightIntakeMotor.setInverted(false);
		elevatorMotor1.setInverted(false);
		elevatorMotor2.setInverted(false);
		frontLeftMotor.setInverted(false);
		rearLeftMotor.setInverted(false);
		frontRightMotor.setInverted(false);
		rearRightMotor.setInverted(false);
	}
	
	public Joystick getManipulatorStick() {
		return manipulatorStick;
	}

	public Toggle getArmButton() {
		return armButton;
	}

	public Joystick getMobilityStick() {
		return mobilityStick;
	}

	public DoubleSolenoid getArmSolenoid() {
		return armSolenoid;
	}

	public Solenoid getChangeGearSolenoid() {
		return changeGearSolenoid;
	}

	public WPI_TalonSRX getLeftIntakeMotor() {
		return leftIntakeMotor;
	}

	public WPI_TalonSRX getRightIntakeMotor() {
		return rightIntakeMotor;
	}

	public SensorCollection getElevatorMotor1Sensors() {
		return elevatorMotor1Sensors;
	}

	public SensorCollection getElevatorMotor2Sensors() {
		return elevatorMotor2Sensors;
	}

	public SpeedControllerGroup getElevatorMotors() {
		return elevatorMotors;
	}

	public WPI_TalonSRX getFrontLeftMotor() {
		return frontLeftMotor;
	}

	public WPI_TalonSRX getRearLeftMotor() {
		return rearLeftMotor;
	}

	public WPI_TalonSRX getFrontRightMotor() {
		return frontRightMotor;
	}

	public WPI_TalonSRX getRearRightMotor() {
		return rearRightMotor;
	}

	public DifferentialDrive getRobotDrive() {
		return robotDrive;
	}
}
