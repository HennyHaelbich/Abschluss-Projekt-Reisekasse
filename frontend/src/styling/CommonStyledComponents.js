import styled from 'styled-components/macro';

export const CardFirstLineStyle = styled.div`
  display: flex;
  justify-content: space-between;
  font-weight: 600;
  font-size: 1.15em;
  p {
    margin: 0;
  }
`;

export const CardPageStyle = styled.div`
  display: grid;
  grid-template-rows: 1fr;
  grid-gap: var(--size-s);
  padding: var(--size-s);
`;
