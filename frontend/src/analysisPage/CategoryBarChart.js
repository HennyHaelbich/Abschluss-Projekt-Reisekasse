import React from 'react';
import { BarChart, Bar, XAxis, YAxis } from 'recharts';

export default function CategoryBarChart({ data }) {
  console.log('ChartPage', data);

  return (
    <BarChart width={300} height={200} data={data}>
      <XAxis dataKey="category" />
      <YAxis />
      <Bar dataKey="amount" barSize={30} fill="rgba(0, 0, 0, 0.12)" />
    </BarChart>
  );
}
