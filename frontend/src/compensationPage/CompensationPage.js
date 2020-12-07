import React, { useContext, useEffect, useState } from 'react';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import Card from '@material-ui/core/Card';
import EventContext from '../contexts/EventContext';
import Header from '../commons/Header';
import {
  CardFirstLineStyle,
  CardSecondLineStyle,
  CardPageStyle,
} from '../styling/CommonStyledComponents';
import CardContent from '@material-ui/core/CardContent';
import useEvent from '../hooks/useEvent';

export default function CompensationPage() {
  const { event } = useEvent();
  const [compensationsPayments, setCompensationPayments] = useState([]);
  const { calculateCompensation } = useContext(EventContext);

  useEffect(() => {
      calculateCompensation(event.members)
        .then((compensations) => setCompensationPayments(compensations))
        .then(console.log(compensationsPayments))
        .catch(console.log);
  }, [event]);

  return compensationsPayments ? (
    <div>
      <Header title="Ausgleichszahlungen" backbutton />
      <CardPageStyle>
        {compensationsPayments.map((compensationsPayment) => (
          <Card>
            <CardContent>
              <CardFirstLineStyle>
                <p>Betrag</p>
                <p>{formattedAmount(compensationsPayment.amount)}</p>
              </CardFirstLineStyle>
              <CardSecondLineStyle>
                <p>
                  von <strong>{displayName(compensationsPayment.payer)}</strong>
                </p>
                <p>
                  an{' '}
                  <strong>
                    {displayName(compensationsPayment.paymentReceiver)}
                  </strong>
                </p>
              </CardSecondLineStyle>
            </CardContent>
          </Card>
        ))}
      </CardPageStyle>
    </div>
  ) : null;
}
