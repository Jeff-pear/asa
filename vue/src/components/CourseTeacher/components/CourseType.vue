<template>
<div>
  <el-radio v-model="teacherType" size="small" label="0" border>中教</el-radio>
  <el-radio v-model="teacherType" size="small" label="1" border>外教</el-radio>
  <el-radio v-model="teacherType" size="small" label="2" border>外聘</el-radio>
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
