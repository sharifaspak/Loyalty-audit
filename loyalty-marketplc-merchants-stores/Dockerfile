ARG JAVA_BASE_IMAGE=default-route-openshift-image-registry.apps.ocpdev.etisalat.corp.ae/loyalty-dev/loyalty-openjdk18-openshift:1.8
FROM ${JAVA_BASE_IMAGE}
WORKDIR /usr/src/app
ADD target/*.jar /usr/src/app/app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
