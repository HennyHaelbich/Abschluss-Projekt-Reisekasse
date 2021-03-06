import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import styled from 'styled-components/macro';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';
import EventContext from '../contexts/EventContext';
import InputAdornment from '@material-ui/core/InputAdornment';
import { displayName } from '../commons/helperFunctions';
import useEvent from '../hooks/useEvent';
import { useTextFieldStyle } from '../commons/styling/MaterialUiStyling';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import { loadUserDataFromLocalStorage } from '../commons/LocalStorage';
import { SmallButtonDiv } from '../commons/styling/CommonStyledComponents';

export default function AddExpenditureForm() {
  const history = useHistory();
  const { event, eventId } = useEvent();
  const members = event?.members;

  const [description, setDescription] = useState('');
  const [amountString, setAmountString] = useState('');
  const [category, setCategory] = useState('none');
  const [date, setDate] = React.useState(
    new Date().toISOString().substring(0, 10)
  );

  const userdata = loadUserDataFromLocalStorage();
  const [payerId, setPayerId] = useState(userdata.sub);

  const { addExpenditure } = useContext(EventContext);
  const classes = useTextFieldStyle();

  const handleBackClick = () => {
    history.push(`/event/${eventId}`);
  };

  return members ? (
    <>
      <Header title="Ausgabe hinzufügen" handleBackClick={handleBackClick} />

      <FormStyled>
        <TextField
          className={classes.root}
          label="Beschreibung"
          value={description}
          onChange={(event) => setDescription(event.target.value)}
          variant="outlined"
        />

        <TextField
          className={classes.root}
          label="Betrag"
          type="number"
          placeholder={'0.00'}
          value={amountString}
          variant="outlined"
          InputProps={{
            endAdornment: <InputAdornment position="end">€</InputAdornment>,
          }}
          onChange={(event) =>
            setAmountString(event.target.value.replace(',', '.'))
          }
        />

        <FormControl variant="outlined" className={classes.root}>
          <InputLabel>Kategorie</InputLabel>
          <Select
            native
            label="Kategorie"
            value={category}
            onChange={(event) => setCategory(event.target.value)}
          >
            <option value="none">Sonstiges</option>
            <option value="excursion">Ausflug</option>
            <option value="entry">Eintritt</option>
            <option value="transport">Fahrtkosten</option>
            <option value="party">Party/Getränke</option>
            <option value="restaurant">Restaurant</option>
            <option value="supermarkt">Supermarkt</option>
            <option value="sleeping">Übernachtung</option>
          </Select>
        </FormControl>

        <FormControl variant="outlined" className={classes.root}>
          <InputLabel>Zahler</InputLabel>
          <Select
            native
            label="Zahler"
            value={payerId}
            onChange={(event) => setPayerId(event.target.value)}
          >
            {members.map((member) => (
              <option key={member.username} value={member.username.toString()}>
                {displayName(member)}
              </option>
            ))}
          </Select>
        </FormControl>

        <TextField
          className={classes.root}
          type="date"
          label="Datum"
          value={date}
          InputLabelProps={{ shrink: true }}
          onChange={(event) => setDate(event.target.value)}
          variant="outlined"
        />

        <SmallButtonDiv>
          <Button
            variant="contained"
            disabled={
              description.length === 0 ||
              amountString.length === 0 ||
              payerId.length === 0
            }
            onClick={saveExpenditure}
          >
            Ausgabe speichern
          </Button>
        </SmallButtonDiv>
      </FormStyled>
    </>
  ) : null;

  function saveExpenditure(event) {
    event.preventDefault();
    const amount = Number(amountString) * 100;
    addExpenditure(
      eventId,
      description,
      members,
      payerId,
      amount,
      category,
      date
    );
    history.push(`/event/${eventId}/expenditures`);
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
`;
