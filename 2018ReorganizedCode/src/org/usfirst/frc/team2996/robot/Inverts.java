package org.usfirst.frc.team2996.robot;

public class Inverts {

	public static void setInverts(Robot robot) { // method for inverting any motors if necessary
		robot.getElevator().getElevatorMasterMotor().setInverted(false);
		robot.getElevator().getElevatorSlaveMotor().setInverted(false);
		robot.getIntake().getLeftIntakeMotor().setInverted(false);
		robot.getIntake().getRightIntakeMotor().setInverted(false);

		robot.getDrive().getFrontLeftMotor().setInverted(false);
		robot.getDrive().getRearLeftMotor().setInverted(false);
		robot.getDrive().getFrontRightMotor().setInverted(false);
		robot.getDrive().getRearRightMotor().setInverted(false);
	}

}
