<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane :label="$t('teacher.listCourse') " name="first">
        <teacher-table listUrl="listCourse" showMyBtn="false" ref="tabList.courseList"></teacher-table>
      </el-tab-pane>
      <el-tab-pane :label="$t('teacher.myCourse') " name="second" v-if="getGroupTag()!='-1'">
        <teacher-table listUrl="myListCourse" showMyBtn="true" ref="tabList.mySelect" ></teacher-table>
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
