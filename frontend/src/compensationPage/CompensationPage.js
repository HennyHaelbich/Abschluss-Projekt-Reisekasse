import React, { useContext, useEffect, useState } from 'react';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import Card from '@material-ui/core/Card';
import EventContext from '../contexts/EventContext';
import Header from '../commons/Header';
import {
  CardSecondLineStyle,
  CardPageStyle,
} from '../styling/CommonStyledComponents';
import CardContent from '@material-ui/core/CardContent';
import useEvent from '../hooks/useEvent';
import styled from 'styled-components/macro';
import { useHistory } from 'react-router-dom';

export default function CompensationPage() {
  const { event } = useEvent();
  const [compensationsPayments, setCompensationPayments] = useState([]);
  const { calculateCompensation } = useContext(EventContext);
  const history = useHistory();

  useEffect(() => {
    event &&
      calculateCompensation(event.members)
        .then((compensations) => setCompensationPayments(compensations))
        .catch(console.log);
  }, [event, calculateCompensation, setCompensationPayments]);

  const handleBackClick = () => {
    history.push(`/event/${event.id}/expenditures`);
  };

  return event ? (
    <>
      <Header title={event.title} handleBackClick={handleBackClick} />

      <CardPageStyle>
        {compensationsPayments.map((compensationsPayment) => (
          <Card
            key={
              compensationsPayment.payer.username +
              compensationsPayment.paymentReceiver.username
            }
          >
            <CardContent>
              <CardSectionsStyled>
                <CardSecondLineStyle>
                  <p>
                    von{' '}
                    <strong>{displayName(compensationsPayment.payer)}</strong>
                  </p>
                  <p>
                    an{' '}
                    <strong>
                      {displayName(compensationsPayment.paymentReceiver)}
                    </strong>
                  </p>
                </CardSecondLineStyle>

                <AmountSectionStyled>
                  {formattedAmount(compensationsPayment.amount)}
                </AmountSectionStyled>
              </CardSectionsStyled>
            </CardContent>
          </Card>
        ))}
      </CardPageStyle>
    </>
  ) : null;
}

const CardSectionsStyled = styled.div`
  display: flex;
  justify-content: space-between;
`;

const AmountSectionStyled = styled.div`
  font-weight: 600;
  font-size: 1.15em;
`;
