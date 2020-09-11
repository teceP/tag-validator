package de.mario.teklic.validator;

import com.google.gson.Gson;
import de.mario.teklic.model.Data;
import de.mario.teklic.ssl.SSLHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    private List<String> urls;

    public Validator() {
    }

    public void init(List<String> urls) {
        this.urls = urls;
    }


    /**
     * wenn es in einem doc. x target="_blank" gibt, muss es auch x noopener & x norefferer geben
     */

    public void validate(String attr, String val) {
        int line = 50;
        int count = 0;
        int errorCount = 0;

        if (urls != null || urls.size() > 0) {
            System.out.println("Searching for attr '" + attr + "' with val '" + val + "'. URL List size: " + (urls.size()+1) + ".");
            List<Data> dataList = new ArrayList<>();
            try {
                for (String url : urls) {
                    if(count == line){
                        line += 50;
                        System.out.println(count + " urls checked. " + (dataList.size() - errorCount) + " cases found. " + ((urls.size()) - count) + " left...");
                        System.out.println("Next url: " + url);
                    }
                    count++;

                    Document doc = SSLHelper.getConnection(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();

                    /**
                     * _blank
                     */
                    Elements targetBlank = doc.select("a[target=_blank]");
                    Elements noOpNoRef = new Elements();
                    for (Element target : targetBlank) {
                        if (!target.toString().contains(attr + "=\"" + val + "\"")) {
                            noOpNoRef.add(target);
                        }
                    }

                    /**
                     * Nicht alle oder keiner hat noopener, norefferer
                     */
                    if (noOpNoRef.size() <= targetBlank.size()) {
                        dataList.add(new Data(url, attr + "." + val + " found: " + (noOpNoRef.size() + 1)));
                    }
                }

                if (dataList.size() > 0) {
                    System.out.println("Saving data to json file... " + (dataList.size() - errorCount) + " cases found. " + errorCount + " errors while connecting to the urls.");
                    this.safeFile(dataList);
                }else{
                    System.out.println("No cases found!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                errorCount++;
                dataList.add(new Data("Exception: ", e.getMessage()));
            }
        } else {
            System.out.println("No urls.");
        }
    }

    public void safeFile(List<Data> dataList) throws IOException {
        FileWriter file = new FileWriter("_cases_found_test" + ".json");
        file.write(new Gson().toJson(dataList));
        file.flush();
        file.close();
    }
}