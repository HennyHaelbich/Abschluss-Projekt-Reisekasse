import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import LoginContext from '../contexts/LoginContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import TextField from '@material-ui/core/TextField';
import Snackbar from '@material-ui/core/Snackbar';
import { Button } from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import { useTextfildStyle } from '../styling/MaterialUiStyling';

const setupCredentials = {
  username: '',
  password: '',
};

export default function LoginPage() {
  const { loginWithUserCredentials } = useContext(LoginContext);
  const [credentials, setCredentials] = useState(setupCredentials);
  const [error, setError] = useState('');
  const history = useHistory();
  const classes = useTextfildStyle();

  return (
    <>
      <Header title="Reisekasse" />

      <FormStyled>
        <TextField
          className={classes.root}
          label="Mailadresse"
          name="username"
          type="text"
          error={!!error}
          value={credentials.username}
          onChange={handleChange}
          variant="outlined"
        />

        <TextField
          className={classes.root}
          label="Passwort"
          name="password"
          type="password"
          error={!!error}
          value={credentials.password}
          onChange={handleChange}
          variant="outlined"
        />

        <Button
          variant="outlined"
          onClick={handleSubmit}
          disabled={!credentials.username || !credentials.password}
        >
          Login
        </Button>

        <br />
        <br />
        <br />
        <p>noch nicht dabei?</p>

        <Button variant="outlined" onClick={() => history.push('/signup')}>
          Registrierung
        </Button>

        <Snackbar open={error} autoHideDuration={3000} onClose={handleClose}>
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
      .catch(() => setError('Mailadresse oder Passwort sind nicht bekannt!'));
  }

  function handleClose(event, reason) {
    if (reason === 'clickaway') {
      return;
    }
    setCredentials(setupCredentials);
    setError('');
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
  text-align: center;
`;
