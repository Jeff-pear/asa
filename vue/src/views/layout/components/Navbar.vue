<template>
  <el-menu class="navbar right-menu" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>

    <breadcrumb></breadcrumb>
    <marquee direction="left" class="right-menu-item2">当前选课周期：{{period}}</marquee>
    <el-dropdown class="avatar-container " trigger="click">

      <div class="avatar-wrapper">

        欢迎您：
        <span class="user-name" v-text="nickname"> </span>

        <!--<img class="user-avatar" :src="avatar+'?imageView2/1/w/80/h/80'">-->
        <i class="el-icon-caret-bottom"></i>
      </div>
      <el-dropdown-menu class="user-dropdown" slot="dropdown">
        <router-link class="inlineBlock" to="/">
          <el-dropdown-item>
            Home
          </el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="logout" style="display:block;">LogOut</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <lang-select class="international right-menu-item"/>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import LangSelect from '@/components/LangSelect'

export default {


  mounted () {
    //VUE组件通信，监听事件发生(监听period.vue)
    this.$bus.$on('change', ()=> {
      this.loadData();
    })},
    created: function(){
      this.listLoading = true;
      this.loadData();
    },
  data() {
    return {
      period: ''
    }
  },
  components: {
    Breadcrumb,
    Hamburger,
    LangSelect,
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'nickname'
    ])
  },
  methods: {
    loadData(){
      this.api({
        url: "/sys/listPeriod",
        method: "get"
      }).then(data => {
        this.listLoading = false;
      console.log("查看选课周期");
      var obj = data.list[0];
      this.period = obj['startDate']+" -- "+obj['endDate'];
    });
    },
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }

  .right-menu-item {
    display: inline-block;
    margin: 0 8px;
    position: absolute;
    right: 6px;
  }
  .right-menu-item2 {
    display: inline-block;
    margin: 0 8px 0 590px;
    position: absolute;
    right: 250px;

  }

  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 65px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 0px;
      position: relative;
      .user-name{
        color:#409EFF;
      }
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 20px;
        font-size: 12px;
      }
    }
  }
}
</style>

