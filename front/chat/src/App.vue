<template>
  <div id="app">
    유저이름:
    <input
        v-model="userName"
        type="text"
    >
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage"
  >
    <div
        v-for="(item, idx) in recvList"
        :key="idx"
    >
      <h3>유저이름: {{ item.userName }}</h3>
      <h3>내용: {{ item.content }}</h3>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'App',
  data() {
    return {
      userName: "",
      message: "",
      recvList: []
    }
  },
  created() {
    this.connect()
  },
  methods: {
    connect() {
      const server = "http://localhost:8080/test"
      let socket = new SockJS(server);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도 중 서버 주소: ${server}`)
      this.stompClient.connect(
          {},
          frame => {
            this.connected = true;
            console.log('소켓 연결 성공', frame);
            this.stompClient.subscribe("/send", res => {
              console.log(res);
              console.log("구독으로 받은 메시지입니다.", res.body);
              this.recvList.push(JSON.parse(res.body))
            });
          },
          error => {
            console.log('소켓 연결 실패', error);
            this.connected = false;
          }
      )
    },
    sendMessage(e) {
      console.log(e);
      if (e.keyCode === 13 && this.userName !== '' && this.message !== '') {
        this.send()
        this.message = ''
      }
    },
    send() {
      console.log('Send Message:' + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          userName: this.userName,
          content: this.message
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
