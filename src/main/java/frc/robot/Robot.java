/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Solenoid;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private WPI_VictorSPX left1 = new WPI_VictorSPX(1); 
  private WPI_VictorSPX left2 = new WPI_VictorSPX(2);
  private WPI_VictorSPX right1 = new WPI_VictorSPX(3);
  private WPI_VictorSPX right2 = new WPI_VictorSPX(4);
  private XboxController joystick = new XboxController(0); 
  private SpeedControllerGroup leftGroup = new SpeedControllerGroup(left1, left2);
  private SpeedControllerGroup rightGroup = new SpeedControllerGroup(right1, right2);
  private DifferentialDrive drive = new DifferentialDrive(leftGroup, rightGroup); 
  private DoubleSolenoid s1 = new DoubleSolenoid(0, 1);
  private DoubleSolenoid s2 = new DoubleSolenoid(2, 3);
  private Double velocityY = 0.0; 
  private boolean isArcade = true;
  private int stateDownCounter = 0;

  public enum armState
  {
    start,
    extended,
    retracted,
  }

  private armState state = armState.retracted;
  Thread m_visionThread;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_visionThread = new Thread(() -> {
      // Get the Axis camera from CameraServer
      UsbCamera camera
          = CameraServer.getInstance().startAutomaticCapture();
      // Set the resolution
      camera.setResolution(180, 120);
      camera.setFPS(30);

      // Get a CvSink. This will capture Mats from the camera
      CvSink cvSink = CameraServer.getInstance().getVideo();
      // Setup a CvSource. This will send images back to the Dashboard
      CvSource outputStream
          = CameraServer.getInstance().putVideo("Rectangle", 180, 120);

      // Mats are very memory expensive. Lets reuse this Mat.
      Mat mat = new Mat();

      // This cannot be 'true'. The program will never exit if it is. This
      // lets the robot stop this thread when restarting robot code or
      // deploying.
      while (!Thread.interrupted()) {
        // Tell the CvSink to grab a frame from the camera and put it
        // in the source mat.  If there is an error notify the output.
        if (cvSink.grabFrame(mat) == 0) {
          // Send the output the error.
          outputStream.notifyError(cvSink.getError());
          // skip the rest of the current iteration
          continue;
        }
        // Put a rectangle on the image
        Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
            new Scalar(255, 255, 255), 5);
        // Give the output stream a new image to display
        outputStream.putFrame(mat);
      }
    });
    m_visionThread.setDaemon(true);
    m_visionThread.start();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */

  @Override
  public void robotPeriodic() 
  {
    if (joystick.getYButtonPressed())
    {
      isArcade = !isArcade;
    }

    velocityY += (joystick.getY(Hand.kLeft) - velocityY) * 0.03f;


    if (velocityY > 1)
    {
      velocityY = 1.0;
    }
    if (velocityY < -1)
    {
      velocityY = -1.0;
    }

    SmartDashboard.putBoolean("Arcade", isArcade);
    if (isArcade)  
    {
      // drive.curvatureDrive(-velocityY, joystick.getX(Hand.kLeft), joystick.getY(Hand.kLeft) == 0);
      drive.arcadeDrive(-velocityY, joystick.getX(Hand.kLeft) * 0.5f, true);
    }else
    {
      drive.arcadeDrive(-joystick.getY(Hand.kLeft), joystick.getX(Hand.kLeft) * 0.7f, true);
    }
  
    if (joystick.getAButtonPressed())
    {
      if (s1.get() == Value.kForward)
      {
        s1.set(Value.kReverse);
      }
      else
      {
        s1.set(Value.kForward);
      }
    }

    if (s1.get() == Value.kForward)
    {
      SmartDashboard.putBoolean("Shelf", true);
    }
    else
    {
      SmartDashboard.putBoolean("Shelf", false);
    }

    if (s2.get() == Value.kForward)
    {
      SmartDashboard.putBoolean("Lever", false);
    }
    else
    {
      SmartDashboard.putBoolean("Lever", true);
    }


    if (joystick.getBButtonPressed())
    {
      if (s2.get() == Value.kForward)
      {
        s2.set(Value.kReverse);
      }
      else
      {
        s2.set(Value.kForward);
      }
    } 

    if (joystick.getXButtonPressed() || state != armState.retracted)
    {
      placeHatch();
    }
  }

  private boolean placeHatch()
  {
    if (stateDownCounter > 0) 
      {
        stateDownCounter--;
        return true;
      }
    if (state == armState.retracted)
    {
      
      state = armState.start;
      s1.set(Value.kReverse);
      stateDownCounter = 10; 
    }
    else if (state == armState.start)
    {
      state = armState.extended;
      s2.set(Value.kReverse);
      stateDownCounter = 10;
    }
    else if (state == armState.extended)
    {
      state = armState.retracted;
      s1.set(Value.kForward);
    }

    return false;
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
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
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

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() 
  {

  }
}
