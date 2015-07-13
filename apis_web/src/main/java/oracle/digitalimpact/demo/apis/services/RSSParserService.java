package oracle.digitalimpact.demo.apis.services;

import java.io.InputStream;

import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.FeedMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RSSParserService implements Constants {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private transient Log logger = LogFactory.getLog(this.getClass());

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    private List<String> rssFeeds = new ArrayList<String>();
    
    // Usually this can be a field rather than a method variable
    private static final Random rand = new Random();

    private static List<FeedMessage> feedMessages = new ArrayList <FeedMessage> ();


    public void addFeedMessage(FeedMessage feedMessage) {
        RSSParserService.feedMessages.add (feedMessage);
    }

    public List<FeedMessage> getFeedMessages() {
        return feedMessages;
    }

    public void addRssFeed(String rssFeed) {
        this.rssFeeds.add(rssFeed);
    }

    public void setRssFeeds(List<String> rssFeeds) {
        this.rssFeeds = rssFeeds;
    }

    public List<String> getRssFeeds() {
        return rssFeeds;
    }

    public void init() throws APIException {

        logger.info("Entered");
        try {
            for (String rssFeed : rssFeeds) {
                URL feedUrl = new URL(rssFeed);
                String description = "";
                String title = "";
                String link = "";
                String language = "";
                String copyright = "";
                String author = "";
                String pubdate = "";
                String guid = "";
                // First create a new XMLInputFactory
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = feedUrl.openStream();
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        String localPart = event.asStartElement().getName().getLocalPart();
                        if (localPart.equalsIgnoreCase(ITEM)) {
                            event = eventReader.nextEvent();
                        } else if (localPart.equalsIgnoreCase(TITLE)) {
                            title = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(DESCRIPTION)) {
                           description  = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(LINK)) {
                           link  = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(GUID)) {
                           guid  = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(LANGUAGE)) {
                           language = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(AUTHOR)) {
                           author  = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(PUB_DATE)) {
                           pubdate = getCharacterData(event, eventReader);
                        }
                        else if (localPart.equalsIgnoreCase(COPYRIGHT)) {
                           copyright = getCharacterData(event, eventReader);
                        }

                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart().equalsIgnoreCase(ITEM)) {
                            if (title.contains ("<") || description.contains("<")) {
                                continue;
                            }
                            FeedMessage message = new FeedMessage();
                            message.setAuthor(author);
                            if (description.equals("<")) {
                                description = "";
                            }
                            message.setDescription(description);
                            message.setGuid(guid);
                            message.setLink(link);
                            message.setTitle(title);
                            
                            if (pubdate != null) {
                                DateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                                Date date = parser.parse(pubdate);
                                DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
                                pubdate = formatter.format(date);
                            }
                            message.setPubDate(pubdate);
                            addFeedMessage(message);
                            event = eventReader.nextEvent();
                            continue;
                        }
                    }
                }
            }
        } catch (Exception expGeneral) {
            throw new APIException("Exception instantiating " + this, expGeneral);
        }
        logger.info("Leaving");

    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
    
    public List <FeedMessage> getRandomMessages (int count) {
        List<FeedMessage> randomMessages = new ArrayList <FeedMessage> ();
        for (int i = 0; i < count; i++) {
            int index = randomInt(0, (getFeedMessages().size()-1));
            randomMessages.add (getFeedMessages().get(index));
        }
        return randomMessages;
    }

    public static void main(String a[]) throws Exception {
        RSSParserService service = new RSSParserService();
        service.addRssFeed("http://feeds.delicious.com/v2/rss/OracleTechnologyNetwork/otnheadlines");
        service.addRssFeed("http://blogs.oracle.com/partnernews/feed/entries/rss?cat=Middleware");
        service.init();
        System.out.println (service.getRandomMessages(3));
        
    }
    
    private int randomInt(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
