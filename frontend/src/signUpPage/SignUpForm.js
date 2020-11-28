import React, {useState} from "react";
import { useHistory } from 'react-router-dom'
import Header from "../commons/Header";
import { signUp } from "../service/SignUpService";
import styled from "styled-components/macro";
import TextField from "@material-ui/core/TextField";
import {Button} from "@material-ui/core";

const setupSignInData = {
  username: '',
  password: '',
  password_rep: '',
  firstName: '',
  lastName: '',
}

const setupError = {
  status: false,
  message: ''
}

export default function SignUpForm() {
  const history = useHistory;
  const [signUpData, setSignUpData] = useState(setupSignInData);
  const [errorUser, setErrorUser] = useState(setupError)
  const [errorPassword, setErrorPassword] = useState(setupError)
  
  return(
    <>
      <Header title="Registrierung" />
      
      <FormStyled>
        <TextField
          label="Mailadresse"
          name="username"
          type="text"
          error={errorUser.status}
          helperText={errorUser.message}
          value={signUpData.username}
          onChange={handleChange}
          variant="outlined"
          />
  
        <TextField
          label="Passwort"
          name="password"
          type="password"
          value={signUpData.password}
          onChange={handleChange}
          variant="outlined"
        />
  
        <TextField
          label="Passwort wiederholen"
          name="password_rep"
          type="password"
          error={errorPassword.status}
          helperText={errorPassword.message}
          value={signUpData.password_rep}
          onChange={handleChange}
          variant="outlined"
        />
  
        <TextField
          label="Vorname"
          name="firstName"
          type="text"
          value={signUpData.firstName}
          onChange={handleChange}
          variant="outlined"
        />
  
        <TextField
          label="Nachname"
          name="lastName"
          type="text"
          value={signUpData.lastName}
          onChange={handleChange}
          variant="outlined"
        />
  
        <Button
          variant="outlined"
          disabled={!signUpData.username || !signUpData.password || !signUpData.password_rep || !signUpData.firstName || !signUpData.lastName}
          onClick={handleSubmit}>
          Login
        </Button>

      </FormStyled>
      </>
  );
  
  function handleChange(event) {
    setSignUpData({...signUpData, [event.target.name]: event.target.value })
  }
  
  function handleSubmit(event) {
    event.preventDefault();
    checkIfPasswordsMatch()
    validatePasswort()
    if (errorPassword.status === false){
      signUp(signUpData)
        .then((data) => console.log(data))
        .then(() => history.push('/login'))
        .catch(() => {
          setErrorUser({ status: true, message: 'Diese Mailadresse existiert bereits' })
        })
    }
  }
  
  function checkIfPasswordsMatch() {
    if(signUpData.password !== signUpData.password_rep){
      setErrorPassword({ status: true, message: 'Passwort und Passwortwiederholung stimmen nicht überein' });
    }
  }
  
  function validatePasswort() {
    // password is long enough
    if (signUpData.password.length < 8 ||
      // password contains numbers
      !/\d/.test(signUpData.password) ||
      // password contains lower case letters
      !/[a-z]/.test(signUpData.password) ||
      // password contains upper case letters
      !/[A-Z]/.test(signUpData.password)
    ) {
      setErrorPassword({ status: true, message: 'das Passwort muss mindestens 8 Zeichen lang sein und Großbuchstaben, ' +
          'Kleinbuchstaben und Zahlen enthalten' })
    }
  }
  
}

const FormStyled = styled.form`
      display: grid;
      gap: var(--size-m);
      grid-auto-rows: min-content;
      padding: var(--size-l);
      `;
    