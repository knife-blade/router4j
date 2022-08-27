import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/misc/redirect/index')
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        redirect: '/rule'
    },
    {
        path: '/404',
        component: () => import('@/views/misc/error-page/404'),
        hidden: true
    },
    {
        path: '/rule',
        component: Layout,
        redirect: '/rule/ruleList',
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
    {
        path: '/application',

        component: Layout,
        redirect: '/application/applicationList',
        meta: {title: '应用管理', icon: 'component'},
        children: [
            {
                path: 'applicationList',
                component: () => import('@/views/application/ApplicationList'),
                name: 'ApplicationList',
                meta: {title: '应用列表'}
            }
        ]
    },

    // 404 page must be placed at the end !!!
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    routes
})

export default router
