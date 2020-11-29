import axios from 'axios';

export const signUp = (signUpData) => {
  const { username, password, firstName, lastName } = signUpData
  return axios
    .post('/auth/registration', { username, password, firstName, lastName })
    .then((response) => response.data)
}
