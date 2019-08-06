<template>
  <div>
  <section class="app-main">
    <transition name="fade" mode="out-in">
      <!-- <router-view :key="key"></router-view> -->
      <router-view></router-view>
    </transition>
  </section>
  <el-dialog
    :title="$t('common.tips')"
    :visible.sync="dialogVisible"
    width="40%"
    :show-close="showClose"
    :close-on-click-modal="showClose"
    :close-on-press-escape="showClose"
   >
    <span>{{$t('profile.notice')}}</span>
    <span slot="footer" class="dialog-footer">
      <router-link :to="{path:'/profile',query: {tab: 1}}"><el-button type="primary" @click="dialogVisible=false">{{$t('common.toModify')}}</el-button></router-link>
    </span>
  </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AppMain',
  data() {
    return {
      dialogVisible: true,
      showClose: false
    };
  },
  methods: {
    routeMethod() {
      let tag = this.needResetPwd();
      this.dialogVisible = tag;
      if(this.$route.name =='profile'){
        this.dialogVisible = false;
      }
    }
  },
  created(){
    this.routeMethod();
  },
}
</script>
