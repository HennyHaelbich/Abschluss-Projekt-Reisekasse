import React from 'react';
import { Button } from '@material-ui/core';
import { useHistory } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { CardFirstLineStyle } from '../commons/styling/CommonStyledComponents';
import { displayName } from '../commons/helperFunctions';

export default function EventCard({ event }) {
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
              <React.Fragment key={member.username}>
                {displayName(member)}{' '}
              </React.Fragment>
            ))}
          </p>
        </CardContent>
      </Card>
    </>
  );
}
