package com.rks.imagescraper.scraper.service.impl;

/*
 * This is the default implementation of the Image Scraper Services prototype and
 * that performs the task of managing scraping tasks, running them,  persisting and retrieving
 * the results.
 *
 * @rks
 * @version 1.0
 * @since 1.0
 * @see IScrapeService
 */
import com.rks.imagescraper.scraper.service.IScrapeService;
import com.rks.imagescraper.scraper.service.model.ScrapeData;
import com.rks.imagescraper.scraper.service.model.ScrapeJob;
import com.rks.imagescraper.scraper.service.persistence.IScrapeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service public class ScrapeServiceImpl implements IScrapeService {

    private IScrapeDao scrapeDb;
    public IScrapeDao getScrapeDb() {
        return scrapeDb;
    }

    @Autowired public void setScrapeDb(final IScrapeDao scrapeDb) {
        this.scrapeDb = scrapeDb;
    }

    @Override public ScrapeJob createScrapeJob(final String url)  {
        ScrapeJob job = new ScrapeJob(null, url);
        getScrapeDb().save(job);
        return job;
    }

    @Override public List<ScrapeJob> getScrapeJobs() {
        return getScrapeDb().findAll();
    }

    public void deleteScrapeJob(final String id)  {
         getScrapeDb().deleteById(id);
    }

    @Override public ScrapeData getScrapeResults(final String id)  {
        final Optional<ScrapeJob> job = getScrapeDb().findById(id);
        ScrapeJob theJob = job.orElse(null);
        return theJob == null ? null : new ScrapeData(theJob.getId(), theJob.getImage());
    }

    @PostConstruct public void initialize() {
        ScrapeJob[] jobs = new ScrapeJob[]{
                new ScrapeJob(null, "http://cnn.com"),
                new ScrapeJob(null, "https://www.cisco.com"),
                new ScrapeJob(null, "https://tomcat.org")
        };

        for (final ScrapeJob job : jobs) {
                getScrapeDb().save(job);
        }
    }
}
