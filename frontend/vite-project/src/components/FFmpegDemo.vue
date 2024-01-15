<template>
  <header><h1>동영상 편집 테스트</h1></header>
  <video :src="video" controls
  @play="(e) => console.log(e.target.currentTime)"
  @timeupdate="(e) => (currentTime = e.target.currentTime)"
  ></video>
  <br />
  <button @click="transcode">Start</button>
  <p>{{ message }}</p>
  <section class="imgs">
    <div 
      v-for="index in 15"
      :key="index"
      @click="() => {
        (currentTime = index - 1)
        console.log(currentTime) 
        console.log(index)
      }"
      :class="currentTime >= index - 1 && currentTime < index ? 'selectImg' : ''"
    >
      <div
        class="thumb"
        :style="{
          'background-image': `url(${video})`,
        }"
      ></div>
      <span>{{ index }}</span>
    </div>  
  </section>
</template>

<script lang="ts">
import { FFmpeg } from '@ffmpeg/ffmpeg';
import { fetchFile, toBlobURL } from '@ffmpeg/util'
import { defineComponent, onMounted, ref } from 'vue'

const baseURL = 'https://unpkg.com/@ffmpeg/core-mt@0.12.6/dist/esm'
const videoURL = 'https://raw.githubusercontent.com/ffmpegwasm/testdata/master/video-15s.avi';
const currentTime = ref(0);

onMounted(() => {});

export default defineComponent({
  name: 'App',
  setup() {
    const ffmpeg = new FFmpeg()
    const message = ref('Click Start to Transcode')
    let video = ref('')
    

    async function transcode() {
      message.value = 'Loading ffmpeg-core.js'
        // ffmpeg.on('log', ({ message: msg }: LogEvent) => {
        //   message.value = msg
        // })
      await ffmpeg.load({
        coreURL: await toBlobURL(`${baseURL}/ffmpeg-core.js`, 'text/javascript'),
        wasmURL: await toBlobURL(`${baseURL}/ffmpeg-core.wasm`, 'application/wasm'),
        workerURL: await toBlobURL(`${baseURL}/ffmpeg-core.worker.js`, 'text/javascript')
      })
      message.value = 'Start transcoding'
      await ffmpeg.writeFile('test.avi', await fetchFile(videoURL))
      await ffmpeg.exec(['-i', 'test.avi', 'test.mp4'])
      message.value = 'Complete transcoding'
      const data = await ffmpeg.readFile('test.mp4')
      video.value = URL.createObjectURL(new Blob([(data as Uint8Array).buffer], { type: 'video/mp4' }))
      // video.value = "C:/Users/SSAFY/Downloads/sample.mp4";
    }
    return {
      video,
      message,
      transcode
    }
  }
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

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