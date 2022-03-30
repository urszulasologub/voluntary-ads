import React from 'react';
import styled from 'styled-components';
import PropTypes from 'prop-types';
import Navbar from 'Components/Navbar/Navbar';

const Wrapper = styled.div`
  background-color: #f2f2f2;
  height: 100vh;
`;
const RootTemplate = ({ children }) => (
  <Wrapper>
    <Navbar />
    {children}
  </Wrapper>
);

RootTemplate.propTypes = {
  children: PropTypes.oneOfType([PropTypes.element, PropTypes.node]).isRequired,
};

export default RootTemplate;
