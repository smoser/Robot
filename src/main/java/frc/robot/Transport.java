
package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Transport
{
    private boolean launching = false;
    
    private  DigitalInput initialSensor = new DigitalInput(2);
    private  DigitalInput firstBallSensor = new DigitalInput(5);
    private  DigitalInput secondSensor = new DigitalInput(3);
    private  DigitalInput stopSensor = new DigitalInput(0);
    private  DigitalInput newSensor = new DigitalInput(8);
    State state = State.initial;

    //private boolean lastFirstValue = false;
    //private boolean lastInitialSensor = false;
    //private boolean lastMiddleSensor = false;
    
    //first = mechanum + roller, second = 2" wheels
    private WPI_VictorSPX firstStageIntake = new WPI_VictorSPX(6);
    private WPI_VictorSPX secondStageIntake = new WPI_VictorSPX(7);

    private WPI_VictorSPX angledBelts = new WPI_VictorSPX(8);
    private WPI_VictorSPX verticalBelts = new WPI_VictorSPX(9);

    //launcher motors
    private TalonSRX launcher1 = new TalonSRX(10);
    private TalonSRX launcher2 = new TalonSRX(11);

    //controllers
    XboxController joystick1;
    XboxController joystick2;

    double timer = 100;
    
    double manualLaunch = -1;
    int lastPOV = -1;

    public Transport (XboxController one, XboxController two)
    {
        joystick1 = one;
        joystick2 = two;
    }

    public void InitTransport()
    {
        timer = 100;
    }

    public void UpdateTransport()
    {
        SmartDashboard.putBoolean("Initial Sensor", initialSensor.get());
        SmartDashboard.putBoolean("First Ball Sensor", firstBallSensor.get());
        SmartDashboard.putBoolean("Second Ball Sensor", secondSensor.get());
        SmartDashboard.putBoolean("Stop Sensor", stopSensor.get());
        SmartDashboard.putBoolean("New Sensor", newSensor.get());

        SmartDashboard.putNumber("Launcher1", launcher1.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Launcher2", launcher2.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Left1 Position", Robot.left1.getSelectedSensorPosition(1));
        SmartDashboard.putNumber("Left1 Velocity", Robot.left1.getSelectedSensorVelocity(1));
        SmartDashboard.putNumber("Current Velocity", Robot.left1.getActiveTrajectoryVelocity());
        GetInput();

        if (!joystick2.getStartButton())
        {
            if(!launching);
            {
                ProcessIO();
            }
            GetLastValues();
        }
    }

    private void GetInput()
    {
        //first and second stage intake on controller 1
        if(joystick1.getAButtonPressed())
        {
            firstStageIntake.set(-0.75);
            secondStageIntake.set(1);
        }
        else if(joystick1.getXButtonPressed())
        {
            firstStageIntake.set(0.8);
            secondStageIntake.set(-1);
        }
        else if (joystick1.getAButtonReleased() || joystick1.getXButtonReleased())
        {
            firstStageIntake.set(0);
            secondStageIntake.set(0);
        }

        if (joystick1.getRightBumper() && !launching)
        {
            double percent = 0.75;
            if (manualLaunch != -1)
            {
                percent = manualLaunch;
            }

            if(percent < 0)
            {
                // failsafe to prtect the motors. percent is always positive
                percent = 0.73;
            }
            launcher1.set(ControlMode.PercentOutput, -percent);
            launcher2.set(ControlMode.PercentOutput, percent);
        }
        if (joystick1.getRightBumperReleased() && !launching)
        {
            launcher1.set(ControlMode.PercentOutput, 0);
            launcher2.set(ControlMode.PercentOutput, 0);
        }

        if (Math.abs(joystick2.getRightY()) > 0.1)
        {
            angledBelts.set(joystick2.getRightY());
            state = State.threeBall;
        }
        else if (state == State.threeBall)
        {
            angledBelts.set(0);
        }

        if (Math.abs(joystick2.getLeftY()) > 0.1)
        {
            verticalBelts.set(joystick2.getLeftY());
            state = State.threeBall;
        }
        else if (state == State.threeBall)
        {
            verticalBelts.set(0);
        }

        SmartDashboard.putNumber("Launch Percent", manualLaunch);

        //initition line
        if (joystick2.getAButton())
        {
            manualLaunch = 0.72f;
        }

        //back trench
        if (joystick2.getYButton())
        {
            manualLaunch = 0.74f;
        }

        //front trench
        if (joystick2.getXButton())
        {
            manualLaunch = 0.73f;
        }

        if (joystick2.getPOV() == 0 && lastPOV != 0)
        {
            if (manualLaunch == -1)
            {
                manualLaunch = 0.75;
            }
            else
            {
                manualLaunch += 0.01f;
            }
        }
        if (joystick2.getPOV() == 180 && lastPOV != 180)
        {
            if (manualLaunch == -1)
            {
                manualLaunch = 0.75;
            }
            else
            {
                manualLaunch -= 0.01f;
            }
        }

        if (joystick2.getPOV() == 90 && lastPOV != 90)
        {
            if (manualLaunch == -1)
            {
                manualLaunch = 0.75;
            }
            else
            {
                manualLaunch += 0.01f;
            }
        }

        //turn off manual mode
        if (joystick2.getBButton())
        {
            manualLaunch = -1;
        }

        if(joystick1.getStartButton())
        {
            Reset();
        }
    }

    public void Reset()
    {
        angledBelts.set(0);
        verticalBelts.set(0);
        firstStageIntake.set(0);
        secondStageIntake.set(0);
        state = State.initial;
    }
   
    public void StartLaunch(double percent)
    {
        if (manualLaunch != -1)
        {
            percent = manualLaunch;
        }
        
        timer -= 1;
        launching = true;
        state = State.initial;
        
        if (timer <= 0)
        {
            //angledBelts.set(-1);
            //verticalBelts.set(-1);
            //prior values for milford comp
            angledBelts.set(-0.38);
            verticalBelts.set(-0.38);
        }
        
       //launcher1.set(ControlMode.PercentOutput, -percent);
       //launcher2.set(ControlMode.PercentOutput, percent);
       launcher1.set(ControlMode.Velocity, -percent * 60000);
       launcher2.set(ControlMode.Velocity, percent * 60000);
    }
 
    public void StopLaunch()
    {
        if (launching)
        {
            angledBelts.set(0);
            verticalBelts.set(0);
        }
        timer = 100;
        launching = false;
        launcher1.set(ControlMode.PercentOutput, 0);
        launcher2.set(ControlMode.PercentOutput, 0);
        state = State.initial;
    }
    public enum State {initial, oneBall, twoBall, threeBall}
    public void ProcessIO()
    { 
        switch(state)
        {
            case initial:
            if(!initialSensor.get())
            {
                angledBelts.set(-0.45);
                verticalBelts.set(-0.32);
            }
            else if (!firstBallSensor.get())
            {
                angledBelts.set(0);
                verticalBelts.set(0);
                state = State.oneBall;
            }
            break;

            case oneBall:
            if(!initialSensor.get())
            {
                angledBelts.set(-0.45);
            }
            else if (!secondSensor.get())
            {
                angledBelts.set(0);
                state = State.twoBall;
            }
            break;

            case twoBall:
            if(!initialSensor.get())
            {
                angledBelts.set(-0.45);
            }
            else if (!newSensor.get())
            {
                angledBelts.set(0);
                state = State.threeBall;
            }
            break;

            case threeBall:
            break;
        }

    }
      
        //This is our "failed" sensore array. The older people have failed to finish this so we have cmmented it out so that nothing absolutly horrible would happen during compotitions.
        //move the 1st ball to the top (firstBall)
        //next ball that comes in goes to the 3rd sensor
        //when 3rd ball enters, move the 2nd ball to the new sensor
         
       /* if (!stopSensor.get())
        {
            angledBelts.set(0);
            verticalBelts.set(0);
            return;
        }

        if (!initialSensor.get())
        {

            angledBelts.set(-0.45);
            verticalBelts.set(-0.32);
            return;

            //milford comp values
            /*
            angledBelts.set(-0.45);
            verticalBelts.set(-0.38);
            return;
            */
        //}
        /*if (!firstBallSensor.get() && lastFirstValue)
        {
            verticalBelts.set(0);
        }
        //^^could have potential jamming issues due to the vertical belts being stopped and needing a ball in the middle
        // see black drawing on the board under "robit"
        //possible solution: change belt speeds a bit?

        if (!newSensor.get() && lastMiddleSensor)
        {
            //verticalBelts.set(??) nate coded this
            //angledBelts.set(??) nate coded this
            //Do not know what to set speeds to, needs testing
        }

       if (!firstBallSensor.get() && lastInitialSensor)
        {
            angledBelts.set(0); 
        }
    }*/
  
    public void GetLastValues()
    {
        //lastFirstValue = firstBallSensor.get();
        lastPOV = joystick2.getPOV();

        //lastMiddleSensor = newSensor.get();
        //lastInitialSensor = firstBallSensor.get();
    }









}
