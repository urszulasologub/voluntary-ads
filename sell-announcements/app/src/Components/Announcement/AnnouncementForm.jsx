import React, { useContext, useState, useEffect } from 'react';
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
import { useHistory } from 'react-router-dom';
import MenuItem from '@material-ui/core/MenuItem';

const AnnouncementForm = () => {
  const classes = useStyles();
  const [state] = useContext(Context);
  const history = useHistory();
  const [category, setCategory] = useState(null);
  const [selectCategory, setSelectCategory] = useState({ value: null });

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
      setCategory(d);
      setSelectCategory({ value: d[0].value });
    });
  }, []);

  const handleChange = event => {
    setSelectCategory({ value: event.target.value });
  };

  let announcementSchema = yup.object().shape({
    name: yup
      .string()
      .max(50)
      .required()
      .label('Title'),
    quantity: yup
      .number()
      .required()
      .min(1)
      .label('Quantity'),
    description: yup
      .string()
      .max(5000)
      .required()
      .label('Description'),
    phoneNumber: yup
      .string()
      .matches(/^((\\+[1-9]{1,4}[ \\-]*)|(\\([0-9]{2,3}\\)[ \\-]*)|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?$/, 'Phone number is not valid'),
    location: yup
      .string()
      .max(200)
      .required()
      .label('Location'),
  });

  const { register, handleSubmit, errors, setError } = useForm({
    validationSchema: announcementSchema,
  });

  const onSubmit = ({ name, quantity, description, phoneNumber, location }) => {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('quantity', quantity);
    formData.append('description', description);
    formData.append('phone_number', phoneNumber);
    formData.append('location', location);
    formData.append('categoryId', selectCategory.value);

    const options = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + state.token,
        'Content-Type': 'application/json',
      },
      url: `${REMOTE_HOST}/announcements/add`,
      data: formData,
    };

    axios(options)
      .then(e => {
        history.push(`/announcements/${e.data.id}`);
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
        label="Title"
        type="text"
        fullWidth
        error={!!errors.name}
        helperText={errors.name ? errors.name.message : ''}
      />
      {category && selectCategory.value ? (
        <TextField
          id="standard-select-category"
          select
          label="Category"
          value={selectCategory.value}
          onChange={handleChange}
          fullWidth
        >
          {category.map(option => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
      ) : null}
      <TextField
        name="quantity"
        inputRef={register}
        label="Quantity"
        type="number"
        fullWidth
        inputProps={{ min: '1', step: '1' }}
        error={!!errors.quantity}
        helperText={errors.quantity ? errors.quantity.message : ''}
      />
      <TextField
        name="description"
        inputRef={register}
        label="Description"
        type="text"
        multiline
        rows={3}
        fullWidth
        error={!!errors.description}
        helperText={errors.description ? errors.description.message : ''}
      />
      <TextField
        name="phoneNumber"
        inputRef={register}
        label="Phone Number"
        type="text"
        fullWidth
        error={!!errors.phoneNumber}
        helperText={errors.phoneNumber ? errors.phoneNumber.message : ''}
      />
      <TextField
        name="location"
        inputRef={register}
        label="Location"
        type="text"
        fullWidth
        error={!!errors.location}
        helperText={errors.location ? errors.location.message : ''}
      />
      <Button variant="contained" type="submit">
        Create
      </Button>
    </StyledForm>
  );
};

export default AnnouncementForm;

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
  width: 60%;
  margin: auto;
  padding: 15px;
`;
