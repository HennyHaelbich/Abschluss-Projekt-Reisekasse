import React, { useContext } from 'react';
import Header from '../commons/Header';
import EventContext from '../contexts/EventContext';
import ListUsers from '../commons/ListUsers';

export default function ShowEvents() {
  const { title } = useContext(EventContext);

  return (
    <>
      <Header title={title} />
      <h2>Übersicht</h2>
      <ListUsers />
      <h2>Ausgaben</h2>
    </>
  );
}
