<template>
  <div class="period-container" >
    <el-form class="small-space" :model="tempPeriod" label-position="left" label-width="130px"
             style='width: 450px; margin-left:50px;'>
      <el-form-item label="教师选课周期">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.teacherPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="学生选课周期">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.studentPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="学生缴费周期">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.feePeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="财务审核费用周期">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.financePeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="结果公示开始时间">
        <div class="block">
          <el-date-picker
            v-model="tempPeriod.noticeStartDate"
            type="date"
            value-format="yyyy.MM.dd">
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="选项">
        <el-radio v-model="tempPeriod.type"  label="0">正选</el-radio>
        <el-radio v-model="tempPeriod.type" label="1">补选</el-radio>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="text" v-model="tempPeriod.comments">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer footer-left" >
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
          teacherPeriod: "",
          studentPeriod: "",
          feePeriod: "",
          financePeriod:"",
          noticeStartDate:"",
          comments: "",
          type: "0"
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
