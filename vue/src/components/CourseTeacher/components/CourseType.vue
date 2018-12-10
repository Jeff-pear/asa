<template>
<div>
  <el-radio v-model="teacherType" size="small" label="0" style="margin-left: 10px;" border>{{$t('teacher.chineseTeacher') }}</el-radio>
  <el-radio v-model="teacherType" size="small" label="1" border>{{$t('teacher.foreignTeacher') }}</el-radio>
  <el-radio v-model="teacherType" size="small" label="2" border>{{$t('teacher.externalTeacher') }}</el-radio>
  <br/>
  <div style="margin-top: 15px;padding-left: 10px;" class="demo-input-suffix" v-if="teacherType==2">
    <el-form-item :label="$t('teacher.needSelfMaterial')">
      <el-radio v-model="needTrainingAid" size="small" label="1" >{{$t('teacher.yes')}}</el-radio>
      <el-radio v-model="needTrainingAid" size="small" label="2" >{{$t('teacher.no')}}</el-radio>
    </el-form-item>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-form-item >
          <el-input type="number" v-model.number="phone" :placeholder="$t('register.phone')" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <el-input type="number" v-model="price" min="0" :placeholder="$t('teacher.price')"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-input style="margin-top: 10px;" v-model="recommendBrand" type="text" :placeholder="$t('teacher.recommendBrand')"></el-input>
  </div>

</div>
</template>

<script>
    export default {
        name: "CourseType",
        props:['dataVal','dataNeedTrainingAid','dataPhone','dataPrice','dataRecommendBrand'],
        data() {
          return {
              teacherType: this.$props['dataVal'],
              needTrainingAid: this.dataNeedTrainingAid || 1,
              phone: this.dataPhone,
              price: this.dataPrice,
              recommendBrand: this.dataRecommendBrand
          }
        }
    }
</script>

<style scoped>

</style>
