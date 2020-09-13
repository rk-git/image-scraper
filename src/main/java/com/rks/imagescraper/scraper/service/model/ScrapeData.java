package com.rks.imagescraper.scraper.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScrapeData {
    @JsonProperty private String id;
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    @JsonProperty private byte[] image;
    public String getId() {
        return id;
    }
    public void setId(final String id) {
        this.id = id;
    }

    public ScrapeData() {
        this(null, null);
    }

    public ScrapeData(final String id) {
        this(id, null);
    }

    public ScrapeData(final String id, final byte[] image) {
        setId(id);
        setImage(image);
    }
}
