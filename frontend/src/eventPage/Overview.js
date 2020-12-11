import React from 'react';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import useEvent from '../hooks/useEvent';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import {
  CardFirstLineStyle,
  CardPageStyle,
  SmallButtonDiv,
} from '../styling/CommonStyledComponents';
import AnalysisPage from '../analysisPage/AnalysisPage';

export default function Overview() {
  const { event } = useEvent();
  const members = event?.members;

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
        </CardContent>
      </Card>
      <AnalysisPage />
    </CardPageStyle>
  );
}
