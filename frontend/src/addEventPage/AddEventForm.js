import React, { useContext, useState } from 'react';
import MembersList from './MembersList';
import styled from 'styled-components/macro';
import EventContext from '../contexts/EventContext';
import useEventMembers from './useEventMembers';
import { useHistory } from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Header from '../commons/Header';
import { useTextFieldStyle } from '../styling/MaterialUiStyling';
import { SmallButtonDiv } from '../styling/CommonStyledComponents';

export default function AddEventForm() {
  const { createEvent } = useContext(EventContext);
  const { addMember, members } = useEventMembers();
  const [newMember, setNewMember] = useState('');
  const [title, setTitle] = useState('');
  const [error, setError] = useState('');
  const history = useHistory();
  const classes = useTextFieldStyle();

  return (
    <>
      <Header title={'Reise hinzufügen'} />
      <FormStyled>
        <TextField
          className={classes.root}
          label="Name der Reise"
          name="title"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          variant="outlined"
        />

        <TextField
          className={classes.root}
          error={!!error}
          label="Teilnehmer"
          value={newMember}
          helperText={error}
          onClick={() => setError('')}
          onChange={(event) => setNewMember(event.target.value)}
          variant="outlined"
        />

        <SmallButtonDiv>
          <Button
            variant="outlined"
            color="secondary"
            onClick={findUser}
            disabled={newMember.length === 0}
          >
            Gruppenmitglied hinzufügen
          </Button>
        </SmallButtonDiv>

        <MembersList members={members} />

        <SmallButtonDiv>
          <Button
            variant="contained"
            color="primary"
            disabled={members.length === 0 || title.length === 0}
            onClick={saveEvent}
          >
            Reise speichern
          </Button>
        </SmallButtonDiv>
      </FormStyled>
    </>
  );

  function findUser(event) {
    event.preventDefault();
    if (members.find((member) => member.username === newMember)) {
      setError('Dieser Benutzer ist bereits Teil der Gruppe');
    } else {
      addMember(newMember).catch(() => {
        setError('Dieser Benutzer existiert nicht');
      });
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
