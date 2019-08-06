<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-input class="filter-item" placeholder="用户名" v-model="listQuery.username"
                    size="small" ref="searchBtn" style="width: 200px;"
                    @keyup.enter.native="handleFilter" clearable/>
          <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px;" size="small"  @click="handleFilter">{{ $t('table.search') }}</el-button>
          <el-button type="primary" class="filter-item" v-if="hasPerm('user:add')" icon="el-icon-edit" size="small" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" height="480" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <!--<el-table-column type="expand">-->
        <!--<template slot-scope="props">-->
          <!--<el-form label-position="left" inline class="demo-table-expand">-->
            <!--<el-form-item label="昵称">-->
              <!--<span>{{ props.row.nickname }}</span>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="创建时间">-->
              <!--<span>{{ props.row.createTime }}</span>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="最近修改时间">-->
              <!--<span>{{ props.row.updateTime }}</span>-->
            <!--</el-form-item>-->
          <!--</el-form>-->
        <!--</template>-->
      <!--</el-table-column>-->

      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户名" prop="username" ></el-table-column>
      <el-table-column align="center" label="英文名" prop="nickname" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="中文名" prop="nicknameCn" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="邮箱" prop="email" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="角色" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-text="scope.row.roleName" v-if="scope.row.roleId===1"></el-tag>
          <el-tooltip placement="top" effect="light" v-else-if="scope.row.roleId===4">
            <div slot="content">年级：{{formatGrade(scope.row.grade)}}<br/>班级：{{scope.row.class}}</div>
            <el-tag type="danger" v-text="scope.row.roleName" ></el-tag>
          </el-tooltip>


          <el-tag type="primary" v-text="scope.row.roleName" v-else></el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="170"></el-table-column>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="是否激活" >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.activeStatus == 1"><i class="el-icon-success"></i>&nbsp;激活</el-tag>
          <el-tag type="danger" v-else><i class="el-icon-error"></i>&nbsp;无效</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="155" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" size="mini" icon="delete" v-if="scope.row.userId!=userId "
                     @click="removeUser(scope.$index)">删除
          </el-button>
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
      <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="用户名" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempUser.username">
          </el-input>
        </el-form-item>
        <el-form-item label="email" >
          <el-input v-model="tempUser.email" />
        </el-form-item>
        <el-form-item label="密码" v-if="dialogStatus=='create'" required>
          <el-input type="password" v-model="tempUser.password">
          </el-input>
        </el-form-item>

        <el-form-item label="新密码" v-else>
          <el-input type="password" v-model="tempUser.password" placeholder="不填则表示不修改">
          </el-input>
        </el-form-item>
        <el-form-item label="角色" required style="width: 335px;">
          <el-select v-model="tempUser.roleId" placeholder="请选择">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="英文名" required>
          <el-input type="text" v-model="tempUser.nickname">
          </el-input>
        </el-form-item>
        <el-form-item label="中文名" required>
          <el-input type="text" v-model="tempUser.nicknameCn">
          </el-input>
        </el-form-item>
        <el-form-item label="年级" style="width: 335px;">
          <el-select placeholder="请选择" v-model="tempUser.grade">
            <el-option
                       v-for="item in options"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>

        </el-form-item>
        <el-form-item label="班级" >
          <el-input type="text" v-model="tempUser.class">
          </el-input>
        </el-form-item>
        <el-form-item label="激活状态" required>
          <el-radio v-model="tempUser.activeStatus" label="1">激活</el-radio>
          <el-radio v-model="tempUser.activeStatus" label="0">无效</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style>
  .demo-table-expand label{width:100px;color:#99a9bf}
  .demo-table-expand .el-form-item{margin-right:0;margin-bottom:0;width:50%}
  .el-form--inline .el-form-item{
    margin-right:0;margin-bottom:0;width:50%
  }
</style>
<script>
  import {mapGetters} from 'vuex'

  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          username: '',
        },
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
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          username: '',
          password: '',
          nickname: '',
          nicknameCn: '',
          roleId: '',
          userId: '',
          email: '',
          grade: '',
          class: '',
          activeStatus: ''
        }
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('user:add') || this.hasPerm('user:update')) {
        this.getAllRoles();
      }
    },
    computed: {
      ...mapGetters([
        'userId'
      ])
    },
    methods: {
      handleFilter(){
        this.listQuery.pageNum = 1;
        this.getList();
      },
      getAllRoles() {
        this.api({
          url: "/user/getAllRoles",
          method: "get"
        }).then(data => {
          this.roles = data.list;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/user/list",
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
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
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
        if(arrVal!=undefined){
          return formatSingle(arrVal);
        }
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.username = "";
        this.tempUser.password = "";
        this.tempUser.nickname = "";
        this.tempUser.nicknameCn = "";
        this.tempUser.roleId = "";
        this.tempUser.userId = "";
        this.tempUser.email = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.tempUser.username = user.username;
        this.tempUser.nickname = user.nickname;
        this.tempUser.nicknameCn = user.nicknameCn;
        this.tempUser.roleId = user.roleId;
        this.tempUser.userId = user.userId;
        this.tempUser.email = user.email;
        this.tempUser.deleteStatus = '1';
        this.tempUser.activeStatus = user.activeStatus;
        this.tempUser.password = '';
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createUser() {
        //添加新用户
        this.api({
          url: "/user/addUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateUser() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/user/updateUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.userId === this.tempUser.userId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
          user.deleteStatus = '2';
          user.username += '__d';
          user.nickname += '__d';
          user.nicknameCn += '__d';
          user.email += '__d';
          _vue.api({
            url: "/user/updateUser",
            method: "post",
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
    }
  }
</script>
