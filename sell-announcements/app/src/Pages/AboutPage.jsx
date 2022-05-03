import React from 'react';
import styled from 'styled-components';
import RootTemplate from 'templates/RootTemplate';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import ImageListItemBar from '@mui/material/ImageListItemBar';
import Hidden from '@mui/material/Hidden';

const AboutPage = () => {
  const imagesToShowInGallery = [
    {
      img: 'https://images.pexels.com/photos/11421071/pexels-photo-11421071.jpeg',
      title: 'Man at Protest Against War in Ukraine',
      author: 'Mathias P.R. Reding',
    },
    {
      img: 'https://images.pexels.com/photos/11291099/pexels-photo-11291099.jpeg',
      title: 'People in the Street Protesting against the War in Ukraine',
      author: 'Katie Godowski',
    }
  ]

  const showGalleryImages = () => {
    return (
        <ImageList>
          {imagesToShowInGallery.map((item) => (
            <ImageListItem key={item.img}>
              <img
                src={`${item.img}?w=248&fit=crop&auto=format`}
                srcSet={`${item.img}?w=248&fit=crop&auto=format&dpr=2 2x`}
                alt={item.title}
                loading="lazy"
              />
              <ImageListItemBar
                title={<Hidden xsDown>{item.title}</Hidden>}
                subtitle={<span>by: {item.author}</span>}
                position="below"
              />
            </ImageListItem>
          ))}
        </ImageList>
    );
  };

  return (
    <RootTemplate>
      <Wrapper>
        <Description>
         <h3>About</h3>
         During those tough times our responsibility is to spread good will among those who need it most. <br/>
         Our mission is to create a space where people can share their love and possibilities to help others,
         especially our friends from Ukraine, whose families are now suffering the enormous pain
        </Description>
        <MainButton href="/announcements">Browse</MainButton>
        {showGalleryImages()}
      </Wrapper>
    </RootTemplate>
  );
};

export default AboutPage;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  overflow: hidden;
  margin: 30px auto;
  width: 90%;
`;

const Description = styled.div`
  font-size: medium;
  text-align: center;
  margin-bottom: 30px;
`;

const MainButton = styled.a`
  margin: 15px;
  padding: 20px;
  width: 300px;
  background: #01579b;
  color: white;
  text-align: center;
  text-decoration: none;
  border-radius: 5px;
`;

