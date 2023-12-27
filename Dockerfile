FROM openjdk:17-oracle

MAINTAINER jaehoon <kjh-work@naver.com>

WORKDIR /app

COPY ./EssentialNews/target/EssentialNews-0.0.1-SNAPSHOT.jar EssentialNews.jar


ENTRYPOINT ["java","-jar","EssentialNews.jar"]