import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', name:'login', component: _import('login/index'), hidden: true},
  {path: '/register', name: 'register',component: _import('register/index'),hidden: true},
  {path: '/active', name: 'active',component: _import('register/active'),hidden: true},
  {path: '/404', name: 'nonePage',component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'indexPage',
    hidden: true,
    children: [{
      path: 'dashboard', name:'dashboard', component: _import('dashboard/index')
    },{
      path: 'profile', name:'profile', component: _import('user/profile')
    }]
  }
]
export default new Router({
   //mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/course',
    component: Layout,
    redirect: '/course/course-teacher',
    name: 'course',
    meta: {title: 'courseManagement', icon: 'tree'},
    children: [
      {
        path: 'course-teacher',
        name: 'courseTeacher',
        component: _import('course/teacher'),
        meta: {title: 'course', icon: 'course'},
        menu: 'course-teacher'
      },
      {
        path: 'course-student',
        name: 'courseStudent',
        component: _import('course/student'),
        meta: {title: 'courseStudent', icon: 'student'},
        menu: 'course-student'
      },
      {
        path: 'course-finance',
        name: 'courseFinance',
        component: _import('course/finance'),
        meta: {title: 'courseFinance', icon: 'hand'},
        menu: 'course-finance'
      },
      {
        path: 'course-notice',
        name: 'courseNotice',
        component: _import('course/notice'),
        meta: {title: 'courseNotice', icon: 'notice'},
        menu: 'course-notice'
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: 'userPower',
    meta: {title: 'userPower', icon: 'table'},
    children: [
      {
        path: '', name: 'userList', component: _import('user/user'), meta: {title: 'userList', icon: 'user'}, menu: 'user'
      },
      {
        path: 'role',
        name: 'roleManage',
        component: _import('user/role'),
        meta: {title: 'permissionList', icon: 'password'},
        menu: 'role'
      },
    ]
  },
  {
    path: '/sys',
    component: Layout,
    redirect: '/sys/period',
    name: 'system',
    meta: {title: 'system', icon: 'system'},
    children: [
      {
        path: 'period',
        name: 'period',
        component: _import('sys/period'),
        meta: {title: 'period', icon: 'peroid'},
        menu: 'period'
      },
      {
        path: 'email',
        name: 'email',
        component: _import('sys/email'),
        meta: {title: 'Email', icon: 'mail'},
        menu: 'email'
      },

    ]
  },

  {path: '*', redirect: '/404',  name:'noneA', hidden: true}
]
