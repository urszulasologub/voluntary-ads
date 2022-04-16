import React, { useContext } from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import AboutPage from 'Pages/AboutPage';
import HomePage from 'Pages/HomePage';
import LoginPage from 'Pages/LoginPage';
import CreateAdminPage from 'Pages/CreateAdminPage';
import RegisterPage from 'Pages/RegisterPage';
import AnnouncementCreatePage from 'Pages/AnnouncementCreatePage';
import AnnouncementShowPage from 'Pages/AnnouncementShowPage';
import AdminPage from 'Pages/AdminPage';
import { Context } from 'Components/data/Store';

const Routes = () => {
  const [state] = useContext(Context);
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/" component={AboutPage} />
        <Route exact path="/announcements" component={HomePage} />
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
