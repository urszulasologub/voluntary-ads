import React, { useContext } from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import HomePage from 'pages/HomePage';
import LoginPage from 'pages/LoginPage';
import CreateAdminPage from 'pages/CreateAdminPage';
import RegisterPage from 'pages/RegisterPage';
import AnnouncementCreatePage from 'pages/AnnouncementCreatePage';
import AnnouncementShowPage from 'pages/AnnouncementShowPage';
import AdminPage from 'pages/AdminPage';
import { Context } from 'components/data/Store';

const Routes = () => {
  const [state] = useContext(Context);
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route exact path="/login" component={LoginPage} />
        <Route exact path="/register" component={RegisterPage} />
        <Route exact path="/announcements/:id" component={AnnouncementShowPage} />
        {state.token ? (
          <>
            <Route exact path="/announcement/create" component={AnnouncementCreatePage} />
            {state.admin ? (
              <>
                <Route exact path="/admin" component={AdminPage} />
                <Route exact path="/create_admin" component={CreateAdminPage} />
              </>
            ) : null}
          </>
        ) : null}
        <Redirect to="/" />
      </Switch>
    </BrowserRouter>
  );
};

export default Routes;
