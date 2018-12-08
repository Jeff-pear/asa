<template>
  <div>
    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" size="small" @change="handleCheckAllChange" border>{{$t('common.checkAll')}}</el-checkbox>
    <div style="margin: 15px 0;"></div>
    <el-checkbox-group v-model="courseDateArr" @change="handleCheckedCitiesChange" size="small">
      <el-checkbox v-for="dateItem in courseDates" :label="dateItem" :key="dateItem" border>{{$t('week.'+dateItem)}}</el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
  const courseDateOptions = ['tue', 'wed', 'thu'];
    export default {
      name: "course-date",
      props:['dataVal','dataArr','dataCheckArr','dataCheckAll','dataIsIndeterminate'],
      data() {
        return {
          checkAll: this.dataCheckAll,
          isIndeterminate: this.dataIsIndeterminate,
          courseDate: this.dataVal,
          courseDateArr:this.dataArr,
          courseDates: courseDateOptions,
        }
      },
      methods:{
        handleCheckAllChange(val) {
          this.$emit('changeCourseDate',value);
          this.courseDateArr = val ? courseDateOptions : [];
          this.isIndeterminate = false;
          if(val){
            this.courseDate = 7;
            this.courseDateArr = ['tue','wed','thu'];
          }else{
            this.courseDate = 0;
            this.courseDateArr = [];
          }
        },
        handleCheckedCitiesChange(value) {
          this.$emit('changeCourseDate',value);
          var checkedCount = value.length;
          this.checkAll = checkedCount === this.courseDates.length;
          this.isIndeterminate = checkedCount > 0 && checkedCount < this.courseDates.length;
          let that = this;
          that.courseDate = 0;
          this.courseDateArr = value;
          value.forEach(function(i){
            if(i == 'tue'){
              that.courseDate = that.courseDate+1;
            }
            if(i == 'wed'){
              that.courseDate = that.courseDate+2;
            }
            if(i == 'thu'){
              that.courseDate = that.courseDate+4;
            }
          });
        },
      }
    }
</script>

<style scoped>

</style>
