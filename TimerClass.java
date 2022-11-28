package com.example.paintproject2;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class TimerClass extends TimerTask {
    private int time;
    private Label timeLabel;
    private File timeFile;


    /**
     * This is the default context of the class TimerClass.
     */
    public TimerClass(){
        Platform.runLater(()->{
            time = 60;
            Main.artBoard.setHeight(515);
        });
    }

    /**
     * The run method will initialize the TimerClass.
     */
    public void run() {
        Platform.runLater(() -> {
            timeLabel = new Label("Time: " + time);
            Main.vBox2.getChildren().setAll(timeLabel);
            logging();
        });
        time--;
        if (time == 0) {
            Platform.runLater(()->{
                try{
                    timeFile = new File("E:\\Computer Science\\Java\\TestFolder\\zPaintTest.jpg");
                    WritableImage wTempImage = new WritableImage((int) Main.artBoard.getWidth(), (int) Main.artBoard.getHeight());
                    byte[] dataFile = timeFile.getPath().getBytes();
                    WritableImage newWTempImage = Main.artBoard.snapshot(null,wTempImage);
                    RenderedImage tempRenderedImage = SwingFXUtils.fromFXImage(newWTempImage,null);
                    FileOutputStream fileOutputStream = new FileOutputStream(timeFile);
                    fileOutputStream.write(dataFile);
                    ImageIO.write(tempRenderedImage,"jpg",fileOutputStream);
                    System.out.println("Saved file");

                }catch(Exception e){
                    System.out.println("Error! File not saved!");
                }
            });
            Platform.runLater(() -> {
                time = 60;
            });

        }
    }
    public void run(Canvas canvas) {
        Platform.runLater(()->{
            timeLabel = new Label("Time: " + time);
            Main.vBox2.getChildren().setAll(timeLabel);
            logging();
        });
        time--;
        if(time ==0){
            Platform.runLater(()->{
                try{
                    timeFile = new File("E:/Computer Science/Java/TestFolder/paintTest.jpg");
                    WritableImage wTempImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    WritableImage newWTempImage = canvas.snapshot(null,wTempImage);
                    RenderedImage tempRenderedImage = SwingFXUtils.fromFXImage(newWTempImage,new BufferedImage((int) canvas.getWidth(), (int) canvas.getHeight(),BufferedImage.TYPE_INT_RGB));
                    System.out.println(timeFile.getPath());
                    ImageIO.write(tempRenderedImage,"jpg",timeFile);
                    System.out.println("Saved file");
                }catch (Exception e){
                    System.out.println("Failed to save file.");
                }
            });
            Platform.runLater(()->{time = 60;});
        }
    }


    /**
     * This is a constructor with an integer parameter, which is insertedTime.
     * @param insertedTime  --the inputted time to set the TimerClass object.
     */
    public TimerClass(int insertedTime){
        this.time = insertedTime;
    }

    /**
     * The toString method returns a string, particularly the amount of time in string.
     * @return It returns the variable time in the form of a string.
     */
    public String toString(){
        return String.valueOf(time);
    }

    public void logging() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-YYYY" + " " + "HH:MMz");
        Date today = new Date(System.currentTimeMillis());
        dateFormat.format(today);
        File logsave = new File("E:\\Computer Science\\Java\\TestFolder\\logSaveDocument.log");
        FileHandler handler;
        try{
            handler = new FileHandler(logsave.getPath());
            Main.logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            Main.logger.info(today+ " " + Main.currentTool);
        }
        catch(Exception e){
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }



}
