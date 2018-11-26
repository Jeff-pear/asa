<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('course-teacher:add')">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" height="530" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="content" label="课程名" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创建时间" width="170">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200" v-if="hasPerm('course-teacher:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
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
      <el-form class="small-space" :model="tempCourse" label-position="left" label-width="100px"
               style='width: 400px; margin-left:50px;'>
        <el-form-item label="课程名">
          <el-input type="text" v-model="tempCourse.content">
          </el-input>
        </el-form-item>
        <el-form-item label="学生数">
          <el-input-number v-model="tempCourse.capacity"
                           :min="1" :max="100">
          </el-input-number>
        </el-form-item>
        <el-form-item label="学费">
          <el-input type="text" v-model="tempCourse.tuition">
          </el-input>
        </el-form-item>
        <el-form-item label="课程时间">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" size="small" @change="handleCheckAllChange" border>全选</el-checkbox>
          <div style="margin: 15px 0;"></div>
          <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange" size="small">
            <el-checkbox v-for="city in cities" :label="city" :key="city" border>{{city}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" :rows="5" v-model="tempCourse.brief"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createCourse">创 建</el-button>
        <el-button type="primary" v-else @click="updateCourse">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  const cityOptions = ['周二', '周三', '周四'];
  export default {
    data() {
      return {
        checkAll: false,
        cities: cityOptions,
        checkedCities: [],
        isIndeterminate: true,

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
          content: "",
          capacity: 25,
          tuition: "",
          courseDate:{
            tue: false,
            wed: false,
            thu: false
          },
          brief: "",
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      handleCheckAllChange(val) {

        this.checkedCities = val ? cityOptions : [];
        this.isIndeterminate = false;
      },
      handleCheckedCitiesChange(value) {
        var checkedCount = value.length;
        this.checkAll = checkedCount === this.cities.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;

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
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempCourse.id = this.list[$index].id;
        this.tempCourse.content = this.list[$index].content;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createCourse() {
        //保存新文章
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
        //修改文章
        this.api({
          url: "/course-teacher/updateCourse",
          method: "post",
          data: this.tempCourse
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
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
