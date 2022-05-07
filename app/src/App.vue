<template>
  <div class="App">
    <input v-show="false" ref="fileRef" type="file" @change="handleChange">
    <button @click="handleClick">上传Excel</button>
    <div>
      <h4>上传列表：</h4>
      <div>
        <ul>
          <li v-for="(item,index) in list" :key="index">
            {{ item.name }}
            <a :href="['/api/download/downloadFile?name=' + item.name ]">下载</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import { axios } from '@/utils/request'

export default {
  name: 'App',
  data() {
    return {
      hello: 'Hello, World!',
      list: []
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    handleClick() {
      this.$refs.fileRef.dispatchEvent(new MouseEvent('click'))
    },
    handleChange() {
      let result = this.$refs.fileRef.files[0]
      let fileFormData = new FormData();
      fileFormData.append('file', result, result.name)
      let requestConfig = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      axios.post(`/api/upload/uploadFile`, fileFormData, requestConfig)
          .then((res) => {
            this.result = `<a href="${ res }">${ res }</a>`
          })
    },
    getData() {
      var url = '/api/upload/getData'
      axios.get(url)
          .then((res) => {
            this.list = res
          })
    }
  }
}
</script>