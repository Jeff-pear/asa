<template>
  <div class="course-student-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
        <el-form-item prop="content">
          <el-input class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.content"
                    size="small" v-if="hasPerm('course-student:list')" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>
          <el-button class="filter-item" type="primary" icon="el-icon-search" size="small" v-if="hasPerm('course-student:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>

          <!--<el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>-->
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="list" v-loading.body="listLoading" element-loading-text="" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="content" label="课程名(详情)" style="width: 60px;">
        <template v-if="scope.row.brief!=null" slot-scope="scope">
          {{scope.row.content}}
          <el-popover class="col-el-popover"
                      placement="top-start"
                      width="400"
                      trigger="click">
            {{scope.row.brief}}
            <i slot="reference" class="el-icon-share" style="cursor: pointer;"></i>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="capacity" label="学生数" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="grade" label="授课年级" style="width: 60px;">
        <template slot-scope="scope">
          {{formatGrade(scope.row.grade)}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="学费" style="width: 60px;">
        <template slot-scope="scope" >
          <div v-if="scope.row.tuitionType=='fee'">
            {{scope.row.tuition}} RMB
            <span v-if="scope.row.tuitionSubType == '1'">(人)</span>
            <span v-if="scope.row.tuitionSubType == '2'">(课)</span>
            <span v-if="scope.row.tuitionSubType == '3'">(学期)</span>
          </div>
          <div v-if="scope.row.tuitionType=='free'">免费</div>

        </template>
      </el-table-column>
      <el-table-column align="center" prop="courseDate" label="课程时间" style="width: 60px;">
        <template slot-scope="scope">
          &nbsp;
          {{scope.row.courseDate}}
          <!--<span v-if="scope.row.courseDate.tue==true">{{$t('week.tue')}}</span>&nbsp;-->
          <!--<span v-if="scope.row.courseDate.wed==true">{{$t('week.wed')}}</span>&nbsp;-->
          <!--<span v-if="scope.row.courseDate.thu==true">{{$t('week.thu')}}</span>-->
        </template>
      </el-table-column>
      <el-table-column align="center" prop="nickname" label="教师" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="origin_fileName" label="附件" width="170">
        <template slot-scope="scope">
          <a style="text-decoration: underline;color: #409EFF;" @click="downloadFromList(scope.row)">{{scope.row.originFileName}}</a>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" v-if="hasPerm('course-student:update') ">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" size="small" v-if="isMySelect == 'false' " @click="showUpdate(scope.$index)">选课</el-button>

          <el-popover v-if="isMySelect == 'true' || isAdmin('管理员')"
            placement="top"
            trigger="click"
            width="160">
            <p>确定撤销选中此课程吗？</p>
            <div style="text-align: center; margin: 0">
              <el-button type="primary" size="mini" @click="deleteMyCourse(scope.row.id)">确定</el-button>
            </div>
            <el-button type="danger" icon="edit" size="small" slot="reference">撤销</el-button>
          </el-popover>

        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>


    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :before-close="beforeClose()">
      <el-row :gutter="24">

      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="success" @click="selectCourse">创 建</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  const courseDateOptions = ['tue', 'wed', 'thu'];
  export default {
    name: 'student-table',
    props:['listUrl','isMySelect'],
    data() {
      return {
        mySelfList: this.$props['isMySelect'],
        courseDates: courseDateOptions,
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          studentCanPick: true,
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          name: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建课程'
        },
        tempCourse: {
          id: "",
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      downloadFromList(obj){
        this.previewUploadFile({url:obj.attachId});
      },
      previewUploadFile(obj){
        this.api({
          url: "/sys/download/"+obj['url'],
          method: "get",
          // responseType: 'blob'
        }).then(response  => {
          this.$message.success("下载成功！");
      });
      },
      formatGrade(arrVal){
        function formatSingle(val){
          let resultVal = '';
          if(val == 0){
            resultVal = 'KG'
          }else{
            resultVal = 'G'+String(val);
          }
          return resultVal;
        }
        if(arrVal){
          return formatSingle(arrVal[0]) +'--'+ formatSingle(arrVal[1]);
        }
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.getList();
      },
      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },
      getList() {
        //查询列表
        if (!this.hasPerm('course-student:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: this.$props['listUrl'],
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
        this.list = data.list;
        this.totalCount = data.totalCount;
      })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val;
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempCourse.courseId = this.list[$index].id;

        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      selectCourse(){
        //保存新课程
        this.api({
          url: "/course-student/pickCourse",
          method: "post",
          data: this.tempCourse
        }).then(() => {
          this.getList();
          this.$emit('changeTab',event.target,'0');
          this.dialogFormVisible = false
        });
      },
      deleteMyCourse(tmpId) {
        //删除选中课程
        this.api({
          url: "/course-student/deleteCourse",
          method: "post",
          data: {id: tmpId}
        }).then(() => {
          //this.$refs['searchBtn'].focus();
          this.$message.success(this.$t('common.deleteSuccess'));
        this.getList();
        this.dialogFormVisible = false;
      })
      },
      beforeClose(){
        console.log('close')
      }
    }
  }
</script>
<style scoped>
  .el-input-number__increase{
    right: 11px;
  }
  .el-form-item__content .el-select, .el-form-item__content .el-input-number{
    width:102% !important;
  }
</style>

