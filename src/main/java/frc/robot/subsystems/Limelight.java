package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.CalcEngine;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;


public class Limelight extends SubsystemBase {

    private final String LimelightStr = "limelight";
    private final String LimelightTabName = "Limelight";

    private ShuffleboardTab llsTab = Shuffleboard.getTab(LimelightTabName);

    protected NetworkTableEntry eX, eY, eArea, eTarget, eDist, eVelo, eRpm;
    protected NetworkTable llNetTable, shuffNetTable;
    protected SendableChooser<Integer> curPipeChooser = new SendableChooser<Integer>();
    protected SendableChooser<Integer> ledModeChooser = new SendableChooser<Integer>();
    protected SendableChooser<Integer> camModeChooser = new SendableChooser<Integer>();

    public final String[] pipelines = {"7660", "1731", "2016", "3130", "3504", "7028", "Camera"};
    public final String[] ledModes = {"Pipeline", "Off", "Blink", "On"};
    public final String[] camModes = {"Vision", "Camera"};

    protected int lastCamMode, lastLedMode, lastPipeline;
    private CalcEngine calc;
    private double launchAngle = Constants.launchAngleFarDegrees;

    public Limelight() {
      llNetTable = NetworkTableInstance.getDefault().getTable(LimelightStr);
      shuffNetTable = NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(LimelightTabName);
      calc = new CalcEngine(Constants.launchHeight, Constants.targetHeight, Constants.wheelDiameterFeet);

      eX = llsTab.add("TX", 0).getEntry();
      eY = llsTab.add("TY", 0).getEntry();
      eArea  = llsTab.add("Area", 0).getEntry();
      eTarget = llsTab.add("Target", false).getEntry();
      eDist = llsTab.add("Distance", 0.0).getEntry();
      eVelo = llsTab.add("ProjVel", 0.0).getEntry();
      eRpm = llsTab.add("ProjRPM", 0.0).getEntry();

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

      for (int i=0; i < ledModes.length; i++) {
          ledModeChooser.addOption(ledModes[i], i);
          if (i == 0) {
              ledModeChooser.setDefaultOption(ledModes[i], i);
          }
      }
      shuffNetTable.getSubTable("LED").addEntryListener("active",
          (table, key, entry, value, flags) -> { handleChange("LED"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
      llsTab.add("LED", ledModeChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);

      llNetTable.addEntryListener("ledMode",
          (table, key, entry, value, flags) -> { handleLimelightChange("LED"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

      for (int i=0; i < camModes.length; i++) {
          camModeChooser.addOption(camModes[i], i);
          if (i == 0) {
              camModeChooser.setDefaultOption(camModes[i], i);
          }
      }
      shuffNetTable.getSubTable("CamMode").addEntryListener("active",
          (table, key, entry, value, flags) -> { handleChange("CamMode"); },
          EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
      llsTab.add("CamMode", camModeChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);


    }

    // handle changes found in limelight network table, sync them to the shuffleboard widgets.
    protected void handleLimelightChange(String key) {
        String newVal = "";
        if (key == "CamMode") {
            int lVal = (int)llNetTable.getEntry("camMode").getDouble(0.0f);
            int sVal = camModeChooser.getSelected();
            if (lVal != sVal) {
                newVal = camModes[lVal];
                shuffNetTable.getSubTable("CamMode").getEntry("selected").setString(newVal);
            }
        } else if (key == "LED") {
            int lVal = (int)llNetTable.getEntry("ledMode").getDouble(0.0f);
            int sVal = ledModeChooser.getSelected();
            if (lVal != sVal) {
                newVal = ledModes[lVal];
                shuffNetTable.getSubTable("LED").getEntry("selected").setString(newVal);
            }
        } else if (key == "Pipeline") {
            int lVal = (int)llNetTable.getEntry("pipeline").getDouble(0.0f);
            int sVal = curPipeChooser.getSelected();
            if (lVal != sVal) {
                newVal = pipelines[lVal];
                shuffNetTable.getSubTable("Pipeline").getEntry("selected").setString(newVal);
            }
        } else {
            System.out.println("Huh: " + key);
        }
    }

    // handle changes made in shuffleboard. this is called via addListener hooks.
    protected void handleChange(String key) {
        if (key == "CamMode") {
            llNetTable.getEntry("camMode").setDouble(camModeChooser.getSelected());
           // llNetTable.getEntry("ledMode").setDouble(1.0f);
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
      int curCamMode = camModeChooser.getSelected();
      if (lastCamMode != curCamMode) {
          handleChange("CamMode");
      }
      lastCamMode = curCamMode;

      int curLedMode = ledModeChooser.getSelected();
      if (lastLedMode != curLedMode) {
          handleChange("LED");
      }
      lastLedMode = curLedMode;

      int curPipeline = curPipeChooser.getSelected();
      if (lastPipeline != curPipeline) {
          handleChange("Pipeline");
      }
      eX.setDouble(tx());
      eY.setDouble(ty());
      eArea.setDouble(ta());
      eTarget.setBoolean(tv());
      eDist.setDouble(distance());
      eVelo.setDouble(projVelo());
      eRpm.setDouble(projRpm());
    }

    public double tx() { //gets x from the limelight
        return llNetTable.getEntry("tx").getDouble(0.0);
    }

    public double ty() { //gets y from limelight
        return llNetTable.getEntry("ty").getDouble(0.0);
    }

     public boolean tv(){
         double d = llNetTable.getEntry("tv").getDouble(0.0);
         return (d != 0.0);
         // return llNetTable.getEntry("tv").getBoolean(false);
     }

     public double ta() { //gets the area from the limelight
        return llNetTable.getEntry("area").getDouble(0.0);
     }

    public double distance() { //gets the distance from the limelight
        return distanceToTarget(
                Constants.limelightMountHeight, Constants.limelightMountAngle,
                Constants.targetHeight, ty());
    }

    // return projectile velocity in feet/sec
    public double projVelo() {
        return calc.projVelo(distance(), launchAngle);
    }

    public double projRpm() {
        return calc.projRpm(distance(), launchAngle);
    }

    public double tableRpm() {
        return calc.tableRpm(distance());
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
