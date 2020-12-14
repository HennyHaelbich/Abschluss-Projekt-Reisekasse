import React from 'react';
import useEvent from '../../hooks/useEvent';
import ExpenditureCard from './ExpenditureCard';
import { ExpCardPageStyle } from '../../commons/styling/CommonStyledComponents';

export default function ExpenditureList() {
  const { event } = useEvent();

  return (
    <ExpCardPageStyle>
      {event?.expenditures.map((expenditure) => (
        <ExpenditureCard expenditure={expenditure} key={expenditure.id} />
      ))}

      <br />
      <br />
      <br />
      <br />
    </ExpCardPageStyle>
  );
}
