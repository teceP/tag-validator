package de.mario.teklic.crawler;

import org.ini4j.Profile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static de.mario.teklic.arguments.Arguments.*;

public class Crawler {

    private String domain;
    private String startLink;
    private String lookingForAllTag;
    private String lookingForAllAttr;
    private List<String> links;
    private int size = 50;
    private int max;
    private int depth;

    public Crawler(Profile.Section urlArgs, Profile.Section maxArgs, Profile.Section lookingArgs) throws MalformedURLException {
        this.startLink = urlArgs.get(URL);
        this.lookingForAllTag = lookingArgs.get(LOOKING_FOR_TAG);
        this.lookingForAllAttr = lookingArgs.get(LOOKING_FOR_ATTR);
        this.domain = new URL(startLink).getHost();
        this.links = new ArrayList<>();
        this.max = Integer.parseInt(maxArgs.get(MAX_PAGES));
        this.depth = Integer.parseInt(maxArgs.get(MAX_DEPTH));
    }

    public List<String> start(){
        List<String> result = this.getPageLinks(startLink);
        return result.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getPageLinks(String URL) {
        if (links.size() == size) {
            System.out.println("Links found: " + size + System.lineSeparator() + "Last found link: " + links.get(links.size()-1));
            size += 50;
        }

        if (links.size() >= max && max != 0) {
            System.out.println("Maximum pages exceeded (" + max + ").");
            return links;
        }

        if (!links.contains(URL)) {
            try {
                if (URL.contains(domain)) {
                    links.add(URL);
                }
                Document document = null;
                try {
                    document = Jsoup.connect(URL).get();
                } catch (IllegalArgumentException ie) {
                    System.err.println(URL);
                }
                Elements linksOnPage = document.select(this.lookingForAllTag+"[" + this.lookingForAllAttr + "]");

                Predicate<Element> goodDomain = e -> e.attr("abs:" + this.lookingForAllAttr).contains(domain);
                Predicate<Element> noEmail = e -> !e.attr("abs:" + this.lookingForAllAttr).contains("@");
                Predicate<Element> noMP4 = e -> !e.attr("abs:" + this.lookingForAllAttr).contains(".mp4");
                Predicate<Element> noJPG = e -> !e.attr("abs:" + this.lookingForAllAttr).contains(".jpg");
                Predicate<Element> noPDF = e -> !e.attr("abs:" + this.lookingForAllAttr).contains(".pdf");
                Predicate<Element> noEN = e -> !e.attr("abs:" + this.lookingForAllAttr).contains("/EN/");
                Predicate<Element> noFR = e -> !e.attr("abs:" + this.lookingForAllAttr).contains("/FR/");

                List<String> filtered = linksOnPage
                        .stream()
                        .distinct()
                        .filter(goodDomain.and(noEmail.and(noMP4).and(noJPG).and(noPDF).and(noEN).and(noFR)))
                        .map(e -> e.attr("abs:" + this.lookingForAllAttr))
                        .collect(Collectors.toList());

                filtered = this.eliminateCovered(filtered, depth);

                for (String link : filtered) {
                    getPageLinks(link);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
        return links;
    }

    public List<String> eliminateCovered(List<String> newLinks, int depth) {
        int offset = this.getOffset(this.startLink);
        Iterator<String> itNewLinks = newLinks.iterator();
        String currentNewLink;

        while(itNewLinks.hasNext()){
            currentNewLink = itNewLinks.next();

            if(!this.domainMatch(currentNewLink)){
                itNewLinks.remove();
                break;
            }

            Iterator<String> itLinks = links.iterator();
            while(itLinks.hasNext()){
                String currentLink = itLinks.next();
                int len = this.getLen(currentLink, offset, depth);

                if(len == -1){
                    if(links.contains(currentLink)){
                        itNewLinks.remove();
                        break;
                    }else if(currentNewLink.regionMatches(true, offset, currentLink, offset, len)){
                        itNewLinks.remove();
                    }
                }
            }
        }
        return newLinks;
    }

    public boolean domainMatch(String newLink) {
        try {
            URL newUrl = new URL(newLink);

            if(newUrl.getHost().equals(domain)){
                return true;
            }
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * Length or index of the x occurance (according to depth) of a '/' from offset
     *
     * e.g. http://google.com/this/is/aworld
     *                offset|->   5
     *
     * @param link
     * @param offset
     * @param depth 0 .. only domain
     *              1, 2, 3 ... first, second, third, ... '/'
     *              -1 ... last '/'
     * @return -1 if no '/' in this depth
     */
    public int getLen(String link, int offset, int depth) {
        int occurance = 0;
        int indexAtLastOccurance = offset;
        int index = offset;

        try {
            while (occurance != depth) {
                if (link.charAt(index) == '/') {
                    occurance++;
                    indexAtLastOccurance = index;
                }
                index++;
            }
        } catch (StringIndexOutOfBoundsException sioobe) {
            //if max. depth
            if(depth == -1){
                return indexAtLastOccurance;
            }
            //No cut possible
            return -1;
        }
        return index;
    }

    public int getOffset(String startLink) {
        int occurance = 0;
        int index = 0;

        while (occurance != 3) {
            if (startLink.charAt(index) == '/') {
                occurance++;
            }
            index++;
        }
        return index;
    }
}