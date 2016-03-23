package com.ishabaev.reader4pda.converters;

import com.ishabaev.reader4pda.service.Post;
import com.ishabaev.reader4pda.service.Posts;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class PostsResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    PostsResponseBodyConverter() {
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Posts postList = new Posts();
        byte[] bodyBytes = value.bytes();
        String string = new String(bodyBytes, "windows-1251");
        Document document = Jsoup.parse(string);

        document.body().child(0);

        Elements postElements = document.getElementsByClass("post");
        for(int i = 0; i < postElements.size(); i++){
            if(postElements.get(i).children().size() > 1 &&
                    postElements.get(i).child(0).children().size()  > 0 &&
                    postElements.get(i).child(1).children().size() > 1 &&
                    postElements.get(i).child(1).child(1).children().size() > 0) {

                Post post = new Post();
                post.setTitle(postElements.get(i).child(0).child(0).attr("title"));
                post.setDesriptionl(postElements.get(i).child(1).child(1).child(0).text());
                //String detailsUrl = postElements.get(i).child(1).child(0).child(0).attr("href");
                String detailsUrl = postElements.get(i).attr("data-ztm");
                String[] d = detailsUrl.split(":");
                detailsUrl = d[1];
                post.setDetailsUrl(detailsUrl);
                String uri = postElements.get(i).child(0).child(0).child(0).attr("src");
                post.setImageUri(uri);
                postList.add(post);
            }
        }
        return (T)postList;
    }
}