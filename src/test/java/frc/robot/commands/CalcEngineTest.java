package frc.robot.commands;

import org.junit.Assert;
import org.junit.Test;

public class CalcEngineTest {
  public static final double DELTA = 1e-2; // acceptable deviation range

  public static double d2rad(double degrees) {
    return degrees * Math.PI / 180;
  }

  @Test // marks this method as a test
  public void test1() {
    // Example 4.8 at
    // https://openstax.org/books/university-physics-volume-1/pages/4-3-projectile-motion
    double v = 30.0f;
    double angle = d2rad(45);
    double time = 3.79f;
    double xdist = v * time * Math.cos(angle);

    // CalcEngine(double, double) takes launchHeight and targetHeight.
    CalcEngine c = new CalcEngine(1.0f, 11.0f);

    Assert.assertEquals(v, c.getVelo(xdist, angle), DELTA);
  }
}
