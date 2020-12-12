import React from 'react';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Cell,
} from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcon from './CategoryIcon';
import { categories2 as categories } from '../styling/categories';

export default function CategoryBarChartWithColors({ data }) {
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

  return (
    <Card>
      <CardContent>
        <ResponsiveContainer width="100%" aspect={4.0 / 2.6}>
          <BarChart data={data}>
            <XAxis
              dataKey="name"
              interval={0}
              height={24}
              tick={renderCustomAxisTick}
            />
            />
            <YAxis width={35} tick={{ fontSize: '0.9em' }} />
            <Bar dataKey="amount" barSize={30} fill="#bdbdbd">
              {data.map((entry, index) => (
                <Cell key={index} fill={categories[entry.name].color} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
