<style rel="stylesheet/scss" lang="scss">
  @import "../../styles/mixin.scss";
  @import "../../styles/register.scss";
</style>
<template>
  <div class="register-container" >
    <el-form autoComplete="on" :model="registerForm" :rules="registerRules" ref="registerForm" label-position="left"
             label-width="0px"
             class="card-box register-form" >

      <h3 class="title">{{$t('register.title')}}
        <div style="text-align:right;">
          <lang-select class="set-language"/>
        </div>
        </h3>
      <!--<el-steps :active="activeStep" finish-status="success" style="margin-bottom: 10px" simple>-->
        <!--<el-step title="基本信息" icon="el-icon-edit"></el-step>-->
        <!--<el-step title="个性信息" icon="el-icon-phone"></el-step>-->
        <!--<el-step title="完成注册" icon="el-icon-success"></el-step>-->
      <!--</el-steps>-->
      <div class="loginBtn"><i class="el-icon-back"></i>
        <router-link to="/login"><a>{{$t('register.backToLogin')}}</a></router-link>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="roleId">
            <span class="svg-container">
              <svg-icon icon-class="user_type"></svg-icon>
            </span>
            <el-select @change="handleChange" v-model="registerForm.roleId" :placeholder="$t('register.userType')">
              <el-option
                v-for="item in roleList"
                :key="item.roleId"
                :label="item.roleName"
                :value="item.roleId">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="username">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="user"/>
            </span>
            <el-input v-model="registerForm.username" autoComplete="on" :placeholder="$t('register.username')"/>
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
                      autoComplete="off" :placeholder="$t('login.password')"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="passwordConfirm">
            <span class="svg-container">
              <svg-icon icon-class="password"></svg-icon>
            </span>
            <el-input type="password" v-model="registerForm.passwordConfirm"
                      autoComplete="off" :placeholder="$t('profile.confirmPwd')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="nickname">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="en"/>
            </span>
            <el-input v-model="registerForm.nickname" autoComplete="on" :placeholder="$t('register.enName')"/>
          </el-form-item>
        </el-col>
        <el-col :span="12" >
          <el-form-item prop="email">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="mail"/>
            </span>
            <el-input v-model="registerForm.email" autoComplete="on" placeholder="Email"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="nicknameCn">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="cn"/>
            </span>
            <el-input v-model="registerForm.nicknameCn" autoComplete="on" :placeholder="$t('register.chName')"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="phone">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="phone"/>
            </span>
            <el-input v-model="registerForm.phone" autoComplete="on" :placeholder="$t('register.phone')"/>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row :gutter="20">

        <el-col :span="12" v-if="registerForm.curUserType!='' && registerForm.curUserType==1">
          <el-form-item prop="class">
            <span class="svg-container svg-container_register">
              <svg-icon icon-class="class"/>
            </span>
            <el-select v-model="registerForm.class" :placeholder="$t('register.grade')">
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

      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleRegister">
              {{$t('register.confirm')}}
            </el-button>
          </el-form-item>
        </el-col>

      </el-row>

    </el-form>
  </div>
</template>
<script>
  import bgPic from '@/assets/common/star.jpeg';
  import LangSelect from '@/components/LangSelect';
  export default {
    name: 'register',
    components: { LangSelect },
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error(this.$t('validation.passwordRequire')));
        } else {
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error(this.$t('validation.passwordConfirmRequire')));
        } else if (value !== this.registerForm.password) {
          callback(new Error(this.$t('validation.differentFor2Times')));
        } else {
          callback();
        }
      };

      var validateEmail = (rule, value, callback) => {
        if (this.registerForm.curUserType==2 ) {
          if (value === '') {
            callback(new Error(this.$t('validation.emailRequire')));
          }else{
            this.api({
              url: "/register/checkEmail",
              method: "get",
              params: {email: this.registerForm.email}
            }).then(() => {
              callback();
          });
          }
        }else{
          callback();
        }

      };
      var validateUsername = (rule, value, callback) => {
        this.api({
          url: "/register/checkUsername",
          method: "get",
          params: {username: this.registerForm.username}
        }).then(() => {
          callback();
      });
      };
      return {
        bgPic: bgPic + '?' + +new Date(),
        roleList:[],
        activeStep:0,
        registerForm: {
          username: '',
          password: '',
          passwordConfirm: '',
          email: '',
          roleId: '',
          nickname: '',
          curUserType: '',
          nicknameCn: '',
          class: ''
        },
        registerRules: {
          roleId:[{required: true, trigger: 'blur', message: this.$t('validation.userTypeRequire')}],
          username: [{required: true, trigger: 'blur', message: this.$t('validation.userNameRequire')},
            {validator: validateUsername, trigger: 'blur'}],
          sex: [{required: true, trigger: 'blur', message: this.$t('validation.sexRequire')}],
          password: [{required: true, trigger: 'blur', message: this.$t('validation.passwordRequire')},{ validator: validatePass, trigger: 'blur' }],
          email: [
            { type: 'email', message: this.$t('validation.emailReg'), trigger: ['blur', 'change'] },
            { validator: validateEmail, trigger: 'blur' }],
          passwordConfirm: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          age: [{required: true, trigger: 'blur', message: "请输入年龄"}],
          phone: [{ type: 'number', message: this.$t('validation.phoneNeedNum')}],
          nickname: [{required: true, trigger: 'blur', message: this.$t('validation.nickNameRequire')}],
          //nicknameCn: [{required: true, trigger: 'blur', message: this.$t('validation.nickNameCnRequire')}],
          // class:[{required: true, trigger: 'blur', message: "请选择年级"}],
        },
        loading: false,
        // sexTypeSelectArr:[{
        //   value: 'male',
        //   label: '男'
        // }, {
        //   value: 'female',
        //   label: '女'
        // }],
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
          value: 'g6',
          label: 'G6'
        }, {
          value: 'g7',
          label: 'G7'
        }, {
          value: 'g8',
          label: 'G8'
        }, {
          value: 'g9',
          label: 'G9'
        }, {
          value: 'music',
          label: 'Music'
        }]

      }
    },
    created() {
      this.getRoleList();
    },
    methods: {
      handleChange(value){
        var resultArr = this.roleList.filter(function(i){if(i.roleId == value) return true;});
        if(resultArr && resultArr.length>0){
          this.registerForm.curUserType = resultArr[0].groupTag;
        }
      },
      handleRegister() {
        this.$refs.registerForm.validate(valid => {
          if (valid) {
            this.loading = true
              //创建用户
              this.api({
                url: "/register/create",
                method: "post",
                data: this.registerForm
              }).then(() => {
                var that = this;
                var message = '注册成功！将要跳转到登录页';
                if(that.registerForm.curUserType == '2'){
                  message = '注册验证码已发送至邮箱，请激活！';
                }
                this.$message({
                  showClose: true,
                  type: 'success',
                  message: message,
                  duration: 2000,
                  onClose: function(){
                    that.$router.push('/login');
                  }
              });

            });

          } else {
            return false
          }
        })
      },
      getRoleList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/register/listRole",
          method: "get"
        }).then(data => {
          this.listLoading = false;
        this.roleList = data.list;
      })
      },
    }
  }
</script>
