import React from 'react';
import {
  CardFirstLineStyle,
  CardSecondLineStyle,
} from '../../commons/styling/CommonStyledComponents';
import { displayName } from '../../commons/helperFunctions';
import styled from 'styled-components/macro';

export default function MembersList({ members }) {
  return (
    <ListStyled>
      {members.map((user) => (
        <li key={user.username}>
          <CardFirstLineStyle>
            <p>{displayName(user)}</p>
          </CardFirstLineStyle>
          <CardSecondLineStyle>
            <p>{user.username}</p>
          </CardSecondLineStyle>
        </li>
      ))}
    </ListStyled>
  );
}

const ListStyled = styled.ul`
  list-style: none;
`;
