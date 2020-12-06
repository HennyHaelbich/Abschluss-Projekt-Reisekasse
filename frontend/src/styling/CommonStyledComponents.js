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

export const CardSecondLineStyle = styled.div`
  display: block;
  p {
    margin: 0;
    font-size: 1em;
    color: grey;
  }
`;

export const CardPageStyle = styled.div`
  padding: var(--size-m);
  & > * {
    margin: var(--size-m) 0;
  }
`;
