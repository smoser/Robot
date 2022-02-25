package frc.robot.subsystems;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


public class LimelightSim extends Limelight {

    private NetworkTable table;

    public LimelightSim() {
        table = NetworkTableInstance.getDefault().getTable("limelightSim");
    }

    public double tx() { //gets x from the limelight
        return table.getEntry("tx").getDouble(9);
     }

     public double ty() { //gets y from limelight
        return table.getEntry("ty").getDouble(9);
     }

     public double ta() { //gets the area from the limelight
        return table.getEntry("ta").getDouble(9.9);
     }
}

