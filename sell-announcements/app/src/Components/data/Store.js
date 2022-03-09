import React, { createContext, useReducer } from 'react';
import Cookie from 'js-cookie';
import Reducer from './Reducer';

const initialState = {
  token: Cookie.get('token') ? Cookie.get('token') : '',
  admin: Cookie.get('admin') ? Cookie.get('admin') : '',
  id: Cookie.get('userId') ? Cookie.get('userId') : '',
};

const Store = ({ children }) => {
  const [state, dispatch] = useReducer(Reducer, initialState);
  return <Context.Provider value={[state, dispatch]}>{children}</Context.Provider>;
};

export const Context = createContext(initialState);
export default Store;
