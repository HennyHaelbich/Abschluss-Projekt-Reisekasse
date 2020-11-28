import axios from 'axios';

export const signUp = (signUpData) => {
  
  const { username, password, firstName, lastName } = signUpData
  console.log({ username, password, firstName, lastName })
  axios
    .post('/api/users', { username, password, firstName, lastName })
    .then((response) => response.data)
}
