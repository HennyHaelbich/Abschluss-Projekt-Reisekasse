import React, {useContext, useState} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import styled from 'styled-components/macro';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';
import EventContext from "../contexts/EventContext";
import InputAdornment from "@material-ui/core/InputAdornment";
import MenuItem from "@material-ui/core/MenuItem";

export default function AddExpenditureForm() {
  const history = useHistory();
  const [description, setDescription] = useState('');
  const [amountString, setAmountString] = useState('');
  const [payer, setPayer] = useState('');
  const { events, updateEvent } = useContext(EventContext);
  const { id } = useParams();
  const event = events.find((event) => event.id === id);
  const members = event.members;
  
  
  return (
    <>
      <Header title='Ausgabe hinzufügen' />
      
      <FormStyled>
        <TextField
          label = "Beschreibung"
          value = {description}
          onChange = {(event) => setDescription(event.target.value)}
          variant = "outlined"
        />

        <TextField
          label = "Betrag"
          type = "number"
          placeholder={'0.00'}
          value = {amountString}
          variant = "outlined"
          InputProps={{
            endAdornment: <InputAdornment position="end">€</InputAdornment>,
          }}
          onChange = {(event) => setAmountString(event.target.value.replace(',', '.'))}
        />
        
        <TextField
          select
          label = "Zahler"
          value = {payer}
          onChange = {(event) => setPayer(event.target.value)}
          variant = "outlined"
        > {members.map((member) => (
          <MenuItem key={member.username} value={member}>
            {member.firstName} {member.lastName.substring(0,1)}.
          </MenuItem>
        ))}
        </TextField>
        
        <Button
          variant="outlined"
          disabled={description.length === 0 || amountString.length === 0 || payer.length === 0 }
          onClick={saveExpenditure}
        >
          Ausgabe speichern
        </Button>

        <Button variant="outlined" onClick={() => history.push('/events')}>
          Abbrechen
        </Button>
      </FormStyled>
    </>
  );

  function saveExpenditure(event) {
    event.preventDefault();
    const amount = Number(amountString) * 100
    updateEvent(description, members, payer, amount, id)
    history.push(`/event/${id}`)
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
`;
