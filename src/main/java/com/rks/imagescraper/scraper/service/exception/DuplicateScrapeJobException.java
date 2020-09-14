package com.rks.imagescraper.scraper.service.exception;

/*
 * This class models the exception used by denote an attempt to create a duplicate scan job definition.
 *
 * @rks
 * @version 1.0
 * @since 1.0
 */

public class DuplicateScrapeJobException extends ScrapeException {
    public DuplicateScrapeJobException(final String id) {
        super(id);
    }
}
