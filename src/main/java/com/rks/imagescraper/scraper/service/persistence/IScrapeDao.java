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
     * Save a new scraper job for the specified URL
     *
     * @param scraper job - the scraper job
     * @returns the scrape job id -
     *  @exception - if the save fails
     */
    //public Long save(final ScrapeJob job) throws ScrapeException;

    /*
     * Retrieve all  scraper jobs
     *
     * @param None
     * @returns the list of scrape jobs
     *
     */
    //public List<ScrapeJob> list() ;

    /*
     * Retrieve specified  scraper job with result
     *
     * @param job id
     * @returns the full result
     *
     */
    //public ScrapeJob get(final String id) ;


    /*
     * Delete specified  scraper job
     *
     * @param id
     * @returns None
     * @exception if it fails to delete or if the job does not exist
     *
     */
    //public void delete(final String jobId) throws ScrapeException;
}
