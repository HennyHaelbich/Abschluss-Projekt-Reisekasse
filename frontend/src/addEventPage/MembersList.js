import React from 'react';

export default function MembersList({members}) {

  return (
    <ul>
      {members.map((user) => (
        <li key={user.username}>{user.username}</li>
      ))}
    </ul>
  );
}
