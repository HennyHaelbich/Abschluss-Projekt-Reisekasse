import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';
import { useParams } from 'react-router-dom';
import styled from 'styled-components/macro';

export default function Overview() {
  const { events } = useContext(EventContext);
  const { id } = useParams();
  const event = events.find((event) => event.id === id);
  console.log("String", event)

  return (
  event ? event.members.map((member) => (
    <ListStyled key={member.username}>
      <p>{member.username}</p>
      <p>{(member.balance / 100).toFixed(2)} €</p>
    </ListStyled>
  )) : null
  );
}

const ListStyled = styled.ul`
  overflow: scroll;
  margin: 0;
  padding: var(--size-m);
  list-style: none;
  display: flex;
  justify-content: space-around; ;
`;
