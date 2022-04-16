import Cookie from 'js-cookie';

const Reducer = (state, action) => {
  switch (action.type) {
    case 'LOGIN':
      Cookie.set('token', action.payload.session);
      Cookie.set('admin', action.payload.is_admin === 'true' ? 'true' : '');
      Cookie.set('userId', action.payload.id);
      return {
        ...state,
        token: action.payload.session,
        admin: action.payload.is_admin === 'true' ? 'true' : '',
        id: action.payload.id,
      };
    case 'LOGOUT':
      Cookie.set('token', '');
      Cookie.set('admin', '');
      Cookie.set('userId', '');
      return {
        ...state,
        token: null,
        admin: null,
        id: null,
      };
    default:
      return state;
  }
};

export default Reducer;
