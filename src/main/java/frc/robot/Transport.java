
package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.SparkMax; 

public class Transport
{
    private boolean launching = false;
    
    private  DigitalInput initialSensor = new DigitalInput(0);
    private  DigitalInput firstBallSensor = new DigitalInput(1);
    private  DigitalInput secondSensor = new DigitalInput(2);
    private  DigitalInput stopSensor = new DigitalInput(3);

    private boolean lastFirstValue = false;
    
    //first = mechanum + roller, second = 2" wheels
    private WPI_VictorSPX firstStageIntake = new WPI_VictorSPX(6);
    private WPI_VictorSPX secondStageIntake = new WPI_VictorSPX(7);

    private WPI_VictorSPX angledBelts = new WPI_VictorSPX(8);
    private WPI_VictorSPX verticalBelts = new WPI_VictorSPX(9);

    //launcher motors
    private TalonSRX launcher1 = new TalonSRX(10);
    private TalonSRX launcher2 = new TalonSRX(11);

    public Transport ()
    {

    }

    public void UpdateTransport()
    {
        SmartDashboard.putNumber("Launcher 1 Velocity", launcher1.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Launcher 2 Velocity", launcher2.getSelectedSensorVelocity(0));

        SmartDashboard.putBoolean("Initial Sensor", initialSensor.get());
        SmartDashboard.putBoolean("First Ball Sensor", firstBallSensor.get());
        SmartDashboard.putBoolean("Second Ball Sensor", secondSensor.get());
        SmartDashboard.putBoolean("Stop Sensor", stopSensor.get());

        if(!launching);
        {
            ProcessIO();
        }
        GetLastValues();
    }

    public void Reset()
    {
        angledBelts.set(0);
        verticalBelts.set(0);
        firstStageIntake.set(0);
        secondStageIntake.set(0);
    }

    public void StartLaunch(double velocity)
    {
        launching = true;
        double input = velocity / 60;
        input = 0.75;

        //launcher1.set(ControlMode.PercentOutput, -input);
        //launcher2.set(ControlMode.PercentOutput, input);
        //launcher1.pulse

        launcher1.setInverted(true);
        launcher1.set(ControlMode.Velocity, velocity);
        launcher2.set(ControlMode.Velocity, velocity);

        angledBelts.set(-0.30);
        verticalBelts.set(-0.15);
    }

    public void StopLaunch()
    {
        if (launching)
        {
            angledBelts.set(0);
            verticalBelts.set(0);
        }
        launching = false;
        launcher1.set(ControlMode.PercentOutput, 0);
        launcher2.set(ControlMode.PercentOutput, 0);
    }

    public void ProcessIO()
    {
        if (!stopSensor.get())
        {
            angledBelts.set(0);
            verticalBelts.set(0);
            firstStageIntake.set(0);
            secondStageIntake.set(0);

            return;
        }

        if (!initialSensor.get())
        {
            angledBelts.set(-0.30);
            verticalBelts.set(-0.15);

            return;
        }
        //upon blocking this, we think this and the inital should be switched
        if (!firstBallSensor.get() && lastFirstValue)
        {
            angledBelts.set(0);
            verticalBelts.set(0);
        }
    }

    public void IntakeForward(boolean on)
    {
        if(on)
        {
            firstStageIntake.set(-0.75);
            secondStageIntake.set(0.5);
        }
        else
        {
            firstStageIntake.set(ControlMode.PercentOutput, 0);
            secondStageIntake.set(ControlMode.PercentOutput, 0);
        }
    }

    public void IntakeBackward(boolean on)
    {
        if(on)
        {
            firstStageIntake.set(0.75);
            secondStageIntake.set(-0.5);
            angledBelts.set(0.25);
            verticalBelts.set(0.20);
        }
        else
        {
            firstStageIntake.set(ControlMode.PercentOutput, 0);
            secondStageIntake.set(ControlMode.PercentOutput, 0);
        }
    }
  
    public void GetLastValues()
    {
        lastFirstValue = firstBallSensor.get();
    }
}
