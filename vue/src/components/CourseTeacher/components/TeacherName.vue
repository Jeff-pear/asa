<template>
  <el-select v-model="teacherName" :placeholder="$t('common.pleaseSelect')">
    <el-option
      v-for="item in allTeacher"
      :key="item.id"
      :label="item.nickname"
      :value="item.id">

      <span style="float: left">En: {{ item.nickname }}</span>
      <span style="float: right; color: #8492a6; font-size: 13px">Zh: {{ item.nickname_cn }}</span>
    </el-option>
  </el-select>
</template>

<script>
  import store from '../../../store'
    export default {
      name: "teacher-name",
      props:['dataVal'],
      data() {
        return {
          teacherName: this.$props['dataVal'] || store.getters.userId,
          allTeacher:[],
        }
      },
      methods: {
        listAllTeacher(){
          this.api({
            url: "/course-teacher/listAllTeacher",
            method: "get",
            params: {}
          }).then(data => {
            this.allTeacher = data
        });
        }
      },
      created(){
        this.listAllTeacher();
      }
    }
</script>

<style scoped>

</style>
