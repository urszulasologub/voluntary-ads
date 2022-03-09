import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import Message from 'components/Message/Message';
import { REMOTE_HOST } from 'config';
import { Context } from 'components/data/Store';

const MessageBox = ({ data }) => {
  const [state] = useContext(Context);
  const [ids, setIds] = useState(null);

  useEffect(() => {
    if (data.user_id.id === Number(state.id)) {
      const options = {
        method: 'GET',
        headers: {
          Authorization: 'Bearer ' + state.token,
          'Content-Type': 'application/json',
        },
        url: `${REMOTE_HOST}/announcements/${data.id}/messages`,
      };

      axios(options).then(e => {
        let allIds = e.data.map(item => item.id);
        setIds(allIds.filter((item, i, ar) => ar.indexOf(item) === i));
      });
    }
  }, [data.id, data.user_id.id, state.id, state.token]);

  return (
    <>
      {Number(state.id) ? (
        <>
          {data.user_id.id === Number(state.id) ? (
            <>
              {ids && ids.length > 0 ? (
                ids.map(el => <Message id={el} addId={data.id} owner={true} key={el.id + Math.random()} />)
              ) : (
                <Card>
                  <Heading>You don't have any messages.</Heading>
                </Card>
              )}
            </>
          ) : (
            <Message id={Number(state.id)} addId={data.id} owner={false} />
          )}
        </>
      ) : null}
    </>
  );
};

export default MessageBox;

const Heading = styled.h5`
  margin: 20px auto;
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
