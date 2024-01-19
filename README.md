# OpenVidu

## 실행방법


### OpenVidu deployment

1. docker 설치([Docker](https://www.docker.com/get-started/ "docker link"))

2. ```docker run -p 4443:4443 --rm -e OPENVIDU_RECORDING=true -e OPENVIDU_RECORDING_PATH=/PATH/TO/VIDEO/FILES -v /var/run/docker.sock:/var/run/docker.sock -v /PATH/TO/VIDEO/FILES:/PATH/TO/VIDEO/FILES openvidu/openvidu-dev:2.29.0```

### Recording

- DOCS: [Recording](https://docs.openvidu.io/en/stable/advanced-features/recording/ "tutorial: Recording docs")

- GITHUB: [Recording](https://github.com/OpenVidu/openvidu-tutorials/tree/master/openvidu-recording-java "tutorial: Recording link")
