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
// const localURL = "http://localhost:5173/src/assets/sample-15s.mp4"; //mp4 영상 파일
const localURL = "http://localhost:5173/src/assets/Big_Buck_Bunny_180_10s.webm"; // webm 영상 파일

const ffmpeg = new FFmpeg();
const message = ref("Click Start to Transcode");

let video = ref("");
let data = ref();

let data2 = ref();
let video2 = ref("");

let data3 = ref();
let video3 = ref("");

let data4 = ref();
let video4 = ref("");

//영상 시작, 러닝타임 정해줄때 사용
let startTime = ref("00:00:00");
let endTime = ref("00:00:08");


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

//mp4 혹은 webm 파일을 가져오기
const bringMP4 = async () => {
  await ffmpeg.load({
    coreURL: await toBlobURL(`${baseURL}/ffmpeg-core.js`, "text/javascript"),
    wasmURL: await toBlobURL(`${baseURL}/ffmpeg-core.wasm`, "application/wasm"),
    workerURL: await toBlobURL(`${baseURL}/ffmpeg-core.worker.js`, "text/javascript"),
  });
  console.log("hi-mp4");
  await ffmpeg.writeFile("test.webm", await fetchFile(localURL));
  message.value = "Complete transcoding";
  data = await ffmpeg.readFile("test.webm");
  video.value = URL.createObjectURL(new Blob([data.buffer], { type: "video/webm" }));
}

//비디오 원하는 초에 자르기
const trim = async () => {
  const args = [
    "-ss",
    `${startTime.value}`,
    "-i",
    "test.webm",
    "-t",
    `${endTime.value}`,
    "-vcodec",
    "copy",
    "-acodec",
    "copy",
    "output1.webm",
  ];
  await ffmpeg.writeFile('test.webm', await fetchFile(localURL));
  await ffmpeg.exec([...args]);

  console.log("3333333");
  data2 = await ffmpeg.readFile("output1.webm");
  console.log("44444444");
  if (video.value) {
  URL.revokeObjectURL(video.value);
  }
  video2.value = URL.createObjectURL(new Blob([data2.buffer], { type: "video/webm" }));

};

//비디오 여기서 시간정해서 자르기
const addVideos = async () => {
  const args = [
    "-ss",
    "00:00:05",
    "-i",
    "test.webm",
    "-t",
    "00:00:05",
    "-acodec",
    "copy",
    "-vcodec",
    "copy",
    "output2.webm",
  ];
  console.log('hi')
  await ffmpeg.exec([...args]);
  console.log("2")
  data3 = await ffmpeg.readFile("output2.webm");
  console.log("3")
  video3.value = URL.createObjectURL(new Blob([data3.buffer], {type: "video/webm"}));

}


//비디오 합치기
const combineVideos = async() => {
  const args = [
    "-i",
    "concat:output1.webm|output2.webm",
    "-c:v",
    "copy",
    "-c:a",
    "copy",
    "output55.webm",
  ];
  await ffmpeg.exec([...args]);
  console.log("@22");
  data4 = await ffmpeg.readFile("output55.webm");
  console.log("#33");
  video4.value = URL.createObjectURL(new Blob([data4.buffer], {type: "video/webm"}));
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

  <!-- 비디오 시간입력한 후 자르는 부분-->
  <video :src="video2" controls style="width: 400px; height: 150px;"></video>
  <div>
    <input type="text" v-model="startTime" placeholder="시작시간 00:00:01 부터">
    <input type="text" v-model="endTime" placeholder="끝 시간 00:00:00">
    <button @click="trim">시간 입력 후 자르기</button>
  </div>
  <!-- 정해진 시간의 비디오 자르기 -->
  <button @click="addVideos">영상 자르기</button>
  <video :src="video3" controls style="width: 400px; height: 150px;"></video>
  <button @click="combineVideos">본영상과 자른 영상합취귀</button>
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
