import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';
import { useParams } from 'react-router-dom';
import styled from 'styled-components/macro';
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'

export default function Overview() {
  const { events } = useContext(EventContext);
  const { id } = useParams();
  const event = events.find((event) => event.id === id);

  return (
  event ? event.members.map((member) => (
    <ListStyled key={member.username}>
      <p>{displayName(member)}</p>
      <p>{formattedAmount(member.balance)}</p>
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
