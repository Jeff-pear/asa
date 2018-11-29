<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :model="listQuery" ref="listQuery">
          <el-form-item prop="content">

            <el-input class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.content"
                      size="small" v-if="hasPerm('course-teacher:list')" ref="searchBtn" style="width: 200px;"
                      @keyup.enter.native="handleFilter" clearable/>
            <el-select size="small" class="filter-item" v-model="listQuery.grade" style="width: 200px;" multiple placeholder="请选择">
              <el-option-group
                v-for="group in options3"
                :key="group.label"
                :label="group.label">
                <el-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-option-group>
            </el-select>
            <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px;" size="small" v-if="hasPerm('course-teacher:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>
            <el-button class="filter-item" type="primary" icon="el-icon-edit" style="margin-left: 0px;" size="small"  v-if="hasPerm('course-teacher:add')" @click="showCreate">添加</el-button>
            <el-button class="filter-item" type="primary" icon="el-icon-download" style="margin-left: 0px;" size="small" v-if="hasPerm('course-teacher:list')" @click="handleFilter">{{ $t('table.export') }}</el-button>
            <el-checkbox style="margin-left: 0px;" v-model="listQuery.myself">只看自己</el-checkbox>
            <!--<el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>-->

          </el-form-item>
      </el-form>
    </div>

    <el-table :data="list" height="530" v-loading.body="listLoading" border fit
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
      <el-table-column align="center" prop="tuition" label="学费" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="courseDate" label="课程时间" style="width: 60px;">
        <template slot-scope="scope">
          &nbsp;
          <span v-if="scope.row.courseDate.tue==true">{{$t('week.tue')}}</span>&nbsp;
          <span v-if="scope.row.courseDate.wed==true">{{$t('week.wed')}}</span>&nbsp;
          <span v-if="scope.row.courseDate.thu==true">{{$t('week.thu')}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" width="170">
        <template slot-scope="scope">
          <span>{{scope.row.updateTime}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200" v-if="hasPerm('course-teacher:update')">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-popover
            placement="top"
            trigger="click"
            width="160">
            <p>确定删除此课程吗？</p>
            <div style="text-align: center; margin: 0">
              <el-button type="primary" size="mini" @click="deleteCourse(scope.row.id)">确定</el-button>
            </div>
            <el-button type="danger" size="small" v-if="hasPerm('course-teacher:delete')" slot="reference">删除</el-button>
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
      <el-row :gutter="24">
          <el-form class="small-space" :model="tempCourse" label-position="left" label-width="100px"
                   style='width: 400px; margin-left:50px;'>
              <el-form-item label="课程名">
                <el-input type="text" v-model="tempCourse.content" clearable>
                </el-input>
              </el-form-item>
              <el-form-item label="学生数">
                <el-input-number v-model="tempCourse.capacity"
                                 :min="1" :max="100" clearable>
                </el-input-number>
              </el-form-item>
              <el-form-item label="课程时间">
                <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" size="small" @change="handleCheckAllChange" border>{{$t('common.checkAll')}}</el-checkbox>
                <div style="margin: 15px 0;"></div>
                <el-checkbox-group v-model="checkedCourseDate" @change="handleCheckedCitiesChange" size="small">
                  <el-checkbox v-for="dateItem in courseDates" :label="dateItem" :key="dateItem" border>{{$t('week.'+dateItem)}}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="课程类型">
                <el-radio v-model="tempCourse.type" size="small" label="1" border>中教</el-radio>
                <el-radio v-model="tempCourse.type" size="small" label="2" border>外教</el-radio>
                <el-radio v-model="tempCourse.type" size="small" label="3" border>外聘</el-radio>
              </el-form-item>
              <el-form-item label="授课年级">
                <el-select v-model="tempCourse.grade" multiple placeholder="请选择">
                  <el-option-group
                    v-for="group in options3"
                    :key="group.label"
                    :label="group.label">
                    <el-option
                      v-for="item in group.options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="学费">

                <el-row>
                  <el-col :span="16">
                    <el-input type="text" v-model="tempCourse.tuition" clearable>
                    </el-input>
                  </el-col>
                  <el-col :span="7" :offset="1">

                    <el-select v-model="tempCourse.value" placeholder="请选择">
                      <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-col>
                </el-row>

              </el-form-item>

              <el-form-item label="简介">
                <el-input type="textarea" :rows="5" v-model="tempCourse.brief"></el-input>
              </el-form-item>
          </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createCourse">创 建</el-button>
        <el-button type="primary" v-else @click="updateCourse">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

  const courseDateOptions = ['tue', 'wed', 'thu'];
  export default {
    data() {
      return {
        checkAll: false,
        courseDates: courseDateOptions,
        checkedCourseDate: [],
        isIndeterminate: true,

        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          content: '',
          myself: false
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建课程'
        },
        tempCourse: {
          id: "",
          content: "",
          capacity: 25,
          tuition: "",
          type: "",
          grade:[],
          courseDate:{
            tue: false,
            wed: false,
            thu: false
          },
          brief: "",
        },
        options: [{
          value: '1',
          label: '/人'
        }, {
          value: '2',
          label: '/课'
        }, {
          value: '3',
          label: '/学期'
        }],
          options3: [{
          label: '热门城市',
          options: [{
            value: 'Shanghai',
            label: '上海'
          }, {
            value: 'Beijing',
            label: '北京'
          }]
        }, {
          label: '城市名',
          options: [{
            value: 'Chengdu',
            label: '成都'
          }, {
            value: 'Shenzhen',
            label: '深圳'
          }, {
            value: 'Guangzhou',
            label: '广州'
          }, {
            value: 'Dalian',
            label: '大连'
          }]
        }],
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
      },
      handleCheckedCitiesChange(value) {
        var checkedCount = value.length;
        this.checkAll = checkedCount === this.courseDates.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.courseDates.length;
        let that = this;
        that.tempCourse.courseDate = {
          tue: false,
          wed: false,
          thu: false
        }
        this.checkedCourseDate.forEach(function(i){
          that.tempCourse.courseDate[i] = true;
        });
      },
      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },
      getList() {
        //查询列表
        if (!this.hasPerm('course-teacher:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/course-teacher/listCourse",
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
        this.tempCourse.capadateItem = 25;
        this.tempCourse.tuition = "";
        this.tempCourse.brief = "";
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.isIndeterminate = false;
        this.checkedCourseDate = [];
        this.checkAll = false;
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempCourse.id = this.list[$index].id;
        this.tempCourse.content = this.list[$index].content;
        this.tempCourse.capadateItem = this.list[$index].capadateItem;
        this.tempCourse.tuition = this.list[$index].tuition;
        this.tempCourse.brief = this.list[$index].brief;
        this.tempCourse.courseDate = this.list[$index].courseDate;
        this.checkedCourseDate = [];
        for(var i in this.tempCourse.courseDate){
          if(this.tempCourse.courseDate[i]){
            this.checkedCourseDate.push(i);
          }
        }
        if(this.checkedCourseDate.length == courseDateOptions.length){
          this.isIndeterminate = false;
          this.checkAll = true;
        }
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createCourse() {

        //保存新课程
        this.api({
          url: "/course-teacher/addCourse",
          method: "post",
          data: this.tempCourse
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateCourse() {
        //修改课程
        if (this.active++ > 2) {
          this.api({
            url: "/course-teacher/updateCourse",
            method: "post",
            data: this.tempCourse
          }).then(() => {
            this.getList();
          this.dialogFormVisible = false
        });
        }
      },
      deleteCourse(tmpId) {
        //删除课程
        this.api({
          url: "/course-teacher/deleteCourse",
          method: "post",
          data: {id: tmpId}
        }).then(() => {
          //this.$refs['searchBtn'].focus();
          this.$message.success(this.$t('common.deleteSuccess'));
          this.getList();
          this.dialogFormVisible = false;
        })
      },
    }
  }
</script>
<style>
  .el-input-number__increase{
    right: 11px;
  }
  .el-form-item__content .el-select, .el-form-item__content .el-input-number{
    width:102%;
  }
</style>
