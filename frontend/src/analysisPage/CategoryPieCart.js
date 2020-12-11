import React from 'react';
import { PieChart, Pie, ResponsiveContainer } from 'recharts';

export default function CategoryPieChart({ data }) {
  let renderLabel = function (entry) {
    return entry.name;
  };

  return (
    <ResponsiveContainer width="100%" aspect={4.0 / 3.0}>
      <PieChart data={data}>
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
    </ResponsiveContainer>
  );
}
