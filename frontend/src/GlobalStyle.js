import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root {
   --primary: #5f2790;
   --primary-dark: #3c096c;
   --primary-light: #7b2cbf;
   
   --secondary: #ff8500;
   --secondary-dark: #ff7900;
  
   --size-xs: 4px;
   --size-s: 8px;
   --size-m: 12px;
   --size-l: 16px;
   --size-xl: 24px;
   --size-xxl: 32px;
   
   --color-charts-excursion: #7eb668;
   --color-charts-entry: #3795aa;
   --color-charts-transport: #534899eb;
   --color-charts-party: #e99652;
   --color-charts-restaurant: #e76642;
   --color-charts-supermarkt: #e4b423;
   --color-charts-sleeping: #2694cf;
   --color-charts-none: #6A7077;
   
   
  }

  html, body {
    margin: 0;
    font-family: 'Titillium Web', sans-serif;

  }
  
`;
