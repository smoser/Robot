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
      applyShuffleboardChanges();
      eDist.setDouble(distance());
    }

    public boolean tv() { //get tv the shuffleboard button.
        return eTarget.getBoolean(false);
    }

    public double tx() { //gets tx shufflebboard
        return eX.getDouble(9);
    }

    public double ty() { //gets ty from shuffleboard
        return eY.getDouble(9);
    }

    public double ta() { //gets area from shuffleboard
        return eTarget.getDouble(9.9);
    }
}

