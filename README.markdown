logviewer
=========

An 'Atmosphere' based log viewer for simulating a Unix tail command in a browser.

Sometime ago I have asked a [question](http://stackoverflow.com/questions/5803776/java-web-application-that-can-stream-the-content-of-an-arbitrary-file-to-the-brow) on Stackoverflow about the existence of a web application that could be used to tail on an arbitrary log file located on the server.

I wanted the application to be deployable on any recent Java application server (Tomcat 6, Jetty, Weblogic 10.x) and also to have some kind of server push (Websocket, Comet) to avoid having to refresh the page.

At the time of writing only one person replied and he told me to write the application myself. I cracked some code in a couple of hours and this is the result.

The application uses the [Atmosphere] (http://atmosphere.java.net/) framework for pushing the log data from the server to the client.
I never worked with Atmoshphere before and I have found the documentation to be very poor, so I may not be using the framework in the perfect way.
Nevertheless the application works in Tomcat 6.x and Jetty 6.1 (with some gotchas, see the "compatibility matrix").

## Build

To build the application, please install [Gradle](http://www.gradle.org/), the Groovy based build tool.

Once installed, type :

`gradle war` 

to generate the deployable artifact.

## What works and what doesn't (compatibility matrix)

### Tomcat 6.0.X
Works, but only with Firefox 3 and 4. Chrome doesn't seem to work. Other browsers not tested.

### Jetty 6.1.x
Works with Chrome and Firefox 4. Other browsers not tested.

### Weblogic 10.3.x (11g)
Doesn't even deploy. :(

Enjoy. Fork, critique, comment, whatever.

2011 - Aestas

[http://www.aestasit.com](http://www.aestasit.com)

[http://blog.aestasit.com](http://blog.aestasit.com)