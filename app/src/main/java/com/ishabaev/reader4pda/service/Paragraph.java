package com.ishabaev.reader4pda.service;

public class Paragraph {

    private String text;
    private String style;
    private String imageUrl;

    public final static String STYLE_TITLE = "title";
    public final static String STYLE_TEXT = "p";
    public final static String STYLE_HEADER = "h2";
    public final static String STYLE_IMAGE= "image";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String umageUrl) {
        this.imageUrl = umageUrl;
    }
}
