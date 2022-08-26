import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/rule',
        component: Layout,
        redirect: 'noRedirect',
        meta: {title: '规则管理', icon: 'component'},
        children: [
            {
                path: 'ruleList',
                component: () => import('@/views/rule/RuleList'),
                name: 'RuleList',
                meta: {title: '规则列表'}
            }
        ]
    },

]

const router = new VueRouter({
    routes
})

export default router
