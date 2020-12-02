import React from 'react';

export default function MembersList({members}) {

  return (
      <ul>
        {members.map((user) => (
          <li key={user.username}>
            <p>{user.firstName} {user.lastName.substring(0,1)}.</p>
            <p>{user.username}</p>
          </li>
        ))}
      </ul>
  );
}
