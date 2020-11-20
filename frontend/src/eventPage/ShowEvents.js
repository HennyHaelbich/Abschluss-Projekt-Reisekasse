import React, { useContext } from 'react';
import Header from '../commons/Header';
import EventContext from '../contexts/EventContext';
import ListUsers from '../commons/ListUsers';
import TabPanel from '../addEventPage/TabPanel';

export default function ShowEvents() {
  const { title } = useContext(EventContext);

  return (
    <>
      <Header title={'title'} />
      <TabPanel />
    </>
  );
}
