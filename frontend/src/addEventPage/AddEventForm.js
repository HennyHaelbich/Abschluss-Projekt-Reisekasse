import React, { useContext, useState } from 'react';
import ListUser from './ListUser';
import styled from 'styled-components/macro';
import EventContext from '../contexts/EventContext';

export default function AddEventForm() {
  const { setTitle, setMember } = useContext(EventContext);
  const [newMember, setNewMember] = useState('');

  return (
    <FormStyled>
      <label>
        Titel
        <InputStyled name="title" onChange={handleChangeTitle} type="text" />
      </label>

      <label>
        Teilnehmer
        <InputStyled
          name="newMember"
          onChange={handleChangeNewMember}
          type="text"
        />
      </label>

      <ButtonStyled type="button" onClick={findUser}>
        Gruppenmitglied hinzufügen
      </ButtonStyled>

      <ListUser />
    </FormStyled>
  );

  function handleChangeNewMember(event) {
    setNewMember(event.target.value);
  }

  function handleChangeTitle(event) {
    setTitle(event.target.value);
  }

  function findUser(event) {
    event.preventDefault();
    console.log('newMember', newMember);
    setMember(newMember);
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
  font-size: 1em;
  padding: var(--size-m);
  border-radius: var(--size-s);
  border: 2px solid var(--secundary-main);
  font-weight: 600;
`;
