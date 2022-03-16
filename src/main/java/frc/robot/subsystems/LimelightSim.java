package frc.robot.subsystems;
import edu.wpi.first.networktables.NetworkTable;


public class LimelightSim extends Limelight {

    private NetworkTable table;

    public LimelightSim() {
      eX.setDouble(9);
      eY.setDouble(9);
      eArea.setDouble(9.9);
      eTarget.setBoolean(tv());
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
      eDist.setDouble(distance());
    }

    public boolean tv() { //gets x from the limelight
        return eTarget.getBoolean(false);
    }

    public double tx() { //gets x from the limelight
        return eX.getDouble(9);
    }

    public double ty() { //gets y from limelight
        return eY.getDouble(9);
    }

    public double ta() { //gets the area from the limelight
        return eTarget.getDouble(9.9);
    }
}

