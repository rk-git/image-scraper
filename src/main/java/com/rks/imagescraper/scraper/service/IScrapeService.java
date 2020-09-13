package com.rks.imagescraper.scraper.service;

import com.rks.imagescraper.scraper.service.exception.ScrapeException;
import com.rks.imagescraper.scraper.service.model.ScrapeData;
import com.rks.imagescraper.scraper.service.model.ScrapeJob;

import java.net.URL;
import java.util.List;

/*
 * This interface establishes the behaviour of a Image Scraper Services
 * that performs the task of managing scraping tasks, running them,  persisting and retrieving
 * the results.
 *
 * @rks
 * @version 1.0
 * @since 1.0
 */
public interface IScrapeService {
    /*
     * Create a new scraper job for the specified URL
     *
     * @param url - the url which is to be scraped
     * @returns the scrape job - an exisiting job of the url already exists
     * @exception - if the url is invalid
     */
    public ScrapeJob createScrapeJob(final String url) throws ScrapeException;

    /*
     * Get all  scraper jobs
     *
     * @param None
     * @returns  - The list of scrape jobs
     */
    public List<ScrapeJob> getScrapeJobs();

    /*
     * Delete the specified scraper job
     *
     * @param id - the id of the existing scraper job
     * @returns  - None
     * @exception - if the id does not exist
     */
    public void deleteScrapeJob(final String id) throws ScrapeException;

    /*
     * Get the specified scraper job results, given the job id
     *
     * @param id - the id of the existing scraper job
     * @returns  - The scrape job results if its complete - null if the job is still underway
     * @exception - if the id does not exist
     */
    public ScrapeData getScrapeResults(final String id) ;

    /*
     * Get the specified scraper job results, given the scrape url
     *
     * @param id - the id of the existing scraper job
     * @returns  - The scrape job results if its complete - null if the job is still underway
     * @exception - if the id does not exist
     */
    default ScrapeJob getScrapeResults(final URL url) throws ScrapeException {
        throw new ScrapeException("URL Lookup");
    }
}
