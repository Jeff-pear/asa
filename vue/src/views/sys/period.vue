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

      <el-form-item label="结果公示开始时间" >
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
        <!--<el-radio v-model="tempPeriod.type" label="1">补选</el-radio>-->
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="text" v-model="tempPeriod.comments">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer footer-left" >
      <el-button type="success" @click="setPeriod">发 布</el-button>
      <span style="color: red;">系统将在财务审核费用截止日期第二天01:00:00自动执行系统任务</span>
    </div>

    <el-table :data="list" v-loading.body="listLoading" style="margin-top: 10px; " element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="教师选课周期"  >
        <template slot-scope="scope">
          {{scope.row.teacherStartDate | formatDate}} -- {{scope.row.teacherEndDate|formatDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="学生选课周期" prop="nickname" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.pickStartDate | formatDate}} -- {{scope.row.pickEndDate | formatDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="学生缴费周期" prop="nicknameCn" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.feeStartDate | formatDate}} -- {{scope.row.feeEndDate | formatDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="财务审核费用周期" prop="email" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.financeStartDate | formatDate}} -- {{scope.row.financeEndDate | formatDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="结果公示开始时间" prop="email" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.noticeStartDate | formatDate}}
        </template>
      </el-table-column>
      <!--<el-table-column align="center" label="选项" width="100">-->
        <!--<template slot-scope="scope">-->
          <!--<el-tag type="success" v-text="scope.row.type" v-if="scope.row.type===0">正</el-tag>-->
          <!--<el-tag type="primary" v-text="scope.row.type" v-else>补</el-tag>-->
        <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column align="center" label="备注" prop="comments" width="170"></el-table-column>

      <el-table-column align="center" :label="$t('table.manage')" width="200" v-if="getGroupTag()=='-1' ">
        <template slot-scope="scope">
          <el-button type="success" size="mini" icon="el-icon-circle-check-outline" @click="startEdit(scope.row)">{{$t('table.edit')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>
<script>

  import {formatDate} from '@/utils/index' //
  export default {
    name: 'peroid',
    data() {
      return {
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        tempPeriod: {
          teacherPeriod: [],
          studentPeriod: [],
          feePeriod: [],
          financePeriod:[],
          noticeStartDate:[],
          comments: "",
          type: "0"
        },
      }
    },

    filters:{
      formatDate(time){
        let date = new Date(time);
        return formatDate(date,'yyyy-MM-dd');
        //此处formatDate是一个函数，将其封装在common/js/date.js里面，便于全局使用
      }
    },
    created() {
      this.getList();
    },
    methods: {
      formatDate(time){
        let date = new Date(time);
        return formatDate(date,'yyyy-MM-dd');
        //此处formatDate是一个函数，将其封装在common/js/date.js里面，便于全局使用
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/sys/listPeriod",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      startEdit(r){
        let that = this;
        that.tempPeriod.teacherPeriod = Object.assign( [that.formatDate(r.teacherStartDate),that.formatDate(r.teacherEndDate)]);
        that.tempPeriod.studentPeriod = Object.assign(  [that.formatDate(r.pickStartDate),that.formatDate(r.pickEndDate)]);
        that.tempPeriod.feePeriod = Object.assign(  [that.formatDate(r.feeStartDate),that.formatDate(r.feeEndDate)]);
        that.tempPeriod.financePeriod = Object.assign(  [that.formatDate(r.financeStartDate),that.formatDate(r.financeEndDate)]);
        that.tempPeriod.noticeStartDate = Object.assign(  that.formatDate(r.noticeStartDate));
        that.tempPeriod.comments = Object.assign(  r.comments);
      },
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
          this.getList();
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
