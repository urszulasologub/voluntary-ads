import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { REMOTE_HOST } from 'config';
import styled from 'styled-components';
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import Alert from '@material-ui/lab/Alert';
import { Context } from 'Components/data/Store';
import DeleteIcon from '@material-ui/icons/Delete';
import CategoryForm from 'Components/Category/CategoryForm';

const PinnedSubheaderList = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(null);
  const [error, setError] = useState(null);
  const [state] = useContext(Context);

  const handleDelete = id => {
    const options = {
      method: 'DELETE',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin_category/delete/${id}`,
    };

    axios(options)
      .then((e) => {
        setData(data.filter((el) => el.id !== id));
      })
      .catch((e) => {
        window.alert('Cannot delete this category as it contains announcements or it is the only category.');
      });
  };

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin_category`,
    };

    setLoading(true);

    axios(options)
      .then(e => {
        setData(e.data);
      })
      .catch(() => {
        setError('Category does not exist');
      })
      .then(() => {
        setLoading(false);
      });
  }, [state.token]);

  return (
    <Wrapper>
      <h4>Manage Categories</h4>
      {error ? <Alert severity="error">{error}</Alert> : null}
      {loading ? <StyledCircularProgress /> : null}
      <CategoryForm categoryList={data} setCategoryList={setData} />
      {data ? (
        <ListWrapper>
          {data.map(el => (
            <Item key={el.id}>
              <TitleItem>{el.name}</TitleItem>
              <IconButton aria-label="delete" onClick={() => (window.confirm('Are you sure you want to delete this category') ? handleDelete(el.id) : null)}>
                <DeleteIcon />
              </IconButton>
            </Item>
          ))}
        </ListWrapper>
      ) : null}
    </Wrapper>
  );
};

export default PinnedSubheaderList;

const StyledCircularProgress = styled(CircularProgress)`
  margin: 0 auto;
`;

const Wrapper = styled.div`
  width: 60%;
  margin: 0 auto;
  padding-top: 10px;
  padding-bottom: 10px;
`;

const ListWrapper = styled.div`
  margin: 5px auto;
  border: 1px solid #e0e0e0;
  border-radius: 2px;
  overflow: hidden;
  position: relative;
`;

const Item = styled.div`
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  flex-direction: row;
  :nth-child(odd) {
    background-color: #e1eded;
  }
`;

const TitleItem = styled.div`
  margin: 10px;
  font-size: 20px;
  width: 90%;
`;
