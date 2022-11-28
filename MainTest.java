package com.example.paintproject2;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.testng.annotations.Test;


import static org.testng.Assert.assertEquals;

class MainTest extends Main{

    @Test
    void startXLineIsConsistent() {
        Line testLine = new Line(10,20,120,40);
        testLine.setStartX(Main.resizeLine(new Canvas(),testLine));
        assertEquals(testLine.getStartX(),testLine.getStartX());
    }

    @Test
    void testMoveSquareTranslateX() {
        Rectangle testSquare = new Rectangle(50,50,30,30);
        testSquare.setTranslateX(moveSquare(new Canvas(), testSquare));
        assertEquals(testSquare,testSquare);

    }

    @Test
    void testMoveRectTranslateX() {
        Rectangle testRectangle = new Rectangle(50,50,20,15);
        testRectangle.setTranslateX(moveRect(new Canvas(),testRectangle));
        assertEquals(testRectangle.getTranslateX(),testRectangle.getTranslateX());
    }

}