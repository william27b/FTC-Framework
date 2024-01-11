package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.shplib.commands.Command;
import org.firstinspires.ftc.teamcode.shplib.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.shplib.utility.Clock;
import org.firstinspires.ftc.teamcode.subsystems.AdjustHolder;
import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PracticeArmServo;

public class LowerArmCommand extends Command {
    private final ArmSubsystem arm;
    private final AdjustHolder wrist;
    private final PracticeArmServo elbow;
    private  double startTime;

    public LowerArmCommand(ArmSubsystem arm, AdjustHolder wrist, PracticeArmServo elbow) {
        // You MUST call the parent class constructor and pass through any subsystems you use
        super(arm, wrist, elbow);

        this.arm = arm;
        this.wrist = wrist;
        this.elbow = elbow;

    }

    // Called once when the command is initially schedule
    @Override
    public void init() {
        startTime = Clock.now();
        wrist.setState(AdjustHolder.State.DOWN);
    }

    // Specifies whether or not the command has finished
    // Returning true causes execute() to be called once

    @Override
    public void end() {
        //wrist.setState(AdjustHolder.State.DOWN);
        //elbow.setState(PracticeArmServo.State.DOWN);
        arm.setState(ArmSubsystem.State.BOTTOM);
        elbow.setState(PracticeArmServo.State.DOWN);
    }

    @Override
    public boolean isFinished() {
        return Clock.hasElapsed(startTime, 1.5);
    }
}
