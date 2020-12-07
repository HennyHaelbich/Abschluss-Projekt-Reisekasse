import React from 'react';
import { useHistory } from 'react-router-dom';
import useEvent from '../hooks/useEvent';
import AddButton from '../styling/AddButton';
import ExpenditureCard from './ExpenditureCard';
import { CardPageStyle } from '../styling/CommonStyledComponents';

export default function ExpenditureList() {
  const { event } = useEvent();
  const history = useHistory();

  return (
    <CardPageStyle>
      {event?.expenditures.map((expenditure) => (
        <ExpenditureCard expenditure={expenditure} key={expenditure.id} />
      ))}

      <AddButton handle={handleAddExpenditure} />

      <br />
      <br />
      <br />
      <br />
    </CardPageStyle>
  );

  function handleAddExpenditure() {
    history.push(`/event/expenditures/${event.id}`);
  }
}
