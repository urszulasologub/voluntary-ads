import React from 'react';
import styled from 'styled-components';
import noImage from 'assets/noImage.png';
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

const StyledPrice = styled.div`
  font-size: 15px;
  font-weight: bold;
  margin-left: auto;
  margin-top: 10px;
`;

const Img = styled.li`
  list-style: none;
  width: 130px;
  height: 130px;
  background-color: #e3e3e3;
  background-image: url(${({ background }) => (background ? `data:image/png;base64,${background}` : noImage)});
  background-repeat: no-repeat;
  background-size: 130px 130px;
  border-radius: 4px;
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
        {data.image ? <Img background={btoa(String.fromCharCode.apply(null, new Uint8Array(data.image)))} /> : <Img />}
        <TextWrapper>
          <StyledName>{data.name}</StyledName>
          <StyledLocation>location: {data.location}</StyledLocation>
        </TextWrapper>
        <StyledPrice>price: {data.price} $</StyledPrice>
      </Wrapper>
    </Card>
  );
};

export default AnnouncementItem;
