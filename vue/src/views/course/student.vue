<template>
  <div class="app-container">

    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane :label="$t('teacher.listCourse') " name="first">
        <student-table listUrl="/course-student/listTeacherCourse" v-on:changeTab="changeTabIndex" isMySelect="false" ref="tabList.courseList"></student-table>
      </el-tab-pane>
      <el-tab-pane :label="$t('student.selectedCourse')" name="second">
        <student-table listUrl="/course-student/listCourse" isMySelect="true" ref="tabList.mySelect"></student-table>
      </el-tab-pane>
    </el-tabs>


  </div>
</template>
<script>
  import StudentTable from '@/components/CourseStudent'
  export default {
    components: {
      StudentTable,
    },
    data() {
      return {
        tabList:{
          courseList:"",
          mySelect:""
        },
        activeTab: 'first',
      }
    },
    methods:{
      changeTabIndex: function(){
        this.activeTab = "second";
        this.$refs['tabList.mySelect'].getList();
      },
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
