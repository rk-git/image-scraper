FROM tomcat:latest
ADD target/scraper-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/scraper
EXPOSE 8080
CMD ["catalina.sh", "run"]
