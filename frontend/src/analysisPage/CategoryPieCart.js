import React from 'react';
import { PieChart, Pie } from 'recharts';

export default function CategoryPieChart({ data }) {
  let renderLabel = function (entry) {
    return entry.category;
  };

  return (
    <PieChart width={300} height={250} data={data}>
      <Pie
        data={data}
        outerRadius={80}
        fill="#bdbdbd"
        dataKey="amount"
        nameKey="category"
        label={renderLabel}
        isAnimationActive={false}
      />
    </PieChart>
  );
}
