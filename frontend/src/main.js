import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
  faMagnifyingGlass,
  faCircleChevronLeft,
  faCircleChevronRight,
  faChevronUp,
  faChevronDown,
  faPaperPlane,
  fas
} from '@fortawesome/free-solid-svg-icons'
import { faCalendarCheck, faUser, far } from '@fortawesome/free-regular-svg-icons'
import { quillEditor } from 'vue3-quill'

import App from './App.vue'
import router from './router'
import VueDOMPurifyHTML from 'vue-dompurify-html'

library.add(
  faMagnifyingGlass,
  faCircleChevronLeft,
  faCircleChevronRight,
  faPaperPlane,
  faChevronUp,
  faChevronDown
)
library.add(faCalendarCheck, faUser, fas, far)

const app = createApp(App)
axios.defaults.baseURL = 'http://i10a402.p.ssafy.io:8080'
app.config.globalProperties.$axios = axios

app.component('font-awesome-icon', FontAwesomeIcon)

app.use(VueDOMPurifyHTML)
app.use(quillEditor)
app.use(createPinia())
app.use(router)

app.mount('#app')
