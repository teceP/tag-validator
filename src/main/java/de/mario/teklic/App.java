package de.mario.teklic;

import de.mario.teklic.crawler.Crawler;
import de.mario.teklic.model.Result;
import de.mario.teklic.validator.Validator;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static de.mario.teklic.arguments.Arguments.*;
import static de.mario.teklic.arguments.Profiles.*;

public class App {
    public static void main(String[] args) throws IOException {
        Ini config = new Ini(new File(args[0]));

        Profile.Section urlArgs = config.get(URL_PROFILE);
        Profile.Section dependingArgs = config.get(DEPENDING_PROFILE);
        Profile.Section testingArgs = config.get(TESTING_PROFILE);
        Profile.Section maxArgs = config.get(MAX_PROFILE);
        Profile.Section crawlForArgs = config.get(CRAWL_PROFILE);

        printArguments(urlArgs, dependingArgs, testingArgs, maxArgs, crawlForArgs);

        System.out.println("Crawling...");

        Crawler crawler = new Crawler(urlArgs, maxArgs, crawlForArgs);
        List<String> urls = crawler.start();

        System.out.println("Found '" + (urls.size() + 1) + "' pages.");

        Validator validator = new Validator(dependingArgs);
        validator.init(urls);

        System.out.println("Validating...");

        Result result = validator.validate(testingArgs);

        System.out.println("Finished.");

        if (result != null && result.getDataList().size() > 0) {
            System.out.println("Safing results now ...");
            validator.safeFile(result);
            System.out.println("Safed.");
        } else if (result.getDataList().size() == 0) {
            System.out.println("No cases where found!");
        } else {
            System.err.println("Error: result is null!");
        }
    }

    public static void printArguments(Profile.Section urlArgs, Profile.Section dependingArgs, Profile.Section testingArgs, Profile.Section maxArgs, Profile.Section crawlForArgs) {
        try {
            System.out.println("URL: " + urlArgs.get(URL));
            System.out.println("Domain: " + (new URL(urlArgs.get(URL)).getHost()));
            System.out.println("Depending Tag: " + dependingArgs.get(DEPEND_TAG));
            System.out.println("Depending Attribute: " + dependingArgs.get(DEPEND_ATTR));
            System.out.println("Depending Value: " + dependingArgs.get(DEPEND_VAL));
            System.out.println("Testing Attribute: " + testingArgs.get(TESTING_ATTR));
            System.out.println("Testing Value: " + testingArgs.get(TESTING_VAL));
            System.out.println("Max. searching depth: " + Integer.parseInt(maxArgs.get(MAX_DEPTH)));
            System.out.println("Max. pages: " + Integer.parseInt(maxArgs.get(MAX_PAGES)));
            System.out.println("Looking for all tags: " + crawlForArgs.get(LOOKING_FOR_TAG));
            System.out.println("Looking for all attributes: " + crawlForArgs.get(LOOKING_FOR_ATTR));
        } catch (MalformedURLException e) {
            if(urlArgs.get(URL) == null){
                System.out.println("URL is null.");
                e.printStackTrace();
            }else{
                System.err.println("URL '" + urlArgs.get(URL) + "' is not a valid url.");
                e.printStackTrace();
                System.exit(0);
            }
        } catch (Exception e){
            System.err.println("Arguments needed.");
            printHelp();
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void printHelp() {
        System.out.println("todo help menu");
    }
}

/**
 * TODO: depth of path only deeper-argument:
 * .../this/is/a/path.html
 * The crawler only searches for links which are deeper or on the same level
 * but will never go higher like:
 * .../this/is/a.html or
 * .../this/is.html
 * <p>
 * TODO: (searching for matches, searching for no matches argument)
 * <p>
 * <p>
 * TODO: Help Menu
 */

/**
 * TODO: (searching for matches, searching for no matches argument)
 */

/**
 *
 * TODO: Help Menu
 */