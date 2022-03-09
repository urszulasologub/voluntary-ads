import React, { useState, useEffect, useContext } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { REMOTE_HOST } from 'config';
import RootTemplate from 'templates/RootTemplate';
import CircularProgress from '@material-ui/core/CircularProgress';
import Alert from '@material-ui/lab/Alert';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import Button from '@material-ui/core/Button';
import noImage from 'assets/noImage.png';
import { Context } from 'components/data/Store';
import { useHistory } from 'react-router-dom';
import MessageBox from 'components/Message/MessageBox';

const AnnouncementShowPage = value => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(null);
  const [error, setError] = useState(null);
  const [state] = useContext(Context);
  const history = useHistory();

  const archiveOrDeleteAdmin = (action, id) => {
    const options = {
      method: action === 'delete' ? 'DELETE' : 'PUT',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin_announcement/${action}/${id}`,
    };

    axios(options).then(() => {
      if (action === 'delete') {
        history.push(`/`);
      } else {
        history.push(`/announcements/${id}`);
      }
    });
  };

  const archiveOrDelete = (action, id) => {
    const options = {
      method: action === 'delete' ? 'DELETE' : 'PUT',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/announcements/${action}/${id}`,
    };

    axios(options).then(() => {
      if (action === 'delete') {
        history.push(`/`);
      } else {
        history.push(`/announcements/${id}`);
      }
    });
  };

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/announcements/${value.match.params.id}`,
    };

    setLoading(true);

    axios(options)
      .then(e => {
        setData(e.data);
      })
      .catch(() => {
        setError('Announcement not exist');
      })
      .then(() => {
        setLoading(false);
      });
  }, [value.match.params.id]);

  return (
    <RootTemplate>
      {error ? <Alert severity="error">{error}</Alert> : null}
      {loading ? <StyledCircularProgress /> : null}
      {data ? (
        <Card>
          <CardWrapper>
            {data.user_id.id === Number(state.id) && state.admin !== 'true' ? (
              <StyledButtonGroup color="secondary" aria-label="outlined secondary button group">
                <Button onClick={() => (window.confirm('Are you sure you want to archive this announcement') ? archiveOrDelete('hide', data.id) : null)}>
                  Archive
                </Button>
                <Button onClick={() => (window.confirm('Are you sure you want to delete this announcement') ? archiveOrDelete('delete', data.id) : null)}>
                  Delete
                </Button>
              </StyledButtonGroup>
            ) : null}

            {state.admin === 'true' ? (
              <StyledButtonGroup color="secondary" aria-label="outlined secondary button group">
                <Button onClick={() => (window.confirm('Are you sure you want to archive this announcement') ? archiveOrDeleteAdmin('hide', data.id) : null)}>
                  Archive
                </Button>
                <Button onClick={() => (window.confirm('Are you sure you want to delete this announcement') ? archiveOrDeleteAdmin('delete', data.id) : null)}>
                  Delete
                </Button>
              </StyledButtonGroup>
            ) : null}

            <Wrapper>
              {data.image ? <Img background={btoa(String.fromCharCode.apply(null, new Uint8Array(data.image)))} /> : <Img />}
              <TitleWrapper>
                <StyledName>{data.name}</StyledName>
                <StyledPrice>{data.price} $</StyledPrice>
                <StyledLocation>location: {data.location}</StyledLocation>
                <StyledLocation>phone number: {data.phone_number}</StyledLocation>
              </TitleWrapper>
            </Wrapper>
            <StyledDescriptionTitle>Description:</StyledDescriptionTitle>
            <StyledDescription>{data.description}</StyledDescription>
            <br />
          </CardWrapper>
        </Card>
      ) : null}
      {data ? <MessageBox data={data} /> : null}
    </RootTemplate>
  );
};

export default AnnouncementShowPage;

const StyledCircularProgress = styled(CircularProgress)`
  margin: 0 auto;
`;

const CardWrapper = styled.div`
  margin: 15px;
  display: flex;
  flex-direction: column;
`;

const StyledButtonGroup = styled(ButtonGroup)`
  margin: 10px 0 0 auto;
`;

const TitleWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin: 10px 15px;
`;

const StyledName = styled.div`
  font-size: 25px;
  font-weight: bold;
`;

const StyledDescriptionTitle = styled.div`
  margin: 10px 20px;
  font-size: 20px;
  font-weight: bold;
`;

const StyledDescription = styled.div`
  margin: 5px 20px;
  font-size: 15px;
  word-wrap: break-word;
`;

const Card = styled.div`
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 5px 10px -5px rgba(0, 0, 0, 0.4);
  margin: 10px auto;
  width: 60%;
  display: flex;
  flex-direction: column;

  @media (max-width: 1100px) {
    width: 80%;
  }
`;

const Wrapper = styled.div`
  margin: 10px 20px;
  display: flex;
  flex-direction: row;
`;

const Img = styled.div`
  list-style: none;
  width: 350px;
  height: 350px;
  background-color: #e3e3e3;
  background-image: url(${({ background }) => (background ? `data:image/png;base64,${background}` : noImage)});
  background-repeat: no-repeat;
  background-size: 350px 350px;
  border-radius: 4px;
  margin: 15px 0;
`;

const StyledLocation = styled.div`
  font-size: 15px;
  font-weight: bold;
  margin: 5px 0;
`;

const StyledPrice = styled.div`
  font-size: 20px;
  font-weight: bold;
  margin: 10px 0 0;
`;
