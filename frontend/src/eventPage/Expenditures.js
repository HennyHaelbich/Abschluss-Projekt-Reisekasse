import React from 'react';
import { useHistory } from 'react-router-dom';
import {
  displayName,
  formattedAmount,
} from '../helperFunctions/helperFunctions';
import useEvent from '../hooks/useEvent';
import AddButton from '../styling/AddButton';
import styled from 'styled-components/macro';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

const useStyles = makeStyles((theme) => ({
  expand: {
    transform: 'rotate(0deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
      duration: theme.transitions.duration.shortest,
    }),
  },
  expandOpen: {
    transform: 'rotate(180deg)',
  },
}));

export default function Expenditures() {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false);
  const { event } = useEvent();
  const history = useHistory();

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  return (
    <CardPageStyle>
      {event?.expenditures.map((expenditure) => (
        <Card className={classes.root}>
          <CardContent>
            <CardFirstLineStyle>
              <p>{expenditure.description}:</p>
              <p>{formattedAmount(expenditure.amount)}</p>
            </CardFirstLineStyle>
            <CardSecondLineStyle>
              <p>
                Bezahlt von {displayName(expenditure.payer)} am{' '}
                {expenditure.timestamp}
              </p>
            </CardSecondLineStyle>
          </CardContent>

          <CardActions disableSpacing>
            <IconButton
              className={clsx(classes.expand, {
                [classes.expandOpen]: expanded,
              })}
              onClick={handleExpandClick}
              aria-expanded={expanded}
              aria-label="show more"
            >
              <ExpandMoreIcon />
            </IconButton>
          </CardActions>
          <Collapse in={expanded} timeout="auto" unmountOnExit>
            <CardContent>
              <p>Method:</p>
              <p>
                Heat 1/2 cup of the broth in a pot until simmering, add saffron
                and set aside for 10 minutes.
              </p>
            </CardContent>
          </Collapse>
        </Card>
      ))}

      <AddButton handle={handleAddExpenditure} />
    </CardPageStyle>
  );

  function handleAddExpenditure() {
    history.push(`/event/expenditures/${event.id}`);
  }
}

const CardPageStyle = styled.div`
  display: grid;
  grid-template-rows: 1fr;
  grid-gap: var(--size-s);
  padding: var(--size-s);
`;

const CardFirstLineStyle = styled.div`
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  p {
    margin: 0;
  }
`;

const CardSecondLineStyle = styled.div`
  display: flex;
  p {
    margin: 0;
    font-size: 0.8em;
  }
`;
