FROM gradle:7.1.0-jdk11

USER root

RUN useradd -ms /bin/bash wasadm

RUN rm -rf /app
RUN mkdir -p /app

RUN chown -R wasadm:wasadm /app

RUN git clone https://github.com/lee-code712/agent.java.git /app/agent.java
RUN git clone https://github.com/lee-code712/Commerce-Common.git /app/Commerce-Common
COPY . /app/Commerce-Auth

WORKDIR /app/Commerce-Auth

RUN cd /app/Commerce-Auth
RUN gradle build

CMD ["java", "-Djennifer.config=/app/agent.java/conf/auth-v2.conf","-javaagent:/app/agent.java/jennifer.jar", "-jar", "/app/Commerce-Auth/build/libs/Commerce-Auth-0.0.1-SNAPSHOT.jar"]