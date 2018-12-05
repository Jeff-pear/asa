<template>
  <div class="menu-wrapper">
    <div class="app-title">
      <img src="../../../../assets/login/logo.jpeg" class="logoImg"/>
      <mallki class-name="mallki-text appTitleText" text=" A S A "/>
    </div>
    <template v-for="item in routes" v-if="!item.hidden&&item.children">
      <router-link v-if="item.children.length===1 && !item.children[0].children" :to="item.path+'/'+item.children[0].path" :key="item.children[0].name">
        <el-menu-item :index="item.path+'/'+item.children[0].path" class='submenu-title-noDropdown'>
          <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon"></svg-icon>
          <span v-if="item.children[0].meta&&item.children[0].meta.title">{{generateTitle(item.children[0].meta.title)}}</span>
        </el-menu-item>
      </router-link>

      <el-submenu v-else :index="item.name||item.path" :key="item.name">
        <template slot="title">
          <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
          <span v-if="item.meta&&item.meta.title" >{{generateTitle(item.meta.title)}}</span>
        </template>

        <template v-for="child in item.children" v-if="!child.hidden">
          <sidebar-item class="nest-menu" v-if="child.children&&child.children.length>0" :routes="[child]" :key="child.path"></sidebar-item>

          <router-link v-else :to="item.path+'/'+child.path" :key="child.name">
            <el-menu-item :index="item.path+'/'+child.path">
              <svg-icon v-if="child.meta&&child.meta.icon" :icon-class="child.meta.icon"></svg-icon>
              <span v-if="child.meta&&child.meta.title">{{generateTitle(child.meta.title)}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>

    </template>
  </div>
</template>

<script>
  import { generateTitle } from '@/utils/i18n';
  import Mallki from '@/components/TextHoverEffect/Mallki';
  export default {
    name: 'SidebarItem',
    components: {
      Mallki,
    },
    props: {
      routes: {
        type: Array
      }
    },
    methods: {
      generateTitle
    }
  }
</script>
<style lang="scss">

  .app-title{
    color: #188EA7;
    font-size: larger;
    font-weight: bolder;
    background-color: white;
    display: inline-flex;
    .appTitleText{
      margin-top: 13px;
      font-style: italic;
      font-size: 26px;
    }
  }
.logoImg{
  width: 27%;height: 27%;    margin-left: 11%;
}
  .app-title:hover,app-title:active{
    transform:scaleX(1);
    animation:changeColor1 1.5s;
    color:#188EA7;
  }

  @keyframes changeColor1{
    from{
      color: #000;
    }
    to{
      color: #188EA7;
    }
  }
  @keyframes changeColor2{
    from{
      color: #188EA7;
    }
    to{
      color: #000;
    }
  }

</style>
