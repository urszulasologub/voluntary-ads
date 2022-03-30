import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';
import styled from 'styled-components';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import Alert from '@material-ui/lab/Alert';
import CircularProgress from '@material-ui/core/CircularProgress';

const StyledWrapper = styled.div`
  margin: 10px auto;
  width: 60%;
`;

const Chart = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(null);
  const [error, setError] = useState(null);
  const [state] = useContext(Context);

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin/statistics`,
    };

    setLoading(true);

    axios(options)
      .then(e => {
        setData(
          Object.entries(e.data)
            .map(item => ({ day: item[0], value: item[1] }))
            .sort((a, b) => (a.day > b.day ? 1 : -1)),
        );
      })
      .catch(() => {
        setError('something went wrong');
      })
      .then(() => {
        setLoading(false);
      });
  }, [state.token]);

  return (
    <StyledWrapper>
      <h4>Count announcement last week: </h4>
      {error ? <Alert severity="error">{error}</Alert> : null}
      {loading ? <StyledCircularProgress /> : null}
      <ResponsiveContainer width="100%" height={400}>
        <BarChart
          data={data}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="day" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="value" fill="#fcd734" />
        </BarChart>
      </ResponsiveContainer>
    </StyledWrapper>
  );
};

export default Chart;

const StyledCircularProgress = styled(CircularProgress)`
  margin: 0 auto;
`;
