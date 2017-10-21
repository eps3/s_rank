// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui'
import api from './api'
import util from "./util"
import 'element-ui/lib/theme-default/index.css'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.prototype.$ajax = axios

router.beforeEach(({ name }, from, next) => {
  if (api.isLogin()) {
    // 如果用户在login页面
    if (name === 'Login') {
      next('/');
    } else {
      next();
    }
  } else {
    if (name === 'addTopic') {
      next({ name: 'Login' });
    } else {
      next();
    }
  }
})

Vue.filter("formatDate", time => {
  var date = new Date(time);
  return util.formatDate(date, 'yyyy-MM-dd hh:mm');
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
