import React from 'react';
import styled from 'styled-components/macro';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import EventsPage from './eventsListPage/EventsPage';
import LoginPage from './loginPage/LoginPage';
import LoginContextProvider from './contexts/LoginContextProvider';
import ProtectedRoute from './routing/ProtectedRoute';
import AddExpenditureForm from './addExpenditurePage/AddExpenditureForm';
import SignUpForm from './signUpPage/SignUpForm';
import CompensationPage from './compensationPage/CompensationPage';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import TabPanel from './eventPage/TabPanel';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#5a189a',
    },
    secondary: {
      main: '#ff8500',
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
              <Route path={'/login'} component={LoginPage} />
              <ProtectedRoute path={'/new'} component={AddEventForm} />
              <ProtectedRoute path={'/events'} component={EventsPage} />
              <ProtectedRoute
                exact
                path={'/event/compensation/:eventId'}
                component={CompensationPage}
              />
              <ProtectedRoute
                exact
                path={'/event/expenditures/:eventId'}
                component={AddExpenditureForm}
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
  height: 100vh;
  background-color: whitesmoke;
`;
