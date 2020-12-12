import React from 'react';
import { Cell, PieChart, Pie, ResponsiveContainer } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcon from './CategoryIcon';
import { categories2 as categories } from '../styling/categories';
import { CardFirstLineStyle } from '../styling/CommonStyledComponents';
import { formattedAmount } from '../helperFunctions/helperFunctions';

export default function CategoryPieChartWithColors({
  data,
  sumTotal,
  sumPerPerson,
}) {
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

  return (
    <Card>
      <CardContent>
        <ResponsiveContainer width="100%" aspect={4.0 / 2.5}>
          <PieChart data={data}>
            <Pie
              data={data}
              outerRadius={80}
              dataKey="amount"
              nameKey="category"
              label={renderCustomizedLabel}
              labelLine={false}
              isAnimationActive={false}
            >
              {data.map((entry, index) => (
                <Cell key={index} fill={categories[entry.name].color} />
              ))}
            </Pie>
          </PieChart>
        </ResponsiveContainer>
        <CardFirstLineStyle>
          <p>Gesamtausgaben:</p>
          <p>{formattedAmount(sumTotal)}</p>
        </CardFirstLineStyle>
        <CardFirstLineStyle>
          <p>Ausgaben pro Person:</p>
          <p>{formattedAmount(sumPerPerson)}</p>
        </CardFirstLineStyle>
      </CardContent>
    </Card>
  );
}
