<template>
  <scroll-bar>
    <el-menu mode="vertical" unique-opened :default-active="$route.path" :collapse="isCollapse"
             background-color="#304156" text-color="#fff" active-text-color="#409EFF">
      <sidebar-item :routes="permission_routers"></sidebar-item>
    </el-menu>
    <el-footer class="footer-style"><div v-if="this.sidebar.opened">@Copyright 2018</div></el-footer>
  </scroll-bar>
</template>
<style>
  .footer-style {
    position: fixed;
    left: 0;
    bottom: 0;
    color: #fff;
    text-align: left;
  }
</style>
<script>
  import {mapGetters} from 'vuex'
  import SidebarItem from './SidebarItem'
  import ScrollBar from '@/components/ScrollBar'

  export default {
    components: {SidebarItem, ScrollBar},
    computed: {
      ...mapGetters([
        'permission_routers',
        'sidebar'
      ]),
      isCollapse() {
        return !this.sidebar.opened
      }
    }
  }
</script>
