import org.junit.Assert;
import org.junit.Test;

import frc.robot.subsystems.Limelight;
public class LimelightTest {
  public static final double DELTA = 1e-2; // acceptable deviation range

  public static double degToRad(double degrees) {
      return degrees * Math.PI / 180;
  }

  public static double feetToMeter(double ft) {
    final double MetersPerFoot = 0.3048;
    return ft * MetersPerFoot;
  }

  @Test // marks this method as a test
  public void test1() {
    double expected = 5.0f;
    double mH = 3.0f ; // camera is mounted around 3 feet high.
    double tH = 8.0f ; // target high hoop is 8 feet high.
    double mountAngle = degToRad(20.0f) ; // say mounted at 20 degrees.
    double offsetAngle = degToRad(25.0f) ; // as read from limelight.
    // tan(20° + 25°) = 1, so this is just targetHeight - mountHeight
    Assert.assertEquals(
      expected, Limelight.distanceToTarget(mH, mountAngle, tH, offsetAngle), DELTA);
  }
}
