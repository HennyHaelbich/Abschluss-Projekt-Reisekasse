import React from 'react';
import Header from '../commons/Header';
import useEvent from '../hooks/useEvent';

export default function AnalysisPage() {
  const expenditures = useEvent();

  console.log(expenditures);

  return (
    <>
      <Header title={'Auswertungen'} backbutton />
      <p>hier soll mal eine Grafik stehen</p>
      {expenditures ? (
        <lu>
          {expenditures.map((exp) => (
            <li key={exp.id}>
              {exp.category}
              {exp.amount}
            </li>
          ))}
        </lu>
      ) : null}
    </>
  );
}
