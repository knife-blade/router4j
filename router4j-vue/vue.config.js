'use strict'
const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
    publicPath: '/',
    outputDir: 'dist',
    assetsDir: 'static',
    configureWebpack: {
        name: 'vue Element Admin',
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    }
}
