import React from 'react';
import { ResponsiveContainer, BarChart, Bar, XAxis, YAxis } from 'recharts';
import CommuteIcon from '@material-ui/icons/Commute';
//import ExpenditureCategoryIcon from '../eventPage/ExpenditureCategoryIcon';

export default function CategoryBarChart({ data }) {
  return (
    <ResponsiveContainer width="100%" aspect={4.0 / 3.0}>
      <BarChart data={data}>
        <XAxis
          dataKey="category"
          interval={0}
          angle={-45}
          textAnchor="end"
          height={70}
          tick={{ fontSize: '0.9em' }}
        />
        />
        <YAxis width={35} tick={{ fontSize: '0.9em' }} />
        <Bar dataKey="amount" barSize={30} fill="#bdbdbd" />
      </BarChart>
    </ResponsiveContainer>
  );
}
