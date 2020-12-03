import React from 'react';
import { useHistory } from "react-router-dom";
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import Card from "@material-ui/core/Card";
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';
import styled from 'styled-components/macro'
import { Button } from "@material-ui/core";
import useEvent from "../hooks/useEvent";


export default function Expenditures() {
  const { event } = useEvent();
  const history = useHistory();
  
  return (
    <div>
      <DivStyled>
        <Fab color="primary" aria-label="add" onClick={() => history.push(`/event/new-expand/${event.id}`)}>
          <AddIcon />
        </Fab>
      </DivStyled>
      
      {event.expenditures.map((expenditure) => (
        <Card key={expenditure.id}>
          <p>{expenditure.description}: {formattedAmount(expenditure.amount)}</p>
          <p>Bezahlt von {displayName(expenditure.payer)}</p>
          <Button variant="outlined" onClick={() => history.push(`/event/${event.id}/${expenditure.id}`)} >
            Details
          </Button>
        </Card>
      ))}
    </div>
  );
  
}

const DivStyled = styled.div`
      position: fixed;
      bottom: 30px;
      right: 30px;
`;