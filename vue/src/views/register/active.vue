<template>
  <div class="errPage-container">
    <el-row>
      <el-col :span="12">
        <h1 class="text-jumbo text-ginormous" v-show="activeStatus == false">Activation!</h1>
        <h1 class="text-jumbo text-ginormous" v-show="activeStatus == true">Successfully!</h1>
        <ul class="list-unstyled">
          <li>Or you can :</li>
          <li class="link-type">
            <router-link to="/login"><a>Back to Login</a></router-link>
          </li>
          <li><a href="#" @click.prevent="dialogVisible=true">Look at the picture. </a></li>
        </ul>
      </el-col>
      <el-col :span="12">
        <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream.">
      </el-col>
    </el-row>
    <el-dialog :visible.sync="dialogVisible" title="Welcome">
      <img :src="ewizardClap" class="pan-img">
    </el-dialog>
  </div>
</template>

<script>
  import errGif from '@/assets/common/star.jpeg'
  import playGif from '@/assets/common/play.gif'
  export default {
    name: 'active',
    created: function(){
      this.activeUser();
    },
    data() {
      return {
        errGif: errGif + '?' + +new Date(),
        ewizardClap: playGif,
        dialogVisible: false,
        activeStatus: false,
      }
    },
    methods: {
      activeUser() {

        this.listLoading = true;
        var code = window.location.href.split('code=')[1];
        this.api({
          url: "/register/activeUser?code="+code,
          method: "get"
        }).then(data => {
          this.listLoading = false;
          console.log("激活成功");
          var that = this;
        var t = setTimeout(function (){
          that.activeStatus = true;
        }, 1000);


      })
      },
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .errPage-container {
    width: 800px;
    max-width: 100%;
    margin: 100px auto;
    .pan-back-btn {
      background: #008489;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #008489;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
</style>
