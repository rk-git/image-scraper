package com.rks.imagescraper.scraper.service.persistence;


import com.rks.imagescraper.scraper.service.exception.ScrapeException;
import com.rks.imagescraper.scraper.service.model.ScrapeJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/*
 * This interface establishes the behaviour of a Image Scraper DAO or
 * the persistence agent.
 * The persistence agent performs CRUD functions on a scraper:
 *   - retrieve all scraper jobs
 *   - retrieve result of a specific scraper job
 *   - create scraper job
 *   - delete scraper job
 *
 * Updates are not supported.

 *
 * @rks
 * @version 1.0
 * @since 1.0
 */
public interface IScrapeDao extends MongoRepository<ScrapeJob, String> {

    /*
     * Find if a job with given URL exists
     *
     * @param url - the scraper job URL
     * @returns the scrape job
     */
    public ScrapeJob getJobByUrl(final String url);
}
