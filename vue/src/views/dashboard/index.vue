<template>
  <div class="dashboard-container">
    <el-container class="welcome-container">
      <el-row :gutter="20" style="width: 100%;">
        <el-col :span="12">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>选课系统简介</span>
            </div>
            系统可以做啥
          </el-card>
        </el-col>

      </el-row>
      <el-row :gutter="20" style="width: 100%;">
        <el-col :span="12">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>选课时间</span>
            </div>
            2018/12/15--2018/01/15<br/>
            请学生按照规则选课!
          </el-card>
        </el-col>
      </el-row>

    </el-container>
    <!--<a href="https://github.com/Heeexy/SpringBoot-Shiro-Vue" target="_blank"><img style="margin: 40px"-->
      <!--src="http://img.heeexy.com/github.png"></a>-->
  </div>
</template>
<style lang="scss">
  .welcome-container{
    padding: 20px;
    display: block;
    .el-row{
      padding-top:10px;
    }
  }
</style>
<script>
  export default {
    name: 'dashboard',
    data() {
      return {}
    },
  }
</script>
