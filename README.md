# Essential news project

## Overview
This project is designed to assist people who have limited time to search for relevant news. By entering a keyword of interest, users can quickly access both the search trends and a list of related news articles.

## How to start project with Docker
1. docker (윈도우용) 설치: [Docker][dockerdownlink]

[dockerdownlink]: https://www.docker.com/products/docker-desktop/

2. 이미지 다운로드

```docker
docker pull wogns0513/essentialnews-app
``` 
3. 컨테이너 실행
```
docker run -d -p 8080:8080 wogns0513/essentialnews-app:latest
```
4. 접속 방법

    웹 브라우저에서 'localhost:8080/welcome'으로 접속 가능합니다.

