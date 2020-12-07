import React, { useContext } from 'react';
import {
  displayName,
  formattedAmount,
  formattedDate,
} from '../helperFunctions/helperFunctions';
import {
  CardFirstLineStyle,
  CardSecondLineStyle,
} from '../styling/CommonStyledComponents';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { makeStyles } from '@material-ui/core/styles';
import styled from 'styled-components/macro';
import EventContext from '../contexts/EventContext';
import { useHistory, useParams } from 'react-router-dom';
import useEvent from '../hooks/useEvent';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';

const useStyles = makeStyles((theme) => ({
  options: {
    padding: '4px',
  },
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

export default function ExpenditureCard({ expenditure }) {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false);
  const { removeExpenditure } = useContext(EventContext);
  const { expenditureId } = useParams();
  const { eventId } = useEvent();
  const history = useHistory();

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  return (
    <Card className={classes.root}>
      <CardContent>
        <CardFirstLineStyle>
          <p>{expenditure.description}</p>
          <p>{formattedAmount(expenditure.amount)}</p>
        </CardFirstLineStyle>
        <CardSecondLineStyle>
          <p>
            Bezahlt von <strong>{displayName(expenditure.payer)}</strong>
          </p>
          <p>{formattedDate(expenditure.timestamp)}</p>
        </CardSecondLineStyle>
      </CardContent>

      <CardActions className={classes.options} disableSpacing>
        <IconButton onClick={() => handleDelete()} aria-label="delete">
          <DeleteIcon />
        </IconButton>
        <IconButton aria-label="edit">
          <EditIcon />
        </IconButton>
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
          <DivStyled>
            <p>
              <strong>Anteil pro Person:</strong>
            </p>
            {expenditure.expenditurePerMemberList.map(
              (expenditurePerMember) => (
                <ListStyled key={expenditurePerMember.username}>
                  <p>{displayName(expenditurePerMember)}</p>
                  <p>{formattedAmount(expenditurePerMember.amount)}</p>
                </ListStyled>
              )
            )}
          </DivStyled>
        </CardContent>
      </Collapse>
    </Card>
  );

  function handleDelete() {
    removeExpenditure(eventId, expenditureId);
    history.push(`/event/expenditures/${eventId}`);
  }
}

const ListStyled = styled.ul`
  overflow: scroll;
  padding: 0;
  margin: 0;
  list-style: none;
  display: flex;
  justify-content: space-between;
`;

const DivStyled = styled.div`
  color: grey;
  p {
    margin: 0;
  }
`;