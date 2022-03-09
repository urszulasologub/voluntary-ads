import React, { useContext } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import styled from 'styled-components';
import Button from '@material-ui/core/Button';
import { REMOTE_HOST } from 'config';
import { Context } from 'components/data/Store';

const CreateAdminAndDb = () => {
  const [state] = useContext(Context);
  const history = useHistory();

  const OnSampleDatabase = () => {
    const options = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin/create_database`,
    };

    axios(options).then(e => {
      history.push('/');
    });
  };

  const onCreateAdmin = () => {
    history.push('create_admin');
  };

  return (
    <Wrapper>
      <Button variant="contained" onClick={() => onCreateAdmin()}>
        Create Admin
      </Button>
      <Button variant="contained" onClick={() => OnSampleDatabase()}>
        Sample database
      </Button>
    </Wrapper>
  );
};

export default CreateAdminAndDb;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: row;
  margin: 20px auto;
  width: 60%;
`;
