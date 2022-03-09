import React, { useContext, useState, useEffect } from 'react';
import axios from 'axios';
import { useForm } from 'react-hook-form';
import styled from 'styled-components';
import * as yup from 'yup';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { REMOTE_HOST } from 'config';
import { Context } from 'components/data/Store';
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
      .label('Announcement Name'),
    price: yup
      .number()
      .required()
      .label('Price'),
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

  const onSubmit = ({ name, price, description, phoneNumber, location, file }) => {
    const formData = new FormData();
    formData.append('file', file[0]);
    formData.append('name', name);
    formData.append('price', price);
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
        label="Announcement Name"
        type="text"
        fullWidth
        error={errors.name ? true : false}
        helperText={errors.name ? errors.name.message : ''}
      />
      <TextField name="file" inputRef={register} type="file" fullWidth error={errors.file ? true : false} helperText={errors.file ? errors.file.message : ''} />
      {category && selectCategory.value ? (
        <TextField
          id="standard-select-category"
          select
          label="Select Category"
          value={selectCategory.value}
          onChange={handleChange}
          fullWidth
          helperText="Please select category"
        >
          {category.map(option => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
      ) : null}
      <TextField
        name="price"
        inputRef={register}
        label="Price"
        type="number"
        fullWidth
        inputProps={{ min: '0', step: '0.01' }}
        error={errors.price ? true : false}
        helperText={errors.price ? errors.price.message : ''}
      />
      <TextField
        name="description"
        inputRef={register}
        label="Description"
        type="text"
        multiline
        fullWidth
        error={errors.description ? true : false}
        helperText={errors.description ? errors.description.message : ''}
      />
      <TextField
        name="phoneNumber"
        inputRef={register}
        label="Phone Number"
        type="text"
        fullWidth
        error={errors.phoneNumber ? true : false}
        helperText={errors.phoneNumber ? errors.phoneNumber.message : ''}
      />
      <TextField
        name="location"
        inputRef={register}
        label="Location"
        type="text"
        fullWidth
        error={errors.location ? true : false}
        helperText={errors.location ? errors.location.message : ''}
      />
      <Button variant="contained" type="submit">
        Create Announcement
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
  width: 80%;
  margin: auto;
`;
