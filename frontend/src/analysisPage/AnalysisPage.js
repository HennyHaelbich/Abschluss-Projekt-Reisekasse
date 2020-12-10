import React, { useState } from 'react';
import Header from '../commons/Header';
import useEvent from '../hooks/useEvent';
import CategoryPieChart from './CategoryPieCart';
import styled from 'styled-components/macro';
import CategoryBarChart from './CategoryBarChart';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
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
    if (!res[value.category]) {
      res[value.category] = { category: value.category, amount: 0 };
      data.push(res[value.category]);
    }
    res[value.category].amount += value.amount;
    return res;
  }, {});

  return (
    <>
      <Header title={'Auswertungen'} backbutton />
      <DivStyled>
        <FlexDiv>
          <FormControl variant="outlined" className={classes.formControl}>
            <Select
              native
              value={show}
              onChange={(event) => setShow(event.target.value)}
            >
              <option value="category">Kategorie</option>
              <option value="date">Datum</option>
              <option value="place">Ort</option>
            </Select>
          </FormControl>
        </FlexDiv>
        <CategoryPieChart data={data} />
        <CategoryBarChart data={data} />
      </DivStyled>
    </>
  );
}

const DivStyled = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  justify-self: center;
  grid-gap: var(--size-xl);
  padding: var(--size-l);
`;

export const FlexDiv = styled.div`
  display: flex;
  justify-content: center;
`;
