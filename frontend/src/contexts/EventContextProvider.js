import EventContext from './EventContext';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function EventContextProvider({ children }) {
  const [members, setMembers] = useState([]);
  const [events, setEvents] = useState([]);
  const [error, setError] = useState({ status: false, message: '' });

  useEffect(() => {
    axios
      .get('api/events')
      .then((response) => response.data)
      .then(setEvents)
      .catch(console.log);
  }, []);

  const createEvent = (title, members) =>
    axios
      .post('/api/events', { title, members })
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);

  const addMember = (member) =>
    axios
      .get('/api/users/' + member)
      .then((response) => response.data)
      .then((newMember) => setMembers([...members, newMember]))
      .catch(() => {
        setError({ status: true, message: 'Dieser Benutzer existiert nicht' });
      });

  return (
    <EventContext.Provider
      value={{
        members,
        addMember,
        createEvent,
        error,
        setError,
        events,
      }}
    >
      {children}
    </EventContext.Provider>
  );
}
