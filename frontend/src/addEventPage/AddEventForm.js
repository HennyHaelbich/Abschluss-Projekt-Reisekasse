import React, { useContext, useState } from 'react';
import ListUser from './ListUser';
import styled, { css } from 'styled-components/macro';
import EventContext from '../contexts/EventContext';
import { useHistory } from 'react-router-dom';

export default function AddEventForm() {
  const { title, setTitle, members, setMember, createEvent } = useContext(
    EventContext
  );
  const [newMember, setNewMember] = useState('');
  const history = useHistory();

  return (
    <FormStyled>
      <label>
        Titel
        <InputStyled
          name="title"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          type="text"
        />
      </label>
      <label>
        Teilnehmer
        <InputStyled
          name="newMember"
          value={newMember}
          onChange={(event) => setNewMember(event.target.value)}
          type="text"
        />
      </label>
      <ButtonStyled type="button" onClick={findUser}>
        Gruppenmitglied hinzufügen
      </ButtonStyled>
      <ListUser />

      <ButtonStyled
        disabled={members.length === 0 || title.length === 0}
        type="button"
        onClick={saveEvent}
      >
        Event speichern
      </ButtonStyled>
    </FormStyled>
  );

  function findUser(event) {
    event.preventDefault();
    setMember(newMember);
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

const InputStyled = styled.input`
  display: block;
  width: 100%;
`;

const ButtonStyled = styled.button`
  padding: var(--size-s);
  border-radius: var(--size-s);
  border: 2px solid var(--secundary-main);
  font-weight: 600;

  ${(props) =>
    props.disabled &&
    css`
      color: grey;
      border: 2px solid grey;
    `}
`;
