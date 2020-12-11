import React, { useState } from 'react';
import useEvent from '../hooks/useEvent';
import CategoryPieChart from './CategoryPieCart';
import styled from 'styled-components/macro';
import CategoryBarChart from './CategoryBarChart';
import makeStyles from '@material-ui/core/styles/makeStyles';

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
}));

export default function AnalysisPage() {
  const classes = useStyles();
  const { event } = useEvent();
  const [show, setShow] = useState('category');

  let data = [];
  event?.expenditures.reduce(function (res, value) {
    if (!res[value[show]]) {
      res[value[show]] = { name: value[show], amount: 0 };
      data.push(res[value[show]]);
    }
    res[value[show]].amount += value.amount / 100;
    return res;
  }, {});

  console.log(event?.expenditures);
  console.log(data);

  return (
    <DivStyled>
      <CategoryPieChart data={data} />
      <CategoryBarChart data={data} />
    </DivStyled>
  );
}

const DivStyled = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  justify-self: center;
  grid-gap: var(--size-xl);
  padding: var(--size-s);
`;
