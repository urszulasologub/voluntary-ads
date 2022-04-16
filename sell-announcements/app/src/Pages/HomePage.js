import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import RootTemplate from 'templates/RootTemplate';
import AnnouncementItem from 'Components/Announcement/AnnouncementItem';
import { REMOTE_HOST } from 'config';
import CircularProgress from '@material-ui/core/CircularProgress';
import Alert from '@material-ui/lab/Alert';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';

const HomePage = () => {
  const [data, setData] = useState(null);
  const [filterData, setFilterData] = useState(null);
  const [displayData, setDisplayData] = useState(null);
  const [loading, setLoading] = useState(null);
  const [error, setError] = useState(null);
  const [category, setCategory] = useState(null);
  const [selectCategory, setSelectCategory] = useState({ value: -10 });

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/categories`,
    };

    axios(options).then(e => {
      let d = e.data.map(el => ({ value: el.id, label: el.name }));
      d.push({ value: -10, label: 'SELECT Category' });
      setCategory(d);
    });
  }, []);

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/announcements`,
    };

    setLoading(true);

    axios(options)
      .then(e => {
        setData(e.data);
        setFilterData(e.data);
        setDisplayData(e.data);
      })
      .catch(e => {
        setError('Failed to load data');
      })
      .then(() => {
        setLoading(false);
      });
  }, []);

  const filter = value => {
    const regex = new RegExp(`${value.replace(/\\/g, '')}`, 'g');
    setFilterData(data.filter(({ name }) => name.toLowerCase().match(regex)));
    setDisplayData(data.filter(({ name }) => name.toLowerCase().match(regex)));
  };

  const handleChange = event => {
    setSelectCategory({ value: event.target.value });
    if (event.target.value !== -10) {
      setDisplayData(filterData.filter(el => el.category_id.id === event.target.value));
    } else {
      setDisplayData(filterData);
    }
  };

  return (
    <RootTemplate>
      <Wrapper>
        {error ? <Alert severity="error">{error}</Alert> : null}
        {loading ? <StyledCircularProgress /> : null}
        <h4>Announcement:</h4>
        <TextFieldWrapper>
          <TextField
            name="search"
            label="Search"
            type="text"
            fullWidth
            onChange={e => {
              filter(e.target.value);
            }}
          />
          {category && selectCategory.value ? (
            <TextField id="standard-select-category" select value={selectCategory.value} onChange={handleChange} fullWidth>
              {category.map(option => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </TextField>
          ) : null}
        </TextFieldWrapper>
        {displayData ? displayData.map(el => <AnnouncementItem data={el} key={el.id} />) : null}
      </Wrapper>
    </RootTemplate>
  );
};

export default HomePage;

const StyledCircularProgress = styled(CircularProgress)`
  margin: 0 auto;
`;

const TextFieldWrapper = styled.div`
  margin: 10px auto;
  width: 50%;
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: 30px auto;
  width: 90%;
`;
