# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11:alpine-jre

# Add maintainer
LABEL maintainer="Credit tracker application team(tlaxman88@gmail.com)"

# Create exploded jar in the image
COPY target/dependency/BOOT-INF/classes /app
COPY target/dependency/BOOT-INF/lib /app/lib
COPY target/dependency/META-INF /app/META-INF
WORKDIR /app

# Run the application on startup
ENTRYPOINT ["java","-cp",".:lib/*","nl.ebay.creditlimittracker.CreditLimitTrackerApplication"]

# Only for documentation purposes
EXPOSE 8080
