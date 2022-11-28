package com.example.paintproject2;
//Note: from Stack Overflow

import javax.swing.*;
import java.awt.*;

public class LineClass {

    private int x1;
    private int x2;
    private int y1;
    private int y2;

//        public LineClass(int height, int width) {
//              super();
//        }

//        public void addLine() {
//                int height = getPreferredSize().height;
//                int width = getPreferredSize().width;
//                Line2D line2D = ne
//        }

    public void paintCom(Graphics graphics, int x1, int y1, int x2, int y2) {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(this.x1, this.y1, this.x2, this.y2);

    }

}


