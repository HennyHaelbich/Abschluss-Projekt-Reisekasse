import React from 'react';
import { ResponsiveContainer, BarChart, Bar, XAxis, YAxis } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcon from './CategoryIcon';

export default function CategoryBarChart({ data }) {
  const renderCustomAxisTick = ({ x, y, payload }) => {
    return (
      <svg
        x={x - 12}
        y={y - 2}
        width={20}
        height={20}
        viewBox="0 0 1024 1024"
        color={categories[payload.value].color}
      >
        <CategoryIcon type={payload.value} />
      </svg>
    );
  };

  const categories = {
    excursion: {
      color: '#A4BB67',
      label: 'Ausflug',
    },
    entry: {
      color: '#5ea4c0',
      label: 'Eintritt',
    },
    transport: {
      color: '#d19e6e',
      label: 'Fahrtkosten',
    },
    party: {
      color: '#8d5ab1',
      label: 'Party/Getränke',
    },
    restaurant: {
      color: '#c75553',
      label: 'Restaurant',
    },
    supermarkt: {
      color: '#a4bb67',
      label: 'Supermarkt',
    },
    sleeping: {
      color: '#4a8fb0',
      label: 'Übernachtung',
    },
    none: {
      color: '#5e6368',
      label: 'Sonstiges',
    },
  };

  return (
    <Card>
      <CardContent>
        <ResponsiveContainer width="100%" aspect={4.0 / 2.6}>
          <BarChart data={data}>
            <XAxis
              dataKey="name"
              interval={0}
              angle={-45}
              textAnchor="end"
              height={24}
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