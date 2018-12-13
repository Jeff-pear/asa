<template>
  <div class="course-notice-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
        <el-form-item prop="content">
          <el-input class="filter-item" :placeholder="$t('teacher.courseNameNoDetail')" v-model="listQuery.content"
                    size="small" v-if="hasPerm('course-notice:list')" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>
          <el-button class="filter-item" type="primary" icon="el-icon-search" size="small" v-if="hasPerm('course-notice:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>
          <el-button :loading="downloadLoading" style="margin-left: 0px;" icon="el-icon-download" type="primary" size="small" v-if="hasPerm('course-notice:list')" @click="handleDownload">{{ $t('excel.export') }} Excel</el-button>

        </el-form-item>
      </el-form>
    </div>

    <el-table ref="noticeTable" :data="list" v-loading.body="listLoading" element-loading-text="" border fit
              :row-class-name="tableRowClassName" :span-method="objectSpanMethod">
      <el-table-column align="center" :label="$t('table.id')" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="nicknameTeacher" :label="$t('teacher.teacherName')" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="content" :label="$t('teacher.courseNameNoDetail')" style="width: 60px;">
      </el-table-column>
      <el-table-column align="center" prop="courseArea" :label="$t('teacher.courseArea')" style="width: 60px;">
      </el-table-column>
      <el-table-column align="center" prop="nicknameStu" :label="$t('student.name')" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="grade" :label="$t('teacher.grade')" style="width: 60px;">
        <template slot-scope="scope">
          {{formatGrade(scope.row.grade)}}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="courseDate" :label="$t('teacher.courseDate')" style="width: 60px;">
        <template slot-scope="scope" v-if="scope.row.courseDate!=null">
          &nbsp;
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('tue')>-1">{{$t('week.tue')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('wed')>-1">{{$t('week.wed')}}</span>
          <span v-if="scope.row.courseDate.indexOf && scope.row.courseDate.indexOf('thu')>-1">{{$t('week.thu')}}</span>
        </template>
      </el-table-column>


      <el-table-column align="center" prop="finalTuition" :label="$t('teacher.tuition')" style="width: 60px;"></el-table-column>


      <el-table-column align="center" prop="financeIsPay" :label="$t('teacher.isPay')" style="width: 100px;">
        <template slot-scope="scope">
          <span >
            <label v-if="scope.row.financeIsPay == '0'">未支付</label>
            <label v-if="scope.row.financeIsPay == '1'">已支付</label>
          </span>
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

  </div>
</template>

<script>
  export default {
    name: 'notice-table',
    components: {
    },
    data() {
      return {
        downloadLoading: false,
        highlightRow: false,
        selectBtnDisabled: false,
        attachBusinessType: 'course-student',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        fileList: [],//上传文件list
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
          create: this.$t('teacher.createCourse'),
          pay: this.$t('student.pay')
        },
        checkAll: false,
        isIndeterminate: true,
        tempCourse: {
          id: "",
          courseDateArr:[],
          courseDate: 0,
          content: '',
          attachIdStu: ""
        },
        globalId: 0,
        globalNum: 0,
        spanArr:[]
      }
    },
    created() {
      this.getList();
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        return 'common-row';
      },
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        if(columnIndex === 1 || columnIndex === 2 || columnIndex === 3){
          const _row = this.spanArr[rowIndex]
          const _col = _row>0?1:0;
          return{
            rowspan:_row,
            colspan:_col
          }
        }
      },
      changeCourseDate(val){
        this.tempCourse.courseDateArr = val;
        if(val==''){
          this.selectBtnDisabled = true;
        }else{
          this.selectBtnDisabled = false;
        }
      },
      downloadFromList(obj){
        this.previewUploadFile({url:obj});
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
          return formatSingle(arrVal);
        }
      },
      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },
      getList() {
        //查询列表
        if (!this.hasPerm('course-finance:list')) {
          return
        }
        this.listLoading = true;
        this.spanArr = [];
        this.api({
          url: '/course-teacher/listCourseResult4Finance',
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
        this.list = data.list;
        this.totalCount = data.totalCount;


        let contactDot = 0;
        this.list.forEach((item,index) => {
          item.index = index;
        this.$set(item, 'edit', false);
        item.originalFinanceIsPay = item.financeIsPay
        if(index === 0){
          this.spanArr.push(1);
        }else{
          if(item.courseId === this.list[index - 1].courseId){
            this.spanArr[contactDot] +=1;
            this.spanArr.push(0);
          }else{
            this.spanArr.push(1);
            contactDot = index;
          }
        }
      });

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
      fileChangeToFather(childFileList){
        this.fileList = childFileList;
        this.selectBtnDisabled = !(this.fileList.length>0);
      },
      formatJson(filterVal, jsonData) {
        return jsonData.map(v => filterVal.map(j => {
          if(j=='grade'){
            return this.formatGrade(v[j]);
          }else if(j=='financeIsPay'){
            if(v[j]=='0'){
              return '未支付';
            }else{
              return '已支付';
            }
          }else{
            return v[j];
          }

        }))
      },
      handleDownload() {
        this.downloadLoading = true;
        if (!this.hasPerm('course-notice:list')) {
          return
        }
        this.api({
          url: "/course-teacher/listCourseResult4Finance",
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
          const columns = this.$refs['noticeTable'].$refs.tableHeader.columns;
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
    }
  }
</script>
<style scoped>
  .course-notice-container{
    padding: 20px;
  }
  .el-input-number__increase{
    right: 11px;
  }
  .el-form-item__content .el-select, .el-form-item__content .el-input-number{
    width:102% !important;
  }
  .el-table--enable-row-hover .el-table__body tr:hover>td{
    background-color: white !important;
  }

</style>

