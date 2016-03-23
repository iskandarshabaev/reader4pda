package com.ishabaev.reader4pda.service;

import java.util.ArrayList;
import java.util.List;

public class Description {

    private String title;
    private List<Paragraph> paragraphs;

    public Description(){
        paragraphs = new ArrayList<>();
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
