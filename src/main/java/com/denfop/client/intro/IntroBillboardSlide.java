package com.denfop.client.intro;

public class IntroBillboardSlide {

    private final String title;
    private final String imageSource;

    public IntroBillboardSlide(String title, String imageSource) {
        this.title = title;
        this.imageSource = imageSource;
    }

    public String title() {
        return title;
    }

    public String source() {
        return imageSource;
    }
}