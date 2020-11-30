import React, {useContext} from 'react';
import EventContext from "../contexts/EventContext";
import {useHistory, useParams} from "react-router-dom";
import Card from "@material-ui/core/Card";
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';
import styled from 'styled-components/macro'
import {Button} from "@material-ui/core";


export default function Expenditures() {
  const { events } = useContext(EventContext);
  const { id } = useParams();
  const event = events.find((event) => event.id === id);
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
          <p>{expenditure.description}: {(expenditure.amount / 100).toFixed(2)} €</p>
          <p>Bezahlt von {expenditure.payer.firstName} {expenditure.payer.lastName.substring(0,1)}.</p>
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

