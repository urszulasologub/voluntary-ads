import React, { useContext } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { useHistory } from 'react-router-dom';
import * as yup from 'yup';
import AuthTemplate from 'templates/AuthTemplate';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import { REMOTE_HOST } from 'config';
import { Context } from 'components/data/Store';
import Button from '@material-ui/core/Button';

const CreateAdminPage = () => {
  const classes = useStyles();
  const [state] = useContext(Context);
  const history = useHistory();

  let loginSchema = yup.object().shape({
    email: yup
      .string()
      .email()
      .required(),
    password: yup
      .string()
      .min(5)
      .required(),
    confirmPassword: yup
      .string()
      .label('Confirm password')
      .test('passwords-match', 'Passwords must match', function(value) {
        return this.parent.password === value;
      }),
  });

  const { register, handleSubmit, errors, setError } = useForm({
    validationSchema: loginSchema,
  });

  const onSubmit = ({ email, password }) => {
    const options = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/create_admin`,
      data: { email: email, password: password },
    };

    axios(options)
      .then(d => {
        history.push('/');
      })
      .catch(() => {
        setError('email', 'email', 'Incorrect e-mail or password');
        setError('password', 'password', 'Incorrect e-mail or password');
      });
  };

  return (
    <AuthTemplate>
      <h3>Create Admin</h3>
      <StyledForm className={classes.root} onSubmit={handleSubmit(onSubmit)}>
        <TextField
          name="email"
          inputRef={register}
          label="email"
          fullWidth
          error={errors.email ? true : false}
          helperText={errors.email ? errors.email.message : ''}
        />
        <TextField
          name="password"
          inputRef={register}
          label="password"
          type="password"
          fullWidth
          error={errors.password ? true : false}
          helperText={errors.password ? errors.password.message : ''}
        />
        <TextField
          name="confirmPassword"
          inputRef={register}
          label="confirm password"
          type="password"
          fullWidth
          error={errors.confirmPassword ? true : false}
          helperText={errors.confirmPassword ? errors.confirmPassword.message : ''}
        />
        <StyledButton variant="contained" type="submit">
          Create Admin
        </StyledButton>
      </StyledForm>
    </AuthTemplate>
  );
};

export default CreateAdminPage;

const useStyles = makeStyles(theme => ({
  root: {
    '& > *': {
      margin: theme.spacing(1),
    },
  },
}));

const StyledForm = styled.form`
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 80%;
  margin: 0 0 30px;
`;

const StyledButton = styled(Button)`
  width: 300px;
`;
