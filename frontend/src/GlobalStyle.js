import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root {
   --primary: #3F51B5;
   --primary-dark: #303F6F;
   --primary-light: #E0E4E8;
   
   --text-icons: #FFFFFF;
   --accent-color: #448AFF;
   --primary-text: #212121;
   --secundary-text: #757575;
   --divider-color: #BDBDBD;
  
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
