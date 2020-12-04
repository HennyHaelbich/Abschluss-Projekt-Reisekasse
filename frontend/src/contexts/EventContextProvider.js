import EventContext from './EventContext';
import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import LoginContext from "./LoginContext";

export default function EventContextProvider({ children }) {
  const { token, tokenIsValid } = useContext(LoginContext);
  const [events, setEvents] = useState([]);
  const [compensationsPayments, setCompensationPayments] = useState([]);
  
  
  const header = (token) => ({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  useEffect(() => {
    tokenIsValid() && axios
      .get('/api/events', header(token))
      .then((response) => response.data)
      .then(setEvents)
      .catch(console.log);
  }, [token, tokenIsValid]);
  
  const updateEvent = (description, members, payer, amount, id) =>
    axios
      .post('/api/events/' + id, {description, members, payer, amount}, header(token))
      .then((response) => response.data)
      .then((updatedEvent) => setEvents(events.map((event) => event.id === id ? updatedEvent : event)))
      .catch(console.log);
  

  const createEvent = (title, members) =>
    axios
      .post('/api/events', { title, members }, header(token))
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);
    
    const removeExpenditure = (eventId, expenditureId) =>
    axios
      .put('/api/events/expenditure/delete', {eventId, expenditureId}, header(token))
      .then((response) => response.data)
      .then((updatedEvent) => setEvents(events.map((event) => event.id === eventId ? updatedEvent : event)))
      .catch(console.log);
  
  const calculateCompensation = (members) =>
    axios
      .put('/api/events/compensations', {members}, header(token))
      .then((response) => response.data)
      .then((compensations) => setCompensationPayments(compensations))
      .then(console.log(compensationsPayments))
      .catch(console.log)
    
  return (
    <EventContext.Provider
      value={{
        createEvent,
        events,
        updateEvent,
        removeExpenditure,
        calculateCompensation,
        compensationsPayments
      }}
    >
      {children}
    </EventContext.Provider>
  );
}
