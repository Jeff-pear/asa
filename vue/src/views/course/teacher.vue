<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="课程列表" name="first">
        <teacher-table listUrl="listCourse" showMyBtn="false" ref="tabList.courseList"></teacher-table>
      </el-tab-pane>
      <el-tab-pane label="我的课程" name="second">
        <teacher-table listUrl="myListCourse" showMyBtn="true" ref="tabList.mySelect"></teacher-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import TeacherTable from '@/components/CourseTeacher';
  const courseDateOptions = ['tue', 'wed', 'thu'];
  export default {
    components: {
      TeacherTable,
    },
    data() {
      return {
        activeTab: 'first',
      }
    },
    methods: {
      handleTabClick: function(tab,event){
        if(tab.index=="0"){
          this.$refs['tabList.courseList'].getList();
        }else{
          this.$refs['tabList.mySelect'].getList();
        }
      }

    }
  }
</script>
