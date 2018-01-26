
package org.usfirst.frc.team2996.robot;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	Joystick manipulatorStick;
	Toggle armSolenoidButton;
	Joystick mobilityStick;

	DoubleSolenoid armSolenoid;
	Solenoid leftDriveSolenoid;
	Solenoid rightDriveSolenoid;

	WPI_TalonSRX intakeMotorLeft;
	WPI_TalonSRX intakeMotorRight;

	WPI_TalonSRX elevatorMotor1;
	WPI_TalonSRX elevatorMotor2;
	SpeedControllerGroup elevatorMotors;

	WPI_TalonSRX frontLeftMotor;
	WPI_TalonSRX rearLeftMotor;
	SpeedControllerGroup leftMotors;

	WPI_TalonSRX frontRightMotor;
	WPI_TalonSRX rearRightMotor;
	SpeedControllerGroup rightMotors;

	DifferentialDrive robotDrive;

	StateRecorder recorder;
	StateRunner runner;

	@Override
	public void robotInit() {
		System.out.println("In robotInit");

		SmartDashboard.putNumber("Speed Set", 0.5);
		SmartDashboard.putString("Gson File Name", "defaultName");

		manipulatorStick = new Joystick(0);
		armSolenoidButton = new Toggle(manipulatorStick, 2);
		mobilityStick = new Joystick(1);

		armSolenoid = new DoubleSolenoid(0, 1);
		leftDriveSolenoid = new Solenoid(2);
		rightDriveSolenoid = new Solenoid(3);

		intakeMotorLeft = new WPI_TalonSRX(0);
		intakeMotorRight = new WPI_TalonSRX(1);

		elevatorMotor1 = new WPI_TalonSRX(2);
		elevatorMotor2 = new WPI_TalonSRX(3);
		elevatorMotors = new SpeedControllerGroup(elevatorMotor1, elevatorMotor2);

		frontLeftMotor = new WPI_TalonSRX(4);
		rearLeftMotor = new WPI_TalonSRX(5);
		leftMotors = new SpeedControllerGroup(frontLeftMotor, rearRightMotor);

		frontRightMotor = new WPI_TalonSRX(6);
		rearRightMotor = new WPI_TalonSRX(7);
		rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);

		robotDrive = new DifferentialDrive(leftMotors, rightMotors);

		recorder = new StateRecorder(this);
		runner = new StateRunner(this);
	}

	@Override
	public void autonomousInit() {
		recorder.counterInitialize();

		try {
			List<State> states = StateReader.read(StateLister.gsonChooser.getSelected());
			runner.setStates(states);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void autonomousPeriodic() {
		runner.run();
	}

	@Override
	public void teleopInit() {
		recorder.initialize();
		recorder.counterInitialize();
	}

	@Override
	public void teleopPeriodic() {
		// for arm
		if(manipulatorStick.getRawButton(1)) {
			armSolenoid.set(DoubleSolenoid.Value.kReverse);
		} else if (manipulatorStick.getRawButton(2)) {
			armSolenoid.set(DoubleSolenoid.Value.kForward);
		} else if (manipulatorStick.getRawButton(4)) {
			armSolenoid.set(DoubleSolenoid.Value.kOff);
		}

		// for intake
		if (manipulatorStick.getRawButton(6) && !manipulatorStick.getRawButton(5)) {
			intakeMotorLeft.set(SmartDashboard.getNumber("Speed Set", 0.25));
			intakeMotorRight.set(-SmartDashboard.getNumber("Speed Set", 0.25));
		} else if (manipulatorStick.getRawButton(5) && !manipulatorStick.getRawButton(6)) {
			intakeMotorLeft.set(-SmartDashboard.getNumber("Speed Set", 0.25));
			intakeMotorRight.set(SmartDashboard.getNumber("Speed Set", 0.25));
		} else {
			intakeMotorLeft.stopMotor();
			intakeMotorRight.stopMotor();
		}

		// for lift
		if (manipulatorStick.getRawAxis(1) > 0.15) {
			elevatorMotors.set(-manipulatorStick.getRawAxis(1) * SmartDashboard.getNumber("Speed Set", 0.5));
		} else if (manipulatorStick.getRawAxis(1) < -0.15) {
			elevatorMotors.set(-manipulatorStick.getRawAxis(1) * SmartDashboard.getNumber("Speed Set", 0.25));
		} else {
			elevatorMotors.stopMotor();
		}

		robotDrive.arcadeDrive(mobilityStick.getRawAxis(1) * SmartDashboard.getNumber("Speed Set", 1),
				mobilityStick.getRawAxis(4) * SmartDashboard.getNumber("Speed Set", 1));

		// for changing gears
		if (mobilityStick.getRawButton(2) && !mobilityStick.getRawButton(1)) {
			leftDriveSolenoid.set(true);
			rightDriveSolenoid.set(true);
		} else if (mobilityStick.getRawButton(1) && !mobilityStick.getRawButton(2)) {
			leftDriveSolenoid.set(false);
			rightDriveSolenoid.set(false);
		}

		recorder.record();

	}

	@Override
	public void disabledInit() {
		System.out.println("In disabledInit");

		List<State> states = recorder.getStates();

		try {
			StatesWriter.writeStates(states, SmartDashboard.getString("Gson File Name", "notGood"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StateLister.getStateNames();
	}

	@Override
	public void disabledPeriodic() {
		// System.out.println("In disabledPeriodic");
	}

	public DoubleSolenoid getArmSolenoid() {
		return armSolenoid;
	}

	public Solenoid getLeftDriveSolenoid() {
		return leftDriveSolenoid;
	}

	public Solenoid getRightDriveSolenoid() {
		return rightDriveSolenoid;
	}

	public WPI_TalonSRX getIntakeMotorLeft() {
		return intakeMotorLeft;
	}

	public WPI_TalonSRX getIntakeMotorRight() {
		return intakeMotorRight;
	}

	public WPI_TalonSRX getElevatorMotor1() {
		return elevatorMotor1;
	}

	public WPI_TalonSRX getElevatorMotor2() {
		return elevatorMotor2;
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


}
