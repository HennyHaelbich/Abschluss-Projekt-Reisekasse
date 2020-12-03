import React from 'react';
import styled from 'styled-components/macro';
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import useEvent from "../hooks/useEvent";

export default function Overview() {
  const { event } = useEvent();

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
