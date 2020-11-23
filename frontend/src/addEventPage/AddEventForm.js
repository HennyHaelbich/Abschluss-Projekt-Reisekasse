import React, {useContext, useState} from 'react';
import ListUsers from './ListUsers';
import styled from 'styled-components/macro';
import EventContext from '../contexts/EventContext';
import {useHistory} from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';

export default function AddEventForm() {
  const { members, addMember, createEvent, error, setError } = useContext(
    EventContext
  );
  const [newMember, setNewMember] = useState('');
  const [title, setTitle] = useState('');
  const history = useHistory();

  return (
    <>
      <Header title={'Reise hinzufügen'} />
      <FormStyled>
        <TextField
          label="Name der Reise"
          name="title"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          variant="outlined"
        />

        <TextField
          error={error.status}
          label="Teilnehmer"
          value={newMember}
          helperText={error.message}
          onClick={() => setError({ status: false, message: '' })}
          onChange={(event) => setNewMember(event.target.value)}
          variant="outlined"
        />

        <Button
          variant="outlined"
          onClick={findUser}
          disabled={newMember.length === 0}
        >
          Gruppenmitglied hinzufügen
        </Button>

        <ListUsers />

        <Button
          variant="outlined"
          disabled={members.length === 0 || title.length === 0}
          onClick={saveEvent}
        >
          Reise speichern
        </Button>
      </FormStyled>
    </>
  );

  function findUser(event) {
    event.preventDefault();
    if (members.find((member) => member.username === newMember)) {
      setError({
        status: true,
        message: 'Dieser Benutzer ist bereits Teil der Gruppe',
      });
    } else {
      addMember(newMember);
    }
    setNewMember('');
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
