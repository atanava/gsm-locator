package com.atanava.bsc.util;

import lombok.Builder;

@Builder(toBuilder = true)
public class OsmUriHelper {
    private String baseUri;
    private String type;
    private String location;
    private String format;
    private int limit;

    public String prepareUri() {
        //Example: "https://nominatim.openstreetmap.org/?q=supermarket+in+tallinn&format=json&limit=49"
        return String.format("%s%s+in+%s&format=%s&limit=%d", baseUri, type, location, format, limit);
    }
}
