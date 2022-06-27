'use strict'
const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
    publicPath: '/',
    // baseUrl: './',
    runtimeCompiler: true,
    chainWebpack: config => {
        config.resolve.alias
            .set('@', resolve('src'))
    }
}
