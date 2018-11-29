<template>
  <div class="app-container">

    <el-tabs v-model="activeTab" type="card" >
      <el-tab-pane label="课程列表" name="first">

        <div class="filter-container">
          <el-form :model="listQuery" ref="listQuery">
            <el-form-item prop="content">
              <el-input class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.content"
                        size="small" v-if="hasPerm('course-teacher:list')" ref="searchBtn" style="width: 200px;"
                        @keyup.enter.native="handleFilter" clearable/>
              <el-button class="filter-item" type="primary" icon="el-icon-search" size="small" v-if="hasPerm('course-student:list')" @click="handleFilter">{{ $t('table.search') }}</el-button>
              </el-button>
              <el-button class="filter-item" size="small" style="margin-left: 0px;" @click="resetForm('listQuery')">重置</el-button>
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
          <el-table-column align="center" prop="tuition" label="学费" style="width: 60px;"></el-table-column>
          <el-table-column align="center" prop="courseDate" label="课程时间" style="width: 60px;">
            <template slot-scope="scope">
              &nbsp;
              <span v-if="scope.row.courseDate.tue==true">{{$t('week.tue')}}</span>&nbsp;
              <span v-if="scope.row.courseDate.wed==true">{{$t('week.wed')}}</span>&nbsp;
              <span v-if="scope.row.courseDate.thu==true">{{$t('week.thu')}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="nickname" label="教师" style="width: 60px;"></el-table-column>
          <el-table-column align="center" label="操作" width="200" v-if="hasPerm('course-student:update')">
            <template slot-scope="scope">
              <el-button type="primary" icon="edit" size="small" @click="showUpdate(scope.$index)">选课</el-button>
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
      </el-tab-pane>
      <el-tab-pane label="我的课程" name="second">我的课程</el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  export default {
    data() {
      return {
        activeTab: 'first',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
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
          content: ""
        }
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
          url: "/course-student/listCourse",
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

    }
  }
</script>
