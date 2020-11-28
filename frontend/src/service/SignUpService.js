import axios from 'axios';

export const signUp = (signUpData) => {
  const { username, password, firstName, lastName } = signUpData
  console.log("SignUpService", { username, password, firstName, lastName })
  return axios
    .post('/auth/registration', { username, password, firstName, lastName })
    .then((response) => response.data)
}
