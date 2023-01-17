package Utilities;
import java.io.IOException;

import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import javafx.scene.image.WritableImage;

public class PieChartGenerator extends Application{
	
	
	static int pass;
	static int fail;
	static int skip;
	
	public void start(Stage stage) throws IOException {
	      //Preparing ObservbleList object
	      ObservableList<PieChart.Data>pieChartData =
	      FXCollections.observableArrayList(
	    		  new PieChart.Data("Fail", fail),
	      new PieChart.Data("Skip",skip),
	      new PieChart.Data("Pass", pass));
	      
	      //Creating a Pie chart
	      PieChart pieChart = new PieChart(pieChartData);
	      pieChart.setTitle("Automation Execution");
	      pieChart.setClockwise(true);
	    //  pieChart.setLabelLineLength(50);
	      pieChart.setLabelsVisible(true);
	      pieChart.setStartAngle(180);
	      
	      //Creating a Group object
	      Scene scene = new Scene(new Group(), 595, 400);
	      stage.setTitle("Automation Pass Rate");
	      ((Group) scene.getRoot()).getChildren().add(pieChart);
	      //Saving the scene as image
	      WritableImage image = scene.snapshot(null);
	      java.io.File file = new java.io.File(System.getProperty("user.dir") + "/test-output/automationPichart.png");
	      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
	      System.out.println("Image Saved");
	     StatusMail.triggerSendMail();
	     System.exit(0);
	   }
	public static void main(String[] args) throws IOException {
		launch(args);
	   
		
	  }
		
	public static void setTestPassed(int passTCcount)
	{
		pass=passTCcount;
	}
	public static void setTestFailed(int failed)
	{
		fail=failed;
	}
	public static void setTestSkiped(int skiped)
	{
		skip=skiped;
	}
}
