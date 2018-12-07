<template>
  <div>
  <el-row>
    <el-radio v-model="tuition.tuitionType" v-bind:ref="tuition.tuitionType" label="fee">{{$t('teacher.tuitionNotFree') }}</el-radio>
    <el-radio v-model="tuition.tuitionType" v-bind:ref="tuition.tuitionType" label="free">{{$t('teacher.tuitionFree') }}</el-radio>
  </el-row>
  <el-row v-if="tuition.tuitionType == 'fee'">
    <el-col :span="12">
      <el-input type="text" v-model="tuition.tuition" clearable>
        <template slot="append">RMB</template>
      </el-input>
    </el-col>
    <el-col :span="11" :offset="1">
      <el-select v-model="tuition.tuitionSubType" :placeholder="$t('common.pleaseSelect')">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-col>
  </el-row>
  </div>
</template>

<script>
    export default {
      name: "TuitionComponent",
      props:['dataTuition','dataTuitionType','dataTuitionSubType'],
      data() {
        return {
          tuition: {
            tuitionType: this.$props['dataTuitionType'],
            tuition: this.$props['dataTuition'],
            tuitionSubType:this.$props['dataTuitionSubType']
          },
              options: [{
                value: '1',
                label: this.$t('teacher.tuitionOption1')
              }, {
                value: '2',
                label: this.$t('teacher.tuitionOption2')
              }, {
                value: '3',
                label: this.$t('teacher.tuitionOption3')
              }],
            }
        }
    }
</script>

<style scoped>

</style>
