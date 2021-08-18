package test3;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
public class DetectLandmarks {
	  public static void main(String[] args) throws IOException {
	    // TODO(developer): Replace these variables before running the sample.
	    String filePath = "C:/pleiades/workspace/test3/src/main/resources/moscow_small.jpeg";
	    detectLandmarks(filePath);
	  }

	  // Detects landmarks in the specified local image.
	  public static void detectLandmarks(String filePath) throws IOException {
	    List<AnnotateImageRequest> requests = new ArrayList<>();
	    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

	    Image img = Image.newBuilder().setContent(imgBytes).build();
	    Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
	    AnnotateImageRequest request =
	        AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	    requests.add(request);

	    // Initialize client that will be used to send requests. This client only needs to be created
	    // once, and can be reused for multiple requests. After completing all of your requests, call
	    // the "close" method on the client to safely clean up any remaining background resources.
	    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
	      BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
	      List<AnnotateImageResponse> responses = response.getResponsesList();

	      for (AnnotateImageResponse res : responses) {
	        if (res.hasError()) {
	          System.out.format("Error: %s%n", res.getError().getMessage());
	          return;
	        }

	        // For full list of available annotations, see http://g.co/cloud/vision/docs
	        for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
	          com.google.cloud.vision.v1.LocationInfo info = annotation.getLocationsList().listIterator().next();
	          System.out.format("Landmark: %s%n %s%n", annotation.getDescription(), info.getLatLng());
	        }
	      }
	    }
	  }
	}