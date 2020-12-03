import React, {useContext} from 'react';
import styled from 'styled-components/macro';
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import {Button} from "@material-ui/core";
import useEvent from "../hooks/useEvent";
import { useHistory } from "react-router-dom";
import EventContext from "../contexts/EventContext";

export default function Overview() {
  const { event } = useEvent();
  const { calculateCompensation } = useContext(EventContext);
  const history = useHistory();
  const members = event?.members;

  return (
    <>
      {members ? members.map((member) => (
        <ListStyled key={member.username}>
          <p>{displayName(member)}</p>
          <p>{formattedAmount(member.balance)}</p>
        </ListStyled>
        )) : null }
      
      <Button variant="outlined" onClick={handleClick}>
        Ausgleichszahlungen berechnen
      </Button>

    </>
  );
  
  function handleClick(event) {
    event.preventDefault();
    console.log("EventMembers", members)
    calculateCompensation(members);
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
