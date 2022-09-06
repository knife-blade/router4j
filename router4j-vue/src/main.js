import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import './icons' // icon
import ElementUI  from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';

import '@/styles/index.scss' // global css

import _ from 'lodash'

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.use(ElementUI);
Vue.config.productionTip = false
Vue.prototype._ = _
Vue.use(mavonEditor)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
