'use strict'
const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
    publicPath: '/',
    outputDir: 'dist',
    assetsDir: 'static',
    configureWebpack: {
        name: 'router4j-vue',
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    }
}
