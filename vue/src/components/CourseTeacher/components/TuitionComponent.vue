<template>
  <div>
  <el-row>
    <el-radio v-model="tuition.tuitionType" v-bind:ref="tuition.tuitionType" label="fee">付费</el-radio>
    <el-radio v-model="tuition.tuitionType" v-bind:ref="tuition.tuitionType" label="free">免费</el-radio>
  </el-row>
  <el-row v-if="tuition.tuitionType == 'fee'">
    <el-col :span="16">
      <el-input type="text" v-model="tuition.tuition" clearable>
        <template slot="append">RMB</template>
      </el-input>
    </el-col>
    <el-col :span="7" :offset="1">
      <el-select v-model="tuition.tuitionSubType" placeholder="请选择">
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
                label: '/人'
              }, {
                value: '2',
                label: '/课'
              }, {
                value: '3',
                label: '/学期'
              }],
            }
        }
    }
</script>

<style scoped>

</style>
