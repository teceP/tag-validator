package de.mario.teklic;

import de.mario.teklic.crawler.Crawler;
import de.mario.teklic.validator.Validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class App {
    public static void main(String[] args) throws MalformedURLException {

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


        System.out.println("URL: " + args[0]);
        String domain = new URL(args[0]).getHost();
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

        System.out.println("Crawling...");
        Crawler crawler = new Crawler(args[0], domain, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        List<String> urls = crawler.start();

        System.out.println("Found '" + (urls.size() + 1) + "' pages.");

        Validator validator = new Validator();
        validator.init(urls);
        System.out.println("Validating...");
        validator.validate(args[1], args[2]);

        System.out.println("Finished.");

    }

    public static void printHelp(){
        System.out.println("todo help menu");
    }
}
