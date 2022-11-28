package com.example.paintproject2;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.Popup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.logging.Logger;

//most code are based on JavaTPoint


public class Main extends Application {
    public static VBox vBox;
    public static VBox vBox2;

    public static Logger logger;

    public CheckMenuItem lineItem;

    public CheckMenuItem dashedLine;

    public CheckMenuItem forSquareItem;

    public CheckMenuItem rectItem;

    public CheckMenuItem circleItem;

    public CheckMenuItem ellipseMenu;

    public CheckMenuItem arcItem;

    public CheckMenuItem polygonItem;

    public static Image appleImage = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Red_Apple.jpg")));

    public static Canvas artBoard = new Canvas(appleImage.getWidth(),appleImage.getHeight());

    public static String currentTool;

    public static double paintbrush_size = 30; // for pencil and eraser

    public static double eraser_size = 30;
    public static double pencil_size = 5;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method would initialize the program, which is the paint program.
     *
     * @param primaryStage --displays the necessary contents of the paint program.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        vBox2 = new VBox();
        BorderPane borderPane = new BorderPane();
        StackPane sPane = new StackPane();//from JavaTPoint
        //final Image appleImage = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Red_Apple.jpg/1024px-Red_Apple.jpg"); //from Wikipedia Commons and StackOverflow
        Image blankImage = new Image("https://png.pngtree.com/background/20210714/original/pngtree-pure-white-background-wallpaper-picture-image_1219015.jpg"); //for apple image
        //Canvas artBoard = new Canvas(appleImage.getWidth(), appleImage.getHeight()); //for apple image
        artBoard.setVisible(true);
        artBoard.getGraphicsContext2D().drawImage(blankImage, 0, 0, appleImage.getWidth(), appleImage.getHeight()); //for apple image
        primaryStage.setResizable(true);
        Stack<WritableImage> stackOfWritableImage = new Stack<>();
        borderPane.setLeft(vBox2);

        //System.out.println("Width: " + artBoard.getWidth() + " Height: " + artBoard.getHeight());//delete

