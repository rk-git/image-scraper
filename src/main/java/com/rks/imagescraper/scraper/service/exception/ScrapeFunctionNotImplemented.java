package com.rks.imagescraper.scraper.service.exception;

public class ScrapeFunctionNotImplemented extends ScrapeException {
    public ScrapeFunctionNotImplemented(final String thisFunction) {
        super("Scpare function not implemented: <" + thisFunction + ">");
    }
}
