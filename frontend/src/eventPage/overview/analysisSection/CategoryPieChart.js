import React from 'react';
import { Cell, PieChart, Pie, ResponsiveContainer } from 'recharts';
import Card from '@material-ui/core/Card';
import { CardContent } from '@material-ui/core';
import CategoryIcons from './CategoryIcons';
import { categories2 as categories } from './Categories';
import { CardFirstLineStyle } from '../../../commons/styling/CommonStyledComponents';
import { formattedAmount } from '../../../commons/helperFunctions';
import { SecondSectionDiv } from '../Overview';

export default function CategoryPieChart({ data, sumTotal, sumPerPerson }) {
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
        <CategoryIcons type={data[index].name} />
      </svg>
    );
  };

  return (
    <Card>
      <CardContent>
        <CardFirstLineStyle>
          <p>Gesamtausgaben:</p>
          <p>{formattedAmount(sumTotal)}</p>
        </CardFirstLineStyle>
        <CardFirstLineStyle>
          <p>Ausgaben pro Person:</p>
          <p>{formattedAmount(sumPerPerson)}</p>
        </CardFirstLineStyle>
        <SecondSectionDiv>
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
              >
                {data.map((entry, index) => (
                  <Cell key={index} fill={categories[entry.name].color} />
                ))}
              </Pie>
            </PieChart>
          </ResponsiveContainer>
        </SecondSectionDiv>
      </CardContent>
    </Card>
  );
}
