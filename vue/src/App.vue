<template>
  <div id="app">
    <!--过场动画-->
    <transition name="slide-fade">
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style lang="scss">
  @import '~normalize.css/normalize.css';// normalize.css 样式格式化
  @import './styles/index.scss'; // 全局自定义的css样式
  .slide-fade{
    position: absolute;left:0;right: 0;
  }
  .slide-fade-enter-active {
    transition: all 1.2s ease;
  }
  .slide-fade-leave-active {

    transition: all .1s cubic-bezier(2.0, 0.5, 0.8, 1.0);
  }
  .slide-fade-enter, .slide-fade-leave-to
  {
    left:0;right: 0;
    /*transform: translateX(50px);*/
    opacity: 0;
  }

</style>
