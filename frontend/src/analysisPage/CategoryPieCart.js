import React from 'react';
import { Cell, PieChart, Pie, ResponsiveContainer } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcon from './CategoryIcon';

export default function CategoryPieChart({ data }) {
  const RADIAN = Math.PI / 180;
  const renderCustomizedLabel = ({
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    index,
  }) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 1.15;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
      <svg
        x={x - 10}
        y={y - 10}
        color={categories[data[index].name].color}
        textAnchor={'middle'}
        dominantBaseline="central"
        width={20}
        height={20}
        viewBox="0 0 1024 1024"
      >
        <CategoryIcon type={data[index].name} />
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
        <ResponsiveContainer width="100%" aspect={4.0 / 2.8}>
          <PieChart data={data}>
            <Pie
              data={data}
              outerRadius={80}
              dataKey="amount"
              nameKey="category"
              label={renderCustomizedLabel}
              labelLine={false}
              isAnimationActive={false}
            />
          </PieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
