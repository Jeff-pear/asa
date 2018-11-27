<template>
  <div class="period-container" >
    <el-form class="small-space" :model="tempPeriod" label-position="left" label-width="130px"
             style='width: 450px; margin-left:50px;'>
      <el-form-item :label="$t('period.navLabel')">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.datePeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="text" v-model="tempPeriod.comments">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer footer-left" >
      <el-button @click="dialogFormVisible = false">清 空</el-button>
      <el-button type="success" @click="setPeriod">发 布</el-button>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'peroid',
    data() {
      return {
        listLoading: false,//数据加载等待动画
        tempPeriod: {
          datePeriod: "",
          comments: ""
        },
      }
    },
    methods: {
      setPeriod() {
        this.listLoading = true;
        //保存新文章
        this.api({
          url: "/sys/setPeriod",
          method: "post",
          data: this.tempPeriod
        }).then(() => {
          console.log("设置成功");
          this.reload();
        this.listLoading = false;
      })
      },
      //组件通信
      reload(){
        this.$bus.$emit('change');
      }

    }
  }
</script>
<style rel="stylesheet/scss" lang="scss">
  .period-container{
    padding: 20px;
    .el-date-editor{
      .el-range-separator{
        width: 7%;
      }
    }
    .footer-left{
      margin-left: 180px;
    }
  }
</style>
