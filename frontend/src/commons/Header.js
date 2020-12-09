import React from 'react';
import styled from 'styled-components/macro';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { useHistory } from 'react-router-dom';
import { IconButton } from '@material-ui/core';

export default function Header({ title, backbutton }) {
  const history = useHistory();

  return (
    <>
      <HeaderStyled>
        <div>
          {backbutton && (
            <IconButton onClick={() => history.push(`/events`)}>
              <ArrowBackIcon />
            </IconButton>
          )}
        </div>
        <div className="item1">
          <HeadingStyled>{title}</HeadingStyled>
        </div>
        <div></div>
      </HeaderStyled>
    </>
  );
}

const HeaderStyled = styled.header`
  position: fixed;
  top: 0;
  left: 0;
  height: 48px;
  display: grid;
  grid-template-columns: 50px 1fr 50px;
  align-items: center;
  background: var(--primary);
  padding: 0;
  color: white;
  z-index: 10;
  width: 100%;
  .MuiIconButton-root {
    color: white;
  }
  .item1 {
    grid-column-start: 0;
  }
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: white;
  font-weight: normal;
  font-family: 'Amaranth';
  font-size: x-large;
  text-align: center;
`;
