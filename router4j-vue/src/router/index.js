import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/ruleList',
        children: [
            {
                path: 'ruleList',
                component: () => import('@/views/rule/RuleList'),
                name: 'RuleList',
                meta: {title: '规则管理', icon: 'home', affix: true}
            }
        ]
    },

]

const router = new VueRouter({
    routes
})

export default router
