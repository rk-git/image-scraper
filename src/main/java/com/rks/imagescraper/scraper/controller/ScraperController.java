package com.rks.imagescraper.scraper.controller;

/*
 * This entity is the MVC controller that implements the REST API FOR
 * the Image scraping service.  It implements the CRUD REST end points for the
 * Image Scraper resource.
 *
 * @rks
 * @version 1.0
 * @since 1.0
 */
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import com.rks.imagescraper.scraper.service.IScrapeService;
import com.rks.imagescraper.scraper.service.exception.DuplicateScrapeJobException;
import com.rks.imagescraper.scraper.service.exception.ScrapeException;
import com.rks.imagescraper.scraper.service.model.ScrapeData;
import com.rks.imagescraper.scraper.service.model.ScrapeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scraper")
public class ScraperController {

    private IScrapeService scrapeService;
    public IScrapeService getScrapeService() {
        return scrapeService;
    }

    @Autowired public void setScrapeService(IScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @GetMapping(path = "/jobs", produces = "application/json")
    @ResponseBody
    public List<ScrapeJob> listAllScrapeJobs() {
        final List<ScrapeJob> jobs = getScrapeService().getScrapeJobs();
        return jobs == null ? new ArrayList<>():jobs;
    }

    @GetMapping(path = "/job/{id}", produces = "application/json")
    @ResponseBody
    public ScrapeData showScrapeData(@PathVariable("id") final String jobId) {
            return getScrapeService().getScrapeResults(jobId);
    }

    @PostMapping(path = "/job", consumes = "application/json", produces = "application/json")
    public ScrapeJob createScrapeJob(@RequestBody String url) throws ScrapeException {
          // if prefix url= is present, strip it off
          if (url.trim().startsWith(("url=")))
              url = url.substring("url=".length());
          return getScrapeService().createScrapeJob(url);
    }

    @DeleteMapping(path = "/job/{id}", produces = "application/json")
    @ResponseBody
    public void deleteJob(@PathVariable("id") final String jobId) {
        try {
             getScrapeService().deleteScrapeJob(jobId);
        } catch (ScrapeException e) {
            /*
             * Add exception handler and send an apt 400.html to REST client
             */
            e.printStackTrace();
        }
    }

    /*
     * Error handler methods
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Specified Scrape Job exists (per URL)")
    void onDuplicateScrapeUrl(final DuplicateScrapeJobException exception) {}

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Specified Scrape URL is invalid")
    void onBadUrlEception(final MalformedURLException exception) {}
}