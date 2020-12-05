import React from 'react';
import styled from 'styled-components/macro';

export default function Header({ title }) {
  return (
    <HeaderStyled>
      <HeadingStyled>{title}</HeadingStyled>
    </HeaderStyled>
  );
}

const HeaderStyled = styled.header`
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--primary);
  padding: var(--size-s);
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: white;
  font-weight: normal;
  font-family: 'Amaranth';
  font-size: x-large;
  text-align: center;
`;
