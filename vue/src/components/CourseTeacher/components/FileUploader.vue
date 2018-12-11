<template>
  <el-upload
    class="upload-demo"
    drag
    :limit="1"
    ref="upload"
    :file-list="fileList"
    :on-success="handleFileUploadSuccess"
    :on-preview="previewUploadFile"
    :on-exceed="handleExceed"
    :on-remove="handleFileRemove"
    :before-remove="beforeFileRemove"
    list-type="text"
    :accept="acceptStr"
    :auto-upload="true"
    action="/api/sys/uploadFile"
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">{{$t('teacher.uploadTip1')}}<em>{{$t('teacher.uploadTip2')}}</em></div>
    <div class="el-upload__tip" slot="tip">
      <!--<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button><br/>-->
      {{$t('teacher.uploadTip3')}}</div>
  </el-upload>
</template>

<script>
    export default {
      name: "FileUploader",
      props:['dataVal','fileListArr','businessId','businessType'],
      data(){
        return {
          attachId: this.$props['dataVal'],
          fileList: this.$props['fileListArr'],//上传文件list
          busId: this.$props['businessId'],//上传文件list
          busType: this.$props['businessType'],
          acceptStr: this.$props['businessType'] == 'course-teacher'?
            'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document':
            'image/jpeg,image/gif,image/png,image/bmp'
        }
      },
      methods: {
        handleFileRemove(file, fileList) {

          console.log(file, fileList);
          this.fileList = [];
          this.$emit('fileChangeToFather',this.fileList);
        },
        beforeFileRemove(file, fileList) {
          this.api({
            url: "/sys/deleteAttachment/",
            method: "post",
            data: {
              id: file.url,
              businessId: this.busId
            }
          }).then(response  => {
            this.$message.success(this.$t('common.deleteSuccess'));
          }).catch(()=>{
            this.$message.error(this.$t('common.deleteFail'));
          });
        },
        handleExceed(files, fileList) {
          this.$message.warning(this.$t('common.restrictionUpload'));
        },
        previewUploadFile(obj){
          this.api({
            url: "/sys/download/"+obj['url'],
            method: "get",
            // responseType: 'blob'
          }).then(response  => {
            this.$message.success(this.$t('common.downloadSuccess'));
          });
        },
        handleFileUploadSuccess(result){
          this.fileList.push({
            name:result.returnData.originFileName,
            url: result.returnData.attachId
          });
          //update attachment table
          debugger
          this.api({
            url: "/sys/updateAttachment/",
            method: "post",
            data: {
              id: result.returnData.attachId,
              businessId: this.$props['businessId'],
              businessType: this.businessType
            }
          }).then(response  => {
            this.$message.success(this.$t('common.uploadSuccess'));

          this.$emit('fileChangeToFather',this.fileList);
          });

        },

        submitUpload() {
          this.$refs.upload.submit();
        },
      }
    }
</script>

<style scoped>

</style>
