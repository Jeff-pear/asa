import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import {getToken} from '@/utils/auth'
import store from '../store'
// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 15000                  // 请求超时时间2
})


// download url
const downloadUrl = url => {
  let iframe = document.createElement('iframe')
  iframe.style.display = 'none'
  iframe.src = url
  iframe.onload = function () {
    document.body.removeChild(iframe)
  }
  document.body.appendChild(iframe)
}

// request拦截器
service.interceptors.request.use(config => {
  if(store.getters.language == '' || store.getters.language =='en'){
    config.headers.common['Accept-Language'] = 'en-US'
  }else{
    config.headers.common['Accept-Language'] = 'zh-CN'
  }
  return config
}, error => {
  // Do something with request error
  console.error(error) // for debug
  Promise.reject(error)
})
// respone拦截器
service.interceptors.response.use(
  response => {
  // 处理download文件
  if (response.headers && (response.headers['content-type'] === 'application/force-download' ||
      response.headers['content-type'] === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' ||
      response.headers['content-type'] === 'application/msword' ||
      response.headers['content-type'] === 'application/pdf')) {
    downloadUrl(response.request.responseURL)
    response.data='';
    response.headers['content-type'] = 'text/json'
    return response;
  }

    const res = response.data;
    if (res.returnCode == '1000') {
      return res;
    }
    if (res.returnCode == '100') {
      return res.returnData;
    } else if (res.returnCode == "20011") {
      Message({
        showClose: true,
        message: res.returnMsg,
        type: 'error',
        duration: 500,
        onClose: () => {
          store.dispatch('FedLogOut').then(() => {
            location.reload()// 为了重新实例化vue-router对象 避免bug
          })
        }
      });
      return Promise.reject("未登录")
    } else {
      Message({
        message: res.returnMsg,
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject(res)
    }
  },
  error => {
    console.error('err' + error)// for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)
export default service

