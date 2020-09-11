package de.mario.teklic;

import de.mario.teklic.crawler.Crawler;
import de.mario.teklic.model.Result;
import de.mario.teklic.validator.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        String domain = "";
        String url = "";
        String depend_Tag = "";
        String depend_Attr = "";
        String depend_Val = "";
        String testing_Attr = "";
        String testing_Val = "";
        int maxPages = -2;
        int maxDepth = -2;

        try {

            /**
             * Comments are examples for orientation.
             */

            //Start URL
            url = args[0];
            System.out.println("URL: " + url);
            domain = new URL(url).getHost();
            System.out.println("Domain: " + domain);

            //Depending on
            //tag - a, div, img, ...
            depend_Tag = args[1];
            System.out.println("Depending Tag: " + depend_Tag);

            //attr title, target, ...
            depend_Attr = args[2];
            System.out.println("Depending Attribute: " + depend_Attr);

            //val
            depend_Val = args[3];
            System.out.println("Depending Value: " + depend_Val);

            //Testing for
            //attr rel
            testing_Attr = args[4];
            System.out.println("Testing Attribute: " + testing_Attr);

            //val "noopener norefferer
            testing_Val = args[5];
            System.out.println("Testing Value: " + testing_Val);

            //Max. pages
            maxPages = Integer.parseInt(args[6]);
            //Max. depth
            maxDepth = Integer.parseInt(args[7]);

            if(maxDepth == -2){
                System.err.println("WARNING: No max depth argument found!" + System.lineSeparator() + "Default values: max Depth '3'.");
                maxDepth = 3;
            }
            System.out.println("Max. searching depth: " + maxDepth);

            if(maxPages == -2){
                System.err.println("WARNING: No max depth argument found!" + System.lineSeparator() + "Default values: max Pages '500'.");
                maxDepth = 500;
            }
            System.out.println("Max. pages: " + maxPages);
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            System.out.println("Arguments needed.");
            printHelp();
            System.exit(0);
        }

        //printArguments(args, domain);

        System.out.println("Crawling...");

        Crawler crawler = new Crawler(url, domain, maxPages, maxDepth);
        List<String> urls = crawler.start();

        System.out.println("Found '" + (urls.size() + 1) + "' pages.");

        Validator validator = new Validator(depend_Tag, depend_Attr, depend_Val);
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