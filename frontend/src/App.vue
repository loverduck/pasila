<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { FFmpeg } from "@ffmpeg/ffmpeg";
import { fetchFile, toBlobURL } from "@ffmpeg/util";
const path = "/src/assets/file_1704701089680_3840x2160_";

const vi = ref(null);
const currentTime = ref(0);

onMounted(() => {});

const targetMP4File = "C:/Users/SSAFY/Downloads/sample.mp4"; //영상 파일

const baseURL = "https://unpkg.com/@ffmpeg/core-mt@0.12.6/dist/esm";
const videoURL = "https://raw.githubusercontent.com/ffmpegwasm/testdata/master/video-15s.avi";
const localURL = "http://localhost:5173/src/assets/sample-15s.mp4"; //영상 파일
// const localURL = "http://localhost:5173/assets/sample.mp4"; //영상 파일

const ffmpeg = new FFmpeg();
const message = ref("Click Start to Transcode");
let video = ref("");
let data = ref();
let data2 = ref();
let data3 = ref();
let data4 = ref();
let video2 = ref("");
let video3 = ref("");
let video4 = ref("");
let output = ref("");
let startTime = ref("00:00:01");
let endTime = ref("00:00:10");


//avi to mp4
async function transcode() {
  message.value = "Loading ffmpeg-core.js";
  await ffmpeg.load({
    coreURL: await toBlobURL(`${baseURL}/ffmpeg-core.js`, "text/javascript"),
    wasmURL: await toBlobURL(`${baseURL}/ffmpeg-core.wasm`, "application/wasm"),
    workerURL: await toBlobURL(`${baseURL}/ffmpeg-core.worker.js`, "text/javascript"),
  });

  console.log("hello");
  message.value = "Start transcoding";
  await ffmpeg.writeFile("test.avi", await fetchFile(videoURL));
  await ffmpeg.exec(["-i", "test.avi", "test.mp4"]);
  message.value = "Complete transcoding";
  data = await ffmpeg.readFile("test.mp4");
  video.value = URL.createObjectURL(new Blob([data.buffer], { type: "video/mp4" }));
}

//mp4파일을 가져오기
const bringMP4 = async () => {
  await ffmpeg.load({
    coreURL: await toBlobURL(`${baseURL}/ffmpeg-core.js`, "text/javascript"),
    wasmURL: await toBlobURL(`${baseURL}/ffmpeg-core.wasm`, "application/wasm"),
    workerURL: await toBlobURL(`${baseURL}/ffmpeg-core.worker.js`, "text/javascript"),
  });
  console.log("hi-mp4");
  await ffmpeg.writeFile("test.mp4", await fetchFile(localURL));
  message.value = "Complete transcoding";
  data = await ffmpeg.readFile("test.mp4");
  video.value = URL.createObjectURL(new Blob([data.buffer], { type: "video/mp4" }));
}

//비디오 원하는 초에 자르기
const trim = async () => {
  const args = [
  "-ss",
    `${startTime.value}`,
    "-to",
    `${endTime.value}`,
    "-i",
    "test.mp4",

    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output.mp4",
  ];
  // const args2 = [
  //   "-i",
  //   "output.mkv",
  //   "-c",
  //   "copy",
  //   "output.mp4"  
  // ]
  await ffmpeg.exec([...args]);
  // await ffmpeg.exec([...args2])
  // await ffmpeg.writeFile("output.mp4");
  console.log("3333333");
  // await ffmpeg.load();
  data2 = await ffmpeg.readFile("output.mp4");
  console.log("44444444");
  video2.value = URL.createObjectURL(new Blob([data2.buffer], { type: "video/mp4" }));

};

//비디오 여기서 시간정해서 자르기
const addVideos = async () => {
  const args = [
    "-ss",
    "00:00:11",
    "-to",
    "00:00:14",
    "-i",
    "test.mp4",
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output22.mp4",
  ];

  await ffmpeg.exec([...args]);
  data3 = await ffmpeg.readFile("output22.mp4");
  console.log("44");
  video3.value = URL.createObjectURL(new Blob([data3.buffer], {type: "video/mp4"}));

}

let str = ref("");

//비디오 합치기
const combineVideos = async() => {
  const args1 = [
    "-i",
    "output22.mp4",
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output22.mkv"
  ]
  const args2 = [
    "-i",
    "output.mp4",
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output.mkv"
  ]
  await ffmpeg.exec([...args1])
  await ffmpeg.exec([...args2])

  const args = [
    "-i",
    "concat:output.mkv|output22.mkv",
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output55.mkv",
  ];

  const args4 = [
    "-i",
    "output55.mkv",
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output00.mp4"
  ]
  console.log(str.value)
  console.log("!11");
  await ffmpeg.exec([...args]); 
  await ffmpeg.exec([...args4])
  console.log("@22");
  data4 = await ffmpeg.readFile("output00.mp4");
  console.log("#33");
  video4.value = URL.createObjectURL(new Blob([data4.buffer], {type: "video/mp4"}));
}

</script>

<template>
  <header><h1>동영상 편집 테스트</h1></header>
  <input type="file" accept="video/*" />

  <br />

  <video :src="video" controls style="width: 400px; height: 150px;"></video>
  <br />
  <button @click="transcode">Start</button>
  <button @click="bringMP4">MP4파일가져온다</button>
  <p>{{ message }}</p>
  <video :src="video2" controls style="width: 400px; height: 150px;"></video>
  <!-- <button @click="trim">자르기</button> -->
  <div>
    <input type="text" v-model="startTime" placeholder="시작시간 00:00:01 부터">
    <input type="text" v-model="endTime" placeholder="끝 시간 00:00:00">
    <button @click="trim">시간 입력 후 자르기</button>
  </div>
  <button @click="addVideos">새로운 영상 만들기</button>
  <video :src="video3" controls style="width: 400px; height: 150px;"></video>
  <button @click="combineVideos">합취귀</button>
  <video :src="video4" controls style="width: 400px; height: 150px;"></video>
  <!-- <video
    controls
    width="400"
    ref="vi"
    @play="(e) => console.log(e.target.currentTime)"
    @timeupdate="(e) => (currentTime = e.target.currentTime)"
  >
    <source src="C:\Users\SSAFY\Downloads\sample.mp4" id="test" />
  </video>

  <br />

  <section class="imgs">
    <div
      v-for="index in 13"
      :key="index"
      @click="() => (vi.currentTime = index)"
      :class="currentTime >= index && currentTime < index + 1 ? 'selectImg' : ''"
    >
      <div
        class="thumb"
        :style="{
          'background-image': `url(${path}${index}.jpg)`,
        }"
      ></div>
      <span>{{ index }}</span>
    </div>
  </section> -->
</template>

<style scoped>
.imgs {
  display: flex;
  overflow-x: scroll;
}
.thumb {
  width: 100px;
  height: 100px;
  margin: 0 1px;
  background-size: contain;
  cursor: pointer;
}
.selectImg {
  border: 3px solid red;
}
</style>
