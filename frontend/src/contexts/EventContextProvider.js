import EventContext from './EventContext';
import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import LoginContext from './LoginContext';

export default function EventContextProvider({ children }) {
  const { token, tokenIsValid } = useContext(LoginContext);
  const [events, setEvents] = useState([]);

  const header = (token) => ({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  useEffect(() => {
    tokenIsValid() &&
      axios
        .get('/api/events', header(token))
        .then((response) => response.data)
        .then(setEvents)
        .catch(console.log);
  }, [token, tokenIsValid]);

  const addExpenditure = (description, members, payerId, amount, eventId) =>
    axios
      .post(
        '/api/events/' + eventId,
        { description, members, payerId, amount },
        header(token)
      )
      .then((response) => response.data)
      .then((updatedEvent) =>
        setEvents(
          events.map((event) => (event.id === eventId ? updatedEvent : event))
        )
      )
      .catch(console.log);

  const createEvent = (title, members) =>
    axios
      .post('/api/events', { title, members }, header(token))
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);

  const removeExpenditure = (eventId, expenditureId) =>
    axios
      .put(
        '/api/events/expenditure/delete',
        { eventId, expenditureId },
        header(token)
      )
      .then((response) => response.data)
      .then((updatedEvent) =>
        setEvents(
          events.map((event) => (event.id === eventId ? updatedEvent : event))
        )
      )
      .catch(console.log);

  const calculateCompensation = (members) =>
    axios
      .put('/api/events/compensations', { members }, header(token))
      .then((response) => response.data);

  return (
    <EventContext.Provider
      value={{
        createEvent,
        events,
        addExpenditure,
        removeExpenditure,
        calculateCompensation,
      }}
    >
      {children}
    </EventContext.Provider>
  );
}
