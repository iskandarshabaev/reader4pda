package com.ishabaev.reader4pda.converters;

import com.ishabaev.reader4pda.service.Posts;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class PostsConverterFactory extends Converter.Factory {


    public static PostsConverterFactory create() {
        return new PostsConverterFactory();
    }

    private PostsConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> c = (Class<?>) type;
        if (!Posts.class.isAssignableFrom(c)) {
            return null;
        }
        return new PostsResponseBodyConverter<>();
    }
}