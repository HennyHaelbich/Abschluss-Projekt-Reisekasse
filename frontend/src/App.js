import React from 'react';
import styled from 'styled-components/macro';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './forms/addEventForm/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import EventsPage from './eventList/EventsPage';
import LoginForm from './forms/LoginForm';
import LoginContextProvider from './contexts/LoginContextProvider';
import ProtectedRoute from './commons/ProtectedRoute';
import AddExpenditureForm from './forms/AddExpenditureForm';
import SignUpForm from './forms/signUpForm/SignUpForm';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import TabPanel from './eventPage/TabPanel';
import CompensationPage from './eventPage/overview/compensationPage/CompensationPage';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#5f2790',
    },
    secondary: {
      main: '#ff7900',
    },
  },
});

function App() {
  return (
    <LoginContextProvider>
      <EventContextProvider>
        <MuiThemeProvider theme={theme}>
          <PageLayout>
            <Switch>
              <Route path={'/signup'} component={SignUpForm} />
              <Route path={'/login'} component={LoginForm} />
              <ProtectedRoute path={'/new'} component={AddEventForm} />
              <ProtectedRoute path={'/events'} component={EventsPage} />
              <ProtectedRoute
                exact
                path={'/event/expenditures/:eventId'}
                component={AddExpenditureForm}
              />
              <ProtectedRoute
                exact
                path={'/event/compensation/:eventId'}
                component={CompensationPage}
              />
              <Redirect
                exact
                from="/event/:eventId/"
                to="/event/:eventId/overview"
              />
              <ProtectedRoute
                exact
                path={'/event/:eventId/:page?'}
                render={(props) => <TabPanel {...props} />}
              />
              <Route path="/">
                <Redirect to="/events" />
              </Route>
            </Switch>
          </PageLayout>
        </MuiThemeProvider>
      </EventContextProvider>
    </LoginContextProvider>
  );
}

export default App;

const PageLayout = styled.div`
  display: grid;
  grid-template-rows: 48px 1fr;
  margin-top: 48px;
`;
