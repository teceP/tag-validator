#tag-validator
Validates whether attributes, which depends on other attributes, are present or not.

Example:
* `<a target="_blank"/>`  ... for security reasons, there should be rel="noopener norefferer"
* `<a target="_blank" rel="noopener norefferer"/>`

The tag-validator has a crawler inside, which looks for all avaiable URLs on a website.
The depth and the amount of pages is adjustable with arguments.

#Example ini file for noopener norefferer: 
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

#Build program with maven
Run following in root directory
`mvn clean compile assembly:single`

validator-1.0-jar-with-dependencies.jar gets created in target directory

#Run program
You can run this program as follows:<br />
* Change the arguments.ini file according to your needs
* Navigate with your terminal to the directory where the arguments.ini and the validator-1.0-jar-with-dependencies.jar are
* Run following command:
`java -jar validator-1.0-jar-with-dependencies.jar arguments.ini`