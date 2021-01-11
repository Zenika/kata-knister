const WS_URL = 'ws://34.78.67.84:8080';
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