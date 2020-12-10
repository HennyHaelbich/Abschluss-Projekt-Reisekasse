import React from 'react';
import { PieChart, Pie } from 'recharts';

export default function CategoryPieChart({ data }) {
  let renderLabel = function (entry) {
    return entry.category;
  };

  return (
    <PieChart width={300} height={200} data={data}>
      <Pie
        data={data}
        outerRadius={100}
        fill="rgba(0, 0, 0, 0.12)"
        dataKey="amount"
        nameKey="category"
        label={renderLabel}
      />
    </PieChart>
  );
}
