<template>
  <div class="profile-container" >

    <el-tabs v-model="activeName" style="margin-left:8px;width: 80%;">
      <el-tab-pane :label="$t('profile.personalTab')" name="first">
        <el-card class="box-card-component" >
          <div class="progress-item">
            <span class="label">{{$t('register.enName')}}:</span>
            <span>{{user[0] && user[0]['nickname']}}</span>
          </div>
          <div class="progress-item">
            <span class="label">{{$t('register.chName')}}:</span>
            <span>{{user[0] && user[0]['nicknameCn']}}</span>
          </div>
          <div class="progress-item">
            <span class="label">{{$t('common.role')}}:</span>
            <span v-if="user[0] && user[0]['roleName'] == '学生'">{{$t('common.studentRole')}}</span>
            <span v-if="user[0] && user[0]['roleName'] == '教师'">{{$t('common.teacherRole')}}</span>
          </div>
          <div class="progress-item" >
            <span class="label">{{$t('register.grade')}}:</span>
            <el-select size="small" v-model="user[0].grade" style="width:130px;" :placeholder="$t('common.pleaseSelect')" clearable>
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="progress-item">
            <span class="label">Class:</span>
            <span>{{user[0] && user[0]['class']}}</span>
          </div>
          <div class="progress-item" v-if="user[0] && user[0]['email']">
            <span class="label">Email:</span>
            <span>{{user[0] && user[0]['email']}}</span>
          </div>
          <div class="progress-item" >
              <el-button type="primary" size="small" @click="updateUserGrade">{{$t('table.confirm')}}</el-button>
          </div>
        </el-card>
      </el-tab-pane>
      <el-tab-pane :label="$t('profile.pwdTab')" name="second">
        <el-card class="box-card-component" >
          <div >
              <el-form class="small-space" status-icon ref="profileForm" :model="tempProfile" :rules="profileRules" label-position="left" label-width="140px"
                       style='width: 450px; margin-left:50px;'>
                <el-form-item :label="$t('profile.oldPwd')" prop="password">
                  <el-input type="password" @keyup.enter.native="handleRegister" v-model="tempProfile.password"
                            autoComplete="off" ></el-input>
                </el-form-item>
                <el-form-item :label="$t('profile.newPwd')" prop="newPassword">
                  <el-input  type="password" v-model="tempProfile.newPassword" autocomplete="off">
                  </el-input>
                </el-form-item>
                <el-form-item :label="$t('profile.confirmPwd')" prop="passwordConfirm">
                  <el-input  type="password" v-model="tempProfile.passwordConfirm" autocomplete="off">
                  </el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer footer-left" >
                <el-button type="success" size="small" :loading="loading" @click.native.prevent="handleRegister">{{$t('table.confirm')}}</el-button>
              </div>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
    export default {
        name: "profile",
      created(){
        this.getUser();
        if(this.$route.query && this.$route.query.tab == '1'){
          this.activeName='second';
        }
      }
      ,data() {

        var validatePass2 = (rule, value, callback) => {
          if (value === '') {
            callback(new Error(this.$t('validation.passwordConfirmRequire')));
          } else if (value !== this.tempProfile.newPassword) {
            callback(new Error(this.$t('validation.differentFor2Times')));
          } else {
            callback();
          }
        };

        return {
          loading: false,
          activeName: 'first',
          user:{},
          tempProfile: {
            password: "",
            newPassword: "",
            passwordConfirm: ""
          },
          profileRules: {
            password: [{required: true, trigger: 'blur', message: this.$t('validation.passwordRequire')}],
            newPassword: [{required: true, trigger: 'blur', message: this.$t('validation.passwordRequire')}],
            passwordConfirm: [
              {required: true, trigger: 'blur', message: this.$t('validation.passwordConfirmRequire')},{ validator: validatePass2, trigger: 'blur' }
            ],

          },
          options: [{
            value: 0,
            label: 'KG'
          }, {
            value: 1,
            label: 'G1'
          }, {
            value: 2,
            label: 'G2'
          }, {
            value: 3,
            label: 'G3'
          }, {
            value: 4,
            label: 'G4'
          }, {
            value: 5,
            label: 'G5'
          }, {
            value: 6,
            label: 'G6'
          }, {
            value: 7,
            label: 'G7'
          }, {
            value: 8,
            label: 'G8'
          }, {
            value: 9,
            label: 'G9'
          }],
        }
      },
      methods: {
        updateUserGrade(){
          this.api({
            url: "/sys/setGrade",
            method: "post",
            data: {grade: this.user[0].grade}
          }).then(() => {
            this.loading = false
            console.log("设置成功");
            this.$message.success("修改成功");
          }).catch((v) => {
            this.loading = false
            console.log(v);
          });
        },
        formatTooltip(val) {
          let resultVal = '';
          if(val == 0){
            resultVal = 'KG'
          }else{
            resultVal = 'G'+String(val);
          }
          return resultVal;
        },
        getUser(){
          this.api({
            url: "/sys/getUserById",
            method: "get"
          }).then(data => {
            this.user = data.list;
          })
        },
        handleRegister() {
          this.$refs.profileForm.validate(valid => {
            if (valid) {
              this.loading = true
              this.api({
                url: "/sys/setPassword",
                method: "post",
                data: this.tempProfile
              }).then(() => {
                this.loading = false
                console.log("设置成功");
              this.$message.success("设置成功");
            }).catch((v) => {
                this.loading = false
                console.log(v);
            });
            } else {
              return false
            }
          })
        },

      }
    }
</script>

<style rel="stylesheet/scss" lang="scss">
  .profile-container{
    padding: 20px;
    .footer-left{
      margin-left: 180px;
    }
  }

  .box-card-component {
    .box-card-header {
      position: relative;
      height: 220px;
      img {
        width: 100%;
        height: 100%;
        transition: all 0.2s linear;
        &:hover {
          transform: scale(1.1, 1.1);
          filter: contrast(130%);
        }
      }
    }
    .mallki-text {
      position: absolute;
      top: 0px;
      right: 0px;
      font-size: 20px;
      font-weight: bold;
    }
    .panThumb {
      z-index: 100;
      height: 70px!important;
      width: 70px!important;
      position: absolute!important;
      top: -45px;
      left: 0px;
      border: 5px solid #ffffff;
      background-color: #fff;
      margin: auto;
      box-shadow: none!important;
      /deep/ .pan-info {
        box-shadow: none!important;
      }
    }
    .progress-item {
      margin-bottom: 10px;
      padding-left: 25px;
      font-size: 14px;
      height: 32px;
      display: flex;
      .label{
        width: 150px;
      }
    }
    @media only screen and (max-width: 1510px){
      .mallki-text{
        display: none;
      }
    }
  }
</style>

