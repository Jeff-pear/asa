import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/register', component: _import('register/index'),hidden: true},
  {path: '/active', component: _import('register/active'),hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
   mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/course',
    component: Layout,
    redirect: '/course/course-teacher',
    name: '课程管理',
    meta: {title: 'courseManagement', icon: 'tree'},
    children: [
      {
        path: 'course-teacher',
        name: '课程',
        component: _import('course/teacher'),
        meta: {title: 'course', icon: 'course'},
        menu: 'course-teacher'
      },
      {
        path: 'course-student',
        name: '学生选课',
        component: _import('course/student'),
        meta: {title: 'courseStudent', icon: 'student'},
        menu: 'course-student'
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '用户权限',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '', name: '用户列表', component: _import('user/user'), meta: {title: '用户列表', icon: 'user'}, menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
    ]
  },
  {
    path: '/sys',
    component: Layout,
    redirect: '/sys/period',
    name: '系统设置',
    meta: {title: '系统设置', icon: 'system'},
    children: [
      {
        path: 'period',
        name: '选课周期',
        component: _import('sys/period'),
        meta: {title: '选课周期', icon: 'peroid'},
        menu: 'period'
      },
      {
        path: 'email',
        name: '邮件管理',
        component: _import('sys/email'),
        meta: {title: '邮件管理', icon: 'mail'},
        menu: 'email'
      },
    ]
  },

  {path: '*', redirect: '/404', hidden: true}
]
