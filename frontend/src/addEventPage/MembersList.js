import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';

export default function MembersList() {
  const { members } = useContext(EventContext);

  return (
    <ul>
      {members.map((user) => (
        <li key={user.username}>{user.username}</li>
      ))}
    </ul>
  );
}
