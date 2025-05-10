package com.denfop.render.streak;

public class RGB {

    private short red;
    private short blue;
    private short green;

    public RGB(short red, short blue, short green) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public short getGreen() {
        return green;
    }

    public void setGreen(final short green) {
        this.green = green;
    }

    public short getBlue() {
        return blue;
    }

    public void setBlue(final short blue) {
        this.blue = blue;
    }

    public short getRed() {
        return red;
    }

    public void setRed(final short red) {
        this.red = red;
    }

}
