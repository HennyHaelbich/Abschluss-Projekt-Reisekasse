import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import LoginContext from '../contexts/LoginContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import TextField from '@material-ui/core/TextField';
import Snackbar from '@material-ui/core/Snackbar';
import { Button } from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import { useTextFieldStyle } from '../styling/MaterialUiStyling';
import {
  CardSecondLineStyle,
  SmallButtonDiv,
} from '../styling/CommonStyledComponents';

const setupCredentials = {
  username: '',
  password: '',
};

export default function LoginPage() {
  const { loginWithUserCredentials } = useContext(LoginContext);
  const [credentials, setCredentials] = useState(setupCredentials);
  const [error, setError] = useState('');
  const history = useHistory();
  const classes = useTextFieldStyle();

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

        <SmallButtonDiv>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={!credentials.username || !credentials.password}
          >
            Login
          </Button>
        </SmallButtonDiv>

        <ImageSection>
          <img
            src={'/undraw_travel_together.svg'}
            alt="people in front of globe"
            height="200"
          />
        </ImageSection>

        <CardSecondLineStyle>noch nicht dabei?</CardSecondLineStyle>

        <SmallButtonDiv>
          <Button
            variant="outlined"
            color="primary"
            onClick={() => history.push('/signup')}
          >
            Registrierung
          </Button>
        </SmallButtonDiv>

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
  padding: var(--size-xxl);
  text-align: center;
`;

const ImageSection = styled.div`
  display: flex;
  justify-content: center;
  padding: var(--size-xxl);
`;
