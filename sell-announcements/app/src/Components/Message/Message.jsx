import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import MessageInput from 'Components/Message/MessageInput';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';

const Message = ({ id, addId, owner }) => {
  const [state] = useContext(Context);
  const [messages, setMessages] = useState(null);

  useEffect(() => {
    const options = {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/announcements/${addId}/messages/${id}`,
    };

    axios(options).then(e => {
      setMessages(e.data);
    });
  }, [addId, id, state.token]);

  return (
    <Card>
      <Wrapper>
        {messages && messages.length > 0
          ? messages.map(item => (
              <MessageWrapper messageOvner={(item.seller && owner) || (!item.seller && !owner) ? true : false} key={item.id + Math.random()}>
                <MessageWrapperText>{item.message}</MessageWrapperText>
              </MessageWrapper>
            ))
          : null}
      </Wrapper>
      <MessageInput addId={addId} id={id} owner={owner} setMessages={setMessages} />
    </Card>
  );
};

export default Message;

const Wrapper = styled.div`
  margin: 10px;
`;

const MessageWrapper = styled.div`
  margin: ${({ messageOvner }) => (messageOvner ? '5px 5px 0 auto;' : '5px 5px 0')};
  max-width: 80%;
  background-color: ${({ messageOvner }) => (messageOvner ? '#b6e0d4' : '#edebeb')};
  border-radius: 10px;
  box-shadow: 0 5px 10px -5px rgba(0, 0, 0, 0.2);
`;

const MessageWrapperText = styled.div`
  margin: 10px 15px;
  word-wrap: break-word;
  font-size: 18px;
  max-width: fit content;
  padding: 5px;
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
