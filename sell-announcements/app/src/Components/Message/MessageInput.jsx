import React, { useContext, useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import TextField from '@material-ui/core/TextField';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';
import Button from '@material-ui/core/Button';

const MessageInput = ({ addId, owner, id, setMessages }) => {
  const { register, handleSubmit } = useForm();
  const [state] = useContext(Context);
  const [message, setMessage] = useState('');

  const onSubmit = ({ message }) => {
    const options = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: owner ? `${REMOTE_HOST}/announcements/${addId}/messages/reply/${id}` : `${REMOTE_HOST}/announcements/${addId}/messages/send`,
      data: { message: message },
    };

    axios(options).then(el => {
      setMessages(oldArray => [...oldArray, el.data]);
      setMessage('');
    });
  };

  return (
    <StyledForm onSubmit={handleSubmit(onSubmit)}>
      <TextField
        name="message"
        inputRef={register}
        label="Message"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        fullWidth
        multiline
      />
      <StyledButton variant="contained" type="submit" disabled={message.length === 0}>
        Send
      </StyledButton>
    </StyledForm>
  );
};

export default MessageInput;

const StyledForm = styled.form`
  display: flex;
  align-items: center;
  flex-direction: row;
  margin: 30px;
`;

const StyledButton = styled(Button)`
  width: 80px;
`;
