import EventContext from './EventContext';
import React, { useState } from 'react';
import axios from 'axios';

export default function EventContextProvider({ children }) {
  const [title, setTitle] = useState('');
  const [members, setMembers] = useState([]);
  const [events, setEvents] = useState([]);

  const createEvent = (title, users) =>
    axios
      .post('/api/event', { title, users })
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);

  const setMember = (user) =>
    axios
      .get('/api/user/' + user)
      .then((response) => response.data)
      .then((newMember) => setMembers([...members, newMember]))
      .catch(console.log);

  return (
    <EventContext.Provider
      value={{ setTitle, members, setMember, createEvent }}
    >
      {children}
    </EventContext.Provider>
  );
}
