 <template>
  <div class="course-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
        <el-form-item prop="content">
          <el-input class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.content"
                    size="small" v-if="hasPerm('course-teacher:list')" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>

          <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px;" size="small" v-if="hasPerm('course-teacher:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>
          <el-button class="filter-item" type="primary" icon="el-icon-edit" style="margin-left: 0px;" size="small"  v-if="hasPerm('course-teacher:add') && mySelfList == 'true'" @click="showCreate">添加</el-button>
          <el-button :loading="downloadLoading" style="margin-left: 0px;" icon="el-icon-download" type="primary" size="small" v-if="hasPerm('course-teacher:list')" @click="handleDownload">{{ $t('excel.export') }} Excel</el-button>
          <!--<el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>-->
        </el-form-item>
      </el-form>
    </div>

    <el-table ref="teacherTable" :data="list" height="530" v-loading.body="listLoading" border fit
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
      <el-table-column align="center" prop="capacity" label="已报名学生数" v-if="mySelfList=='true'" style="width: 60px;">

      </el-table-column>
      <el-table-column align="center" prop="capacity" label="学生数" v-if="mySelfList=='false'" style="width: 60px;"></el-table-column>

      <el-table-column align="center" label="学费" v-if="mySelfList=='true'" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.tuition}} RMB
          <span v-if="scope.row.tuitionSubType == '1'">(人)</span>
          <span v-if="scope.row.tuitionSubType == '2'">(课)</span>
          <span v-if="scope.row.tuitionSubType == '3'">(学期)</span>
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
      <el-table-column align="center" prop="updateTime" label="更新时间" width="170">

      </el-table-column>
      <el-table-column align="center" prop="nickname" label="教师" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="管理" width="200" v-if="hasPerm('course-teacher:update') && mySelfList == 'true'">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-popover :visible.sync="deleteAlertVisible"
            placement="top"
            trigger="click"
            width="160">
            <p>确定删除此课程吗？</p>
            <div style="text-align: center; margin: 0">
              <el-button type="primary" size="mini" @click="deleteCourse(scope.row.id)">确定</el-button>
            </div>
            <el-button type="danger" size="small" v-if="hasPerm('course-teacher:delete')" @click="deleteAlertVisible=true" slot="reference">删除</el-button>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-steps :active="stepActive" finish-status="success" simple >
        <el-step title="基本信息" ></el-step>
        <el-step title="其他信息" ></el-step>
        <el-step title="课程预览" ></el-step>
      </el-steps>
      <br/>
      <el-row :gutter="24">
        <el-form class="small-space" :model="tempCourse" label-position="left" label-width="100px"
                 style='width: 460px; margin-left:50px;'>
          <div v-if="stepActive==1">
            <el-form-item label="课程名">
              <el-input type="text" v-model="tempCourse.content" clearable>
              </el-input>
            </el-form-item>
            <el-form-item label="学生数">
              <el-input-number v-model="tempCourse.capacity" :min="1" :max="100" clearable>
              </el-input-number>
            </el-form-item>
            <el-form-item label="课程时间">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" size="small" @change="handleCheckAllChange" border>{{$t('common.checkAll')}}</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="checkedCourseDate" @change="handleCheckedCitiesChange" size="small">
                <el-checkbox v-for="dateItem in courseDates" :label="dateItem" :key="dateItem" border>{{$t('week.'+dateItem)}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
          <div v-if="stepActive==2">
            <el-form-item label="授课年级">
              <slider-with-labels v-bind:dataVal="tempCourse.grade" ref="grade"></slider-with-labels>
            </el-form-item>
            <el-form-item label="课程类型">
              <course-type v-bind:dataVal="tempCourse.teacherType" ref="teacherType"></course-type>
            </el-form-item>
            <el-form-item label="学费">
              <tuition-com v-bind:dataTuition="tempCourse.tuition" v-bind:dataTuitionType="tempCourse.tuitionType" v-bind:dataTuitionSubType="tempCourse.tuitionSubType" ref="tuition" ></tuition-com>
            </el-form-item>

            <el-form-item label="简介">
              <el-input type="textarea" :rows="5" v-model="tempCourse.brief"></el-input>
            </el-form-item>
            <el-form-item label="附件">
              <el-upload
                class="upload-demo"
                drag
                :limit="1"
                ref="upload"
                list-type="picture"
                accept="image/jpeg,image/png,image/bmp"
                :auto-upload="false"
                action="/api/course-teacher/uploadFile"
                >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">
                  <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
                  <br/> 只能上传jpg/png文件，且不超过500kb</div>
              </el-upload>
            </el-form-item>
          </div>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>

          <el-button v-if="dialogStatus=='create'" type="success" @click="createCourse" v-if="stepActive==1">下一步</el-button>
          <el-button type="primary" v-else @click="updateCourse" v-if="stepActive==1">下一步</el-button>

          <el-button v-if="dialogStatus=='create'" type="success" @click="createCourse">创 建</el-button>
          <el-button type="primary" v-else @click="updateCourse">修 改</el-button>

          <el-button v-if="dialogStatus=='create'" type="success" @click="createCourse">创 建</el-button>
          <el-button type="primary" v-else @click="updateCourse">修 改</el-button>


      </div>
    </el-dialog>
  </div>
</template>

<script>
  import SliderWithLabels from './components/SliderWithLabels';
  import TuitionCom from './components/TuitionComponent';
  import CourseType from './components/CourseType';
  const courseDateOptions = ['tue', 'wed', 'thu'];
  export default {
    name: 'teacher-table',
    props:['listUrl','showMyBtn'],
    components: {
      SliderWithLabels,TuitionCom,CourseType,
    },
    data() {
      return {
        dropZoneOptions: {
          url: 'https://httpbin.org/post',
          thumbnailWidth: 150,
          maxFilesize: 0.5,
          headers: { "My-Awesome-Header": "header value" },
          addRemoveLinks: true,
          autoProcessQueue: false,
          accept(file, done) {
            done();
          },
        },
        mySelfList: this.$props['showMyBtn'],
        checkAll: false,
        stepActive:1,
        courseDates: courseDateOptions,
        checkedCourseDate: [],
        isIndeterminate: true,
        downloadLoading: false,
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        excelList: [],
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          content: '',
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        deleteAlertVisible: false,
        textMap: {
          update: '编辑',
          create: '创建课程'
        },
        tempCourse: {
          id: "",
          content: "",
          capacity: 25,
          courseDateArr:[],
          grade: [0,9],
          teacherType: "",
          courseDate: 0,
          brief: "",
        },

      }
    },
    created() {
      this.getList();
    },
    methods: {
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.getList();
      },
      handleCheckAllChange(val) {
        this.checkedCourseDate = val ? courseDateOptions : [];
        this.isIndeterminate = false;
        if(val){
          this.tempCourse.courseDate = 7;
          this.tempCourse.courseDateArr = ['tue','wed','thu'];
        }else{
          this.tempCourse.courseDate = 0;
          this.tempCourse.courseDateArr = [];
        }
      },
      submitUpload() {
        this.$refs.upload.submit();
      },
      handleCheckedCitiesChange(value) {
        var checkedCount = value.length;
        this.checkAll = checkedCount === this.courseDates.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.courseDates.length;
        let that = this;
        that.tempCourse.courseDate = 0;
        this.tempCourse.courseDateArr = value;
        value.forEach(function(i){
          if(i == 'tue'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+1;
          }
          if(i == 'wed'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+2;
          }
          if(i == 'thu'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+4;
          }
        });
      },
      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },

    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        // if (j === 'courseDate') {
        //   var resultStr = '';
        //   if(v[j]['tue']){
        //     resultStr+='Tue ';
        //   }
        //   if(v[j]['wed']){
        //     resultStr+='Wed ';
        //   }
        //   if(v[j]['thu']){
        //     resultStr+='Thu ';
        //   }
        //   return resultStr;
        // }else {
          return v[j];
        //}
      }))
    },
      handleDownload() {
        this.downloadLoading = true;
        if (!this.hasPerm('course-teacher:list')) {
          return
        }
        this.api({
          url: "/course-teacher/"+this.$props['listUrl'],
          method: "get",
          params:  {
            content: this.listQuery.content
          },
        }).then(data => {

          this.excelList = data.list;
          this.totalCount = data.totalCount;

          import('@/vendor/Export2Excel').then(excel => {
            const tHeader = [];
            const filterVal = []
            const columns = this.$refs['teacherTable'].$refs.tableHeader.columns;
            columns.forEach(function(i){
              if(i['property']){
                tHeader.push(i['label']);
                filterVal.push(i['property']);
              }
            });
            const list = this.excelList
            const data = this.formatJson(filterVal, list)
            excel.export_json_to_excel({
              header: tHeader,
              data,
              filename: this.filename,
              autoWidth: this.autoWidth,
              bookType: this.bookType
            })
            this.downloadLoading = false
          })

        });


      },
      getList() {
        //查询列表
        if (!this.hasPerm('course-teacher:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/course-teacher/"+this.$props['listUrl'],
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        });
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
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
      showCreate() {
        //显示新增对话框
        this.tempCourse.content = "";
        this.tempCourse.capacity = 25;
        //this.tempCourse.tuition = "";
        this.tempCourse.brief = "";
        this.tempCourse.grade = [0, 9];
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.isIndeterminate = false;
        this.checkedCourseDate = [];
        this.checkAll = false;

      },
      showUpdate($index) {
        //显示修改对话框
        this.tempCourse = this.list[$index];
        this.checkedCourseDate = [];
        let arr = this.tempCourse.courseDate.split(',');
        this.tempCourse.courseDateArr = arr;
        for(var i in arr){
            this.checkedCourseDate.push(arr[i]);
        }
        this.tempCourse.courseDate = 0 ;
        var that = this;
        arr.forEach(function(i){
          if(i == 'tue'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+1;
          }
          if(i == 'wed'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+2;
          }
          if(i == 'thu'){
            that.tempCourse.courseDate = that.tempCourse.courseDate+4;
          }
        });
        if(this.checkedCourseDate.length == courseDateOptions.length){
          this.isIndeterminate = false;
          this.checkAll = true;
          this.tempCourse.courseDate = 7;
        }
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
      },
      createCourse() {
        Object.assign(this.tempCourse, this.$refs['tuition']['tuition']);
        this.tempCourse.teacherType = this.$refs['teacherType']['teacherType'];
        this.tempCourse.grade = this.$refs['grade']['grade'];
        //保存新课程
        this.api({
          url: "/course-teacher/addCourse",
          method: "post",
          data: this.tempCourse
        }).then(() => {
          this.getList();
        this.dialogFormVisible = false
        });
      },
      updateCourse() {
        //修改课程
        Object.assign(this.tempCourse, this.$refs['tuition']['tuition']);
        this.tempCourse.teacherType = this.$refs['teacherType']['teacherType'];
        this.tempCourse.grade = this.$refs['grade']['grade'];
          this.api({
            url: "/course-teacher/updateCourse",
            method: "post",
            data: this.tempCourse
          }).then(() => {
            this.getList();
          this.dialogFormVisible = false
        });

      },
      deleteCourse(tmpId) {
        //删除课程
        this.api({
          url: "/course-teacher/deleteCourse",
          method: "post",
          data: {id: tmpId}
        }).then(() => {
          this.$refs['searchBtn'].focus();
          this.$message.success(this.$t('common.deleteSuccess'));
          this.getList();
          this.dialogFormVisible = false;
          this.deleteAlertVisible = false;
      })
      },
    }
  }
</script>
<style scoped>
  .event-active {
    font-weight: bold;
    color: #78CB5B;
    text-transform: uppercase;
    letter-spacing: 1.2px;
  }
  .active {
     color: #78CB5B;
   }

  .inactive {
    color: #fff000;
  }

  .fa.fa-circle:before {
    content: "\25C9";
    /*color: #000;*/
  }
</style>
