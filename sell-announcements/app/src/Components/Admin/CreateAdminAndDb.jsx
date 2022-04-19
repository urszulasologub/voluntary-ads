import React, { useContext } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import styled from 'styled-components';
import Button from '@material-ui/core/Button';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';

const CreateAdminAndDb = () => {
  const [state, dispatch] = useContext(Context);
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
      dispatch({ type: 'LOGOUT' });
    });
  };

  const onCreateAdmin = () => {
    history.push('create_admin');
  };

  return (
    <Wrapper>
      <Button variant="contained" onClick={() => OnSampleDatabase()}>
        Seed Sample Database
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
  padding-bottom: 20px;
`;
