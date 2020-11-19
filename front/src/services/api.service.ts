import axios from 'axios';

const API_URL = process.env.VUE_APP_API_URL;

export default {
  get(url: string): Promise<any> {
    return axios
      .get(`${API_URL}${url}`)
      .then(response => response.data)
      .catch(err => {
        console.log(err);
      });
  },
  post({ url, body = {} }: { url: string; body?: {} }): Promise<any> {
    return axios
      .post(`${API_URL}${url}`, body)
      .then(response => response.data)
      .catch(err => {
        console.log(err);
      });
  },
};
