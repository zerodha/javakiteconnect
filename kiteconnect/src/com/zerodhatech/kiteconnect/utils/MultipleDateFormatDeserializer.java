package com.zerodhatech.kiteconnect.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleDateFormatDeserializer implements JsonDeserializer<Date> {

    private final List<SimpleDateFormat> dateFormats;

    public MultipleDateFormatDeserializer(String... formats) {
        this.dateFormats = Arrays.stream(formats)
            .map(SimpleDateFormat::new).collect(Collectors.toList());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        for (SimpleDateFormat format : dateFormats) {
            try {
                return format.parse(json.getAsString());
            } catch (ParseException e) {
                // Try next format
            }
        }
        throw new JsonParseException("Unparseable date: \"" + json.getAsString() + "\". Supported formats: " + Arrays.toString(dateFormats.stream().map(SimpleDateFormat::toPattern).toArray()));
    }
}