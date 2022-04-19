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
import Succes from 'Components/atoms/Succes';
import { REMOTE_HOST } from 'config';

const RegisterPage = () => {
  const classes = useStyles();
  const [succes, setSucces] = useState(null);

  const passwordMinLength = 5;
  let registerSchema = yup.object().shape({
    email: yup
      .string()
      .email('E-mail format is not valid')
      .required('E-mail is required'),
    password: yup
      .string()
      .min(passwordMinLength, `Password must be at least ${passwordMinLength} characters long`)
      .required('Password is required'),
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
        setSucces('An account has been created');
      })
      .catch(() => {
        setError('email', 'email', 'This e-mail is already taken');
      });
  };

  return (
    <AuthTemplate>
      <h1>Register</h1>
      {
        succes ?
          <>
            <Succes>{succes}</Succes>
            <StyledForm>
              <StyledMaterialLink component={Link} to="/login">
                Login
              </StyledMaterialLink>
            </StyledForm>
          </>
        :
          <StyledForm className={classes.root} onSubmit={handleSubmit(onSubmit)}>
            <TextField
              name="email"
              inputRef={register}
              label="E-mail"
              fullWidth
              error={!!errors.email}
              helperText={errors.email ? errors.email.message : ''}
            />
            <TextField
              name="password"
              inputRef={register}
              label="Password"
              type="password"
              fullWidth
              error={!!errors.password}
              helperText={errors.password ? errors.password.message : ''}
            />
            <TextField
              name="confirmPassword"
              inputRef={register}
              label="Confirm Password"
              type="password"
              fullWidth
              error={!!errors.confirmPassword}
              helperText={errors.confirmPassword ? errors.confirmPassword.message : ''}
            />
            <StyledButton variant="contained" type="submit">
              SIGN UP
            </StyledButton>
            <StyledMaterialLink component={Link} to="/login">
              Login
            </StyledMaterialLink>
          </StyledForm>
      }
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
  margin-top: 15px;
  width: 300px;
`;

const StyledMaterialLink = styled(MaterialLink)`
  font-size: 1rem;
`;
