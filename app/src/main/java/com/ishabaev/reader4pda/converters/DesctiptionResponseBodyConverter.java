package com.ishabaev.reader4pda.converters;

import com.ishabaev.reader4pda.service.Description;
import com.ishabaev.reader4pda.service.Paragraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class DesctiptionResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    DesctiptionResponseBodyConverter() {
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Description description = new Description();
        List<Paragraph> paragraps = new ArrayList<>();
        byte[] bodyBytes = value.bytes();
        String string = new String(bodyBytes, "windows-1251");
        Document document = Jsoup.parse(string);
        String title = document.getElementsByAttributeValue("name", "keywords").attr("content");
        description.setTitle(title);
        Elements contents = document.getElementsByClass("content");
        Elements elements = contents.get(0).child(0).children();
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).childNodeSize() > 0) {
                if (elements.get(i).childNode(0).attr("href") != null &&
                        !elements.get(i).childNode(0).attr("href").equals("")) {
                    String imageUrl = elements.get(i).childNode(0).attr("href");
                    Paragraph paragraph = new Paragraph();
                    paragraph.setImageUrl(imageUrl);
                    paragraph.setStyle(Paragraph.STYLE_IMAGE);
                    paragraps.add(paragraph);
                } else {
                    String text = getTextFromElement(elements.get(i));
                    Paragraph paragraph = new Paragraph();
                    paragraph.setText(text);
                    paragraph.setStyle(elements.get(i).tag().toString());
                    paragraps.add(paragraph);
                }
            }
        }
        description.setParagraphs(paragraps);
        return (T) description;
    }

    private String getTextFromElement(Element elemenet) {
        String text = "";
        for (int i = 0; i < elemenet.childNodeSize(); i++) {
            if (elemenet.childNode(i) instanceof TextNode) {
                text += ((TextNode) elemenet.childNode(i)).text();
            } else if (elemenet.childNode(i) instanceof Element) {
                text += getTextFromElement((Element) elemenet.childNode(i));
            }
        }
        return text;
    }
}