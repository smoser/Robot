package frc.robot.commands;

import org.junit.Assert;
import org.junit.Test;

public class CalcEngineTest {
  public static final double DELTA = 5e-2; // acceptable deviation range

  public static double d2rad(double degrees) {
    return degrees * Math.PI / 180;
  }

  public static double m2ft(double meters) {
      return 3.28084f * meters;
  }

  @Test // marks this method as a test
  public void test1() {
    // Example 4.8 at
    // https://openstax.org/books/university-physics-volume-1/pages/4-3-projectile-motion
    double v = m2ft(30.0f);
    double angle = 45.0;
    double time = 3.79f;
    double xdist = v * time * Math.cos(d2rad(angle));
    double wheelDiameter = .5;

    // CalcEngine(double, double) takes launchHeight and targetHeight.
    CalcEngine c = new CalcEngine(m2ft(1.0f), m2ft(11.0f), wheelDiameter);

    double foundVelo = c.projVelo(xdist, angle);
    Assert.assertEquals(v, foundVelo, DELTA);
    Assert.assertEquals(v * 60 / (wheelDiameter * Math.PI),
            c.projRpm(xdist, angle), 1);
  }

}
