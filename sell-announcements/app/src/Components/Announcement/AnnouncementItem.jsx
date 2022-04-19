import React from 'react';
import styled from 'styled-components';
import AddCircleOutlineRoundedIcon from '@mui/icons-material/AddCircleOutlineRounded';
import Grid from '@mui/material/Grid';
import { useHistory } from 'react-router-dom';

const Card = styled.div`
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 5px 10px -5px rgba(0, 0, 0, 0.4);
  margin: 10px auto;
  padding: 10px;
  width: 70%;
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

const StyledAddOutlinedIcon = styled(AddCircleOutlineRoundedIcon)`
  margin: 5px;
`;

const StyledDataDescription = styled.div`
  margin-top: 10px;
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
        <Grid item xs={6} md={4}>
          <StyledAddOutlinedIcon/><br/>
          <TextWrapper>
            <StyledName>{data.name}</StyledName>
            <StyledDataDescription>location: {data.location}</StyledDataDescription>
            <StyledDataDescription>{`${data.description.substring(0, 300)}...`}</StyledDataDescription>
          </TextWrapper>
        </Grid>
        <StyledQuantity>quantity: {data.quantity}</StyledQuantity>
      </Wrapper>
    </Card>
  );
};

export default AnnouncementItem;
