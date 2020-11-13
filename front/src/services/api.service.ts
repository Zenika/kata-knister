import axios, { AxiosResponse } from 'axios';

const API_URL = process.env.VUE_APP_API_URL;

export default {
  get(url: string): Promise<unknown> {
    return axios.get(`${API_URL}${url}`);
  },
  post({
    url,
    body = {},
  }: {
    url: string;
    body?: {};
  }): Promise<AxiosResponse<unknown>> {
    return axios
      .post(`${API_URL}${url}`, body)
      .then(response => response.data)
      .catch(err => {
        console.log(err);
      });
  },
};
