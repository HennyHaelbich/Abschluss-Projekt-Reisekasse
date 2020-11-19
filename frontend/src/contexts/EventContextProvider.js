import EventContext from './EventContext';
import React, { useState } from 'react';
import axios from 'axios';

export default function EventContextProvider({ children }) {
  const [title, setTitle] = useState('');
  const [members, setMembers] = useState([]);
  const [events, setEvents] = useState([]);
  const [error, setError] = useState({ status: false, message: '' });

  const createEvent = (title, members) =>
    axios
      .post('/api/events', { title, members })
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);

  const setMember = (member) =>
    axios
      .get('/api/users/' + member)
      .then((response) => response.data)
      .then((newMember) => setMembers([...members, newMember]))
      .catch((err) => {
        setError({ status: true, message: 'Dieser Benutzer existiert nicht' });
      });

  return (
    <EventContext.Provider
      value={{
        title,
        setTitle,
        members,
        setMember,
        createEvent,
        error,
        setError,
      }}
    >
      {children}
    </EventContext.Provider>
  );
}
