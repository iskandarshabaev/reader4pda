package com.ishabaev.reader4pda.converters;

import com.ishabaev.reader4pda.service.Description;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class DescriptionConverterFactory extends Converter.Factory {
    public static DescriptionConverterFactory create() {
        return new DescriptionConverterFactory();
    }

    private DescriptionConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> c = (Class<?>) type;
        if (!Description.class.isAssignableFrom(c)) {
            return null;
        }
        return new DesctiptionResponseBodyConverter<>();
    }
}