import React, { useContext, useState } from 'react';
import ListUser from './ListUser';
import styled from 'styled-components/macro';
import EventContext from '../contexts/EventContext';
import { useHistory } from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';

export default function AddEventForm() {
  const {
    title,
    setTitle,
    members,
    setMember,
    createEvent,
    error,
  } = useContext(EventContext);
  const [newMember, setNewMember] = useState('');
  const history = useHistory();

  return (
    <>
      <Header title={'Event anlegen'} />
      <FormStyled>
        <TextField
          label="Event Name"
          name="title"
          value={title}
          // helperText="Incorrect entry."
          onChange={(event) => setTitle(event.target.value)}
          variant="outlined"
        />

        <TextField
          error={error.status}
          label="Teilnehmer"
          value={newMember}
          helperText={error.message}
          onChange={(event) => setNewMember(event.target.value)}
          variant="outlined"
        />

        <Button variant="outlined" onClick={findUser}>
          Gruppenmitglied hinzufügen
        </Button>

        <ListUser />

        <Button
          variant="outlined"
          disabled={members.length === 0 || title.length === 0}
          onClick={saveEvent}
        >
          Event speichern
        </Button>
      </FormStyled>
    </>
  );

  function findUser(event) {
    event.preventDefault();
    setMember(newMember);
    setNewMember('');
    console.log(newMember);
  }

  function saveEvent(event) {
    event.preventDefault();
    createEvent(title, members);
    history.push('/events');
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  padding: var(--size-l);
`;
