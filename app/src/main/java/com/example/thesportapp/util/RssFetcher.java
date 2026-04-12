package com.example.thesportapp.util;

import com.example.thesportapp.model.NewsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class RssFetcher {

    private static final String BBC_FOOTBALL_RSS = "https://feeds.bbci.co.uk/sport/football/rss.xml";

    private RssFetcher() {
    }

    public static List<NewsItem> fetchFootballHeadlines() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BBC_FOOTBALL_RSS)
                .header("User-Agent", "TheSportApp/1.0")
                .build();
        String body;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IllegalStateException("RSS HTTP " + response.code());
            }
            body = response.body().string();
        }

        XmlPullParser parser = android.util.Xml.newPullParser();
        parser.setInput(new StringReader(body));

        List<NewsItem> items = new ArrayList<>();
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                readItem(parser, items);
            }
            event = parser.next();
        }
        return items;
    }

    private static void readItem(XmlPullParser parser, List<NewsItem> out)
            throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        while (!(parser.next() == XmlPullParser.END_TAG && "item".equals(parser.getName()))) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String n = parser.getName();
            if ("title".equals(n)) {
                title = parser.nextText();
            } else if ("link".equals(n)) {
                link = parser.nextText();
            }
        }
        if (title != null && link != null) {
            out.add(new NewsItem(title.trim(), link.trim()));
        }
    }
}
