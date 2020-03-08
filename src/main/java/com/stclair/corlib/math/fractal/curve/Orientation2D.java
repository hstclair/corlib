package com.stclair.corlib.math.fractal.curve;

public enum Orientation2D {
    Up(1),       //  2
    Down(2),     // -2
    Right(3),    //  1
    Left(4);     // -1

    int value;


    Orientation2D(int value) {
        this.value = value;
    }
}
