import React from 'react';
import { useHistory } from 'react-router-dom';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import Card from '@material-ui/core/Card';
import { Button } from '@material-ui/core';
import useEvent from '../hooks/useEvent';
import AddButton from '../styling/AddButton';

export default function Expenditures() {
  const { event } = useEvent();
  const history = useHistory();

  return (
    <div>
      {event.expenditures.map((expenditure) => (
        <Card key={expenditure.id}>
          <p>
            {expenditure.description}: {formattedAmount(expenditure.amount)}
          </p>
          <p>Bezahlt von {displayName(expenditure.payer)}</p>
          <Button
            variant="outlined"
            onClick={() =>
              history.push(`/event/expenditures/${event.id}/${expenditure.id}`)
            }
          >
            Details
          </Button>
        </Card>
      ))}

      <AddButton handle={handleAddExpenditure} />
    </div>
  );

  function handleAddExpenditure() {
    history.push(`/event/expenditures/${event.id}`);
  }
}
