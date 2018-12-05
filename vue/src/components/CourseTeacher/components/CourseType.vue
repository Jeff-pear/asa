<template>
<div>
  <el-radio v-model="teacherType" size="small" label="0" border>{{$t('teacher.chineseTeacher') }}</el-radio>
  <el-radio v-model="teacherType" size="small" label="1" border>{{$t('teacher.foreignTeacher') }}</el-radio>
  <el-radio v-model="teacherType" size="small" label="2" border>{{$t('teacher.externalTeacher') }}</el-radio>
</div>
</template>

<script>
    export default {
        name: "CourseType",
        props:['dataVal'],
        data() {
          return {
              teacherType: this.$props['dataVal']
          }
        }
    }
</script>

<style scoped>

</style>
