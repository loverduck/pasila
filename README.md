# OpenVidu

[OpenVidu](https://openvidu.io/ "OpenVidu link")

- WebRTC 라이브러리

- Mesh -> SFU 서버 구조 적용을 위해 사용

- OpenVidu CE(무료)는 미디어 서버로 Kurento 사용

## 실행방법
- Application Server: java
- OpenVidu deployment: docker hub
- Application client: vue

### Application Server

- DOCS: [JAVA](https://docs.openvidu.io/en/stable/application-server/openvidu-basic-java/ "tutorial: java docs")

- GITHUB: [JAVA](https://github.com/OpenVidu/openvidu-tutorials/tree/master/openvidu-basic-java "tutorial: java link")

### OpenVidu deployment

1. docker 설치([Docker](https://www.docker.com/get-started/ "docker link"))

2. ```docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-dev:2.29.0```

### Application Client

- DOCS: [VUE](https://docs.openvidu.io/en/stable/tutorials/openvidu-vue/ "tutorial: vue docs")

- GITHUB: [VUE](https://github.com/OpenVidu/openvidu-tutorials/tree/master/openvidu-vue "tutorial: vue link")
