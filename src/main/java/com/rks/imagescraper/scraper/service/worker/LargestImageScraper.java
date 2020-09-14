package com.rks.imagescraper.scraper.service.worker;

/*
 * A worker entity that crawls a base URL to identify IMG tags
 * and find the largest image found on the site.
 *
 * @author rks
 * @version 1.0
 * @since 1.0
 */
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.rks.imagescraper.scraper.service.model.ScrapeJob;
import com.rks.imagescraper.scraper.service.persistence.IScrapeDao;
import org.apache.commons.io.IOUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LargestImageScraper implements Runnable {

    private ScrapeJob job;
    public ScrapeJob getJob() {
        return job;
    }
    public void setJob(ScrapeJob job) {
        this.job = job;
    }

    private IScrapeDao dao;
    public IScrapeDao getDao() {
        return dao;
    }
    public void setDao(IScrapeDao dao) {
        this.dao = dao;
    }

    // The URL from which to scrape the largest image
    public String getScrapeUrl() {
        return getJob() == null ? null:getJob().getUrl();
    }

    public LargestImageScraper() {
        this(null, null);
    }

    public LargestImageScraper(final ScrapeJob job, IScrapeDao dao) {
        setJob(job);
        setDao(dao);
    }

    /**
     * Scan the given URL for images and persist the largest image found so far
     *
     * @return void
     */
    public void scan_images() {
        System.out.println("Start background task: " + Thread.currentThread().getName() + Thread.currentThread().getId() + " "
                + (Thread.currentThread().isDaemon() ? "isDaemon":"isNotDaemon"));

        /*
         * Update the job status in DB
         */
        getJob().setStatus(ScrapeJob.Status.statusSTARTED);
        getDao().save(getJob());

        byte[] largest_image = null;
        try {
            Document document =
                    Jsoup
                            .connect(getScrapeUrl())
                            .ignoreHttpErrors(true)
                            .timeout(10 * 1000)
                            .get();

            Elements elements = document.select("img[src]");

            for(Element e : elements){
                System.out.println("Absolute URL: " + e.absUrl("src"));
                final String image_url = e.absUrl("src");
                byte[] this_image = downloadImage(image_url);
                if (largest_image == null) {
                    largest_image = this_image;
                }
                else if (largest_image.length < this_image.length) {
                    largest_image = this_image;
                }

                /*
                 * Scanner found a new candidate largest image - persist it
                 */
                saveCandidateImage(largest_image);
            }
            /*
             * Update the job completion status and end time in DB
             */
            getJob().setStatus(ScrapeJob.Status.statusCOMPLETED);
            getJob().setEndTime(new Date().toString());
            getDao().save(getJob());
        } catch (IOException e) {
            e.printStackTrace();
            getJob().setStatus(ScrapeJob.Status.statusABORTED);
            getJob().setEndTime(new Date().toString());
            getDao().save(getJob());
        } finally {
            //
            // TODO: MEMORY LEAK : Clean up the bgScanTask from roster of bg tasks
        }

        return;
    }

    /*
     * Persist the specified image as a result for the given scrape job
     */
    private void saveCandidateImage(final byte[] largest_image) {
       getJob().setImage(largest_image);
       getDao().save(getJob());
    }


    /*
     * Down the image from the specified URL
     *
     * @param image_utl - URL from which to download the image
     */
    private byte[]  downloadImage(final String image_url) {
        byte[] image_data = null;
        try {
            final URL theUrl =new URL(image_url);
            InputStream inputStream = theUrl.openStream();
            image_data = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image_data;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        /* Start scanning */
        scan_images();
    }
}
