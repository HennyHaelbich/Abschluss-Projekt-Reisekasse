import React from 'react';
import { Cell, PieChart, Pie, ResponsiveContainer } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';

export default function CategoryPieChartWithColors({ data }) {
  let renderLabel = function (entry) {
    return categories[entry.name].label;
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
        <ResponsiveContainer width="100%" aspect={4.0 / 2.0}>
          <PieChart data={data}>
            <Pie
              data={data}
              outerRadius={80}
              dataKey="amount"
              nameKey="category"
              // label={renderLabel}
              isAnimationActive={false}
            >
              {data.map((entry, index) => (
                <Cell key={index} fill={categories[entry.name].color} />
              ))}
            </Pie>
          </PieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
