<template>
  <div class="profile-container" >
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
      <el-button type="success" :loading="loading" @click.native.prevent="handleRegister">{{$t('table.confirm')}}</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "profile"
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
        }
      },
      methods: {
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
</style>
