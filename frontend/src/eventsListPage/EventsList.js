import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';
import { Button } from '@material-ui/core';
import { useHistory } from 'react-router-dom';
import styled from 'styled-components/macro';

export default function ShowEventList() {
  const { events } = useContext(EventContext);
  const history = useHistory();

  return (
    <ul>
      {events.map((event) => (
        <ListStyled key={event.id}>
          {event.title}
          <Button
            variant="outlined"
            onClick={() => history.push(`/event/${event.id}`)}
          >
            Details
          </Button>
        </ListStyled>
      ))}
    </ul>
  );
}

const ListStyled = styled.ul`
  overflow: scroll;
  margin: 0;
  padding: var(--size-m);
  list-style: none;
  display: grid;
  grid-auto-rows: min-content;
  gap: var(--size-m);
`;
