/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2996.robot;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

	// declarations of objects for each class with methods that need to be called in
	// this class
	private Joysticks joysticks;

	private Drive drive;

	private AutoMethods autoMethods;

	private StateRecorder recorder;
	private StateRunner runner;
	
	// Constants constants;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		// instantiations of all previously declared objects
		joysticks = new Joysticks();
		
		drive = new Drive();
		
		// objects that need to be instantiated at the end of robotInit because they use
		// other objects in this class
		autoMethods = new AutoMethods(this);

		recorder = new StateRecorder(this);
		runner = new StateRunner(this);

		// constants = new Constants();

		// static methods that need to be called in robotInit
		SmartDashboardSettings.displaySettings(); // put things on the SmartDashboard
		Inverts.setInverts(this); // invert any motors if necessary
		
		 CameraServer camera = CameraServer.getInstance();
		 camera.startAutomaticCapture();
		 UsbCamera usbCam2 = camera.startAutomaticCapture("usb2", 0);
		 usbCam2.setResolution(120, 80);
		 VideoMode mode2 = usbCam2.getVideoMode();
		 SmartDashboard.putString("mode2", mode2.pixelFormat.toString());
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//autoMethods.getInfo(); // get the field color configuration
		autoMethods.autoReset();
		autoMethods.startDelayTimer(); // start timer to possibly be used to delay auto
		
		
		runner.counterInitialize(); // set counter to 0
		try {
			// read the gson file for the selected gson auto
			List<State> states = StateReader.read(StateLister.gsonChooser.getSelected());
			System.out.println("state size =" + states.size());
			runner.setStates(states); // get all the states from the gson file to be used in auto
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//if (SmartDashboardSettings.useDeadReckoningAuto) {
			//autoMethods.pickAuto(); // run selected dead reckoning auto
		//} else if (SmartDashboardSettings.useRecorderAuto) {
			runner.run(); // run selected gson auto
		//}
		
		//autoMethods.driveForwardCrossLine();
	}

	@Override
	public void teleopInit() {
		
		recorder.initialize(); // create new array list
		runner.counterInitialize(); // set counter to 0
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		joysticks.setJoystickInputValues(); // checks all values being inputted into the controllers

		drive.arcadeDrive(joysticks.getDriveForwardAxis(), joysticks.getDriveTurnAxis()); // run the drive train

		recorder.record(); // record the states of the motors and solenoids every 20 milliseconds
	}

	@Override
	public void disabledInit() {
		SmartDashboardSettings.setConstantVars();
		
		if (SmartDashboardSettings.shouldRecord) {
			List<State> states = recorder.getStates(); // get the states recorded in teleop
			try {
				// write the states to a gson file
				System.out.println("writing");
				StatesWriter.writeStates(states, SmartDashboardSettings.gsonFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		StateLister.getStateNames(); // list all available gson files on the smartDahsboard
	}

	@Override
	public void disabledPeriodic() {
		SmartDashboardSettings.setConstantVars();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	// getters for all the objects declared in this class
	public Joysticks getJoysticks() {
		return joysticks;
	}

	public Drive getDrive() {
		return drive;
	}

	public StateRecorder getRecorder() {
		return recorder;
	}

	public StateRunner getRunner() {
		return runner;
	}

}
