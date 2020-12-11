import React from 'react';
import { ResponsiveContainer, BarChart, Bar, XAxis, YAxis } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcon from './CategoryIcon';

const renderCustomAxisTick = ({ x, y, payload }) => {
  return (
    <svg x={x - 12} y={y + 4} width={24} height={24} viewBox="0 0 1024 1024">
      <CategoryIcon type={payload.value} />
    </svg>
  );
};

export default function CategoryBarChart({ data }) {
  return (
    <Card>
      <CardContent>
        <ResponsiveContainer width="100%" aspect={4.0 / 3.0}>
          <BarChart data={data}>
            <XAxis
              dataKey="name"
              interval={0}
              angle={-45}
              textAnchor="end"
              height={70}
              // tick={{ fontSize: '0.9em' }}
              tick={renderCustomAxisTick}
            />
            />
            <YAxis width={35} tick={{ fontSize: '0.9em' }} />
            <Bar dataKey="amount" barSize={30} fill="#bdbdbd" />
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
