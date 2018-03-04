# WebCrawler

This is a simple webcrawler using springboot with async process and will generate a sitemap in json format.

> This is a Proof of Concept project. Use as you wish. 
> The main goal of this project is to demonstrate my experience and skills in software engineering. I have developed with the best write-code practices I've acquired on my career.


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
* More comments (Normally, I prefer a verbose code than comments)
* Domain parameter can be moved to be a property in application.yml


License
----
Choose one

**Free Software, Hell Yeah!**
