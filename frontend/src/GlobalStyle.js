import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root {
   --primary: #5a189a;
   --primary-dark: #3c096c;
   --primary-light: #7b2cbf;
   
   --secundary: #ff8500;
   --secundary-dark: #ff7900;
   
  
   --size-xs: 4px;
   --size-s: 8px;
   --size-m: 12px;
   --size-l: 16px;
   --size-xl: 24px;
   --size-xxl: 32px;
   
    }

  html, body {
    margin: 0;
    font-family: 'Titillium Web', sans-serif;

  }
  
`;
