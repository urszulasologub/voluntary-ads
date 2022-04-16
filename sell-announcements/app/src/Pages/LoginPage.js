import React, { useContext } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { Link, useHistory } from 'react-router-dom';
import * as yup from 'yup';
import MaterialLink from '@material-ui/core/Link';
import AuthTemplate from 'templates/AuthTemplate';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';

const LoginPage = () => {
  const classes = useStyles();
  const [, dispatch] = useContext(Context);
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
  });

  const { register, handleSubmit, errors, setError } = useForm({
    validationSchema: loginSchema,
  });

  const onSubmit = ({ email, password }) => {
    const options = {
      method: 'POST',
      url: `${REMOTE_HOST}/login`,
      data: { email: email, password: password },
    };

    axios(options)
      .then(d => {
        dispatch({ type: 'LOGIN', payload: d.data });
        history.push('/');
      })
      .catch(() => {
        setError('email', 'email', 'Incorrect e-mail or password');
        setError('password', 'password', 'Incorrect e-mail or password');
      });
  };

  return (
    <AuthTemplate>
      <h1>sign in</h1>
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
        <StyledButton variant="contained" type="submit">
          sign in
        </StyledButton>
        <StyledMaterialLink component={Link} to="/register">
          Dou do not have an account?
        </StyledMaterialLink>
      </StyledForm>
    </AuthTemplate>
  );
};

export default LoginPage;

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

const StyledMaterialLink = styled(MaterialLink)`
  font-size: 1rem;
`;
