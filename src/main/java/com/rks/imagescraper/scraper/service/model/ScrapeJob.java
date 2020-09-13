package com.rks.imagescraper.scraper.service.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "scrapejobs")
public class ScrapeJob {
    public enum Status {
        statusUNKNOWN,
        statusQUEUED,
        statusSTARTED,
        statusABORTED,
        statusCOMPLETED
    }

    @Id @JsonProperty private String id;
    @JsonProperty private String url;
    @JsonProperty private String startTime; // Since we use date only for display and not for processing, we can treat them as strings
    @JsonProperty private String endTime;   //
    @JsonProperty private Status status;
    @JsonIgnore private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void start() {
        setStatus(Status.statusSTARTED);
    }

    public void complete() {
        setStatus(Status.statusCOMPLETED);
    }


    public ScrapeJob() {
    }

    public ScrapeJob(final String id, final String url) {
        setId(id);
        setUrl(url);
        setStartTime(new Date().toString());
        this.status = Status.statusQUEUED; // Will be marked statusSTARTED when scan starts
    }

}
