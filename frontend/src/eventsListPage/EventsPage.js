import React, { useContext } from 'react';
import { useHistory } from 'react-router-dom';
import Header from '../commons/Header';
import { CardPageStyle } from '../styling/CommonStyledComponents';
import EventsCard from './EventsCard';
import AddButton from '../styling/AddButton';
import EventContext from '../contexts/EventContext';

export default function EventPage() {
  const history = useHistory();
  const { events } = useContext(EventContext);

  return (
    <>
      <Header title={'Reisekasse'} />
      <CardPageStyle>
        {events ? events.map((event) => <EventsCard event={event} />) : null}
      </CardPageStyle>
      <AddButton handle={handleAddExpenditure} />
    </>
  );

  function handleAddExpenditure() {
    history.push(`/new`);
  }
}
