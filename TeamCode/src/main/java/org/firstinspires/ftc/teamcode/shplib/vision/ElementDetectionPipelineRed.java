package org.firstinspires.ftc.teamcode.shplib.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class ElementDetectionPipelineRed extends OpenCvPipeline {
    ArrayList<double[]> frameList;

    public static double strictLowS = 140; //TODO: Tune in dashboard
    public static double strictHighS = 255;
    Telemetry telemetry;
    public double leftValue;
    public double rightValue;

    public ElementDetectionPipelineRed() {
        frameList = new ArrayList<>();
    }

    public enum loc{
        RIGHT,
        LEFT,
        NONE
    };
    //loc location;
    //sub matrices to divide image
    //we can draw rectangles on the screen to find the perfect fit
    //edit as necessary
    static final Rect LEFT_ROI = new Rect(
            new Point(1, 1), //TODO: MAGIC NUMBERS ;-;
            new Point(260, 447)
    );

    static final Rect RIGHT_ROI = new Rect(
            new Point(320, 10),
            new Point(700, 447)
    );

    //threshold(lowest possible) percentage of that color
    static double THRESHOLD = 0.03; //TODO threshold

    private void inRange(Mat src1, Scalar leftBoundary, Scalar rightBoundary, Mat dst) {
        if (leftBoundary.val[0] > rightBoundary.val[0]) {

            Mat leftMat = new Mat();
            Core.inRange(src1, new Scalar(0, leftBoundary.val[1], leftBoundary.val[2]), rightBoundary, leftMat);

            Mat rightMat = new Mat();
            Core.inRange(src1, leftBoundary, new Scalar(180, rightBoundary.val[1], rightBoundary.val[2]), rightMat);

            Core.bitwise_or(leftMat, rightMat, dst);

            leftMat.release();
            rightMat.release();

        } else {

            Core.inRange(src1, leftBoundary, rightBoundary, dst); //ONLY returns the pixels in the range

        }
    }

    @Override
    public Mat processFrame(Mat input){
        Mat mat = new Mat();

        //From RGB to HSV to for better tuning
        //EasyOpenCV hue is 0-180
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        //Lower and upper bounds for the color to detect
        Scalar lowHSV = new Scalar(350/2, 50, 60); //TODO: currently for RED. need to tune
        Scalar highHSV = new Scalar(15/2, 255, 255); // red is like 0-15 & 350-360 wtf
        //                                              aka 350-15
        //                                              new function to account for values like red

        Mat detected = new Mat();
        inRange(mat, lowHSV, highHSV, detected); //ONLY returns the pixels in the HSV range
        // using inRange (which has wrap around HSV values, I.E. from 175 to 180 and from 0 to 7.5)
        // instead of Core.inRange (which does not wrap around, and 175-7.5 would result in an error)

        Mat masked = new Mat();
        //colors the white portion of detected in with the color and outputs to masked
        Core.bitwise_and(mat, mat, masked, detected);
        Scalar average = Core.mean(masked, detected);

        Mat scaledMask = new Mat();
        //scale the average saturation to 150
        masked.convertTo(scaledMask, -1, 150 / average.val[1], 0);

        Mat scaledThresh = new Mat();
        //you probably want to tune this
        Scalar strictLowHSV = new Scalar(0, strictLowS, 0); //strict lower bound HSV for yellow
        Scalar strictHighHSV = new Scalar(255, strictHighS, 255); //strict higher bound HSV for yellow
        //apply strict HSV filter onto scaledMask to get rid of any yellow other than pole
        Core.inRange(scaledMask, strictLowHSV, strictHighHSV, scaledThresh);

        Mat finalMask = new Mat();

        Core.bitwise_and(mat, mat, finalMask, scaledThresh);

        Mat edges = new Mat();
        //detect edges(only useful for showing result)(you can delete)
        Imgproc.Canny(scaledThresh, edges, 100, 200);

        //contours, apply post processing to information
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        //find contours, input scaledThresh because it has hard edges
        Imgproc.findContours(scaledThresh, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //
        //
        Mat left = scaledThresh.submat(LEFT_ROI);
        Mat right = scaledThresh.submat(RIGHT_ROI);

        leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 225;
        rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 225;

        //        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar colorExists = new Scalar (0, 255, 0);
        Scalar colorInexistant = new Scalar (255, 0, 0);

        Imgproc.rectangle(scaledThresh, LEFT_ROI, leftValue>rightValue? colorInexistant:colorExists, 3);
        Imgproc.rectangle(scaledThresh, RIGHT_ROI, leftValue<rightValue? colorInexistant:colorExists, 3);
        //

        //list of frames to reduce inconsistency, not too many so that it is still real-time, change the number from 5 if you want
        if (frameList.size() > 5) {
            frameList.remove(0);
        }

        //RELEASE EVERYTHING
        input.release();
        scaledThresh.copyTo(input);
        scaledThresh.release();
        scaledMask.release();
        mat.release();
        masked.release();
        edges.release();
        detected.release();
        finalMask.release();
        hierarchy.release();
        left.release();
        right.release();

        return input;

    }
    public int getLocation(){
        if(leftValue>rightValue && leftValue>THRESHOLD){
            return 1;
        }
        if(rightValue>leftValue && rightValue>THRESHOLD){
            return 2;
        }
        return 3;

    }
}