import React, { useContext } from 'react';
import styled from 'styled-components/macro';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import { Button } from '@material-ui/core';
import useEvent from '../hooks/useEvent';
import { useHistory } from 'react-router-dom';
import EventContext from '../contexts/EventContext';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import {
  CardFirstLineStyle,
  CardPageStyle,
} from '../styling/CommonStyledComponents';

export default function Overview() {
  const { event, eventId } = useEvent();
  const history = useHistory();
  const members = event?.members;

  return (
    <CardPageStyle>
      {members
        ? members.map((member) => (
            <Card>
              <CardContent>
                <CardFirstLineStyle>
                  <p>{displayName(member)}</p>
                  <p>{formattedAmount(member.balance)}</p>
                </CardFirstLineStyle>
              </CardContent>
            </Card>
          ))
        : null}
      <ButtonDiv>
        <Button variant="contained" color="primary" onClick={handleClick}>
          Ausgleichszahlungen
        </Button>
      </ButtonDiv>
    </CardPageStyle>
  );

  function handleClick(event) {
    event.preventDefault();
    history.push(`/event/compensation/${eventId}`);
  }
}

const ButtonDiv = styled.div`
  display: flex;
  justify-content: center;
`;
