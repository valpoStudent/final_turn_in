package com.example.paintproject2;
import javafx.scene.canvas.Canvas;

public class ResizeableCanvas extends Canvas {


    public Boolean isResizeable() {
        return super.isResizable();
    }

    @Override
    public double maxHeight(double width) {
        return super.maxHeight(width);
    }

    @Override
    public double minHeight(double width) {
        return super.minHeight(width);
    }

    @Override
    public double maxWidth(double height) {
        return super.maxWidth(height);
    }

    @Override
    public double minWidth(double height) {
        return super.minWidth(height);
    }

    @Override
    public void resizeRelocate(double x, double y, double width, double height) {
        super.resizeRelocate(x, y, width, height);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
    }
}
