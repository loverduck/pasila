<script setup>
import ReadyStep from '@/components/ready/ReadyStep.vue'

const props = defineProps({
  data: String
})

const stepName = ['상품 등록', '대본 생성', '챗봇 생성', '라이브 예약']
</script>

<template>
  <div class="step-container">
    <ready-step v-bind:data="props.data" v-bind:numbers="1" v-bind:names="stepName[0]" />
  </div>
</template>

<style lang="scss" scoped>
.step-container {
  @include flex-box($justify: flex-start);
  @include box(100%, null, null, 0, 2px, 5px);
  margin-left: 1rem;
  margin-top: 1rem;
}
</style>
