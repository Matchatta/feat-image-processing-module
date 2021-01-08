import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Computing {
    private String sole = "soleImage";
    private String side = "sideImage";

    public Computing() {

    }

//    private Mat ConvertBase64ToImage(Object rawBase64) {
//        byte[] rawByteImage = Base64.decode(((String) rawBase64).split(",")[1]);
//        return Imgcodecs.imdecode(new MatOfByte(rawByteImage), Imgcodecs.IMREAD_UNCHANGED);
//    }

//    public Volunteer TransformData() {
//        Map<String, feetarcestimation.service.models.Raw.Foot> feet = new HashMap<String, feetarcestimation.service.models.Raw.Foot>();
//        Volunteer transformedVolunteer = new Volunteer();
//        transformedVolunteer.setRawData(volunteer);
//        feet.forEach((key, foot) -> {
//            try {
//                Map<String, Map> footDictionary = ObjectToMap(foot);
//                String soleBase64Image = (String) ((Object) footDictionary.get(sole));
//                String sideBase64Image = (String) ((Object) footDictionary.get(side));
//                Mat soleImage = prepareImage(ConvertBase64ToImage(soleBase64Image));
//                Mat sideImage = prepareImage(ConvertBase64ToImage(sideBase64Image));
//                int footNosePosition = (int) footDictionary.get("metatarsalgia").get("y");
//                int forePosition = (int) footDictionary.get("fore").get("y");
//                int middlePosition = (int) footDictionary.get("middle").get("y");
//                int heelPosition = (int) footDictionary.get("heel").get("y");
//                int ankleSprainYPosition = (int) footDictionary.get("ankleSprain").get("y");
//                int ankleSprainXPosition = (int) footDictionary.get("ankleSprain").get("x");
////                double footArc = (double) ((Object) footDictionary.get("footArc"));
//
//                double footLength = getFootLength(soleImage);
//                ShoeSizeConverter shoeSizeConverter = new ShoeSizeConverter();
//                double pixelRatio = getPixelRatio(footLength, shoeSizeConverter.convertToCm(volunteer.getShoeSize(), volunteer.getGender()));
//
//                Publish publish = new Publish();
//                String soleUrl = soleBase64Image;
//                String sideUrl = sideBase64Image;
////                String soleUrl = publish.publishToCloudinary(soleBase64Image, key, volunteer.getName(), sole);
////                String sideUrl = publish.publishToCloudinary(sideBase64Image, key, volunteer.getName(), side);
//
//                Foot transformedFoot = getFoot(soleImage, sideImage, shoeSizeConverter.convertToCm(volunteer.getShoeSize(), volunteer.getGender()), footNosePosition, forePosition, middlePosition, heelPosition, ankleSprainYPosition, ankleSprainXPosition, pixelRatio, soleUrl, sideUrl);
//                transformedVolunteer.setGender(volunteer.getGender());
//                transformedVolunteer.setCollectorEmail(volunteer.getCollectorEmail());
//                switch (key) {
//                    case "left1":
//                        transformedVolunteer.setLeftFoot1(transformedFoot);
//                        break;
//                    case "left2":
//                        transformedVolunteer.setLeftFoot2(transformedFoot);
//                        break;
//                    case "right1":
//                        transformedVolunteer.setRightFoot1(transformedFoot);
//                        break;
//                    case "right2":
//                        transformedVolunteer.setRightFoot2(transformedFoot);
//                        break;
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return transformedVolunteer;
//    }

//    private Foot getFoot(Mat soleImage, Mat sideImage, double footLength, int nose, int fore, int middle, int heel, int ankleSprainY, int ankleSprainX, double ratio, String soleUrl, String sideUrl) {
//        double heelToFore = Math.round(getLengthBetweenComponents(fore, heel, ratio) * 100.0) / 100.0;
//        double heelToMid = Math.round(getLengthBetweenComponents(middle, heel, ratio) * 100.0) / 100.0;
//        double heelToNose = Math.round(getLengthBetweenComponents(nose, heel, ratio) * 100.0) / 100.0;
//
//        Mat edgeImage = detectEdge(soleImage);
//        Mat sideEdgeImage = detectEdge(sideImage);
//        double foreWidth = Math.round(getComponentWidth(nose, edgeImage, ratio) * 100.0) / 100.0;
//        double heelWidth = Math.round(getComponentWidth(heel, edgeImage, ratio) * 100.0) / 100.0;
//        double midWidth = Math.round(getComponentWidth(middle, edgeImage, ratio) * 100.0) / 100.0;
//        double footHeight = Math.round(getFootHeight(ankleSprainX, ankleSprainY, sideEdgeImage, ratio) * 100.0) / 100.0;
//
//        return new Foot(soleUrl, sideUrl, footLength, heelToFore, heelToMid, heelToNose, foreWidth, midWidth, heelWidth, footHeight);
//    }

    private Mat prepareImage(Mat image) {
        Mat hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();
        hsvImg.create(image.size(), CvType.CV_8U);
        Imgproc.cvtColor(image, hsvImg, Imgproc.COLOR_BGR2HSV);
        Core.split(hsvImg, hsvPlanes);
        double threshValue = getHistogramAverage(hsvImg, hsvPlanes.get(0));
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 255.0, Imgproc.THRESH_BINARY_INV);

        //Get mask
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);
//        Imgcodecs imageCodecs = new Imgcodecs();
        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 255.0, Imgproc.THRESH_BINARY);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        image.copyTo(foreground, thresholdImg);

        //Post
        Imgproc.cvtColor(foreground, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(image, image, threshValue, 255.0, Imgproc.THRESH_BINARY_INV);
//        String file = "./asd.jpg";
        double kernelSize = image.width() / 100;
        Mat kernel = new Mat(new Size(kernelSize, kernelSize), CvType.CV_8UC1);
        Imgproc.morphologyEx(image, image, Imgproc.MORPH_OPEN, kernel);
//        imageCodecs.imwrite(file, image);

        return image;
    }

    private static double getHistogramAverage(Mat hsvImg, Mat hueValues) {
        // init
        double average = 0.0;
        Mat hist_hue = new Mat();
        // 0-180: range of Hue
        MatOfInt histSize = new MatOfInt(190);
        List<Mat> hue = new ArrayList<>();
        hue.add(hueValues);

        // compute the histogram
        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));
        for (int h = 0; h < 180; h++) {
            // for each bin, get its value and multiply it for the corresponding
            // hue
            average += (hist_hue.get(h, 0)[0] * h);
        }

        // return the average hue of the image
        return average / hsvImg.size().height / hsvImg.size().width;
    }

    private double getPixelRatio(double pixels, double cm) {
        return cm / pixels;
    }

    private Rect getBounding(Mat image) {
        Mat points = Mat.zeros(image.size(), image.type());
        Core.findNonZero(image, points);
        MatOfPoint mat = new MatOfPoint(points);
        return Imgproc.boundingRect(mat);
    }

    private double getFootLength(Mat image) {
        Rect bounding = getBounding(image);
        Imgcodecs imageCodecs = new Imgcodecs();
        return bounding.height;
    }

    private double getLengthBetweenComponents(double components1, double components2, double ratio) {
        return (components2 - components1) * ratio;
    }

    private double getComponentWidth(int position, Mat edgeImage, double ratio) {
        Boolean flag = false;
        int count = 0;
        for (int col = 0; col < edgeImage.width(); col++) {
            double[] color = edgeImage.get(position, col);
            if (color[0] == 0.0) {
                flag = !flag;
            }
            if (flag) {
                count++;
            }
        }
        return count * ratio;
    }

    private double getFootHeight(int positionX, int positionY, Mat edgeImage, double ratio) {
        Boolean found = true;
        int length = 0;
        for (int row = positionY - 1; row < edgeImage.height(); row++) {
            double[] color = edgeImage.get(row, positionX);
            if (color[0] == 255.0) {
                found = !found;
            }
            if (found) {
                length++;
            }
        }
        Imgcodecs im = new Imgcodecs();
        im.imwrite("D:/Senior_project/sideMArker.jpg", edgeImage);
        return length * ratio;
    }

//    private Map<String, Map> ObjectToMap(feetarcestimation.service.models.Raw.Foot foot) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(foot);
//        Map<String, Map> footDictionary = mapper.readValue(jsonString, Map.class);
//        return footDictionary;
//    }

    private Mat detectEdge(Mat image) {
        Mat preImage = image.clone();
//        Mat contourImage = new Mat(preImage.size(), preImage.type());
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>() {{
//            add(findContours(preImage));
//        }};
//        Imgproc.drawContours(contourImage, contours, 0, new Scalar(255), -1);
        Mat edge = new Mat();
        Imgproc.Canny(image, edge, 160, 220);
//        if(contourImage.size().height < 50 || contourImage.size().width < 50){
//            Imgproc.Canny(image, edge, 160, 220);
//            return edge;
//        }
        return edge;
    }
}
