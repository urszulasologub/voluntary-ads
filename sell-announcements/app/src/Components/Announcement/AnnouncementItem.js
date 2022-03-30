import React from 'react';
import styled from 'styled-components';
import { useHistory } from 'react-router-dom';

const Card = styled.div`
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 5px 10px -5px rgba(0, 0, 0, 0.4);
  margin: 10px auto;
  width: 90%;
`;

const Wrapper = styled.div`
  margin: 10px 20px;
  display: flex;
  flex-direction: row;
`;

const TextWrapper = styled.ul`
  list-style: none;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  position: relative;
  max-height: 150px;
  margin: 0 0 auto;
`;

const StyledName = styled.div`
  margin-top: 10px;
  font-size: 25px;
  font-weight: bold;
`;

const StyledLocation = styled.div`
  margin-top: auto;
  font-size: 15px;
`;

const StyledQuantity = styled.div`
  font-size: 15px;
  font-weight: bold;
  margin-left: auto;
  margin-top: 10px;
`;

const AnnouncementItem = ({ data }) => {
  const history = useHistory();

  return (
    <Card
      onClick={() => {
        history.push(`/announcements/${data.id}`);
      }}
    >
      <Wrapper>
        <TextWrapper>
          <StyledName>{data.name}</StyledName>
          <StyledLocation>location: {data.location}</StyledLocation>
        </TextWrapper>
        <StyledQuantity>quantity: {data.quantity}</StyledQuantity>
      </Wrapper>
    </Card>
  );
};

export default AnnouncementItem;
