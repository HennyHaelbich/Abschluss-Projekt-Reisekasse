import React, {useState} from "react";
import { useHistory } from "react-router-dom";
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

export default function SignUpForm() {
  const [signUpData, setSignUpData] = useState(setupSignInData);
  const [errorUser, setErrorUser] = useState('')
  const [errorPassword, setErrorPassword] = useState('')
  const history = useHistory();
  
  return(
    <>
      <Header title="Registrierung" />
      
      <FormStyled>
        <TextField
          label="Mailadresse"
          name="username"
          type="email"
          error={!!errorUser}
          helperText={errorUser}
          value={signUpData.username}
          onChange={handleChange}
          variant="outlined"
          />
  
        <TextField
          label="Passwort"
          name="password"
          type="password"
          helperText='das Passwort muss mindestens 8 Zeichen lang sein und Großbuchstaben, Kleinbuchstaben und Zahlen enthalten'
          value={signUpData.password}
          onChange={handleChange}
          variant="outlined"
        />
  
        <TextField
          label="Passwort wiederholen"
          name="password_rep"
          type="password"
          error={!!errorPassword}
          helperText={errorPassword}
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
          disabled={ !signUpData.username || !signUpData.password || !signUpData.password_rep || !signUpData.firstName ||
            !signUpData.lastName }
          onClick={handleSubmit}>
          Registrieren
        </Button>

      </FormStyled>
      </>
  );
  
  function handleChange(event) {
    setSignUpData({...signUpData, [event.target.name]: event.target.value })
  }
  
  function handleSubmit(event) {
    
    event.preventDefault();
    (console.log("Passwortfehler:", errorPassword))
  
    if (doPasswordsMatch() && isPasswordValid()){
      signUp(signUpData)
        .then(() => history.push("/login"))
        .catch((err) => {
          console.log(err.response.data.message);
          if (err.response.data.message === 'user already exists') {
            setErrorUser( 'Diese Mailadresse existiert bereits')
          } else {
            console.log('Fehler in der Registrierung: ', err.response)
          }
        })
    }
  }
  
  function doPasswordsMatch() {
    if(signUpData.password !== signUpData.password_rep){
      setErrorPassword('Passwort und Passwortwiederholung stimmen nicht überein');
      return false;
    } else {
      setErrorPassword('')
      return true;
    }
  }
  
  function isPasswordValid() {
    // password is long enough
    if (signUpData.password.length < 8 ||
      // password contains numbers
      !/\d/.test(signUpData.password) ||
      // password contains lower case letters
      !/[a-z]/.test(signUpData.password) ||
      // password contains upper case letters
      !/[A-Z]/.test(signUpData.password)
    ) {
      setErrorPassword('das Passwort muss mindestens 8 Zeichen lang sein und Großbuchstaben, Kleinbuchstaben ' +
          'und Zahlen enthalten')
      return false;
      
    }
    return true;
  }
  
}

const FormStyled = styled.form`
      display: grid;
      gap: var(--size-m);
      grid-auto-rows: min-content;
      padding: var(--size-l);
      `;
    