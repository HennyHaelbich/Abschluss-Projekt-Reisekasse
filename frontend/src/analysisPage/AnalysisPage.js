import React from 'react';
import Header from '../commons/Header';
import useEvent from '../hooks/useEvent';

export default function AnalysisPage() {
  const { event } = useEvent();
  console.log(event?.expenditures);

  let result = [];
  event?.expenditures.reduce(function (res, value) {
    if (!res[value.category]) {
      res[value.category] = { category: value.category, amount: 0 };
      result.push(res[value.category]);
    }
    res[value.category].amount += value.amount;
    return res;
  }, {});
  console.log('Result', result);

  return (
    <>
      <Header title={'Auswertungen'} backbutton />

      <PieChart width={800} height={400} onMouseEnter={this.onPieEnter}>
        <Pie
          data={data}
          cx={300}
          cy={200}
          labelLine={false}
          label={renderCustomizedLabel}
          outerRadius={80}
          fill="#8884d8"
        >
          {data.map((entry, index) => (
            <Cell fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
      </PieChart>
    </>
  );
}
