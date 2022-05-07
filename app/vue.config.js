module.exports = {
  devServer: {
    port: 8000,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        ws: false,
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
}