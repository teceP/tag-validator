package de.mario.teklic.validator;

import com.google.gson.Gson;
import de.mario.teklic.model.Data;
import de.mario.teklic.model.Result;
import de.mario.teklic.ssl.SSLHelper;
import org.ini4j.Profile;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.mario.teklic.arguments.Arguments.*;

public class Validator {

    private List<String> urls;

    private String dependingTag, dependingAttr, dependingVal;

    private String testingAttr, testingVal;

    public Validator(Profile.Section arguments){
        this.dependingTag = arguments.get(DEPEND_TAG);
        this.dependingAttr = arguments.get(DEPEND_ATTR);
        this.dependingVal = arguments.get(DEPEND_VAL);
    }

    public void init(List<String> urls) {
        this.urls = urls;
    }

    public Result validate(Profile.Section testingArgs) {
        this.testingAttr = testingArgs.get(TESTING_ATTR);
        this.testingVal = testingArgs.get(TESTING_VAL);
        int line = 50;
        int count = 0;
        int errorCount = 0;
        List<Data> dataList = new ArrayList<>();

        if (urls != null || urls.size() > 0) {
            System.out.println("Searching for attr '" + testingAttr + "' with val '" + testingVal + "'. URL List size: " + (urls.size() + 1) + ".");

            for (String url : urls) {
                try {
                    if (count == line) {
                        line += 50;
                        this.message(url, dataList.size(), errorCount, count);
                    }
                    count++;

                    Document doc = SSLHelper.getConnection(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();

                    /**
                     * Find tags
                     */
                    Elements targetBlank = doc.select(dependingTag + "[" + dependingAttr + "=" + dependingVal + "]");
                    Elements cases = new Elements();
                    for (Element target : targetBlank) {
                        if (!target.toString().contains(testingAttr + "=\"" + testingVal + "\"")) {
                            cases.add(target);
                        }
                    }

                    /**
                     * Nicht alle oder keiner hat val
                     */
                    if (cases.size() <= targetBlank.size()) {
                        dataList.add(new Data(url, cases.size()));
                    }
                } catch (IOException e) {
                    System.err.println("Error connecting url: " + url);
                    errorCount++;
                }
            }
        } else {
            System.out.println("No urls.");
        }
        return new Result(dataList, errorCount);
    }

    public void safeFile(Result result) throws IOException {
        FileWriter file = new FileWriter("cases_found_" + result.getDataList().size() + ".json");
        file.write(new Gson().toJson(result));
        file.flush();
        file.close();
    }

    public void message(String currentUrl, int cases, int errorCount, int count) {
        System.out.println(count + " urls checked. " + (cases - errorCount) + " cases found. " + ((urls.size()) - count) + " left...");
        System.out.println("Next url: " + currentUrl);
    }
}