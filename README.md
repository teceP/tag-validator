# tag-validator

Validates if attributes which depends on other attributes, are present or not.

Example:

<a target="_blank">   : for security reasons, there should be rel="noopener norefferer"
<a target="_blank" rel="noopener norefferer"/>

The tag-validator has a crawler inside, which looks for all avaiable URLs on a website.
The depth and the amount of pages is adjustable with arguments.

Example: 
java -jar validator-0.9.jar "https://awebsite.com" rel "noopener norefferer" 0 3
rel = search for this attribute
"noopener norefferer" = search for this value
0 = max pages
3 = depth


At this time, the app only searches for a-tags and target attributes.
