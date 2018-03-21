package org.usfirst.frc.team2996.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

	public enum CameraStates {
		ON, OFF
	}
	CameraStates currentCameraState = CameraStates.OFF;
	
	CameraServer camera;
	UsbCamera usbCam1;
	
	public Camera() {
//		camera = CameraServer.getInstance();
////		usbCam1 = new UsbCamera("usb1", 0);
//		usbCam1 = camera.startAutomaticCapture("usb1", 0);
		
		CameraServer camera = CameraServer.getInstance();
		UsbCamera usbCam1 = camera.startAutomaticCapture("usb1", 0);
	}
	
	public void cameraxxxFunctions(boolean cameraButtonOutput) {
		switch(currentCameraState) {
		case ON:
			camera.startAutomaticCapture(usbCam1);
			break;
		case OFF:
			camera.removeCamera("usb1");
			break;
		}
	}
}
