package org.firstinspires.ftc.teamcode.debug.PurePursuit.Geometry;

import org.apache.commons.math3.util.MathUtils;

public class Position2D {
    private double x;
    private double y;
    private double headingRadians;

    public Position2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.headingRadians = 0;
    }

    public Position2D(double x, double y, double headingRadians) {
        this.x = x;
        this.y = y;
        this.headingRadians = MathUtils.normalizeAngle(headingRadians, 0.0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeadingRadians() {
        return headingRadians;
    }

    public void setHeadingRadians(double headingRadians, boolean clamp) {
        this.headingRadians = clamp ? MathUtils.normalizeAngle(headingRadians, 0.0) : headingRadians;
    }

    public void add(Position2D position2D, boolean clamp) {
        this.x += position2D.getX();
        this.y += position2D.getY();
        this.headingRadians = this.headingRadians + position2D.getHeadingRadians();
        if (clamp) {
            this.headingRadians = MathUtils.normalizeAngle(this.headingRadians, 0.0);
        }
    }

    public static Position2D add(Position2D p1, Position2D p2) {
        return new Position2D(
                p1.getX() + p2.getX(),
                p1.getY() + p2.getY(),
                MathUtils.normalizeAngle(p1.getHeadingRadians() + p2.getHeadingRadians(), 0.0)
        );
    }

    public void multiply(double scalar, boolean clamp) {
        this.x *= scalar;
        this.y *= scalar;
        this.headingRadians = this.headingRadians * scalar;
        if (clamp) {
            this.headingRadians = MathUtils.normalizeAngle(this.headingRadians, 0.0);
        }
    }

    public static Position2D multiply(Position2D p1, double scalar) {
        return new Position2D(
                p1.getX() * scalar,
                p1.getY() * scalar,
                MathUtils.normalizeAngle(p1.getHeadingRadians() * scalar, 0.0)
        );
    }

    public double getMagnitude() {
        return Math.sqrt((this.x*this.x)+(this.y*this.y));
    }

    public static Position2D normalize(Position2D position2D) {
        return new Position2D(
                position2D.getX() / position2D.getMagnitude(),
                position2D.getY() / position2D.getMagnitude(),
                position2D.getHeadingRadians()
        );
    }

    public Position2D getNegative() {
        return new Position2D(
                -this.getX(),
                -this.getY(),
                -this.getHeadingRadians()
        );
    }

    public double dist(Position2D position2D) {
        double x2 = position2D.getX();
        double y2 = position2D.getY();
        return Math.sqrt(((this.x - x2) * (this.x - x2)) + ((this.y - y2) * (this.y - y2)));
    }

    public static double dist(Position2D p1, Position2D p2) {
        return Math.sqrt(((p2.x - p1.x) * (p2.x - p1.x)) + ((p2.y - p1.y) * (p2.y - p1.y)));
    }

    public static double angle(Position2D p1, Position2D p2) {
        return Math.acos( ((p1.getX()*p2.getX()) + (p1.getY()*p2.getY())) / (p1.getMagnitude() * p2.getMagnitude()) );
    }

    public void rotate(double radiansClockwise) {
        double newX = x * Math.cos(radiansClockwise) - y * Math.sin(radiansClockwise);
        double newY = y * Math.cos(radiansClockwise) + x * Math.sin(radiansClockwise);
        double newHeading = MathUtils.normalizeAngle(this.headingRadians + radiansClockwise, 0.0);

        this.x = newX;
        this.y = newY;
        this.headingRadians = newHeading;
    }

    public static Position2D rotate(Position2D position2D, double radiansClockwise) {
        double x = position2D.getX();
        double y = position2D.getY();
        double r = position2D.getHeadingRadians();

        return new Position2D(
                x * Math.cos(radiansClockwise) - y * Math.sin(radiansClockwise),
                y * Math.cos(radiansClockwise) + x * Math.sin(radiansClockwise),
                MathUtils.normalizeAngle(r + radiansClockwise, 0.0)
        );
    }
}