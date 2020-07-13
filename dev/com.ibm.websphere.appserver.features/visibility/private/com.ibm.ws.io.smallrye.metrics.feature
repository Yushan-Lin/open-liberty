-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=com.ibm.ws.io.smallrye.metrics
singleton=true
-bundles=com.ibm.ws.io.smallrye.metrics; location:="dev/api/stable/,lib/",\
 com.ibm.websphere.javaee.jsonp.1.1; location:="dev/api/spec/,lib/"; mavenCoordinates="javax.json:javax.json-api:1.1.3", \
 com.ibm.ws.org.glassfish.json.1.1
kind=noship
edition=core
