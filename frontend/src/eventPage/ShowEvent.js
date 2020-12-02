import React, { useContext } from 'react';
import Header from '../commons/Header';
import EventContext from '../contexts/EventContext';
import TabPanel from './TabPanel';
import { useParams } from 'react-router-dom';

export default function ShowEvent() {
  const { events } = useContext(EventContext);
  const { eventId } = useParams();
  const event = events.find((event) => event.id === eventId);

  return (
    <>
      <Header title={event?.title} />
      <TabPanel />
    </>
  );
}
