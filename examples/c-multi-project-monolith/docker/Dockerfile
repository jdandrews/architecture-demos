FROM container-registry.oracle.com/os/oraclelinux:9
WORKDIR /opt

# Install Java 17
RUN curl https://download.oracle.com/java/17/archive/jdk-17.0.6_linux-aarch64_bin.tar.gz --output jdk-17.0.6.tar.gz \
# RUN curl https://download.oracle.com/java/17/archive/jdk-17.0.6_linux-x64_bin.tar.gz --output jdk-17.0.6.tar.gz \
    && tar xvfz jdk-17.0.6.tar.gz \
    && rm jdk-17.0.6.tar.gz \
    && mv jdk-17.0.6 java

ENV JAVA_HOME '/opt/java'
ENV PATH "${PATH}:${JAVA_HOME}/bin"

COPY ./build/libs app

EXPOSE 8080

ENTRYPOINT ["java", "--class-path", "/opt/app/*", "demo.Main"]
