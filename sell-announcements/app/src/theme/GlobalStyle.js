import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  @import url('https://fonts.googleapis.com/css?family=Montserrat:300,600');
  
  *, *::before, *::after {
    box-sizing: border-box;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
  
  body {
    background-color: #f2f2f2;
    margin: 0;
    padding: 0;
    font-size: 1.6rem;
    font-family: "Montserrat", sans-serif;
  }
`;

export default GlobalStyle;
