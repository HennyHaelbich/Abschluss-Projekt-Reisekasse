import React from 'react';
import useEvent from '../hooks/useEvent';
import ExpenditureCard from './ExpenditureCard';
import { CardPageStyle } from '../styling/CommonStyledComponents';

export default function ExpenditureList() {
  const { event } = useEvent();

  return (
    <CardPageStyle>
      {event?.expenditures.map((expenditure) => (
        <ExpenditureCard expenditure={expenditure} key={expenditure.id} />
      ))}

      <br />
      <br />
      <br />
      <br />
    </CardPageStyle>
  );
}
