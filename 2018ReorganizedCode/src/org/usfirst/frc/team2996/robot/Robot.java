/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2996.robot;

import java.util.List;

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

	private Joysticks joysticks;
	
	private Elevator elevator;
	private Intake intake;
	private Arm arm;
	
	private Drive drive;
	
	private ChangeGear elevatorChangeGear;
	private ChangeGear driveChangeGear;
	
	private AutoMethods autoMethods;
	
	private StateRecorder recorder;
	private StateRunner runner;
	
	//Constants constants;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		elevator = new Elevator();
		intake = new Intake();
		arm = new Arm();
		
		drive = new Drive();
		
		elevatorChangeGear = new ChangeGear(Constants.ELEVATOR_GEAR_DEFAULT);
		driveChangeGear = new ChangeGear(Constants.DRIVE_GEAR_DEFAULT);
		
		autoMethods = new AutoMethods(this);
		
		recorder = new StateRecorder(this);
		runner = new StateRunner(this);
		
		//constants = new Constants();
		
		SmartDashboardSettings.initialize();
		Inverts.setInverts(this);
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
		autoMethods.getInfo();
		
		recorder.counterInitialize();
		try {
			List<State> states = StateReader.read(StateLister.gsonChooser.getSelected());
			runner.setStates(states);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		if (SmartDashboardSettings.useDeadReckoningAuto) {
			autoMethods.pickAuto();
		} else if (SmartDashboardSettings.useRecorderAuto) {
			runner.run();
		}
	}

	@Override
	public void teleopInit() {
		recorder.initialize();
		recorder.counterInitialize();
	}
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		joysticks.setJoystickInputValues();
		
		elevator.elevatorFunctions(joysticks.getElevatorAxis());
		intake.intakeFunctions(joysticks.getIntakeTrigger(), joysticks.getOuttakeTrigger());
		arm.armFunctions(joysticks.isArmButtonOutput());
		
		drive.arcadeDrive(joysticks.getDriveForwardAxis(), joysticks.getDriveTurnAxis());
		
		elevatorChangeGear.changeGear(joysticks.isElevatorHighGearButton(), joysticks.isElevatorLowGearButton(), elevator.getChangeElevatorGearSolenoid());
		driveChangeGear.changeGear(joysticks.isDriveHighGearButton(), joysticks.isDriveLowGearButton(), drive.getChangeDriveGearSolenoid());
		
		if (SmartDashboardSettings.shouldRecord) {
			recorder.record();
		}
	}

	@Override
	public void disabledInit() {
		List<State> states = recorder.getStates();

		if (SmartDashboardSettings.shouldRecord) {
			try {
				StatesWriter.writeStates(states, SmartDashboard.getString("Gson File Name", "notGood"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		StateLister.getStateNames();
	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public Joysticks getJoysticks() {
		return joysticks;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public Intake getIntake() {
		return intake;
	}

	public Arm getArm() {
		return arm;
	}

	public Drive getDrive() {
		return drive;
	}

	public ChangeGear getElevatorChangeGear() {
		return elevatorChangeGear;
	}

	public ChangeGear getDriveChangeGear() {
		return driveChangeGear;
	}

	public StateRecorder getRecorder() {
		return recorder;
	}

	public StateRunner getRunner() {
		return runner;
	}

}
