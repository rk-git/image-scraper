package com.rks.imagescraper.scraper.service.exception;

/*
 * This interface models the exception used by scraper service to denite
 * an error to the end user interface. The message contains the error.
 *
 * @rks
 * @version 1.0
 * @since 1.0
 */
public class ScrapeException extends Exception {
    public ScrapeException(final String msg) {
        super(msg);
    }
}
