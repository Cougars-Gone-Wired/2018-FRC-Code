package org.usfirst.frc.team2996.robot;

import java.io.File;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateLister {

	static SendableChooser<String> gsonChooser = new SendableChooser<>();

	// method that gets all the gson files stored on the roborio and makes each one
	// an option to be chosen on the SmartDashboard to be run in autonomous
	public static List<String> getStateNames() {

		File dir = new File("/home/lvuser/gsonFiles/");
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
			gsonChooser.addObject(files[i].getName(), files[i].getAbsolutePath());
		}
		SmartDashboard.putData("Gson choices", gsonChooser);

		return null;
	}
}
