package de.mario.teklic;

import de.mario.teklic.crawler.Crawler;
import de.mario.teklic.model.Result;
import de.mario.teklic.validator.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        try {
            args[0].length();
            args[1].length();
            args[2].length();
            args[3].length();
            args[4].length();
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            System.out.println("Arguments needed.");
            printHelp();
            System.exit(0);
        }

        String domain = new URL(args[0]).getHost();

        printArguments(args, domain);

        System.out.println("Crawling...");

        Crawler crawler = new Crawler(args[0], domain, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        List<String> urls = crawler.start();

        System.out.println("Found '" + (urls.size() + 1) + "' pages.");

        Validator validator = new Validator();
        validator.init(urls);

        System.out.println("Validating...");

        Result result = validator.validate(args[1], args[2]);

        System.out.println("Finished.");

        if (result != null && result.getDataList().size() > 0) {
            System.out.println("Safing results now ...");
            validator.safeFile(result);
            System.out.println("Safed.");
        }else if(result.getDataList().size() == 0){
            System.out.println("No cases where found!");
        }else{
            System.err.println("Error: result is null!");
        }
    }

    public static void printArguments(String[] args, String domain) {
        System.out.println("URL: " + args[0]);
        System.out.println("Domain: " + domain);
        System.out.println("Attribute: '" + args[1] + "'");
        System.out.println("Key: '" + args[2] + "'");

        if (Integer.parseInt(args[3]) == 0) {
            System.out.println("Max subsites: no limit");
        } else {
            System.out.println("Max subsites: " + args[3]);
        }
        if (Integer.parseInt(args[4]) == 0) {
            System.out.println("Max depth: no limit");
        } else {
            System.out.println("Max depth: " + args[4]);
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
 */

/**
 * TODO: tag, dependend attribute x value argument
 * ATM: only testing for a[targert="_blank"]
 */

/**
 * TODO: (searching for matches, searching for no matches argument)
 */