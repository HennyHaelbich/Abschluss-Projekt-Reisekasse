import React, {useContext} from 'react';
import { displayName, formattedAmount } from '../helperFunctions/helperFunctions'
import Card from "@material-ui/core/Card";
import EventContext from "../contexts/EventContext";

export default function CompensationPage() {
  const { compensationsPayments } = useContext(EventContext);
  
  
  return (
    compensationsPayments ?
      <div>
        {compensationsPayments.map((compensationsPayment) => (
          <Card>
            <p>Betrag {formattedAmount(compensationsPayment.amount)}</p>
            <p>von {displayName(compensationsPayment.payer)}</p>
            <p>an {displayName(compensationsPayment.paymentReceiver)}</p>
          </Card>
        ))}
      </div> : null
  );
  
}
