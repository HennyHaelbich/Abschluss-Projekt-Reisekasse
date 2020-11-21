import React from 'react';
import Header from '../commons/Header';
import EventsList from './EventsList';

export default function EventPage() {
  return (
    <>
      <Header title={'Reisekasse'} />
      <EventsList />
    </>
  );
}
