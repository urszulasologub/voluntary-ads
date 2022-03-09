import React, { useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import * as yup from 'yup';
import MaterialLink from '@material-ui/core/Link';
import AuthTemplate from 'templates/AuthTemplate';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Succes from 'components/atoms/Succes';
import { REMOTE_HOST } from 'config';

const RegisterPage = () => {
  const classes = useStyles();
  const [succes, setSucces] = useState(null);

  let registerSchema = yup.object().shape({
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
    validationSchema: registerSchema,
  });

  const onSubmit = ({ email, password }) => {
    const options = {
      method: 'POST',
      url: `${REMOTE_HOST}/register`,
      data: { email: email, password: password },
    };

    axios(options)
      .then(() => {
        setError(null);
        setSucces('Account has been created');
      })
      .catch(() => {
        setError('email', 'email', 'email is already exist');
      });
  };

  return (
    <AuthTemplate>
      <h1>sign up</h1>
      {succes ? <Succes>{succes}</Succes> : null}
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
          sign up
        </StyledButton>
        <StyledMaterialLink component={Link} to="/login">
          Dou do not have an account?
        </StyledMaterialLink>
      </StyledForm>
    </AuthTemplate>
  );
};

export default RegisterPage;

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
