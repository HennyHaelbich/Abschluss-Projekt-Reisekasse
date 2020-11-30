import React, {useContext} from 'react';
import EventContext from "../contexts/EventContext";
import {useHistory, useParams} from "react-router-dom";
import Card from "@material-ui/core/Card";
import {Button} from "@material-ui/core";

export default function ExpenditurePage() {
  const { events } = useContext(EventContext);
  const { id, expenditureId } = useParams();
  const event = events.find((event) => event.id === id);
  const expenditure = event?.expenditures.find((exp) => exp.id === expenditureId)
  const history = useHistory();
  
  console.log(expenditure);

  return (
    expenditure ? (
      <>
        <p>{expenditure.description}</p>
        <p>{(expenditure.amount / 100).toFixed(2)} €</p>
        <p>Bezahlt von: {expenditure.payer.firstName} {expenditure.payer.lastName.substring(0,1)}.</p>
        {expenditure.expenditurePerMemberList.map((ExpenditurePerMember) => (
          <Card key={ExpenditurePerMember.username}>
            <p>{ExpenditurePerMember.firstName} {ExpenditurePerMember.lastName.substring(0,1)}.:
              {(ExpenditurePerMember.amount / 100).toFixed(2)} €</p>
          </Card>
        ))}
        <Button variant = 'outlined' onClick={() => history.goBack()}>Zurück</Button>
        </>) : null
  )

}