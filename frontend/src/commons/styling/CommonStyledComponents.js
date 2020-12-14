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
    margin-bottom: var(--size-m);
  }

  .MuiCardContent-root:last-child {
    padding-bottom: 16px;
  }
  .MuiCardContent-root {
    padding-right: 24px;
    padding-left: 24px;
  }
`;

export const ExpCardPageStyle = styled.div`
  padding: var(--size-m);
  & > * {
    margin-bottom: var(--size-m);
  }
  .MuiCardContent-root:last-child {
    padding-bottom: 16px;
  }
  .MuiCardContent-root {
    padding-right: 24px;
  }
`;

export const SmallButtonDiv = styled.div`
  display: flex;
  justify-content: center;
`;
