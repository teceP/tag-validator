# tag-validator
Validates whether attributes, which depends on other attributes, are present or not.

Example:
* `<a target="_blank"/>`  ... for security reasons, there should be rel="noopener norefferer"
* `<a target="_blank" rel="noopener norefferer"/>`

The tag-validator has a crawler inside, which looks for all avaiable URLs on a website.
The depth and the amount of pages is adjustable with arguments.

# Example ini file for noopener norefferer: 
```
[url]
url:https://www.a.de/home.html
[depending]
depend_Tag:a
depend_Attr:target
depend_Val:_blank
[testing]
testing_Attr:rel
testing_Val:"noopener norefferer"
[max]
maxPages:500
maxDepth:3
[crawlFor]
looking_Tag:a
looking_Attr:href
```

# ini-file Explanation
---------------------------------
* url: Start URL for the crawler
---------------------------------
* depend_Tag: When this tag was found by the validator && depend_Attr && depend_Val
* depend_Attr: When this attribute was found by the validator && depend_Tag && depend_Val
* depend_Val: When this value was found by the validator && depend_Tag && depend_Attr <br />
---------------------------------
= then the validator will check if following two arguments are present:
---------------------------------
* testing_Attr: Validator looks if this attribute is present or not.
* testing_Val: Validator looks if this value is present or not.
---------------------------------
* maxPages: Crawler page limit 
---------------------------------
* maxDepth: Crawler url depth limit<br />
- Example for depth == 1:<br />
https:www.url.com/europe/article_0.html
https:www.url.com/europe/article_1.html
https:www.url.com/europe/article_2.html

If its not neccesary to get all pages when there are several document with the same type,
in this example 'europe', the crawler will remove all other articles about europe and keep
only article_0.<br />

To get all pages and keep all articles of the same type, use -1 for maxDepth.

---------------------------------
Should be everytime `a` and `href`. This is the pair of where the crawler is looking for on a page, 
to get other urls.
* looking_Tag:a
* looking_Attr:href
---------------------------------

# Build program with maven
Run following in root directory
`mvn clean compile assembly:single`

validator-1.0-jar-with-dependencies.jar gets created in target directory

# Run program
You can run this program as follows:<br />
* Change the arguments.ini file according to your needs
* Navigate with your terminal to the directory where the arguments.ini and the validator-1.0-jar-with-dependencies.jar are
* Run following command:
`java -jar validator-1.0-jar-with-dependencies.jar arguments.ini`