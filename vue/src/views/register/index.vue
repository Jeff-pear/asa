<template>
  <div class="register-container">
    <el-form autoComplete="on" :model="registerForm" :rules="registerRules" ref="registerForm" label-position="left"
             label-width="0px"
             class="card-box register-form">
      <h3 class="title">Register SIBS ASA</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="userType">
            <span class="svg-container">
              <svg-icon icon-class="user_type"></svg-icon>
            </span>
            <el-select  v-model="registerForm.userType" placeholder="用户类型">
              <el-option
                v-for="item in userTypeSelectArr"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="username">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="user"/>
            </span>
            <el-input v-model="registerForm.username" autoComplete="on" placeholder="用户名"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password"></svg-icon>
            </span>
            <el-input type="password" @keyup.enter.native="handleRegister" v-model="registerForm.password"
                      autoComplete="off" placeholder="密码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="passwordConfirm">
            <span class="svg-container">
              <svg-icon icon-class="password"></svg-icon>
            </span>
            <el-input type="password" v-model="registerForm.passwordConfirm"
                      autoComplete="off" placeholder="确认密码"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="email">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="mail"/>
            </span>
            <el-input v-model="registerForm.email" autoComplete="on" placeholder="email"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="sex">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="sex"/>
            </span>
            <el-select  v-model="registerForm.sexType" placeholder="性别">
              <el-option
                v-for="item in sexTypeSelectArr"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="age">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="age"/>
            </span>

            <el-input v-model="registerForm.age" placeholder="年龄" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="class">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="class"/>
            </span>
            <el-select v-model="registerForm.class" multiple placeholder="授课年级">
              <el-option
                v-for="item in classTypeSelectArr"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>


      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleRegister">
          注册
        </el-button>
      </el-form-item>
      <div class="linkto">
        <router-link  to="/login">返回登录！</router-link>
      </div>
    </el-form>
  </div>
</template>
<script>
  export default {
    name: 'register',
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.registerForm.password !== '') {
            this.$refs.registerRules.validateField('password');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        registerForm: {
          username: '',
          password: '',
          passwordConfirm: '',
          email: '',
          userType: '',
          sexType: '',
          age: '',
          class: ''
        },
        registerRules: {
          userType:[{required: true, trigger: 'blur', message: "请选择用户类型"}],
          username: [{required: true, trigger: 'blur', message: "请输入用户名"}],
          sex: [{required: true, trigger: 'blur', message: "请选择性别"}],
          password: [{required: true, trigger: 'blur', message: "请输入密码"},{ validator: validatePass, trigger: 'blur' }],
          email: [{required: true, trigger: 'blur', message: "请输入邮箱"}, { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
          passwordConfirm: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          age: [{required: true, trigger: 'blur', message: "请输入年龄"}],
          class:[{required: true, trigger: 'blur', message: "请选择年级"}],
        },
        loading: false,
        userTypeSelectArr:[{
          value: 't',
          label: '教师'
        }, {
          value: 's',
          label: '学生'
        }],
        sexTypeSelectArr:[{
          value: 'male',
          label: '男'
        }, {
          value: 'female',
          label: '女'
        }],
        classTypeSelectArr:[{
          value: 'kg',
          label: 'KG'
        }, {
          value: 'g1',
          label: 'G1'
        }, {
          value: 'g2',
          label: 'G2'
        }, {
          value: 'g3',
          label: 'G3'
        }, {
          value: 'g4',
          label: 'G4'
        }, {
          value: 'g5',
          label: 'G5'
        }, {
          value: 'esl',
          label: 'ESL'
        }, {
          value: 'hseng',
          label: 'HS Eng'
        }, {
          value: 'hssc',
          label: 'HS SC'
        }, {
          value: 'pe',
          label: 'PE'
        }, {
          value: 'music',
          label: 'Music'
        }]

      }
    },
    methods: {
      handleRegister() {
        this.$refs.registerForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$store.dispatch('Register', this.registerForm).then(data => {
              this.loading = false
              if ("success" === data.result) {
                this.$router.push({path: '/'})
              } else {
                this.$message.error("账号/密码错误");
              }
            }).catch(() => {
              this.loading = false
            })
          } else {
            return false
          }
        })
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss">
  @import "../../styles/mixin.scss";
  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;
  html, body {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background: #001122;
    line-height: 0;
    font-size: 0;
  }
  .register-container {
    @include relative;
    height: 100vh;
    background-color: $bg;
    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
      -webkit-text-fill-color: #fff !important;
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
    }
    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
      &_register {
        font-size: 20px;
      }
    }
    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .register-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 800px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
    }
    .thirdparty-button {
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
    .linkto{
      color: #fff;
      text-decoration: underline;
    }
  }
</style>
