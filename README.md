# tag-validator

Validates if attributes which depends on other attributes, are present or not.

Example:

* \<a target="_blank"/>  ... for security reasons, there should be rel="noopener norefferer"
* \<a target="_blank" rel="noopener norefferer"/>

The tag-validator has a crawler inside, which looks for all avaiable URLs on a website.
The depth and the amount of pages is adjustable with arguments.

Example for noopener norefferer: 
* "https://www.a.url/thema/topic/hello" a target "_blank" rel "noopener norefferer" 250 3 href
* a = a-tag
* target = looking for attribute
* "_blank" = looking for value
* rel = search for this attribute
* "noopener norefferer" = search for this value
* 0 = max pages
* 3 = depth
* href = all items of href

At this time, the app only searches for a-tags and target attributes.
