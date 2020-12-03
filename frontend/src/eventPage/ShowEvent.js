import React from 'react';
import Header from '../commons/Header';
import TabPanel from './TabPanel';
import useEvent from "../hooks/useEvent";

export default function ShowEvent() {
  const { event } = useEvent();

  return (
    <>
      <Header title={event?.title} />
      <TabPanel />
    </>
  );
}
