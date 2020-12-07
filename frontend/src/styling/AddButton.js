import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import React from 'react';
import styled from 'styled-components/macro';

export default function AddButton({ handle }) {
  return (
    <DivStyled>
      <Fab color="primary" aria-label="add" onClick={() => handle()}>
        <AddIcon />
      </Fab>
    </DivStyled>
  );
}

const DivStyled = styled.div`
  position: fixed;
  bottom: 30px;
  right: 30px;
`;