        FileChooser chooser1 = new FileChooser();
        chooser1.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG files", "*.jpeg"),
                new FileChooser.ExtensionFilter("GIF files", "*.gif"),
                new FileChooser.ExtensionFilter("BMP files", "*.bmp"));

        Image pinkBackground = new Image("https://htmlcolorcodes.com/assets/images/colors/watermelon-pink-color-solid-background-1920x1080.png");
        BackgroundImage backdrop = new BackgroundImage(pinkBackground, null, null, null, null);
        Background b = new Background(backdrop);


        sPane.getChildren().add(artBoard); //from Oracle

        //borderPane.setBottom(artBoard);
        borderPane.setCenter(sPane);
        //borderPane.setCenter(view); //from Oracle
        borderPane.setBackground(b);

        Scene imageScene = new Scene(borderPane, 650, 650); //from JavaTPoint

        ColorPicker colorChooser = new ColorPicker();

        MenuBar paintBar = new MenuBar();
        borderPane.setTop(paintBar);

        Menu paintMenu = new Menu("Paint");
        MenuItem eraseItem = new MenuItem("Erase");
        CheckBox eraseCheckBox = new CheckBox();
        ImageView eraserIcon = new ImageView("https://cdn.onlinewebfonts.com/svg/img_555094.png");
        eraserIcon.setFitWidth(30);
        eraserIcon.setFitHeight(30);
        eraseCheckBox.setGraphic(eraserIcon);
        eraseItem.setGraphic(eraseCheckBox);
        paintMenu.getItems().add(eraseItem);
        eraseCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                erase(artBoard, stackOfWritableImage);
                currentTool = "Eraser";
            }
        });
        MenuItem pencilItem = new MenuItem("Pencil");
        CheckBox paintCheckBox = new CheckBox();
        ImageView paintIcon = new ImageView("https://cdn-icons-png.flaticon.com/512/103/103410.png"); //from the flaticon website
        paintIcon.setFitWidth(30);
        paintIcon.setFitHeight(30);
        paintCheckBox.setGraphic(paintIcon);
        CheckBox pencilCheckBox = new CheckBox();
        ImageView pencilIcon = new ImageView("https://cdn.onlinewebfonts.com/svg/img_376363.png");
        pencilIcon.setFitHeight(30);
        pencilIcon.setFitWidth(30);
        pencilCheckBox.setGraphic(pencilIcon);
        pencilItem.setGraphic(pencilCheckBox);
        MenuItem paintItem = new MenuItem("Paint", paintCheckBox);
        //borderPane.setLeft(paintCheckBox); //from Oracle*/
        paintCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (paintCheckBox.isSelected()) {
                    Paint(artBoard, colorChooser, stackOfWritableImage);
                    currentTool = "Paint";
                } else {

                }
            }
        });
        pencilCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pencilCheckBox.isSelected()) {
                    pencilDraw(artBoard, stackOfWritableImage);
                    currentTool = "Pencil";
                }
            }
        });

        Popup aboutPaintTool = new Popup();
        Text aboutPaintInfo = new Text("Please check off the paint checkbox and draw on the canvas.");
        HBox paintInfoBox = new HBox(aboutPaintInfo);
        paintInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        aboutPaintTool.getContent().add(paintInfoBox);
        aboutPaintTool.setX(690);
        aboutPaintTool.setY(290);
        paintCheckBox.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutPaintTool.show(primaryStage);
            }
        }); paintCheckBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(aboutPaintTool.isShowing()){
                    aboutPaintTool.hide();
                }
            }
        });

        Popup aboutPencilPopup = new Popup();
        Text aboutPencilInfo = new Text("Click on the pencil checkbox and draw on the canvas.");
        HBox aboutPencilBox = new HBox(aboutPencilInfo);
        aboutPencilBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        aboutPencilPopup.getContent().add(aboutPencilBox);
        aboutPencilPopup.setX(690);
        aboutPencilPopup.setY(290);
        pencilCheckBox.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutPencilPopup.show(primaryStage);
            }
        });
        pencilCheckBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutPencilPopup.hide();
            }
        });

        Popup aboutEraser = new Popup();
        Text eraserInfo = new Text("Click on the erase checkbox and erase on canvas.");
        HBox eraserInfoBox = new HBox(eraserInfo);
        eraserInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        aboutEraser.getContent().add(eraserInfoBox);
        aboutEraser.setX(690);
        aboutEraser.setY(290);
        eraseCheckBox.setOnMouseMoved(event -> {aboutEraser.show(primaryStage);});
        eraseCheckBox.setOnMouseExited(event -> {
            if(aboutEraser.isShowing()){
                aboutEraser.hide();
            }
        });

        logger = Logger.getLogger(this.getClass().getName());

        Button closeButton = new Button("Close");
        vBox = new VBox(5, closeButton);
        //Group g = new Group(closeButton);
        borderPane.setRight(vBox);

        Menu saveMenu = new Menu("Save work");
        MenuItem saveMenuItem1 = new MenuItem("Save");
        Button savingButton = new Button("Save work");
        saveMenuItem1.setGraphic(savingButton);

        savingButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { //method structure based on paint tutorial
                try {
                    WritableImage w = new WritableImage((int) artBoard.getWidth(), (int) artBoard.getHeight());
                    RenderedImage rImage = SwingFXUtils.fromFXImage(artBoard.snapshot(null,w), new BufferedImage((int) artBoard.getWidth(),(int)artBoard.getHeight(),BufferedImage.TYPE_INT_RGB));
                    ImageIO.write(rImage, "jpg", new File("E:/Computer Science/Java/paintApp/src/TestFolder.jpg"));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        MenuItem saveMenuItem2 = new MenuItem("Save as");
        Button saveAsButton = new Button("Save Work to Path");
        saveMenuItem2.setGraphic(saveAsButton);
//        saveAsButton.setOnAction(event ->  {
//                saveAs(sPane,chooser1,primaryStage);
//                System.out.println("The file is saved.");
//        });
        saveMenuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveAs(artBoard, chooser1, primaryStage);
                System.out.println("The file is saved.");
            }
        });
        saveMenuItem2.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        Menu miscellaneous = new Menu("Miscellaneous");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        undo.setAccelerator(KeyCombination.keyCombination("Ctrl+U"));
        redo.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        Button undoButton = new Button();
        Button redoButton = new Button();
        undo.setGraphic(undoButton);
        redo.setGraphic(redoButton);
        ArrayList<WritableImage> imageArrayList = new ArrayList<>();
        ArrayList<WritableImage> tempImageArrayList = new ArrayList<>();
        tempImageArrayList.ensureCapacity(1);
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tempImageArrayList.add(stackOfWritableImage.lastElement());
                imageArrayList.add(stackOfWritableImage.pop());
                artBoard.getGraphicsContext2D().setImageSmoothing(true);
                artBoard.getGraphicsContext2D().drawImage(stackOfWritableImage.peek(), artBoard.getTranslateX(), artBoard.getTranslateY(), stackOfWritableImage.peek().getWidth(), stackOfWritableImage.peek().getHeight());
            }
        });
        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!stackOfWritableImage.isEmpty()) {
                    if (imageArrayList.size() > 1) {
                        stackOfWritableImage.push(imageArrayList.get(imageArrayList.size() - 1));
                        imageArrayList.remove((imageArrayList.size() - 1));
                    } else {
                        stackOfWritableImage.push(tempImageArrayList.get(0));
                    }
                    artBoard.getGraphicsContext2D().setImageSmoothing(true);
                    artBoard.getGraphicsContext2D().drawImage(stackOfWritableImage.peek(), 0, 0, imageArrayList.get(imageArrayList.size() - 1).getWidth(), imageArrayList.get(imageArrayList.size() - 1).getHeight());

                }
            }
        });
        miscellaneous.getItems().addAll(undo, redo);
        paintBar.getMenus().addAll(miscellaneous);

        MenuItem uploadImage = new MenuItem("Default Image Upload");
        MenuItem uploadImage2 = new MenuItem("Upload from File");
        Button appleImageButton = new Button();
        Button fromFileButton = new Button();
        uploadImage.setGraphic(appleImageButton);
        FileChooser uploader = new FileChooser();
        uploadImage2.setGraphic(fromFileButton);
        uploadImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                artBoard.getGraphicsContext2D().drawImage(appleImage, 0, 0, appleImage.getWidth(), appleImage.getHeight());
            }
        });
        uploadImage2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                uploader.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                        new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                        new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"),
                        new FileChooser.ExtensionFilter("GIF files", "*.gif"), new FileChooser.ExtensionFilter("BMP files", "*.bmp"));
                File imageFile = uploader.showOpenDialog(primaryStage); //from Oracle
                if (imageFile != null) {//from Oracle
                    Image fromUploadImage = new Image(imageFile.getAbsolutePath());
                    artBoard.getGraphicsContext2D().drawImage(fromUploadImage, 0, 0, artBoard.getWidth(), artBoard.getHeight());
                    PixelReader imagePixelReader = fromUploadImage.getPixelReader();
                    artBoard.setOnMouseClicked(event1 -> {
                        if (colorChooser.getCustomColors().size() <= 12) {
                            colorChooser.getCustomColors().addAll(
                                    imagePixelReader.getColor((int) event1.getX(), (int) event1.getY()));
                        } else {
                            colorChooser.getCustomColors().remove(12, 13);
                        }
                    });
                }
            }
        });
        miscellaneous.getItems().addAll(uploadImage, uploadImage2);

        Popup aboutUpload = new Popup();
        Text uploadInfo = new Text("To upload an image, click on the upload button and select your preferred file");
        HBox uploadBox = new HBox(uploadInfo);
        uploadBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        aboutUpload.getContent().add(uploadBox);
        aboutUpload.setX(690);
        aboutUpload.setY(560);
        fromFileButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutUpload.show(primaryStage);
            }
        }); fromFileButton.setOnMouseExited(event -> {
            if(aboutUpload.isShowing()){
                aboutUpload.hide();
            }
        });

        CheckBox rotateSmallBox = new CheckBox();
        Button tempButton90 = new Button("90");
        Button tempButton180 = new Button("180");
        Button tempButton270 = new Button("270");
        HBox rotateDegrees = new HBox(rotateSmallBox, tempButton90, tempButton180, tempButton270);
        MenuItem selectAndRotateItem = new MenuItem("Rotate for selected part");
        rotateDegrees.setSpacing(2.7);
        selectAndRotateItem.setGraphic(rotateDegrees);
        ImageView partOfCanvas = new ImageView();
        rotateSmallBox.setOnAction(event -> {
            artBoard.setOnMouseDragged(event1 -> {
                partOfCanvas.setImage(artBoard.snapshot(null,null));
                partOfCanvas.setViewport(new Rectangle2D(event1.getX(), event1.getY(), event1.getSceneX()+115, event1.getSceneY()+200));
                partOfCanvas.setVisible(true);
                sPane.getChildren().add(partOfCanvas);
            });
            tempButton90.setOnAction(event1 -> {
                partOfCanvas.setRotate(partOfCanvas.getRotate()+90);
            });
            tempButton180.setOnAction(event1 -> {
                partOfCanvas.setRotate(partOfCanvas.getRotate()+180);
            });
            tempButton270.setOnAction(event1 -> {
                partOfCanvas.setRotate(partOfCanvas.getRotate()+270);
            });

        });

        CheckBox copyCheckBox = new CheckBox();
        MenuItem copy_and_paste = new MenuItem("Copy and Paste", copyCheckBox);
        copy_and_paste.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        copy_and_paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WritableImage tempImage = artBoard.snapshot(null, null);
                ImageView imageView = new ImageView(tempImage);
                Rectangle rectangle = new Rectangle();

                if (copyCheckBox.isSelected() && !paintCheckBox.isSelected() && !pencilCheckBox.isSelected() && !eraseCheckBox.isSelected()) {
                    artBoard.setOnMouseDragged(event1 -> {
                        rectangle.setX(event1.getX() - 80);
                        rectangle.setY(event1.getY() - 100);
                        rectangle.setWidth(event1.getX() + 120);
                        rectangle.setHeight(event1.getY() + 170);
                        Rectangle2D rect2d = new Rectangle2D(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                        imageView.setViewport(rect2d);
                        imageView.setVisible(true);
                        sPane.getChildren().add(imageView);
                    });
                    imageView.setOnKeyReleased(event1 -> {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        Image temporaryImage = imageView.snapshot(null, null);
                        switch (event1.getCode()) { //from Stack Overview
                            case C:
                                if (event1.isControlDown()) {   //structure from stack overflow
                                    content.putImage(temporaryImage);
                                    clipboard.setContent(content);
                                }
                            case V:
                                if (event1.isControlDown()) {
                                    Image copiedImage = (Image) clipboard.getContent(DataFormat.IMAGE);
                                    ImageView copiedImageView = new ImageView(copiedImage);
                                    sPane.getChildren().add(copiedImageView);
                                    copiedImageView.setVisible(true);
                                    copiedImageView.setTranslateX(imageView.getTranslateX() + 10);
                                    copiedImageView.setTranslateY(imageView.getTranslateY() + 10);

                                }
                        }
                    });
                }
            }
        });
        miscellaneous.getItems().add(copy_and_paste);

        CheckBox moveImageBox = new CheckBox();
        MenuItem moveItem = new MenuItem("Move", moveImageBox);
        moveImageBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WritableImage tempImage = artBoard.snapshot(null, null);
                ImageView imageView = new ImageView(tempImage);
                Rectangle rect = new Rectangle();
                rect.setOpacity(20);
                artBoard.setOnMouseDragged(event1 -> {
                    rect.setX(event1.getX() - 180);
                    rect.setY(event1.getY() - 100);
                    rect.setHeight(event1.getX() + 380);
                    rect.setWidth(event1.getY() + 275);

                    Rectangle2D rectangle2D = new Rectangle2D(rect.getX(), rect.getY(), rect.getHeight(), rect.getWidth());
                    imageView.setViewport(rectangle2D);
                    imageView.setVisible(true);
                    sPane.getChildren().add(imageView);


                });
                sPane.setOnMouseDragged(event1 -> {
                    imageView.setTranslateX(event1.getX());
                    imageView.setTranslateY(event1.getY());
                });


            }
        });
        miscellaneous.getItems().add(moveItem);

        miscellaneous.getItems().add(selectAndRotateItem);

        MenuItem rotateItem = new MenuItem("Rotate");
        Button ninetyDegree = new Button("90");
        ninetyDegree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPane.setRotate(sPane.getRotate()+90);
            }
        });
        Button hundredEightyDegree = new Button("180");
        hundredEightyDegree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPane.setRotate(sPane.getRotate()+180);
            }
        });
        Button twoHundredSeventy = new Button("270");
        twoHundredSeventy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPane.setRotate(sPane.getRotate()+270);
            }
        });
        VBox degrees = new VBox(3.5,ninetyDegree,hundredEightyDegree,twoHundredSeventy);
        rotateItem.setGraphic(degrees);
        miscellaneous.getItems().add(rotateItem);

        MenuItem mirror = new MenuItem("Mirror");
        Button mirror1 = new Button("Mirror horizontally");
        mirror1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPane.setScaleX(sPane.getScaleX()*-1);
            }
        });
        Button mirror2 = new Button("Mirror vertically");
        mirror2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPane.setScaleY(sPane.getScaleY()*-1);
            }
        });
        VBox mirrorBox = new VBox(3.5, mirror1,mirror2);
        mirror.setGraphic(mirrorBox);
        miscellaneous.getItems().add(mirror);

        MenuItem clearCanvasItem = new MenuItem("Clear Canvas", new Button());
        clearCanvasItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        clearCanvasItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BackgroundImage whiteBackgroundImage = new BackgroundImage(new Image(
                        "https://png.pngtree.com/background/20210714/original/pngtree-pure-white-background-wallpaper-picture-image_1219015.jpg" //from pngTree
                ), null, null, null, null);
                Background whiteBackground = new Background(whiteBackgroundImage);
                Popup clearCanvasPopup = new Popup();
                Button yesButton = new Button("Yes");
                Button noButton = new Button("No");
                yesButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Image whiteImage = new Image("https://png.pngtree.com/background/20210714/original/pngtree-pure-white-background-wallpaper-picture-image_1219015.jpg");
                        artBoard.getGraphicsContext2D().drawImage(whiteImage, 0, 0, artBoard.getWidth(), artBoard.getHeight());
                        clearCanvasPopup.hide();
                    }
                });
                noButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        clearCanvasPopup.hide();
                    }
                });
                GridPane gridPane = new GridPane();
                Text text = new Text("Are you sure that you want to clear the canvas?");
                gridPane.setBackground(whiteBackground);
                gridPane.addRow(1, text);
                gridPane.addRow(2, yesButton, noButton);
                clearCanvasPopup.centerOnScreen();
                clearCanvasPopup.getContent().addAll(gridPane);
                if (!clearCanvasPopup.isShowing()) { //from Geeks to Geeks
                    clearCanvasPopup.show(primaryStage);
                } else {
                    clearCanvasPopup.hide();
                }
            }
        });
        miscellaneous.getItems().add(clearCanvasItem);

        Popup clearCanvasPopup = new Popup();
        Text aboutClear = new Text("Click on the clear button if you are not satisfied with your work");
        HBox aboutClearBox = new HBox(aboutClear);
        aboutClearBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        clearCanvasPopup.getContent().add(aboutClearBox);
        clearCanvasPopup.setX(630);
        clearCanvasPopup.setY(560);
        clearCanvasItem.getGraphic().setOnMouseMoved(event -> {clearCanvasPopup.show(primaryStage);}); //from oracle
        clearCanvasItem.getGraphic().setOnMouseExited(event -> {  //from oracle
            if(clearCanvasPopup.isShowing()){
                clearCanvasPopup.hide();
            }
        });

        saveMenu.getItems().addAll(saveMenuItem1, saveMenuItem2);
        paintBar.getMenus().add(saveMenu);

        paintMenu.getItems().addAll(paintItem, pencilItem);
        paintBar.getMenus().add(paintMenu);

        Menu paintSize = new Menu("Change Tool Size");
        TextField paintSizeField = new TextField(String.valueOf(paintbrush_size));
        TextField eraserSizeField = new TextField(String.valueOf(paintbrush_size));
        TextField pencilSizeField = new TextField(String.valueOf(pencil_size));
        paintSizeField.setPrefColumnCount(2);
        eraserSizeField.setPrefColumnCount(2);
        pencilSizeField.setPrefColumnCount(2);
        MenuItem sizeForPaintbrush = new MenuItem("Size for paintbrush", paintSizeField);
        MenuItem sizeForEraser = new MenuItem("Size for eraser", eraserSizeField);
        MenuItem sizeForPencil = new MenuItem("Size for pencil", pencilSizeField);
        sizeForPaintbrush.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paintbrush_size = Double.parseDouble(paintSizeField.getText());
            }
        });
        sizeForEraser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eraser_size = Double.parseDouble(eraserSizeField.getText());
            }
        });
        sizeForPencil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pencil_size = Double.parseDouble(pencilSizeField.getText());
            }
        });
        paintSize.getItems().addAll(sizeForPaintbrush,sizeForEraser,sizeForPencil);
        paintBar.getMenus().add(paintSize);

        Menu shapeMenu = new Menu("Shape");
        lineItem = new CheckMenuItem("Line");
        MenuItem widthAdjustment = new MenuItem("Adjust width: ");
        Slider widthSlider = new Slider(10, 50, 10);
        widthAdjustment.setGraphic(widthSlider);
        CheckBox checkLine = new CheckBox();
        lineItem.setGraphic(checkLine);
        Line line1 = new Line(10, 20, 120, 40);
        line1.setStrokeWidth(10);
        line1.setVisible(false);
        checkLine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ; //based on Stack OverFlow
                lineItem.setSelected(true);
                currentTool = "Default Line Tool";
                if (checkLine.isSelected()) {
                    line1.toFront();
                    line1.setVisible(true);
                    resizeLine(artBoard, line1);
                } else {
                    line1.setVisible(false);
                    currentTool = null;
                    try {
                        stop();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        widthSlider.setOnMouseDragged(event -> {
            line1.setStrokeWidth(widthSlider.getValue());
        });
        sPane.getChildren().add(line1);
        shapeMenu.getItems().add(lineItem);
        shapeMenu.getItems().add(widthAdjustment);
        paintBar.getMenus().add(shapeMenu);
        borderPane.setTop(paintBar);

        dashedLine = new CheckMenuItem("Dashed Line");
        CheckBox checkDash = new CheckBox();
        MenuItem dashAdjust = new MenuItem("Adjust the Dash Line");
        double min = 2;
        double firstElement = min / 2 + 1;
        double secondElement = min / 3 + 5;
        double thirdElement = min / 4 + 9;
        double fourthElement = min / 5 + 13;
        double fifthElement = min / 6 + 17;
        Slider dashedLineAdjust = new Slider(min, 15, min);
        dashAdjust.setGraphic(dashedLineAdjust);
        Line line2 = new Line(40, 50, 100, 55);
        line2.getStrokeDashArray().addAll(firstElement, secondElement, thirdElement, fourthElement + fifthElement); //from Java GUI
        line2.setStrokeMiterLimit(1);
        line2.setStrokeWidth(2);
        line2.setVisible(false);
        checkDash.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashedLine.setSelected(true);
                currentTool = "Dashed Line Tool";
                if (checkDash.isSelected()) {
                    line2.setVisible(true);
                    resizeLine(artBoard, line2);
                } else {
                    line2.setVisible(false);
                    currentTool = null;
                }
            }
        });
        dashedLineAdjust.setOnMouseDragged(event -> {
            double tempNum = dashedLineAdjust.getValue();
            line2.setStrokeWidth(dashedLineAdjust.getValue());
        });
        dashedLine.setGraphic(checkDash);
        sPane.getChildren().add(line2);
        shapeMenu.getItems().addAll(dashedLine, dashAdjust);

        forSquareItem = new CheckMenuItem("Square");
        MenuItem squareWidth = new MenuItem("Square Width");
        Rectangle square = new Rectangle();
        square.setWidth(30);
        square.setHeight(30);
        square.setX(50);
        square.setY(50);
        square.setFill(Color.DARKBLUE);
        square.setVisible(false);
        sPane.getChildren().add(square);
        Slider squareController = new Slider(30, 150, 10);
        CheckBox checkSquare = new CheckBox();
        forSquareItem.setGraphic(checkSquare);
        squareWidth.setGraphic(squareController);
        squareController.setOnMouseDragged(event -> {
            square.setWidth(squareController.getValue());
            square.setHeight(squareController.getValue());
        });
        checkSquare.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                forSquareItem.setSelected(true);
                currentTool = "Circle Tool";
                if (checkSquare.isSelected()) {
                    square.setVisible(true);
                    moveSquare(artBoard, square);
                } else {
                    square.setVisible(false);
                    currentTool = null;
                }
            }
        });
        shapeMenu.getItems().addAll(forSquareItem, squareWidth);

        rectItem = new CheckMenuItem("Rectangle");
        MenuItem rectAdjust = new MenuItem("Rectangle Width");
        Rectangle rect = new Rectangle(50, 50, 20, 15);
        rect.setVisible(false);
        CheckBox checkRect = new CheckBox();
        Slider rectWidth = new Slider(20, 150, 20);
        rectItem.setGraphic(checkRect);
        rectAdjust.setGraphic(rectWidth);
        rectWidth.setOnMouseDragged(event -> {
            rect.setWidth(rectWidth.getValue() + 35);
            rect.setHeight(rectWidth.getValue() - 10);
        });
        checkRect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rectItem.setSelected(true);
                currentTool = "Rectangle tool";
                if (checkRect.isSelected()) {
                    rect.setVisible(true);
                    moveRect(artBoard, rect);
                } else {
                    rect.setVisible(false);
                    currentTool = null;
                }
            }
        });
        sPane.getChildren().add(rect);
        shapeMenu.getItems().addAll(rectItem, rectAdjust);

        circleItem = new CheckMenuItem("Circle");
        MenuItem radiusAdjust = new MenuItem("Adjust Radius");
        Circle circle = new Circle(10);
        circle.setVisible(false);
        CheckBox checkCircle = new CheckBox();
        Slider cRad = new Slider(10, 100, 10);
        circleItem.setGraphic(checkCircle);
        radiusAdjust.setGraphic(cRad);
        checkCircle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                circleItem.setSelected(true);
                currentTool = "Circle tool";
                if (checkCircle.isSelected()) {
                    circle.setVisible(true);
                    moveCircle(artBoard, circle);
                } else {
                    circle.setVisible(false);
                    currentTool = null;
                }
            }
        });
        cRad.setOnMouseDragged(event -> {
            circle.setRadius(cRad.getValue());
        });
        sPane.getChildren().add(circle);
        shapeMenu.getItems().addAll(circleItem, radiusAdjust);

        ellipseMenu = new CheckMenuItem("Ellipse");
        MenuItem ellipseAdjust = new MenuItem("Adjust Ellipse");
        Ellipse ellipse = new Ellipse(50, 50, 10, 20);
        ellipse.setVisible(false);
        CheckBox checkEllipse = new CheckBox();
        Slider eRad = new Slider(10, 100, 10);
        ellipseMenu.setGraphic(checkEllipse);
        ellipseAdjust.setGraphic(eRad);
        checkEllipse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ellipseMenu.setSelected(true);
                currentTool = "Ellipse Tool";
                if (checkEllipse.isSelected()) {
                    ellipse.setVisible(true);
                    moveEllipse(artBoard, ellipse);
                } else {
                    ellipse.setVisible(false);
                    currentTool = null;
                }
            }
        });
        eRad.setOnMouseDragged(event -> {
            ellipse.setRadiusX(eRad.getValue());
            ellipse.setRadiusY(eRad.getValue() + 27);
        });
        sPane.getChildren().add(ellipse);
        shapeMenu.getItems().addAll(ellipseMenu, ellipseAdjust);

        arcItem = new CheckMenuItem("Arc");
        CheckBox arcCheck = new CheckBox();
        Arc arc = new Arc(40, 90, 10, 10, 45, 180);
        arc.setVisible(false);
        arc.setType(ArcType.CHORD);
        arc.setFill(Color.DARKBLUE);
        arcItem.setGraphic(arcCheck);
        MenuItem adjustArc = new MenuItem("Adjust Arc");
        Slider arcSize = new Slider(10, 100, 10);
        adjustArc.setGraphic(arcSize);
        arcCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                arcItem.setSelected(true);
                currentTool = "Arc Tool";
                if (arcCheck.isSelected()) {
                    arc.setVisible(true);
                    moveArc(artBoard, arc);
                } else {
                    arc.setVisible(false);
                    currentTool = null;
                }
            }
        });
        arcSize.setOnMouseDragged(event -> {
            arc.setRadiusX(arcSize.getValue());
            arc.setRadiusY(arcSize.getValue());
        });
        shapeMenu.getItems().addAll(arcItem, adjustArc);
        sPane.getChildren().add(arc);

        polygonItem = new CheckMenuItem("Polygon");
        Polygon polygon = new Polygon((new double[]{0, 0, 40, -40, 80, 0, 60, 40, 20, 40}));
        polygon.setVisible(false);
        CheckBox polygonCheck = new CheckBox();
        MenuItem pointItem = new MenuItem("Change Polygons");
        Button addPointButton = new Button();
        MenuItem polygonSize = new MenuItem("Polygon Size");
        Slider polygonSlider = new Slider(1, 5, 1);
        polygonItem.setGraphic(polygonCheck);
        pointItem.setGraphic(addPointButton);
        polygonSize.setGraphic(polygonSlider);
        ArrayList<Double> list = new ArrayList<>(5);
        Random random = new Random();
        addPointButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (polygon.getPoints().size() / 2 < 6) {
                    polygon.getPoints().remove(2, 4);
                    polygon.getPoints().add(2, 20.0);
                    polygon.getPoints().add(3, -40.0);
                    polygon.getPoints().add(4, 60.0);
                    polygon.getPoints().add(5, -40.0);
                } else if ((polygon.getPoints().size() / 2 < 7) && polygonCheck.isSelected()) {
                    polygon.getPoints().remove(2, 6);
                    polygon.getPoints().add(2, 10.0);
                    polygon.getPoints().add(3, -40.0);
                    polygon.getPoints().add(4, 40.0);
                    polygon.getPoints().add(5, -55.0);
                    polygon.getPoints().add(6, 70.0);
                    polygon.getPoints().add(7, -40.0);
                } else if (polygon.getPoints().size() / 2 < 8 && polygonCheck.isSelected()) {
                    polygon.getPoints().removeAll(polygon.getPoints());
                    polygon.getPoints().addAll(0.0, 0.0, 0.0, 30.0, 30.0, 60.0, 60.0, 60.0, 90.0, 30.0, 90.0, 0.0, 60.0, -30.0, 30.0, -30.0);
                } else if ((polygon.getPoints().size() / 2 < 9) && polygonCheck.isSelected()) {
                    polygon.getPoints().removeAll(polygon.getPoints());
                    polygon.getPoints().addAll(10.0, 0.0, 20.0, 31.0, 50.0, 50.0, 83.0, 42.0, 108.0, 18.0, 108.0, -18.0, 83.0, -42.0, 50.0, -50.0, 20.0, -31.0);
                    polygon.setRotate(90);
                } else if ((polygon.getPoints().size() / 2 < 10) && polygonCheck.isSelected()) {
                    ArrayList<Double> list = new ArrayList<>(20);
                    polygon.getPoints().removeAll(polygon.getPoints());
                    polygon.getPoints().addAll(0d, 0d, 10d, 20d, 30d, 30d, 50d, 30d, 70d, 20d, 80d, 0d, 70d, -20d, 50d, -30d, 30d, -30d, 10d, -20d);
                    ;
                } else {
                    polygon.getPoints().removeAll(polygon.getPoints());
                    polygon.getPoints().addAll(0d, 0d, 40d, -40d, 80d, 0d, 60d, 40d, 20d, 40d);
                    polygon.setRotate(360);
                }
            }
        });
        polygonCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Random rand = new Random();
                polygonItem.setSelected(true);
                currentTool = "Polygon Tool";
                if (polygonCheck.isSelected() /*&& !addPointText.getText().isBlank()*/) {
                    polygon.setVisible(true);
                    movePolygon(artBoard, polygon);
                } else {
                    polygon.setVisible(false);
                    currentTool = null;
                }
            }
        });
        polygonSlider.setOnMouseDragged(event -> {
            polygon.setScaleX(polygonSlider.getValue());
            polygon.setScaleY(polygonSlider.getValue());
        });
        sPane.getChildren().add(polygon);
        shapeMenu.getItems().addAll(polygonItem, polygonSize, pointItem);

        Popup aboutLine = new Popup();
        Text lineInfo = new Text("Check off the line checkbox and drag on the canvas to change the length of the line. " +
                "Use a slider to change size");
        HBox lineBox = new HBox(lineInfo);
        lineBox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        aboutLine.getContent().add(lineBox);
        aboutLine.setX(690);
        aboutLine.setY(600);
        checkLine.setOnMouseMoved(event -> {aboutLine.show(primaryStage);});
        checkLine.setOnMouseExited(event -> {aboutLine.hide();});

        Popup aboutSquare = new Popup();
        Text squareInfo = new Text("Select the square checkbox. Move the canvas to change position. Use slider to change size");
        HBox squareBox = new HBox(squareInfo);
        aboutSquare.getContent().add(squareBox);
        aboutSquare.setX(690);
        aboutSquare.setY(600);
        checkSquare.setOnMouseMoved(event -> {aboutLine.show(primaryStage);});
        checkSquare.setOnMouseExited(event -> {aboutLine.hide();});

        colorChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkSquare.isSelected() && !checkLine.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected() && !checkDash.isSelected() && !polygonCheck.isSelected()) {
                    square.setFill(colorChooser.getValue());
                } else if (checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected() && !checkDash.isSelected() && !polygonCheck.isSelected()) {
                    line1.setStroke(colorChooser.getValue());
                } else if (checkRect.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected() && !checkDash.isSelected() && !polygonCheck.isSelected()) {
                    rect.setFill(colorChooser.getValue());
                } else if (checkCircle.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkEllipse.isSelected() && !checkDash.isSelected() && !polygonCheck.isSelected()) {
                    circle.setFill(colorChooser.getValue());
                } else if (checkEllipse.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkDash.isSelected() && !polygonCheck.isSelected()) {
                    ellipse.setFill(colorChooser.getValue());
                } else if (checkDash.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected() && polygonCheck.isSelected()) {
                    line2.setStroke(colorChooser.getValue());
                } else if (arcCheck.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected() && !polygonCheck.isSelected()) {
                    arc.setFill(colorChooser.getValue());
                } else if (polygonCheck.isSelected() && !checkLine.isSelected() && !checkSquare.isSelected() && !checkRect.isSelected() && !checkCircle.isSelected() && !checkEllipse.isSelected()) {
                    polygon.setFill(colorChooser.getValue());
                }
            }
        });
        artBoard.setOnMouseClicked(event -> {
            colorChooser.setValue(artBoard.snapshot(null, null).getPixelReader().getColor((int) event.getX(), (int) event.getY())); //code from a student
        });
        ObservableList<Color> customColors = colorChooser.getCustomColors();
        PixelReader pixelReader = appleImage.getPixelReader();
        artBoard.setOnMouseClicked(event -> {
            if (customColors.size() < 12) {
                customColors.addAll(pixelReader.getColor((int) event.getX(), (int) event.getY()));
            } else {
                customColors.remove(11);
                customColors.add(pixelReader.getColor((int) event.getX(), (int) event.getY()));
            }
        });

        Menu color = new Menu("Pick A Color");
        MenuItem colorList = new MenuItem("Colors");
        MenuItem testColorList = new MenuItem("This is a test color picker");
        colorList.setGraphic(colorChooser);
        color.getItems().add(colorList);
        paintBar.getMenus().add(color);

        TextField customColorText = new TextField();
        customColorText.setPrefColumnCount(4);
        MenuItem customColorInput = new MenuItem("Custom Color Insert", customColorText);
        customColorText.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle (ActionEvent event){
                                            try {
                                                colorChooser.setValue(Color.web(customColorText.getText()));
                                            } catch (Exception exception) {
                                                System.out.println("Incorrect color!");
                                            }
                                        }
                                    }
        );
        color.getItems().add(customColorInput);

        Menu helpSection = new Menu("Help");
        Button helpButton = new Button("  ");
        MenuItem helpOption = new MenuItem("Need help saving", helpButton);
        Button aboutButton = new Button(" ");
        MenuItem about = new MenuItem("About", aboutButton);
        Popup savePopup = new Popup();
        Button aboutSaveButton = new Button("Please use the save-as option on the very left");

        Popup aboutPopup = new Popup();
        Button description = new Button("This is a paint project where you can decorate your imageArrayList.");
        aboutPopup.centerOnScreen();
        aboutPopup.show(primaryStage);
        aboutPopup.getContent().add(description);

        aboutSaveButton.setCancelButton(true);
        savePopup.setWidth(200);
        savePopup.setHeight(100);
        savePopup.show(primaryStage);
        savePopup.centerOnScreen();
        savePopup.getContent().add(aboutSaveButton);
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (helpButton.isPressed())//from GeeksAndGeeks
                    if (!savePopup.isShowing()) {
                        savePopup.show(primaryStage);
                    } else {
                        savePopup.hide();
                    }
            }
        });
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (aboutButton.isPressed()) {
                    if (!aboutPopup.isShowing()) {
                        aboutPopup.show(primaryStage);
                    } else {
                        aboutPopup.hide();

                    }
                }
            }
        });
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!aboutPopup.isShowing()) {
                    aboutPopup.show(primaryStage);
                } else {
                    aboutPopup.hide();
                }
            }
        });
        helpOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!savePopup.isShowing()) {
                    savePopup.show(primaryStage);
                } else {
                    savePopup.hide();
                }
            }
        });
        aboutSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                savePopup.hide();
            }
        });
        description.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutPopup.hide();
            }
        });
        about.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        helpOption.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        helpSection.getItems().add(helpOption);
        helpSection.getItems().add(about);
        paintBar.getMenus().add(helpSection);

        Menu size = new Menu("Size");
        MenuItem sizeItem = new MenuItem("Input Size");
        size.getItems().add(sizeItem);
        paintBar.getMenus().add(size);
        TextField sizeTextField = new TextField("500");
        sizeTextField.setPrefColumnCount(4);
        sizeTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String textInput = sizeTextField.getText();
                Double numberText = Double.parseDouble(textInput);

                WritableImage tempImage = new WritableImage((int) artBoard.getWidth(), (int) artBoard.getHeight());
                WritableImage xImage = artBoard.snapshot(null, null);
                ImageView view = new ImageView(xImage);
                view.setFitHeight(numberText);
                view.setFitWidth(numberText);
                view.setPreserveRatio(true);
                view.setSmooth(true);
                view.setCache(true);
                artBoard.setHeight(numberText);
                artBoard.setWidth(numberText);
                artBoard.getGraphicsContext2D().setImageSmoothing(true);
                artBoard.getGraphicsContext2D().drawImage(view.getImage(), view.getX(), view.getY(), artBoard.getWidth(), artBoard.getHeight());

            }
        });
        sizeItem.setGraphic(sizeTextField);

        TimerClass timerClass = new TimerClass();
        TimerClass timerClass1 = new TimerClass();

        timerClass.run(artBoard);
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerClass, 0, 1000);

        } catch (Exception e) {
            System.out.println("Out of time.");
        }


        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setVisible(true);
        scrollBar.setMax(400);
        scrollBar.setMin(50);
        scrollBar.setValue(50);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(600);
        scrollBar.setBlockIncrement(50);
        scrollBar.setUnitIncrement(48);
        vBox.getChildren().add(scrollBar);
        borderPane.setRight(vBox);

        Menu newTab = new Menu("New Tab");
        MenuItem tabItem = new MenuItem();
        Button tabButton = new Button("");
        tabItem.setGraphic(tabButton);
        VBox tabBox = new VBox(1.7);
        newTab.getItems().add(tabItem);
        ArrayList<Tab> tabArrayList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            tabArrayList.add(i, new Tab("Tab"));
        }
        final int[] counter = {9};
        tabButton.setOnAction(new EventHandler<ActionEvent>() {
                                  @Override
                                  public void handle(ActionEvent event) {
                                      int icounter = counter[0];
                                      TabPane tabPane = new TabPane();
                                      tabPane.setBackground(b);
                                      tabPane.setVisible(true);
                                      tabPane.setPrefWidth(600);
                                      tabPane.setPrefHeight(600);
                                      tabPane.setTranslateX(0);
                                      tabPane.setTranslateY(0);
                                      sPane.getChildren().add(tabPane);
                                      Tab newTab = tabArrayList.get(counter[0]);
                                      Tab newTab2 = new Tab("Tab");
                                      tabPane.getTabs().addAll(newTab, newTab2);
                                      tabArrayList.get(counter[0]).setClosable(true);
                                      newTab2.setClosable(true);
                                      WritableImage tTempImage = new WritableImage((int) artBoard.getWidth(), (int) artBoard.getHeight());
                                      WritableImage tImage = artBoard.snapshot(null, tTempImage);
                                      Canvas newCanvas = new Canvas(artBoard.getWidth(), artBoard.getHeight());
                                      newCanvas.getGraphicsContext2D().setImageSmoothing(true);
                                      //artBoard.getGraphicsContext2D().drawImage(tTempImage, 0, 0, tImage.getWidth(), tImage.getHeight());
                                      newCanvas.getGraphicsContext2D().drawImage(tTempImage, 0, 0, tImage.getWidth(), tImage.getHeight());
                                      paintCheckBox.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {
                                              Paint(artBoard, colorChooser, stackOfWritableImage);
                                              Paint(newCanvas, colorChooser, stackOfWritableImage);
                                          }
                                      });
                                      pencilCheckBox.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {
                                              pencilDraw(newCanvas, stackOfWritableImage);
                                              pencilDraw(artBoard, stackOfWritableImage);
                                          }
                                      });
                                      eraseCheckBox.setOnAction(new EventHandler<ActionEvent>() {
                                                                    @Override
                                                                    public void handle(ActionEvent event) {
                                                                        erase(newCanvas, stackOfWritableImage);
                                                                    }
                                                                }
                                      );
                                      newTab.setContent(artBoard);
                                      newTab2.setContent(newCanvas);
                                  }

                              }
        );
        paintBar.getMenus().add(newTab);

        primaryStage.setScene(imageScene);

        primaryStage.show();

        Popup closeButtonInformation = new Popup();
        Text closeInformationText = new Text("Click on the close button, and select if you want to save.");
        HBox infobox = new HBox(closeInformationText);
        infobox.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        closeButtonInformation.getContent().add(infobox);
        closeButtonInformation.setAnchorX(690);
        closeButtonInformation.setAnchorY(250);
        closeButtonInformation.centerOnScreen();
        closeButton.setOnMouseMoved(event -> {
            closeButtonInformation.show(primaryStage);
        }); closeButton.setOnMouseExited(event -> {
            if(closeButtonInformation.isShowing()){
                closeButtonInformation.hide();
            }
        });


        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (closeButton.isArmed()) {
                    Popup awareSavePopup = new Popup();
                    Button cancelSave = new Button("Cancel");
                    Button saveButtonForSavePop = new Button("Save");
                    Button goWithoutSaveButton = new Button("Don't Save");
                    Text savePrompt = new Text("Do you want to save?");
                    Image backGround = new Image("https://png.pngtree.com/thumb_back/fh260/background/20200821/pngtree-pure-white-minimalist-background-wallpaper-image_396581.jpg"); //from pngTree
                    BackgroundImage backgroundImage = new BackgroundImage(backGround, null, null, null, null);
                    Background whiteBackground = new Background(backgroundImage);
                    awareSavePopup.show(primaryStage);
                    awareSavePopup.centerOnScreen();
                    GridPane savePane = new GridPane();
                    savePane.setBackground(whiteBackground);
                    StackPane textStructure = new StackPane();
                    textStructure.getChildren().add(savePrompt);
                    savePane.addRow(1, textStructure);
                    savePane.addRow(2, saveButtonForSavePop, goWithoutSaveButton, cancelSave);
                    awareSavePopup.getContent().addAll(savePane);
                    cancelSave.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            awareSavePopup.hide();
                        }
                    });
                    goWithoutSaveButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            primaryStage.hide();
                            System.exit(0);
                        }
                    });
                    saveButtonForSavePop.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            awareSavePopup.hide();
                            saveAs(artBoard, chooser1, primaryStage);
                            System.exit(0);
                        }
                    });

                }
            }
        });

    }

    /**
     * The erase method would remove any "pencil" or "paint" marks on the canvas.
     *
     * @param artwork --is the canvas in which it would be erased upon.
     * @param stack   --helps to implement the addToStack method.
     */
    public static void erase(Canvas artwork, Stack stack) {
        GraphicsContext g = artwork.getGraphicsContext2D();
        artwork.setOnMouseDragged(event -> {
            g.clearRect(event.getX() - 10, event.getY() - 10, paintbrush_size, paintbrush_size);
        });
        addToStack(artwork, stack);

    }

    /**
     * It inserts any writable images to the stack (which would be stackImage, in this case).
     *
     * @param canvas     --helps to create a writable image in which it will be inserted into the stack.
     * @param stackImage --retrieves the writable images and "stores" them.
     */
    public static void addToStack(Canvas canvas, Stack stackImage) {
        stackImage.push(canvas.snapshot(null, null));
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stackImage.push(canvas.snapshot(null, null));
                canvas.getGraphicsContext2D().setEffect(null);
            }
        });
    }

    /**
     * The paint method will create a mark on the canvas.
     *
     * @param artwork    --displays the paint mark.
     * @param picker     --changes the paint mark's color.
     * @param imageStack --helps to insert a writable object into the stack.
     */
    public static void Paint(Canvas artwork, ColorPicker picker, Stack imageStack) { //method based on paint tutorial
        GraphicsContext graphics = artwork.getGraphicsContext2D();

        artwork.setOnMouseDragged(event -> {  //event acts like a "listener"
            graphics.setFill(picker.getValue());//from paint tutorial
            graphics.fillOval(event.getX() - 10, event.getY() - 10, paintbrush_size, paintbrush_size);
        });
        addToStack(artwork, imageStack);

    }

    public static void saveAs(Canvas artwork, FileChooser filePicker, Stage stage) { //alert objects and methods is from Oracle.

        filePicker.setTitle("Saving picture.");
        File newFile = filePicker.showSaveDialog(stage);

        if ((newFile != null)) {
            try {
                WritableImage w = new WritableImage((int) artwork.getWidth(), (int) artwork.getHeight());
                byte[] data = newFile.getAbsolutePath().getBytes(); //from GeekstoGeeks

                WritableImage writablePicture = artwork.snapshot(null, w); //originally replacing this code with another code based on Tabnine
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                RenderedImage rImage = SwingFXUtils.fromFXImage(writablePicture, new BufferedImage((int) artwork.getWidth(), (int) artwork.getHeight(),BufferedImage.TYPE_INT_RGB));
                ImageIO.write(rImage,"jpg",newFile);

                Alert warning = new Alert(Alert.AlertType.WARNING);
                if (newFile.getAbsolutePath().contains(".png")) {
                    HBox warningButtonBox = new HBox();
                    Button noWarning = new Button("No");
                    Button yesWarning = new Button("Yes");
                    warningButtonBox.getChildren().addAll(yesWarning,noWarning);
                    warning.setHeaderText("You are about to save a file with an alternative format. It could result in data loss. Are you sure to save?");
                    warning.setGraphic(warningButtonBox);
                    noWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            warning.hide();
                        }
                    }); yesWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            warning.hide();
                        }
                    });
                    warning.show();
                } else if (newFile.getAbsolutePath().contains(".jpeg")) {
                    HBox warningButtonBox = new HBox();
                    Button noWarning = new Button("No");
                    Button yesWarning = new Button("Yes");
                    warningButtonBox.getChildren().addAll(yesWarning,noWarning);
                    warning.setHeaderText("You are about to save a file with an alternative format. It could result in data loss. Are you sure to save?");
                    warning.setGraphic(warningButtonBox);
                    noWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (warning.isShowing()) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                warning.hide();
                            }
                        }
                    }); yesWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            warning.hide();
                        }
                    });
                    warning.show();
                } else if (newFile.getAbsolutePath().contains(".gif")) {
                    HBox warningButtonBox = new HBox();
                    Button noWarning = new Button("No");
                    Button yesWarning = new Button("Yes");
                    warningButtonBox.getChildren().addAll(yesWarning,noWarning);
                    warning.setHeaderText("You are about to save a file with an alternative format. It could result in data loss. Are you sure to save?");
                    warning.setGraphic(warningButtonBox);
                    noWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (warning.isShowing()) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                warning.hide();
                            }
                        }
                    }); yesWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            warning.hide();
                        }
                    });
                    warning.show();
                } else if (newFile.getAbsolutePath().contains(".bmp")) {
                    Button noWarning = new Button("No");
                    Button yesWarning = new Button("Yes");
                    HBox warningButtonBox = new HBox();
                    warningButtonBox.getChildren().addAll(yesWarning,noWarning);
                    warning.setHeaderText("You are about to save a file with an alternative format. It could result in data loss. Are you sure to save?");
                    warning.setGraphic(warningButtonBox);
                    noWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (warning.isShowing()) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                warning.hide();
                            }
                        }
                    }); yesWarning.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            warning.hide();
                        }
                    });
                    warning.show();
                } else if (newFile.getAbsolutePath().contains(".jpg")) {
                    ImageIO.write(rImage,"jpg",fileOutputStream);
                }
                //ImageIO.write(rImage, "jpg", fileOutputStream);
            } catch (IOException e) {
                System.out.println("Error!");
            }

        }

    }

    public static double resizeLine(Canvas artwork, Line line) {
        artwork.setOnMouseDragged(event -> {
            line.setStartX(event.getX() * 1.38);
            line.setStartY(event.getY() * 1.38);
            line.setEndX(event.getX() * 2.6);
            line.setEndY(event.getY() * 2.6);
        });
        return line.getStartX();
    }

    public static double moveSquare(Canvas canvas, Rectangle sq) {
        canvas.setOnMouseDragged(event -> {
            sq.setTranslateX(event.getX() - 5);
            sq.setTranslateY(event.getY() - 10);
        });
        return sq.getTranslateX();
    }

    public static double moveRect(Canvas canvas, Rectangle rectangle) {
        canvas.setOnMouseDragged(event -> {
            rectangle.setTranslateX(event.getX());
            rectangle.setTranslateY(event.getY());
        });
        return rectangle.getTranslateX();
    }

    public static double moveCircle(Canvas canvas, Circle c) {
        canvas.setOnMouseDragged(event -> {
            c.setTranslateX(event.getX() - 10);
            c.setTranslateY(event.getY() - 10);

        });
        return c.getRadius();
    }

    public static double moveEllipse(Canvas canvas, Ellipse e) {
        canvas.setOnMouseDragged(event -> {
            e.setTranslateX(event.getX() - 10);
            e.setTranslateY(event.getY() - 10);
        });
        return e.getRadiusY();

    }

    public static void pencilDraw(Canvas canvas, Stack stackOfImages) {
        Effect clearEffect = canvas.getEffect();
        Blend pencilBlend = new Blend();    //based on JavaTPoint
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(event -> {
            pencilBlend.setOpacity(10);
            graphicsContext.setFill(Color.BLACK);
            pencilBlend.setMode(BlendMode.SOFT_LIGHT); //the PencilBlend methods is from the JavaTPoint
            pencilBlend.setBottomInput(new GaussianBlur()); //based on JavaTPoint
            pencilBlend.setTopInput(new GaussianBlur());
            graphicsContext.setEffect(pencilBlend);
            graphicsContext.fillRoundRect(event.getX() - 10, event.getY() - 5, pencil_size, pencil_size + 10, 10, 10);
        });
        addToStack(canvas, stackOfImages);

    }

    public static void moveArc(Canvas canvas, Arc arc) {
        canvas.setOnMouseDragged(event -> {
            arc.setTranslateX(event.getX() + 10);
            arc.setTranslateY(event.getY() + 10);
        });

    }

    public static void movePolygon(Canvas canvas, Polygon polygon) {
        canvas.setOnMouseDragged(event -> {
            polygon.setTranslateX(event.getSceneX());
            polygon.setTranslateY(event.getSceneY());
        });
    }

    public void turnToggleOff(){
        lineItem.setSelected(false);
        dashedLine.setSelected(false);
        forSquareItem.setSelected(false);
        rectItem.setSelected(false);
        circleItem.setSelected(false);
        ellipseMenu.setSelected(false);
        arcItem.setSelected(false);
        polygonItem.setSelected(false);
    }

}