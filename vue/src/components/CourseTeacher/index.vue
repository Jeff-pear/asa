 <template>
  <div class="course-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
        <el-form-item prop="content">
          <el-input class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.content"
                    size="small" v-if="hasPerm('course-teacher:list')" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>

          <!--<el-select size="small" v-model="listQuery.grade" class="filter-item" style="width: 200px;" :placeholder="$t('teacher.grade')" clearable>-->
            <!--<el-option-->
              <!--v-for="item in options"-->
              <!--:key="item.value"-->
              <!--:label="item.label"-->
              <!--:value="item.value">-->
            <!--</el-option>-->
          <!--</el-select>-->

          <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px;" size="small" v-if="hasPerm('course-teacher:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>
          <el-button class="filter-item" type="primary" icon="el-icon-edit" style="margin-left: 0px;" size="small"  v-if="hasPerm('course-teacher:add') && mySelfList == 'true'" @click="showCreate">{{ $t('table.add') }}</el-button>
          <el-button :loading="downloadLoading" style="margin-left: 0px;" icon="el-icon-download" type="primary" size="small" v-if="hasPerm('course-teacher:list')" @click="handleDownload">{{ $t('excel.export') }} Excel</el-button>
          <!--<el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>-->
        </el-form-item>
      </el-form>
    </div>

    <el-table ref="teacherTable" :data="list" height="530" v-loading.body="listLoading" border fit
              highlight-current-row>
      <el-table-column align="center" :label="$t('table.id')" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <!--课程名-->
      <el-table-column align="center" prop="content" :label="$t('teacher.courseName')" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.content}}
          <el-popover class="col-el-popover"
                      placement="top-start"
                      width="400"
                      trigger="click">
            {{scope.row.brief}}
            <i slot="reference" class="el-icon-share" v-if="scope.row.brief!=null" style="cursor: pointer;"></i>
          </el-popover>
        </template>
      </el-table-column>
      <!--学生数-->
      <el-table-column align="center" prop="capacity" :label="$t('teacher.studentNum')" v-if="mySelfList=='true'" style="width: 60px;">
        <template slot-scope="scope">
          {{scope.row.pickStudentNum}}/
          {{scope.row.capacity}}
          <el-popover class="col-el-popover"
                      placement="right"
                      trigger="click">
            <el-table :data="selectStudentData">
              <el-table-column width="250" property="nickname" label="学生姓名">
                <template slot-scope="scope2">
                  {{scope2.row.nickname}}({{scope2.row.nickname_cn}})
                </template>
              </el-table-column>
              <el-table-column width="150" property="course_date" label="选课日期"></el-table-column>
              <el-table-column width="100" property="is_pay" label="是否缴费">
                <template slot-scope="scope2">
                  <span v-if="scope2.row.is_pay == '0'">未支付</span>
                  <span v-if="scope2.row.is_pay == '1'">已支付</span>
                </template>
              </el-table-column>
            </el-table>
            <i slot="reference" class="el-icon-share" v-if="scope.row.brief!=null" style="cursor: pointer;" @click="showStudentList(scope.row.id)"></i>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="capacity" :label="$t('teacher.studentNum')" v-if="mySelfList=='false'" style="width: 60px;"></el-table-column>
      <!--授课年级-->
      <el-table-column align="center" prop="grade" :label="$t('teacher.grade')" style="width: 60px;">
        <template slot-scope="scope">
          {{formatGrade(scope.row.grade)}}
        </template>
      </el-table-column>
      <!--学费-->
      <el-table-column align="center" :label="$t('teacher.tuition')" v-if="mySelfList=='true'" style="width: 60px;">
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
      <!--课程时间-->
      <el-table-column align="center" prop="courseDate" :label="$t('teacher.courseDate')" style="width: 60px;">
        <template slot-scope="scope">
          &nbsp;
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('tue')>-1">{{$t('week.tue')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('wed')>-1">{{$t('week.wed')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('thu')>-1">{{$t('week.thu')}}</span>
          <!--<span v-if="scope.row.courseDate.tue==true">{{$t('week.tue')}}</span>&nbsp;-->
          <!--<span v-if="scope.row.courseDate.wed==true">{{$t('week.wed')}}</span>&nbsp;-->
          <!--<span v-if="scope.row.courseDate.thu==true">{{$t('week.thu')}}</span>-->
        </template>
      </el-table-column>
      <!--更新时间-->
      <el-table-column align="center" prop="updateTime" :label="$t('teacher.updateDate')" width="170">
      </el-table-column>

      <el-table-column align="center" prop="origin_fileName" :label="$t('teacher.attachment')" width="170">
        <template slot-scope="scope">
          <a style="text-decoration: underline;color: #409EFF;" @click="downloadFromList(scope.row)">{{scope.row.originFileName}}</a>
        </template>
      </el-table-column>
      <!--教师-->
      <el-table-column align="center" prop="nickname" :label="$t('teacher.teacherName')" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="status" :label="$t('table.status')" style="width: 60px;">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 'draft'" type="danger">{{$t('table.draft')}}</el-tag>
          <el-tag v-if="scope.row.status == 'publish'" type="success">{{$t('table.publish')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.manage')" width="200" v-if="(hasPerm('course-teacher:update') && mySelfList == 'true') || isAdmin('管理员')">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="edit" @click="showUpdate(scope.$index)">{{$t('table.edit')}}</el-button>
          <el-popover :visible.sync="deleteAlertVisible"
            placement="top"
            trigger="click"
            width="160">
            <p>{{$t('table.deleteConfirm')}}</p>
            <div style="text-align: center; margin: 0">
              <el-button type="primary" size="mini" @click="deleteCourse(scope.row.id)">{{$t('table.confirm')}}</el-button>
            </div>
            <el-button type="danger" size="small" v-if="hasPerm('course-teacher:delete')" @click="deleteAlertVisible=true" slot="reference">{{$t('table.delete')}}</el-button>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" class="teacherCourseDialog" :before-close="handleClose">
      <el-steps :active="tempCourse.stepActive" finish-status="success" simple >
        <el-step :title="$t('teacher.step1')" ></el-step>
        <el-step :title="$t('teacher.step2')" ></el-step>
        <el-step :title="$t('teacher.step3')" ></el-step>
      </el-steps>
      <br/>

        <el-form class="small-space" :model="tempCourse" label-position="left" label-width="100px"
                 style='width: 500px; margin-left:50px;'>
          <div v-if="tempCourse.stepActive==1">
            <el-form-item :label="$t('teacher.courseNameNoDetail')">
              <el-input type="text" v-model="tempCourse.content" clearable>
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('teacher.teacherName')">
              <!--<el-input type="text" v-model="tempCourse.teacherName" clearable>-->
              <!--</el-input>-->
              <teacher-name v-bind:dataVal="tempCourse.teacherName" ref="teacherName"></teacher-name>

            </el-form-item>
            <el-form-item :label="$t('teacher.studentNum')">
              <el-input-number v-model="tempCourse.capacity" :min="1" :max="15" clearable>
              </el-input-number>
            </el-form-item>
            <el-form-item :label="$t('teacher.courseDate')">
              <course-date v-bind:dataVal="tempCourse.courseDate"
                           v-bind:dataArr="tempCourse.courseDateArr"
                           v-bind="$attrs"
                           v-on="$listeners"
                           role="teacher"
                           v-bind:dataCheckAll="checkAll"
                           v-bind:dataIsIndeterminate="isIndeterminate"
                           v-on:changeCourseDate="changeCourseDate"
                           v-bind:courseDateOptions="courseDateOptionsInside"
                           ref="courseDate"></course-date>
            </el-form-item>
            <el-form-item :label="$t('teacher.brief')">
              <el-input type="textarea" :rows="5" v-model="tempCourse.brief"></el-input>
            </el-form-item>
          </div>
          <div v-if="tempCourse.stepActive==2">
            <el-form-item :label="$t('teacher.grade')">
              <slider-with-labels v-bind:dataVal="tempCourse.grade" ref="grade"></slider-with-labels>
            </el-form-item>
            <el-form-item :label="$t('teacher.courseType')">
              <course-type v-bind:dataVal="tempCourse.teacherType"

                           v-bind:dataNeedTrainingAid="tempCourse.needTrainingAid"
                           v-bind:dataPhone="tempCourse.phone"
                           v-bind:dataPrice="tempCourse.price"
                           v-bind:dataRecommendBrand="tempCourse.recommendBrand"
                           ref="teacherType"></course-type>
            </el-form-item>
            <el-form-item :label="$t('teacher.tuition')">
              <tuition-com v-bind:dataTuition="tempCourse.tuition" v-bind:dataTuitionType="tempCourse.tuitionType" v-bind:dataTuitionSubType="tempCourse.tuitionSubType" ref="tuition" ></tuition-com>
            </el-form-item>
            <el-form-item :label="$t('teacher.attachment')">
              <file-uploader v-bind:dataVal="tempCourse.attachId" v-bind:fileListArr="fileList" v-bind:businessId="tempCourse.id"></file-uploader>
            </el-form-item>

          </div>
          <div v-if="tempCourse.stepActive==3">
            <el-row>
              {{$t('teacher.courseNameNoDetail') }}: {{tempCourse.content}}
            </el-row>

            <el-row>
              {{$t('teacher.teacherName') }}: {{tempCourse.teacherName}}
            </el-row>

            <el-row>
              {{$t('teacher.studentNum') }}: {{tempCourse.capacity}}
            </el-row>

            <el-row>
              {{$t('teacher.courseDate') }}: {{tempCourse.capacity}}
            </el-row>

            <el-row>
              {{$t('teacher.grade') }}: {{tempCourse.grade}}
            </el-row>

            <el-row>
              {{$t('teacher.courseType') }}: {{tempCourse.teacherType}}
            </el-row>

            <el-row>
              {{$t('teacher.tuition') }}:
              <span v-if="tempCourse.tuitionType == 'free'">{{$t('teacher.tuitionFree') }}</span>
              <span v-if="tempCourse.tuitionType == 'fee'">{{tempCourse.tuition}} {{tempCourse.tuitionSubType}} </span>
            </el-row>

            <el-row>
              {{$t('teacher.brief') }}: {{tempCourse.brief}}
            </el-row>

            <el-row>
              {{$t('teacher.attachment') }}: {{tempCourse.attachment}}
            </el-row>
          </div>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <!--<el-button @click="dialogFormVisible = false">取 消</el-button>-->
        <el-button type="success" @click="prevStep" v-if="tempCourse.stepActive!=1">{{$t('teacher.prevStep')}}</el-button>
        <el-button type="primary" @click="nextStep" v-if="tempCourse.stepActive!=3">{{$t('teacher.nextStep')}}</el-button>
        <el-button type="primary" @click="nextStep" v-if="tempCourse.stepActive==3">{{$t('teacher.finishStep')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import SliderWithLabels from './components/SliderWithLabels';
  import TuitionCom from './components/TuitionComponent';
  import CourseType from './components/CourseType';
  import TeacherName from './components/TeacherName';
  import CourseDate from './components/CourseDate';
  import FileUploader from './components/FileUploader';
  import store from '../../store'

  export default {
    name: 'teacher-table',
    props:['listUrl','showMyBtn'],
    components: {
      SliderWithLabels,TuitionCom,CourseType,TeacherName,CourseDate,FileUploader
    },
    data() {

      return {
        mySelfList: this.$props['showMyBtn'],
        checkAll: false,
        isIndeterminate: true,
        downloadLoading: false,
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        fileList: [],//上传文件list
        selectStudentData:[],//表格的数据
        excelList: [],
        courseDateOptionsInside : [{label: 'tue',disabled: false}, {label:'wed',disabled: false}, {label:'thu',disabled: false}],
        options: [{
          value: '0',
          label: 'KG'
        }, {
          value: '1',
          label: 'G1'
        }, {
          value: '2',
          label: 'G2'
        }, {
          value: '3',
          label: 'G3'
        }, {
          value: '4',
          label: 'G4'
        }, {
          value: '5',
          label: 'G5'
        }, {
          value: '6',
          label: 'G6'
        }, {
          value: '7',
          label: 'G7'
        }, {
          value: '8',
          label: 'G8'
        }, {
          value: '9',
          label: 'G9'
        }],
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
          update: this.$t('table.edit'),
          create: this.$t('teacher.createCourse')
        },
        tempCourse: {
          id: "",
          teacherName: '',
          stepActive:1,
          content: "",
          capacity: 15,
          courseDateArr:[],
          grade: [0,9],
          teacherType: "",
          courseDate: 0,
          brief: "",
          status: 1,
          originFileName:"",
          attachId: "",
          needTrainingAid: 1,
          phone: '',
          price: 0,
          recommendBrand: '',
        },

      }
    },
    created() {
      this.getList();
    },
    methods: {
      resetTempCourse(){
        this.fileList = [];
        this.tempCourse = {
          id: "",
          teacherName: '',
          stepActive:1,
          content: "",
          status: 1,
          capacity: 15,
          courseDateArr:[],
          grade: [0,9],
          teacherType: "",
          courseDate: 0,
          brief: "",
          needTrainingAid: 1,
          phone: '',
          price: 0,
          recommendBrand: '',
        };
      },
      showStudentList(id){
        this.selectStudentData = [];
        this.api({
          url: "/course-student/listStudentDetail4Teacher/",
          method: "get",
          params:  {
            courseId: id
          },
        }).then(response  => {
          this.selectStudentData = response.list;
        });
      },

      //download file
      downloadFromList(obj){
        this.previewUploadFile({url:obj.attachId});
      },
      previewUploadFile(obj){
        this.api({
          url: "/sys/download/"+obj['url'],
          method: "get",
          // responseType: 'blob'
        }).then(response  => {
          this.$message.success(this.$t('common.downloadSuccess'));
        });
      },

      nextStep(){
        if(this.tempCourse.stepActive==1 ){
          if(this.tempCourse.id==''){
            this.createCourse();
          }else{
            this.updateCourse();
          }
        }else{
          this.updateCourse();
        }
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
      prevStep(){
        this.tempCourse.stepActive--;
        this.dialogFormVisible = false;
        this.dialogFormVisible = true;
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.getList();
      },

      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },

    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
          return v[j];
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
              header: tHeader,data,
              filename: this.filename,
              autoWidth: this.autoWidth,
              bookType: this.bookType
            })
            this.downloadLoading = false;
          })
        });
      },
      getList() {
        //查询列表
        if (!this.hasPerm('course-teacher:list')) {
          return
        }
        if(!this.dialogFormVisible){
          this.listLoading = true;
        }

        this.api({
          url: "/course-teacher/"+this.$props['listUrl'],
          method: "get",
          params: this.listQuery
        }).then(data => {
          if(!this.dialogFormVisible){
            this.listLoading = false;
          }
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
        this.resetTempCourse();

        this.tempCourse.stepActive = 1;
        //显示新增对话框
        this.tempCourse.content = "";
        this.tempCourse.capacity = 15;
        //this.tempCourse.tuition = "";
        this.tempCourse.brief = "";
        this.tempCourse.grade = [0, 9];
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.isIndeterminate = false;
        this.checkAll = false;
        this.tempCourse.courseDate = 0;
        this.tempCourse.courseDateArr = [];
        if(this.$refs['courseDate']){
          this.$refs['courseDate']['courseDate'] = this.tempCourse.courseDate;
          this.$refs['courseDate']['courseDateArr'] = this.tempCourse.courseDateArr;
          this.$refs['courseDate']['checkAll'] = this.checkAll;
          this.$refs['courseDate']['isIndeterminate'] = this.isIndeterminate;
        }
        if(this.$refs['teacherName']){
          this.$refs['teacherName']['teacherName'] = undefined;
        }
      },
      showUpdate($index) {
        debugger;
        this.resetTempCourse();
        //显示修改对话框

        this.tempCourse = this.list[$index];

        if(this.tempCourse.attachId && this.tempCourse.attachId!=''){
          this.fileList.push({name: this.tempCourse.originFileName, url: this.tempCourse.attachId})
        }
        this.tempCourse.stepActive = 1;

        this.tempCourse.courseDateArr = this.tempCourse.courseDate.split(',');
        this.tempCourse.courseDate = 0 ;
        var that = this;
        this.tempCourse.courseDateArr.forEach(function(i){
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
        if(this.tempCourse.courseDateArr.length == 3){
          this.isIndeterminate = false;
          this.checkAll = true;
          this.tempCourse.courseDate = 7;
        }
        if(this.$refs['courseDate']){
          this.$refs['courseDate']['courseDate'] = this.tempCourse.courseDate;
          this.$refs['courseDate']['courseDateArr'] = this.tempCourse.courseDateArr;
          this.$refs['courseDate']['checkAll'] = this.checkAll;
          this.$refs['courseDate']['isIndeterminate'] = this.isIndeterminate;
        }
        if(this.$refs['teacherName']){
          this.$refs['teacherName']['teacherName'] = this.tempCourse.teacherName;
        }else{
          if(this.tempCourse['needTrainingAid']==undefined){
            this.tempCourse.needTrainingAid = 1;
          }
        }
        //
        // if(this.$refs['teacherType']){
        //   this.$refs['teacherType']['needTrainingAid'] = this.tempCourse.needTrainingAid;
        // }

        this.dialogStatus = "update";
        this.dialogFormVisible = true;

      },
      createCourse() {
        this.beforePersist();

        //保存新课程
        this.api({
          url: "/course-teacher/addCourse",
          method: "post",
          data: this.tempCourse
        }).then((result) => {
          this.getList();
          this.tempCourse.id = result.id;
          this.tempCourse.stepActive++;
        }).catch(v=>{
          this.getList();
          console.warn(v);
        });
      },
      changeCourseDate(val){
        this.tempCourse.courseDateArr = val;
      },
      beforePersist(){
        if(this.tempCourse.stepActive == 1){
          this.tempCourse.courseDate = this.$refs['courseDate']['courseDate'];
          this.courseDateArr = this.$refs['courseDate']['courseDateArr'];
          this.tempCourse.teacherName = this.$refs['teacherName']['teacherName'];
        }
        if(this.tempCourse.stepActive == 2){
          Object.assign(this.tempCourse, this.$refs['tuition']['tuition']);
          this.tempCourse.teacherType = this.$refs['teacherType']['teacherType'];

          this.tempCourse.needTrainingAid = this.$refs['teacherType']['needTrainingAid'];
          this.tempCourse.phone = this.$refs['teacherType']['phone'];
          this.tempCourse.price = this.$refs['teacherType']['price'];
          this.tempCourse.recommendBrand = this.$refs['teacherType']['recommendBrand'];

          this.tempCourse.grade = this.$refs['grade']['grade'];
        }
        if(this.tempCourse.stepActive == 3){
          this.tempCourse.status = 2;
        }else{
          this.tempCourse.status = 1;
        }
      },
      updateCourse() {
        //修改课程
        this.beforePersist();
          this.api({
            url: "/course-teacher/updateCourse",
            method: "post",
            data: this.tempCourse
          }).then(() => {
            this.getList();
          //this.tempCourse.stepActive++;
            if(this.tempCourse.stepActive==3){
              this.dialogFormVisible = false
            }else{
              this.tempCourse.stepActive++;
            }
          }).catch(v=>{
            this.getList();
              console.warn(v);
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
          this.deleteAlertVisible = false;
      })
      },
      handleClose(){
        this.dialogFormVisible = false;
        this.api({
          url: "/course-teacher/"+this.$props['listUrl'],
          method: "get",
          params: this.listQuery
        }).then(data => {

          this.list = data.list;
          this.totalCount = data.totalCount;
        });
      }
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
  .teacherCourseDialog .el-form-item__content .el-select, .teacherCourseDialog .el-form-item__content .el-input-number{
    width:102% !important;
  }
</style>
