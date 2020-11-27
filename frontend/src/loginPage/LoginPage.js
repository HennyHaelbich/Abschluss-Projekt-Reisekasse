import React, {useContext, useState} from "react";
import { useHistory } from 'react-router-dom';
import LoginContext from "../contexts/LoginContext";
import Header from "../commons/Header";
import styled from 'styled-components/macro';
import TextField from "@material-ui/core/TextField";
import Snackbar from "@material-ui/core/Snackbar";
import {Button} from "@material-ui/core";
import Alert from "@material-ui/lab/Alert";


const setupCredentials = {
  username: '',
  password: '',
};

const setupError = {
  status: false,
  message: ''
}

export default function LoginPage() {
  const { loginWithUserCredentials } = useContext(LoginContext);
  const [credentials, setCredentials] = useState(setupCredentials);
  const [error, setError] = useState(setupError);
  const history = useHistory();
  
  return(
  <>
    <Header title="Reisekasse" />
    <FormStyled>
      <TextField
        label="Mailadresse"
        name="username"
        type="text"
        error={error.status}
        value={credentials.username}
        onChange={handleChange}
        variant="outlined"
      />
      
      <TextField
        label="Passwort"
        name="password"
        type="password"
        error={error.status}
        value={credentials.password}
        onChange={handleChange}
        variant="outlined"
      />
        
        <Button
          variant="outlined"
          onClick={handleSubmit}
          disabled={!credentials.username || !credentials.password} >
          Login
        </Button>
      
  
        <Snackbar open={error.status} autoHideDuration={3000} onClose={handleClose} >
          <Alert severity="error">
            Mailadresse oder Passwort sind nicht bekannt!
          </Alert>
        </Snackbar>
  
      </FormStyled>
  </>
  );
  
  function handleChange(event) {
    setCredentials({ ...credentials, [event.target.name]: event.target.value });
  }
  
  function handleSubmit(event) {
    event.preventDefault();
    loginWithUserCredentials(credentials)
      .then(() => history.push('/events'))
      .catch(() => setError({ status: true, message: 'Mailadresse oder Passwort sind nicht bekannt!' }));
  }
  
  function handleClose(event, reason) {
    if (reason === 'clickaway') {
      return;
    }
    setCredentials(setupCredentials);
    setError({ status: false, message: '' });
  }
  
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
`;