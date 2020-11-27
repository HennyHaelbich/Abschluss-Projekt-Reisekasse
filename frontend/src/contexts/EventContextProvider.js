import EventContext from './EventContext';
import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import LoginContext from "./LoginContext";

export default function EventContextProvider({ children }) {
  const [members, setMembers] = useState([]);
  const [events, setEvents] = useState([]);
  const [error, setError] = useState({ status: false, message: '' });
  const { token, tokenIsValid } = useContext(LoginContext);
  
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
  
  const updateEvent = (description, members, payer, amount, id) => {
    axios
      .post('/api/events/' + id, {description, members, payer, amount})
      .then((response) => response.data)
      .then((updateEvent) => setEvents(events.map((event)=> event.id === id ? updateEvent : event)))
      .catch(console.log);
  }

  const createEvent = (title, members) =>
    axios
      .post('/api/events', { title, members }, header(token))
      .then((response) => response.data)
      .then((newEvent) => setEvents([...events, newEvent]))
      .catch(console.log);

  const addMember = (member) =>
    axios
      .get('/api/users/' + member, header(token))
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
        updateEvent
      }}
    >
      {children}
    </EventContext.Provider>
  );
}
