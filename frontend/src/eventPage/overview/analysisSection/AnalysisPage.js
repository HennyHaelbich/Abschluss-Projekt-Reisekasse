import React, { useState, useEffect } from 'react';
import useEvent from '../../../hooks/useEvent';
import CategoryPieChart from './CategoryPieChart';

export default function AnalysisPage() {
  const { event } = useEvent();
  const [sumTotal, setSumTotal] = useState(0);
  const [sumPerPerson, setSumPerPerson] = useState(0);

  useEffect(() => {
    const total = event?.expenditures.reduce(
      (res, { amount }) => res + amount,
      0
    );
    setSumTotal(total);

    const perPerson = total / event?.members.length;
    setSumPerPerson(perPerson);
  }, [event, sumTotal]);

  let data = [];
  event?.expenditures.reduce(function (res, value) {
    if (!res[value.category]) {
      res[value.category] = { name: value.category, amount: 0 };
      data.push(res[value.category]);
    }
    res[value.category].amount += value.amount / 100;
    return res;
  }, {});

  return sumTotal ? (
    <CategoryPieChart
      data={data}
      sumTotal={sumTotal}
      sumPerPerson={sumPerPerson}
    />
  ) : null;
}
