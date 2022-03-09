import React from 'react';
import styled from 'styled-components';

const StyledWrapper = styled.div`
  position: fixed;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #126e69;
  background: linear-gradient(135deg, #126e69 0%, #6dd6d1 100%);
`;

const StyledAuthCard = styled.div`
  width: 375px;
  min-height: 350px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 20px 30px 2px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const AuthTemplate = ({ children }) => (
  <StyledWrapper>
    <StyledAuthCard>{children}</StyledAuthCard>
  </StyledWrapper>
);

export default AuthTemplate;
