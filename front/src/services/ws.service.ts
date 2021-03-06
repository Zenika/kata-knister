const WS_URL = 'ws://localhost:8080';
let websocket: WebSocket;

const openWS = () => {
  return new Promise((resolve, reject) => {
    websocket = new WebSocket(`${WS_URL}/knisterSocket`);

    websocket.onopen = () => {
      resolve(websocket);
    }

    websocket.onerror = (error) => {
      reject(error);
    }
    
    websocket.onmessage = (message) => {
      console.log(message);
    }
  });
}

export default {
  async open() {
    if (!websocket) {
      await openWS();
    }

    return websocket;
  },
  async send(payload: any) {
    if (!websocket) {
      await openWS();
    }

    websocket.send(payload);
  }
}


enum WebSocketEvents {
  Created = 'created',
}