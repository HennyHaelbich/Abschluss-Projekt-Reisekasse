import React from 'react';
import { useHistory } from 'react-router-dom';
import { displayName, formattedAmount } from '../../commons/helperFunctions';
import useEvent from '../../hooks/useEvent';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import {
  CardFirstLineStyle,
  CardPageStyle,
  SmallButtonDiv,
} from '../../commons/styling/CommonStyledComponents';
import AnalysisPage from './analysisSection/AnalysisPage';
import { Button } from '@material-ui/core';
import styled from 'styled-components/macro';

export default function Overview() {
  const { event } = useEvent();
  const members = event?.members;
  const history = useHistory();

  return (
    <CardPageStyle>
      <Card>
        <CardContent>
          {members
            ? members.map((member) => (
                <CardFirstLineStyle key={member.username}>
                  <p>{displayName(member)}</p>
                  <p>{formattedAmount(member.balance)}</p>
                </CardFirstLineStyle>
              ))
            : null}
          <SecondSectionDiv>
            <SmallButtonDiv>
              <Button
                color="primary"
                variant="outlined"
                onClick={() => history.push(`/event/compensation/${event.id}`)}
              >
                Abrechnung
              </Button>
            </SmallButtonDiv>
          </SecondSectionDiv>
        </CardContent>
      </Card>
      <AnalysisPage />
      <br />
      <br />
      <br />
      <br />
    </CardPageStyle>
  );
}

export const SecondSectionDiv = styled.div`
  padding-top: var(--size-m);
`;
