<script setup>
import ReadySteps from '@/components/ready/ReadySteps.vue'
import ProductInput from '@/components/ready/ProductInput.vue'
import NextButton from '@/components/ready/NextButton.vue'
import DescPreview from '@/components/editor/DescPreview.vue'
import DescEditor from '@/components/editor/DescEditor.vue'

import { ref } from 'vue'
import { useReadyLiveStore } from '@/stores/readyLive'

const step = ref('register')
const nextStep = ref('script')
const preview = ref('')
const store = useReadyLiveStore()
const product = ref({})
const updateDesc = (message) => {
  preview.value = message
}

const sendProduct = async () => {
  for (let i = product.value.length - 1; i >= 0; i--) {
    if (product.value.options[i].name == '') {
      product.value.options.splice(i, 1)
    }
  }

  store.liveProduct = product.value
  store.productDesc = preview.value
}

const currentTab = ref(0)
const tabs = ref(['작성하기', '미리보기'])
</script>

<template>
  <div class="container">
    <ready-steps :data="step" />
    <div class="body">
      <div class="input-body">
        <product-input
          @getProduct="
            (e) => {
              product.value = e
            }
          "
        />
      </div>
      <div class="editor-body">
        <div id="tabs" class="tabs">
          <ul class="tab-menu">
            <li
              v-for="(tab, index) in tabs"
              :class="[currentTab == index ? 'active-tab' : 'none-tab']"
              :key="index"
            >
              <a href="#" @click="currentTab = index">{{ tab }}</a>
            </li>
          </ul>
        </div>
        <div v-show="currentTab == 0" class="show-body">
          <desc-editor
            :message="preview"
            @preview-content="updateDesc"
            @only-script="
              (e) => {
                store.onlyScript = e
              }
            "
          />
        </div>
        <div v-show="currentTab == 1" class="show-body">
          <desc-preview :preview="preview" />
        </div>
      </div>
    </div>
    <next-button :data="nextStep" @click="sendProduct" />
  </div>
</template>

<style lang="scss" scoped>
.container {
  @include box(95%, 100%, none, 0.3rem, 0.8rem, 0.5rem);

  .body {
    @include box(100%, 80%, white, 0, 0.3rem, 0.1rem);
    @include flex-box($align: flex-start, $justify: space-evenly);
    border: 2px solid $main;
    border-radius: 0.5rem;

    .input-body {
      @include box(48%, 100%, white, 0, 0.3rem, 0.1rem);
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .editor-body {
      @include box(48%, 100%, white, 0, 0.3rem, 0.1rem);
      display: flex;
      flex-direction: column;
      border-left: 1px solid $main;
      min-height: 30rem;
      margin-top: 2rem;

      .tabs {
        display: flex;
        flex-direction: row;
        list-style: none;
        font-size: 0.08rem;

        .tab-menu {
          @include box(90%, 100%, white, 0.1rem, 0, 0);
          display: flex;
          flex-direction: row;
          margin-left: 2.5rem;

          .none-tab {
            @include box(5rem, 2rem, $light-gray, 0.1rem, 0, 0);
            @include font-factory($fs-1, null, $dark);
          }
          .active-tab {
            @include box(5rem, 2rem, $main, 0.1rem, 0, 0);
            @include font-factory($fs-1, null, $dark);
            color: white;
          }
          li {
            list-style: none;
            text-align: center;
            margin-bottom: 0;

            a,
            a:visited {
              vertical-align: text-bottom;
              text-decoration-line: none;
              color: inherit;
            }
          }
        }
      }

      .show-body {
        @include box(98%, 100%, none, 0, 0, 0);
        display: flex;
        justify-content: center;
      }
    }
  }
}
</style>
