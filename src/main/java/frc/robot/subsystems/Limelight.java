package frc.robot.subsystems;

import frc.robot.Constants;


import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;


public class Limelight extends SubsystemBase {

    private final String LimelightStr = "Limelight";
    private final String LimelightTabName = "Limelight";

    private ShuffleboardTab llsTab = Shuffleboard.getTab(LimelightTabName);

    protected NetworkTableEntry eX, eY, eArea, eTarget, eDist;
    protected NetworkTable llNetTable, shuffNetTable;
    protected SendableChooser<Integer> curPipeChooser = new SendableChooser<Integer>();
    protected SendableChooser<Integer> ledModeChooser = new SendableChooser<Integer>();
    protected SendableChooser<Integer> camModeChooser = new SendableChooser<Integer>();

    public final String[] pipelines = {"Byting Irish", "Play"};

    public Limelight() {
      llNetTable = NetworkTableInstance.getDefault().getTable(LimelightStr);
      shuffNetTable = NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(LimelightTabName);

      NetworkTableInstance n = NetworkTableInstance.getDefault();
      eX = llsTab.add("TX", 0).getEntry();
      eY = llsTab.add("TY", 0).getEntry();
      eArea  = llsTab.add("Area", 0).getEntry();
      eTarget = llsTab.add("Target", false).getEntry();
      eDist = llsTab.add("Distance", 0.0).getEntry();

      int curPipe = (int)llNetTable.getEntry("getpipe").getDouble(0.0f);
      for (int i=0; i < pipelines.length; i++) {
          curPipeChooser.addOption(pipelines[i], i);
          if (i == curPipe) {
              curPipeChooser.setDefaultOption(pipelines[i], i);
          }
      }
      llsTab.add("Pipeline", curPipeChooser);
      shuffNetTable.getSubTable("Pipeline").addEntryListener("active",
          (table, key, entry, value, flags) -> { handleChange("Pipeline"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

      ledModeChooser.addOption("Pipeline", 0);
      ledModeChooser.addOption("Off", 1);
      ledModeChooser.addOption("Blink", 2);
      ledModeChooser.addOption("On", 3);
      ledModeChooser.setDefaultOption("Pipeline", 0);
      shuffNetTable.getSubTable("LED").addEntryListener("active",
          (table, key, entry, value, flags) -> { handleChange("LED"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
      llsTab.add("LED", ledModeChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);

      llNetTable.addEntryListener("ledMode",
          (table, key, entry, value, flags) -> { handleLimelightChange("LED"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

      camModeChooser.addOption("Vision", 0);
      camModeChooser.addOption("Camera", 1);
      camModeChooser.setDefaultOption("Vision", 0);
      shuffNetTable.getSubTable("CamMode").addEntryListener("active",
          (table, key, entry, value, flags) -> { handleChange("CamMode"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
      llsTab.add("CamMode", camModeChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);


    }

    protected void handleLimelightChange(String key) {
        if (key == "CamMode") {
            // llNetTable.getEntry("camMode").setDouble(camModeChooser.getSelected());
        } else if (key == "LED") {
            int lVal = (int)llNetTable.getEntry("ledMode").getDouble(0.0f);
	    int sVal = ledModeChooser.getSelected();
	    if (lVal != sVal) {
                shuffNetTable.getSubTable("LED").getEntry("selected").setDouble(lVal);
	    }
            // llNetTable.getEntry("ledMode").setDouble(ledModeChooser.getSelected());
        } else if (key == "Pipeline") {
            // llNetTable.getEntry("pipeline").setDouble(curPipeChooser.getSelected());
            // shuffNetTable.getSubTable("CamMode").getEntry("selected").setString("Vision");
        } else {
            System.out.println("Huh: " + key);
        }
    }

    protected void handleChange(String key) {
        if (key == "CamMode") {
            llNetTable.getEntry("camMode").setDouble(camModeChooser.getSelected());
        } else if (key == "LED") {
            llNetTable.getEntry("ledMode").setDouble(ledModeChooser.getSelected());
        } else if (key == "Pipeline") {
            llNetTable.getEntry("pipeline").setDouble(curPipeChooser.getSelected());
            // shuffNetTable.getSubTable("CamMode").getEntry("selected").setString("Vision");
        } else {
            System.out.println("Huh: " + key);
        }
    }

    /*
    public void handler(NetworkTable table, String key,
        NetworkTableEntry entry, NetworkTableValue value, int flags) {
            String d = value.getString();
            int v = curPipeChooser.getSelected();
            System.out.println("Got " + key + " -> " + value.getString());
            System.out.println("The value: " + v);
    }
    */

    @Override
    public void periodic() {
      eX.setDouble(tx());
      eY.setDouble(ty());
      eArea.setDouble(ta());
      eTarget.setBoolean(tv());
      eDist.setDouble(distance());
    }

    public double tx() { //gets x from the limelight
        return eX.getDouble(0.0);
    }

    public double ty() { //gets y from limelight
        return eY.getDouble(0.0);
    }

     public boolean tv(){
         return eTarget.getBoolean(false);
     }

     public double ta() { //gets the area from the limelight
        return eArea.getDouble(0.0);
     }

    public double distance() { //gets the distance from the limelight
        return distanceToTarget(
                Constants.limelightMountHeight, Constants.limelightMountAngle,
                Constants.targetHeight, ty());
    }
    
    
    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
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
