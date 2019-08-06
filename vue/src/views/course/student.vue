<template>
  <div class="app-container">

    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane :label="total" name="first">
        <student-table listUrl="/course-student/listTeacherCourse" v-on:changeTab="changeTabIndex" isMySelect="false" ref="tabList.courseList" v-on:getTotal="getTotal"></student-table>
      </el-tab-pane>
      <el-tab-pane :label="myTotal" name="second">
        <student-table listUrl="/course-student/listCourse" isMySelect="true" ref="tabList.mySelect" v-on:getTotal="getMyTotal"></student-table>
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
        total: '',
        myTotal: '',
        activeTab: 'first',
      }
    },
    methods:{
      getTotal(data) {
        this.total = this.$t('teacher.listCourse') + ' ('+data +')'
      },
      getMyTotal(data) {
        this.myTotal = this.$t('student.selectedCourse') + ' ('+data +')'
      },

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
      },

    }
  }
</script>
