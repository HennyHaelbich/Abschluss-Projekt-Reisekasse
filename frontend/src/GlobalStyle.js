import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root {
   --primary-main: #193251;
   --primary-75: #7589A2;
   --primary-50: #E0E4E8;
   --primary-25: #F8F8F8;

   --secundary-main: #FF5A36;
   --secundary-75: #FF9C86;
   --secundary-50: #FFBDAF;
   --secundary-25: #FFDED7;
  
   --size-xs: 4px;
   --size-s: 8px;
   --size-m: 12px;
   --size-l: 16px;
   --size-xl: 24px;
   --size-xxl: 32px;
   
   
    }

  html, body {
    margin: 0;
    font-family: 'Roboto', sans-serif;

  }
  
`;
