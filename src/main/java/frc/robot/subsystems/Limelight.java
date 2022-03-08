package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.commands.CalcEngine;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Limelight extends SubsystemBase {
    
    private NetworkTable table;
    private CalcEngine calc;
    int pCount = 0;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        calc = new CalcEngine(Constants.launchHeightFeet, Constants.targetHeight);
    }

    @Override
    public void periodic() {
      // just refresh every 10 times (.2s)
      if (pCount != 10) {
        pCount++;
        return;
      }

      pCount = 0;
      // This method will be called once per scheduler run
      SmartDashboard.putNumber("LL X", tx());
      SmartDashboard.putNumber("LL Y", ty());
      SmartDashboard.putNumber("LL TA", ta());
      SmartDashboard.putNumber("LL Target", tv());
      SmartDashboard.putNumber("LL Distance", distance());

      SmartDashboard.putNumber("LL ProjVel", projVelo());
      SmartDashboard.putNumber("LL ProjRPM", projRpm());
      SmartDashboard.putNumber("LL TablRPM", tableRpm());
    }
    
    public double tx() { //gets x from the limelight
        return table.getEntry("tx").getDouble(0.0);
     }
    
     public double ty() { //gets y from limelight
        return table.getEntry("ty").getDouble(0.0);
     }

     public double tv(){
         return table.getEntry("tv").getDouble(0.0);
     }

     public double ta() { //gets the area from the limelight
        return table.getEntry("ta").getDouble(0.0);
     }

    public double distance() { //gets the distance from the limelight
        return distanceToTarget(
                Constants.limelightMountHeight, Constants.limelightMountAngle,
                Constants.targetHeight, ty());
    }
    
    
    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public double projVelo() {
      return calc.getVelo(distance(), degreesToRadians(Constants.limelightMountAngle));
    }

    public double projRpm() {
      return projVelo() * 60 / (Constants.launchWheelDiameterFeet * Math.PI);
    }

    public double tableRpm() {
      return distance() * 82.738 + 143.2;
    }

    // distanceToTarget:
    //   Return the distance to the hub along the horizontal plane.
    //   Known (measured) values:
    //      mountHeight (h1) - height above floor that camera is mounted.
    //      mountAngle (a1) - angle in radians above horizontal that limelight is pointed.
    //      targetHeight (h2) - target height (height of hub)
    //   Given the limelight provided 'ty' value:
    //      offsetAngle (a2) - vertical offset from crosshair to target
    //   https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
    //
    // Angles are in Degrees.
    public static double distanceToTarget(double h1, double a1, double h2, double a2) {
    //List the variables that will be needed for the equation
    //this method only will work on cameras that are fixed
    
    //This is the height of the target
    //h2 = 100;
    
    //this is the height of the camera above the floor;
    //h1 = 134;

        //This is the angle of the fixed camera
    //    a1 = 90;

    /*This is the variable for the angle to the target you are aiming at.
    The limelight will determine this for you*/
    //    a2 =70;
    
        
    //start the equation to calculate distance d = (h2-h1) / tan(a1+a2).

    //h2 - h1
    //h is the variable for both of the heights after they are subtracted from each other
    //this makes the equation d = h / tan(a1+a2)
        double h = h2 - h1;

    //a1 + a2
    //a is the variable for both of the angles
    //this makes the equation d = h / tan(a)
    double a = a1 + a2;

    //tan(a)
    //tanA is the tangent of the variable a
    //this will make the equation d = h / tanA
    double tanA = Math.tan(degreesToRadians(a));

    //h / tanA
    //d is the distance to the target
    //this will make the equation d
    double d = h / tanA;
    return d;
    
    }
}
