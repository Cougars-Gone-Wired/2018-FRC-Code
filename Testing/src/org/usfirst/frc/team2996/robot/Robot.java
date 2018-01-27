
package org.usfirst.frc.team2996.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public static final String testLift = "Test Lift";
	public static final String testIntake = "Test Intake";
	public static final String testArm = "Test Arm";
	public static final String other = "Other";
	String testSelected;
	SendableChooser<String> testChooser = new SendableChooser<>();

	Joystick stick;
	WPI_TalonSRX talon1;
	WPI_TalonSRX talon2;
	SpeedControllerGroup talons;
	
	DigitalInput reedSwitch;
	DigitalInput lineBreak;
	AnalogInput ultrasonic;

	// Toggle armToggle;

	SensorCollection talon1Sensors;
	SensorCollection talon2Sensors;

	DoubleSolenoid solenoid;

	AnalogInput pressureSensor;

	AHRS navX;

	@Override
	public void robotInit() {
		System.out.println("In robotInit");

		testChooser.addObject("Test Lift", testLift);
		testChooser.addObject("Test Intake", testIntake);
		testChooser.addObject("Test Arm", testArm);
		testChooser.addObject("Other", other);
		SmartDashboard.putData("Test Mode", testChooser);

		SmartDashboard.putNumber("Speed Set", 0.5);

		stick = new Joystick(0);

		talon1 = new WPI_TalonSRX(3);
		talon2 = new WPI_TalonSRX(2);
		talons = new SpeedControllerGroup(talon1, talon2);
		
		reedSwitch = new DigitalInput(0);
		lineBreak = new DigitalInput(1);
		
		ultrasonic = new AnalogInput(0);

		// armToggle = new Toggle(stick, 1);

		talon1Sensors = new SensorCollection(talon1);
		talon2Sensors = new SensorCollection(talon2);
		talon1.setSelectedSensorPosition(0, 0, 0);

		solenoid = new DoubleSolenoid(1, 0);

//		pressureSensor = new AnalogInput(0);

		navX = new AHRS(SPI.Port.kMXP);
		navX.reset();

		// CameraServer camera = CameraServer.getInstance();
		// UsbCamera usbCam1 = new UsbCamera("usb1", 0);
		// //usbCam1.setPixelFormat(PixelFormat.kGray);
		// //VideoMode mode = usbCam1.getVideoMode();
		// //SmartDashboard.putString("mode", mode.pixelFormat.toString());
		// usbCam1.setResolution(600, 480);
		// camera.addCamera(usbCam1);
		// camera.startAutomaticCapture();
		// UsbCamera usbCam2 = camera.startAutomaticCapture("usb2", 1);
		// usbCam2.setResolution(120, 80);
		// VideoMode mode2 = usbCam2.getVideoMode();
		// SmartDashboard.putString("mode2", mode2.pixelFormat.toString());

	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		testSelected = testChooser.getSelected();
	}

	@Override
	public void teleopPeriodic() {
		switch (testSelected) {
		case testLift:
			// for lift
			if (stick.getRawAxis(1) > 0.15) {
				talons.set(-stick.getRawAxis(1) * SmartDashboard.getNumber("Speed Set", 0.5));
			} else if (stick.getRawAxis(1) < -0.15) {
				talons.set(-stick.getRawAxis(1) * SmartDashboard.getNumber("Speed Set", 0.5));
			} else {
				talons.stopMotor();
			}
			break;
		case testIntake:
			// for intake
			if (stick.getRawButton(1)) {
				talon1.set(SmartDashboard.getNumber("Speed Set", 0.5));
				talon2.set(-SmartDashboard.getNumber("Speed Set", 0.5));
			} else if (stick.getRawButton(2)) {
				talon1.set(-SmartDashboard.getNumber("Speed Set", 0.5));
				talon2.set(SmartDashboard.getNumber("Speed Set", 0.5));
			} else {
				talons.stopMotor();
			}
			break;
		case testArm:
			// for arm
			if (stick.getRawButton(1)) {
				solenoid.set(DoubleSolenoid.Value.kReverse);
			} else if (stick.getRawButton(2)) {
				solenoid.set(DoubleSolenoid.Value.kForward);
			} else if (stick.getRawButton(4)) {
				solenoid.set(DoubleSolenoid.Value.kOff);
			}
			break;
		case other:
			// other
			if (stick.getRawButton(1)) {
				talon1.set(SmartDashboard.getNumber("Speed Set", 0.25));
			} else if (stick.getRawButton(2)) {
				talon1.set(-SmartDashboard.getNumber("Speed Set", 0.25));
			} else {
				talon1.set(0);
			}

			if (stick.getRawButton(3)) {
				talon2.set(SmartDashboard.getNumber("Speed Set", 0.25));
			} else {
				talon2.set(0);
			}

			SmartDashboard.putNumber("Voltage", talon1.getMotorOutputVoltage());
			SmartDashboard.putNumber("Current", talon1.getOutputCurrent());
			SmartDashboard.putNumber("Encoder Position", talon1.getSelectedSensorPosition(0));
//			SmartDashboard.putNumber("Pressure Sensor", pressureSensor.getValue());
			SmartDashboard.putNumber("Gyro Yaw", navX.getYaw());
			SmartDashboard.putBoolean("FwdLimitSwitch", talon1Sensors.isFwdLimitSwitchClosed());
			SmartDashboard.putBoolean("RevLimitSwitch", talon1Sensors.isRevLimitSwitchClosed());
			SmartDashboard.putBoolean("Reed Switch", reedSwitch.get());
			SmartDashboard.putBoolean("Line Break", lineBreak.get());
			SmartDashboard.putNumber("Ultrasonic", ultrasonic.getVoltage() * 108.7);
			break;
		}

	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {

	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}

}
