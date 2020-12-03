import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';
import { useParams } from 'react-router-dom';
import styled from 'styled-components/macro';
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import {Button} from "@material-ui/core";
import useCalculateCompensation from "../compensationPage/useCalculateCompensation";

export default function Overview() {
  const { events, calculateCompensations } = useContext(EventContext);
  const { eventId } = useParams();
  const event = events.find((event) => event.id === eventId);
  const { calculateCompensation } = useCalculateCompensation();

  return (
    <>
      event ? event.members.map((member) => (
        <ListStyled key={member.username}>
          <p>{displayName(member)}</p>
          <p>{formattedAmount(member.balance)}</p>
        </ListStyled>
      )) : null
  
      <Button variant="outlined" onClick={handleClick}>
        Ausgleichszahlungen berechnen
      </Button>
      
    </>
  );
  
  function handleClick(event) {
    event.preventDefault();
    calculateCompensation(event.members);
    history.push('/balancing/:eventId');
  }
}

const ListStyled = styled.ul`
  overflow: scroll;
  margin: 0;
  padding: var(--size-m);
  list-style: none;
  display: flex;
  justify-content: space-around; ;
`;
