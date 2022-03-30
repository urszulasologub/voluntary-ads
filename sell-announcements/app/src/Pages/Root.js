import React from 'react';
import GlobalStyle from 'theme/GlobalStyle';
import Store from 'Components/data/Store';
import Routes from 'Pages/Routes';

const Root = () => {
  return (
    <Store>
      <GlobalStyle />
      <Routes />
    </Store>
  );
};

export default Root;
