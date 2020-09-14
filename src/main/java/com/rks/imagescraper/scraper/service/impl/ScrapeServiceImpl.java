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
import com.rks.imagescraper.scraper.service.exception.DuplicateScrapeJobException;
import com.rks.imagescraper.scraper.service.exception.ScrapeException;
import com.rks.imagescraper.scraper.service.model.ScrapeData;
import com.rks.imagescraper.scraper.service.model.ScrapeJob;
import com.rks.imagescraper.scraper.service.persistence.IScrapeDao;
import com.rks.imagescraper.scraper.service.worker.LargestImageScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service public class ScrapeServiceImpl implements IScrapeService {

    private IScrapeDao scrapeDb;
    public IScrapeDao getScrapeDb() {
        return scrapeDb;
    }
    @Autowired public void setScrapeDb(final IScrapeDao scrapeDb) {
        this.scrapeDb = scrapeDb;
    }

    /*
     * Maintain a roster of scan jobs so we can clean up when a job is deleted
     */
    Map<String, CompletableFuture<Void>> bgJobs;
    public Map<String, CompletableFuture<Void>> getBgJobs() {
        return bgJobs;
    }
    public void setBgJobs(Map<String, CompletableFuture<Void>> bgJobs) {
        this.bgJobs = bgJobs;
    }



    /*
     * Following section comprises implementation opf IScrapeService interface
     */
    @Override public ScrapeJob createScrapeJob(final String url) throws ScrapeException {
        final String decoded_url = uudecode(url);
        ScrapeJob job = getJobByUrl(decoded_url);
        if (job != null) {
            System.out.println("Found duplicate job: " + job.getId() + ", " + job.getUrl());
            throw new DuplicateScrapeJobException(decoded_url);
        }
        job = new ScrapeJob(null, decoded_url);
        getScrapeDb().save(job);

        /*
         * Start scanning the specified URL
         */
        System.out.println("[Scrape service: start completable future]");
        startScanning(job);
        return job;
    }

    /*
     * Start scanning the specified URL and update the status in DB
     */
    private void startScanning(final ScrapeJob job) {
        /*
         * Start a completable future task that will scan the site
         * and update the database
         */
        Runnable bgScan = new LargestImageScraper( job, getScrapeDb());
        CompletableFuture<Void> bgScanTask = CompletableFuture.runAsync(bgScan);
        final ScrapeJob updatedJob = getJobByUrl(job.getUrl());
        getBgJobs().put(updatedJob.getId(), bgScanTask);
    }

    /*
     * List all available scrape jobs - pending or completed
     */
    @Override public List<ScrapeJob> getScrapeJobs() {
        List<ScrapeJob> jobs = getScrapeDb().findAll();
        // For each of display, display un-uuencoded URLs
        //
        return jobs.stream().map(job -> uudecode(job)).collect(Collectors.toList());
    }

    /*
     * Delete specified  scrape job - pending or completed
     */
    @Override public void deleteScrapeJob(final String id)  {
        // Clean up any pending bg task
        CompletableFuture<Void>  bgTask = getBgJobs().get(id);
        if (bgTask != null) {
            if (!bgTask.isDone() && !bgTask.isCancelled()) {
                bgTask.cancel(true);
            }
            getBgJobs().remove(id);
        }

        // Removed all loose ends - clean up now
        getScrapeDb().deleteById(id);
    }

    /*
     * List all available scrape jobs - pending or completed
     */
    public ScrapeJob getJobByUrl(final String url) {
        for ( ScrapeJob nextJob : getScrapeJobs()) {
            nextJob = uudecode(nextJob);
            if (url.compareTo(nextJob.getUrl()) == 0) {
                // Loose comparison - will match iff and only iff the hostname and path are exactly the same
                return nextJob;
            }
        }

        return null;
    }

    /*
     * Fetch tge current result of the specified  scrape job - pending or completed
     */
    @Override public ScrapeData getScrapeResults(final String id)  {
        final Optional<ScrapeJob> job = getScrapeDb().findById(id);
        ScrapeJob theJob = job.orElse(null);
        return theJob == null ? null : new ScrapeData(theJob.getId(), theJob.getImage());
    }

    @PostConstruct public void initialize() {
        System.out.println("[Scrape service: Initializing...]");
        setBgJobs(new ConcurrentHashMap<>());
    }

    // Undecode URL, if its uuencoded - fopr ease of viewing
    private static String uudecode(String url) {
        if (url != null && url.trim().length() > 0)
            return UriUtils.decode(url, "UTF-8");

        return null;
    }

    // Undecode URL, if its uuencoded - fopr ease of viewing
    private static ScrapeJob uudecode(ScrapeJob job) {
        if (job != null && job.getUrl() != null)
            job.setUrl(uudecode(job.getUrl()));

        return job;
    }

}
