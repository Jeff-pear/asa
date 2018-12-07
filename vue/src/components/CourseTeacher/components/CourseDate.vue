<template>
  <div>
    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" size="small" @change="handleCheckAllChange" border>{{$t('common.checkAll')}}</el-checkbox>
    <div style="margin: 15px 0;"></div>
    <el-checkbox-group v-model="checkedCourseDate" @change="handleCheckedCitiesChange" size="small">
      <el-checkbox v-for="dateItem in courseDates" :label="dateItem" :key="dateItem" border>{{$t('week.'+dateItem)}}</el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
  const courseDateOptions = ['tue', 'wed', 'thu'];
    export default {
      name: "course-date",
      data() {
        return {
          checkAll: false,
          isIndeterminate: true,
          checkedCourseDate: [],
          courseDates: courseDateOptions,
        }
      },
      methods:{
        handleCheckAllChange(val) {
          this.checkedCourseDate = val ? courseDateOptions : [];
          this.isIndeterminate = false;
          if(val){
            this.tempCourse.courseDate = 7;
            this.tempCourse.courseDateArr = ['tue','wed','thu'];
          }else{
            this.tempCourse.courseDate = 0;
            this.tempCourse.courseDateArr = [];
          }
        },
        handleCheckedCitiesChange(value) {
          var checkedCount = value.length;
          this.checkAll = checkedCount === this.courseDates.length;
          this.isIndeterminate = checkedCount > 0 && checkedCount < this.courseDates.length;
          let that = this;
          that.tempCourse.courseDate = 0;
          this.tempCourse.courseDateArr = value;
          value.forEach(function(i){
            if(i == 'tue'){
              that.tempCourse.courseDate = that.tempCourse.courseDate+1;
            }
            if(i == 'wed'){
              that.tempCourse.courseDate = that.tempCourse.courseDate+2;
            }
            if(i == 'thu'){
              that.tempCourse.courseDate = that.tempCourse.courseDate+4;
            }
          });
        },
      }
    }
</script>

<style scoped>

</style>
