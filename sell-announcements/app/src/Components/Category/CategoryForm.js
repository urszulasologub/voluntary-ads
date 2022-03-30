import React, { useContext } from 'react';
import axios from 'axios';
import { useForm } from 'react-hook-form';
import styled from 'styled-components';
import * as yup from 'yup';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { REMOTE_HOST } from 'config';
import { Context } from 'Components/data/Store';
import Alert from '@material-ui/lab/Alert';

const CategoryForm = ({ categoryList, setCategoryList }) => {
  const classes = useStyles();
  const [state] = useContext(Context);

  let announcementSchema = yup.object().shape({
    name: yup
      .string()
      .max(50)
      .required()
      .label('Category Name'),
  });

  const { register, handleSubmit, errors, setError } = useForm({
    validationSchema: announcementSchema,
  });

  const onSubmit = ({ name }) => {
    const options = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/admin_category/create`,
      data: { name },
    };

    axios(options)
      .then(el => {
        setCategoryList(oldArray => [...oldArray, el.data]);
      })
      .catch(() => {
        setError('api', 'api', 'Something went wrong, try again later!');
      });
  };

  return (
    <StyledForm className={classes.root} onSubmit={handleSubmit(onSubmit)}>
      {errors.api ? <Alert severity="error">{errors.api.message}</Alert> : null}
      <TextField
        name="name"
        inputRef={register}
        label="Category Name"
        type="text"
        fullWidth
        error={errors.name ? true : false}
        helperText={errors.name ? errors.name.message : ''}
      />
      <Button variant="contained" type="submit">
        Create Category
      </Button>
    </StyledForm>
  );
};

export default CategoryForm;

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
  flex-direction: row;
  margin: auto;
`;
