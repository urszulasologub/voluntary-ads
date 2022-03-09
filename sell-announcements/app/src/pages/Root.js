import React from 'react';
import GlobalStyle from 'theme/GlobalStyle';
import Store from 'components/data/Store';
import Routes from 'pages/Routes';

const Root = () => {
  return (
    <Store>
      <GlobalStyle />
      <Routes />
    </Store>
  );
};

export default Root;
