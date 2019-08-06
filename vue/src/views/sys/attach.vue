<template>
  <div class="attach-container" >

    <div class="main">
      <span class="blink">请谨慎操作</span>
    </div>
    <el-table :data="list" v-loading.body="listLoading" style="margin-top: 10px; " element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="附件Id" prop="id">
      </el-table-column>
      <el-table-column align="center" label="附件类型 " prop="type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == 'course-student'" type="success" >
            <span >学生附件</span>
          </el-tag>
          <el-tag v-else>
            <span >课程附件</span>
          </el-tag>

        </template>
      </el-table-column>
      <el-table-column align="center" label="课程名/学生名" prop="content">
      </el-table-column>
      <el-table-column align="center" label="课程Id" prop="business_id">
      </el-table-column>
      <el-table-column align="center" label="附件名" prop="origin_fileName">
        <template slot-scope="scope">
          <a target="_blank" style="text-decoration: underline;" @click="previewFromList(scope.row.id)" >{{scope.row.origin_fileName}}</a>
        </template>
      </el-table-column>
      <el-table-column align="center" label="附件位置" prop="location">
      </el-table-column>
      <el-table-column align="center" :label="$t('table.manage')" width="200" v-if="getGroupTag()=='-1' ">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeData(scope.$index)">{{$t('table.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>
<script>

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
    created() {
      this.getList();
    },
    methods: {
      removeData($index) {
        let _vue = this;
        this.$confirm('确定删除此附件?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let datas = _vue.list[$index];
          _vue.api({
            url: "/sys/deleteAttachment4Admin",
            method: "post",
            data: datas
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败!!")
          })
        })
      },
      previewFromList(obj){
        window.open('/api/sys/preview/'+obj);
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/sys/listAttach",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss">
  .attach-container{
    padding: 20px;
    .el-date-editor{
      .el-range-separator{
        width: 7%;
      }
    }
    .footer-left{
      margin-left: 180px;
    }
    .main{
      color: #666;margin-left: 10px;
    }
    /* 定义keyframe动画，命名为blink */
    @keyframes blink{
      0%{opacity: 1;}
      100%{opacity: 0;}
    }
    /* 添加兼容性前缀 */
    @-webkit-keyframes blink {
      0% { opacity: 1; }
      100% { opacity: 0; }
    }
    @-moz-keyframes blink {
      0% { opacity: 1; }
      100% { opacity: 0; }
    }
    @-ms-keyframes blink {
      0% {opacity: 1; }
      100% { opacity: 0;}
    }
    @-o-keyframes blink {
      0% { opacity: 1; }
      100% { opacity: 0; }
    }
    /* 定义blink类*/
    .blink{
      color: #dd4814;
      animation: blink 1s linear infinite;
      /* 其它浏览器兼容性前缀 */
      -webkit-animation: blink 1s linear infinite;
      -moz-animation: blink 1s linear infinite;
      -ms-animation: blink 1s linear infinite;
      -o-animation: blink 1s linear infinite;
    }
  }
</style>
