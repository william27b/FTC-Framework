package org.firstinspires.ftc.teamcode.debug.PurePursuit.Geometry.Waypoints;

import org.firstinspires.ftc.teamcode.debug.PurePursuit.Geometry.Position2D;

public class EndWaypoint implements Waypoint {
    private final Position2D position2d;

    public EndWaypoint(Position2D position2D) {
        this.position2d = position2D;
    }

    @Override
    public Position2D getPosition() {
        return this.position2d;
    }
}
