import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import styled from 'styled-components/macro';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';

export default function AddExpenditureForm() {
  const history = useHistory();
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState(0.0);

  return (
    <>
      <Header title={'Ausgabe hinzufügen'} />
      <FormStyled>
        <TextField
          label="Beschreibung"
          value={description}
          onChange={(event) => setDescription(event.target.value)}
          variant="outlined"
        />

        <TextField
          label="Betrag"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
          variant="outlined"
        />

        <Button
          variant="outlined"
          disabled={description.length === 0 || amount.length === 0.0}
          onClick={saveExpenditure}
        >
          Ausgabe speichern
        </Button>

        <Button variant="outlined" onClick={() => history.push('/event/:id')}>
          Abbrechen
        </Button>
      </FormStyled>
    </>
  );

  function saveExpenditure(event) {
    event.preventDefault();
    history.push('/event/:id');
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
`;
