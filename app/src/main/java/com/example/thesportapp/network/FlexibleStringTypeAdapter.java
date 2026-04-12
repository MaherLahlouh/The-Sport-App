package com.example.thesportapp.network;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * TheSportsDB sometimes returns scores as JSON strings and sometimes as numbers.
 * Plain String deserialization fails on numbers and breaks the whole response.
 */
public final class FlexibleStringTypeAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (token == JsonToken.STRING) {
            return in.nextString();
        }
        if (token == JsonToken.NUMBER) {
            double d = in.nextDouble();
            if (d == Math.rint(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        if (token == JsonToken.BOOLEAN) {
            return String.valueOf(in.nextBoolean());
        }
        in.skipValue();
        return null;
    }
}
