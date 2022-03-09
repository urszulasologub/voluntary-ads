import React from 'react';
import styled from 'styled-components';
import RootTemplate from 'templates/RootTemplate';
import AnnouncementForm from 'components/Announcement/AnnouncementForm';

const StyledFromCard = styled.div`
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 10px 20px -5px rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: 30px auto;
  width: 80%;
`;

const AnnouncementCreatePage = () => {
  return (
    <RootTemplate>
      <StyledFromCard>
        <h3>Add Announcement</h3>
        <AnnouncementForm />
      </StyledFromCard>
    </RootTemplate>
  );
};

export default AnnouncementCreatePage;
