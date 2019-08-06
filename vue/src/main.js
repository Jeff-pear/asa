import Vue from 'vue'
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import {default as api} from './utils/api'
import {hasPermission, getGroupTag, getPeriod, needResetPwd} from "./utils/hasPermission";
import i18n from './lang' // Internationalization
Vue.use(ElementUI, {  i18n: (key, value) => i18n.t(key, value)})
Vue.prototype.api = api
//全局的常量
Vue.prototype.hasPerm = hasPermission
Vue.prototype.getGroupTag = getGroupTag
Vue.prototype.getPeriod = getPeriod;
Vue.prototype.needResetPwd = needResetPwd;
//生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = (process.env.NODE_ENV != 'production');
Vue.prototype.$bus = new Vue();
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  template: '<App/>',
  components: {App}
});
/**
 * 对象合并polyfill
 * */
  if (Object.assign) {
    Object.defineProperty(Object, "assign", {
      enumerable: false,
      configurable: true,
      writable: true,
      value: function (target, firstSource) {
        "use strict";
        if (target === undefined || target === null)
          throw new TypeError("Cannot convert first argument to object");
        var to = Object(target);
        for (var i = 1; i < arguments.length; i++) {
          var nextSource = arguments[i];
          if (nextSource === undefined || nextSource === null) continue;
          var keysArray = Object.keys(Object(nextSource));
          for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
            var nextKey = keysArray[nextIndex];
            var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
            if (desc !== undefined && desc.enumerable) to[nextKey] = nextSource[nextKey];
          }
        }
        return to;
      }
    });
  }
