import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import React from 'react';
import styled from 'styled-components/macro';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#5a189a',
    },
  },
});

export default function AddButton({ handle }) {
  return (
    <DivStyled>
      <MuiThemeProvider theme={theme}>
        <Fab color="primary" aria-label="add" onClick={() => handle()}>
          <AddIcon />
        </Fab>
      </MuiThemeProvider>
    </DivStyled>
  );
}

const DivStyled = styled.div`
  position: fixed;
  bottom: 30px;
  right: 30px;
`;
