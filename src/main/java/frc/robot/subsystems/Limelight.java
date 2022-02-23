package frc.robot.subsystems;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Limelight {
    
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

    static NetworkTable table;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }
    
    public static double tx() { //gets x from the limelight
        return table.getEntry("tx").getDouble(0.0);
     }
    
     public static double ty() { //gets y from limelight
        return table.getEntry("ty").getDouble(0.0);
     }

     public static double ta() { //gets the area from the limelight
        return table.getEntry("ta").getDouble(0.0);
     }

    public static double d() { //gets the distance from the limelight


        double h2 = 200f; //target height
        double h1 = 200f; //camera height

        double a1 = 90f; //camera angle
        double a2 = ty(); //target angle
    
        
        //d = (h2-h1) / tan(a1+a2).

        double h = h2 - h1;
        double a = a1 + a2;
        double tanA = Math.tan(a);
        double d = h / tanA;

        return d; //returns distance
        }
    
    
    
    // The test to see if the program is working to get the distance from the limelight
    public static double distanceToTarget(double h1, double a1,
        double h2, double a2) {
    //List the variables that will be needed for the equation
    //this method only will work on cameras that are fixed
    //They are all done in CENTIMETERS

    
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
    double tanA = Math.tan(a);

    //h / tanA
    //d is the distance to the target
    //this will make the equation d
    double d = h / tanA;
    return d;
    
    }
}