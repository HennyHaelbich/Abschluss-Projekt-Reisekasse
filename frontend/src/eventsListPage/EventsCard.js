import React, { useContext } from 'react';
import EventContext from '../contexts/EventContext';
import { Button } from '@material-ui/core';
import { useHistory } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { CardFirstLineStyle } from '../styling/CommonStyledComponents';
import { displayName } from '../helperFunctions/helperFunctions';

export default function ShowEventList({ event }) {
  const history = useHistory();

  return (
    <>
      <Card key={event.id}>
        <CardContent>
          <CardFirstLineStyle>
            <p>{event.title}</p>
            <Button
              variant="outlined"
              color="primary"
              onClick={() => history.push(`/event/${event.id}`)}
            >
              Details
            </Button>
          </CardFirstLineStyle>
          <p>
            {event.members.map((member) => (
              <>{displayName(member)} </>
            ))}
          </p>
        </CardContent>
      </Card>
    </>
  );
}
