import React, {useEffect, useState} from 'react'
import LoginContext from "./LoginContext";
import {
  loadTokenFromLocalStorage,
  loadUserDataFromLocalStorage,
  saveTokenToLocalStorage,
  saveUserDataToLocalStorage,
} from '../service/LocalStorage';
import axios from 'axios';


export default function LoginContextProvider({children}) {
  const [token, setToken] = useState(loadTokenFromLocalStorage());
  const [userData, setUserData] = useState(loadUserDataFromLocalStorage());
  
  useEffect(() => {
    if(token) {
      try {
        const decoded = jwtDecode(token);
        if (decoded.exp > new Date().getTime() / 1000) {
          setUserData(decoded);
          saveTokenToLocalStorage(token);
          saveUserDataToLocalStorage(decoded);
        }
      } catch(e) {
        console.log(e);
      }
    }
    }, [token])
  
  const tokenIsValid = () =>
    token && userData?.exp > new Data().getTime() / 1000;
  
  const loginWithUserCredentials = (loginData) =>
    axios
      .post('auth/login', loginData)
      .then((response) => setToken(response.data));
  
  return(
    <LoginContext.Provider
      value={{
        token,
        tokenIsValid,
        loginWithUserCredentials,
        userData,
      }}
      >
      {children}
    </LoginContext.Provider>
  )
  
}
