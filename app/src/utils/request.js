import axios from 'axios'

// 创建实例
const instance = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? 'http://172.20.10.3:8002' : 'http://localhost:8000',
  timeout: 6000
})

// 请求拦截器
instance.interceptors.request.use(config => {
  return config
}, (err) => {
  return Promise.reject(err)
})

// 响应拦截器
instance.interceptors.response.use(response => {
  return response.data
}, (err) => {
  return Promise.reject(err)
})

export {
  instance as axios
}
