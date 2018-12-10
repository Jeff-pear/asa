<template>
  <div class="course-student-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
        <el-form-item prop="content">
          <el-input class="filter-item" :placeholder="$t('teacher.courseNameNoDetail')" v-model="listQuery.content"
                    size="small" v-if="hasPerm('course-student:list')" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>
          <el-button class="filter-item" type="primary" icon="el-icon-search" size="small" v-if="hasPerm('course-student:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>

          <!--<el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>-->
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="list" v-loading.body="listLoading" element-loading-text="" border fit
              highlight-current-row>
      <el-table-column align="center" :label="$t('table.id')" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="content" :label="$t('teacher.courseName')" style="width: 60px;">
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
      <el-table-column align="center" prop="capacity" :label="$t('teacher.studentNum')" style="width: 60px;"></el-table-column>
      <!--<el-table-column align="center" prop="grade" label="授课年级" style="width: 60px;">-->
        <!--<template slot-scope="scope">-->
          <!--{{formatGrade(scope.row.grade)}}-->
        <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column align="center" :label="$t('teacher.tuition')" style="width: 60px;">
        <template slot-scope="scope" >
          <div v-if="scope.row.tuitionType=='fee'">
            {{scope.row.tuition}} RMB
            <span v-if="scope.row.tuitionSubType == '1'">{{$t('teacher.tuitionOption1')}}</span>
            <span v-if="scope.row.tuitionSubType == '2'">{{$t('teacher.tuitionOption2')}}</span>
            <span v-if="scope.row.tuitionSubType == '3'">{{$t('teacher.tuitionOption3')}}</span>
          </div>
          <div v-if="scope.row.tuitionType=='free'">{{$t('teacher.tuitionFree')}}</div>

        </template>
      </el-table-column>
      <el-table-column align="center" prop="courseDateStudent" :label="$t('teacher.courseDate')" v-if="isMySelect == 'true' " style="width: 60px;">
        <template slot-scope="scope">
          &nbsp;
          <span v-if="scope.row.courseDateStudent.indexOf && scope.row.courseDateStudent.indexOf('tue')>-1">{{$t('week.tue')}}</span>
          <span v-if="scope.row.courseDateStudent.indexOf && scope.row.courseDateStudent.indexOf('wed')>-1">{{$t('week.wed')}}</span>
          <span v-if="scope.row.courseDateStudent.indexOf && scope.row.courseDateStudent.indexOf('thu')>-1">{{$t('week.thu')}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="courseDate" :label="$t('teacher.courseDate')" v-if="isMySelect == 'false' " style="width: 60px;">
        <template slot-scope="scope">
          &nbsp;
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('tue')>-1">{{$t('week.tue')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('wed')>-1">{{$t('week.wed')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('thu')>-1">{{$t('week.thu')}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="nickname" :label="$t('teacher.teacherName')" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="origin_fileName" :label="$t('teacher.attachment')" width="170">
        <template slot-scope="scope">
          <a style="text-decoration: underline;color: #409EFF;" @click="downloadFromList(scope.row)">{{scope.row.originFileName}}</a>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.manage')" width="200" v-if="getGroupTag()=='-1' || (hasPerm('course-student:update') && getPeriod('canPick'))">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" size="small" v-if="isMySelect == 'false' " @click="showUpdate(scope.$index)">{{$t('student.pickCourse')}}</el-button>

          <el-popover v-if="isMySelect == 'true' || getGroupTag() == '-1'"
            placement="top"
            trigger="click"
            width="160">
            <p>{{$t('table.deleteConfirm')}}</p>
            <div style="text-align: center; margin: 0">
              <el-button type="primary" size="mini" @click="deleteMyCourse(scope.row.id)">{{$t('table.confirm')}}</el-button>
            </div>
            <el-button type="danger" icon="edit" size="small" slot="reference" v-if="getGroupTag()=='-1' || (hasPerm('course-student:update') && getPeriod('canPick'))">{{$t('student.revoke')}}</el-button>
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


    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :before-close="beforeClose">
      <el-row :gutter="24">
        <el-form class="small-space" :model="tempCourse" label-position="left" label-width="100px"
                 style='width: 500px; margin-left:50px;'>
          <el-form-item :label="$t('teacher.courseNameNoDetail')">
            <span>{{tempCourse.content}}</span>
          </el-form-item>
          <el-form-item :label="$t('teacher.courseDate')">
            <course-date v-bind:dataVal="tempCourse.courseDate"
                         v-bind:dataArr="tempCourse.courseDateArr"
                         v-bind="$attrs" v-on="$listeners"
                         v-bind:dataCheckAll="checkAll"
                         v-bind:dataIsIndeterminate="isIndeterminate"
                         v-bind:courseDateOptions="courseDateOptions"
                         v-on:changeCourseDate="changeCourseDate"
                         ref="courseDate"></course-date>
          </el-form-item>
        </el-form>

      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="success" v-bind:disabled="selectBtnDisabled" @click="selectCourse">{{$t('student.select')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import CourseDate from '../CourseTeacher/components/CourseDate';
  import FileUploader from '../CourseTeacher/components/FileUploader';
  export default {
    name: 'student-table',
    props:['listUrl','isMySelect'],
    components: {
     CourseDate,FileUploader
    },
    data() {
      return {
        mySelfList: this.$props['isMySelect'],
        selectBtnDisabled: false,
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          studentCanPick: true,
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          name: ''
        },
        courseDateOptions: [],
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: this.$t('student.pickCourse'),
          create: this.$t('teacher.createCourse')
        },
        checkAll: false,
        isIndeterminate: true,
        tempCourse: {
          id: "",
          courseDateArr:[],
          courseDate: 0,
          content: '',
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      changeCourseDate(val){
        this.tempCourse.courseDateArr = val;
        if(val==''){
          this.selectBtnDisabled = true;
        }else{
          this.selectBtnDisabled = false;
        }
      },
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
        this.tempCourse = this.list[$index];
        this.tempCourse.courseId = this.list[$index].id;
        if(this.$refs['courseDate']){
          if(this.list[$index].courseDate == 'tue'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = true;
            this.$refs['courseDate']['courseDate'] = 1;
            this.$refs['courseDate']['courseDateArr'] = ['tue']
          }else if(this.list[$index].courseDate == 'wed'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = true;
            this.$refs['courseDate']['courseDate'] = 2;
            this.$refs['courseDate']['courseDateArr'] = ['wed']
          }else if(this.list[$index].courseDate == 'tue,wed'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = true;
            this.$refs['courseDate']['courseDate'] = 3;
            this.$refs['courseDate']['courseDateArr'] = ['tue','wed']
          }else if(this.list[$index].courseDate == 'thu'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = false;
            this.$refs['courseDate']['courseDate'] = 4;
            this.$refs['courseDate']['courseDateArr'] = ['thu']
          }else if(this.list[$index].courseDate == 'tue,thu'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = false;
            this.$refs['courseDate']['courseDate'] = 5;
            this.$refs['courseDate']['courseDateArr'] = ['tue','thu']
          }else if(this.list[$index].courseDate == 'wed,thu'){
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = true;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = false;
            this.$refs['courseDate']['courseDate'] = 6;
            this.$refs['courseDate']['courseDateArr'] = ['wed','thu']
          }else{
            this.$refs['courseDate']['courseDateOptions'][0]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][1]['disabled'] = false;
            this.$refs['courseDate']['courseDateOptions'][2]['disabled'] = false;
            this.$refs['courseDate']['courseDate'] = 7;
            this.$refs['courseDate']['courseDateArr'] = ['tue','wed','thu']
          }
        }else{
          let courseDateOptionsArr = [{label: 'tue',disabled: false}, {label:'wed',disabled: false}, {label:'thu',disabled: false}];
          if(this.list[$index].courseDate == 'tue'){
            courseDateOptionsArr[1]['disabled'] = true;
            courseDateOptionsArr[2]['disabled'] = true;
            this.tempCourse.courseDate = 1;
            this.tempCourse.courseDateArr = ['tue'];
          }else if(this.list[$index].courseDate == 'wed'){
            courseDateOptionsArr[0]['disabled'] = true;
            courseDateOptionsArr[2]['disabled'] = true;
            this.tempCourse.courseDate = 2;
            this.tempCourse.courseDateArr = ['wed'];
          }else if(this.list[$index].courseDate == 'tue,wed'){
            courseDateOptionsArr[2]['disabled'] = true;
            this.tempCourse.courseDate = 3;
            this.tempCourse.courseDateArr = ['tue','wed'];
          }else if(this.list[$index].courseDate == 'thu'){
            courseDateOptionsArr[0]['disabled'] = true;
            courseDateOptionsArr[1]['disabled'] = true;
            this.tempCourse.courseDate = 4;
            this.tempCourse.courseDateArr = ['thu'];
          }else if(this.list[$index].courseDate == 'tue,thu'){
            courseDateOptionsArr[1]['disabled'] = true;
            this.tempCourse.courseDate = 5;
            this.tempCourse.courseDateArr = ['tue','thu'];
          }else if(this.list[$index].courseDate == 'wed,thu'){
            courseDateOptionsArr[0]['disabled'] = true;
            this.tempCourse.courseDate = 6;
            this.tempCourse.courseDateArr = ['wed','thu'];
          }else{
            courseDateOptionsArr[0]['disabled'] = false;
            courseDateOptionsArr[1]['disabled'] = false;
            courseDateOptionsArr[2]['disabled'] = false;
            this.tempCourse.courseDate = 7;
            this.tempCourse.courseDateArr = ['tue','wed','thu'];
          }
          this.courseDateOptions = courseDateOptionsArr;
        }
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      selectCourse(){
        this.tempCourse.courseDate = this.$refs['courseDate']['courseDate'];
        this.courseDateArr = this.$refs['courseDate']['courseDateArr'];

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
        this.dialogFormVisible = false;
        this.api({
          url: this.$props['listUrl'],
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
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

