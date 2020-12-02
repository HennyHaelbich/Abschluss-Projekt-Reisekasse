import React, {useContext} from 'react';
import EventContext from "../contexts/EventContext";
import {useHistory, useParams} from "react-router-dom";
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import Card from "@material-ui/core/Card";
import {Button} from "@material-ui/core";

export default function ExpenditurePage() {
  const { events , removeExpenditure } = useContext(EventContext);
  const { eventId, expenditureId } = useParams();
  const event = events.find((event) => event.id === eventId);
  const expenditure = event?.expenditures.find((exp) => exp.id === expenditureId)
  const history = useHistory();
  

  return (
    expenditure ? (
      <>
        <p>{expenditure.description}</p>
        <p>{(formattedAmount(expenditure.amount))}</p>
        <p>Bezahlt von: {displayName(expenditure.payer)}</p>
        {expenditure.expenditurePerMemberList.map((expenditurePerMember) => (
          <Card key={expenditurePerMember.username}>
            <p>{displayName(expenditurePerMember)}: {(formattedAmount(expenditurePerMember.amount))}</p>
          </Card>
        ))}
        <Button variant='outlined' onClick={() => history.goBack()}>Zurück</Button>
        <Button variant="outlined" onClick={handleDelete}>Löschen</Button>
        </>) : null
  )
  
  function handleDelete() {
    removeExpenditure(eventId, expenditureId)
    history.goBack()
  }

}