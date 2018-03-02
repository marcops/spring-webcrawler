# WebCrawler

This project is a Proof of Concept. Use as you wish.
The main objective with this project was to show my experience and skills about software engineering. I have developed having in mind the best write-code practices i've acquired on my career

## Prerequisites

Requirements:
 - Java 8
 - Gradle Build Tools

## Building 
Follow the steps as below:

```sh
$ gradle clean build
```

## Running 
```sh
$ java -jar spring-webcrawler.jar http://www.example.com
```

These instrutions should give you a sitemap.json, that contains a list of links whose content is explained below: 

| Key | Description |
| ------ | ------ |
| url | url navigated/read |
| title | title of page navigated |
| lastModified | last time the page was modified/updated  |
| childrens | list with all links who are inside the URL read  |

### sitemap.json example
```json 
[  
   {  
      "url":"http://example.com/",
      "title":"Example Domain",
      "lastModified":"Fri, 09 Aug 2013 23:54:35 GMT",
      "childrens":[  
         "http://www.iana.org/domains/example"
      ]
   }
]
```

# What I can do if I had more time!

* I can change HashMap to REDIS, for use distributed.
* Sharpen the unit tests 


License
----
Choose one

**Free Software, Hell Yeah!**
