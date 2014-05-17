unicon-cas-overlay
==================

Generic CAS maven war overlay to exercise the latest versions of CAS as well as the latest features of [cas-addons](https://github.com/Unicon/cas-addons)

This overlay could be freely used as a starting template for local CAS maven war overlays.

# Versions
```xml
<cas.version>3.5.2</cas.version>
<cas-addons.version>1.9.1</cas-addons.version>
```

# Requirements
* Apache Maven 3
* Web Server (e.g. Apache Tomcat 7)
* Apache Ant (Optional)

# Configuration
The `etc` directory contains the sample configuration files that would need to be copied to an external file system location (`/etc/cas` by default) and configured to satisfy local CAS installation needs.

# Deployment

## Ant
* Set `CATALINA_HOME` to the location of the Tomcat directory (e.g. `/apps/tomcat`)
* Set `M2_HOME` to the location of the Maven directory (e.g. `apps/maven`)
* Execute `ant deploy`

The ant build automatically invokes maven to produce the `cas.war` file. It then copies the `cas.xml` context fragment to Tomcat, pointing to the location of the web application file removing the need to copy the web application to the `webapps` directory.

## Shell
* Set `CATALINA_HOME` to the location of the Tomcat directory (e.g. `/apps/tomcat`)
* Modify the `cas.xml` file to point to the location of `cas.war` file. (e.g. `/apps/unicon-cas-overlay/target/cas.war`)
* Execute `deploy.sh`

Tomcat is shutdown first. The build automatically invokes maven to produce the `cas.war` file. It then copies the `cas.xml` context fragment to Tomcat, pointing to the location of the web application file removing the need to copy the web application to the `webapps` directory. Finally, tomcat is restarted to load the webapp.

## Manual
* Execute `mvn clean package`
* Modify the `cas.xml` file to point to the location of `cas.war` file. (e.g. `/apps/unicon-cas-overlay/target/cas.war`)
* Copy the file over to `$CATALINA_HOME/conf/Catalina/localhost`
* Restart tomcat.
