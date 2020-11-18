import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';

export default function ListUser({}) {
  const { members } = useContext(EventContext);

  console.log('ListMembers', members);

  return (
    <ul>
      {members.map((user) => (
        <li key={user.username}>{user.username}</li>
      ))}
    </ul>
  );
}
