import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import ShowEvent from './eventPage/ShowEvent';
import EventsPage from './eventsListPage/EventsPage';
import LoginPage from "./loginPage/LoginPage";
import LoginContextProvider from "./contexts/LoginContextProvider";
import ProtectedRoute from "./routing/ProtectedRoute";
import AddExpenditureForm from './addExpenditurePage/AddExpenditureForm';
import SignUpForm from "./signUpPage/SignUpForm";
import ExpenditurePage from "./expenditurePage/ExpenditurePage";

function App() {
  return (
    <LoginContextProvider>
      <EventContextProvider>
        <Switch>
          <Route path={'/signup'} component={SignUpForm} />
          <Route path={'/login'} component={LoginPage} />
          <ProtectedRoute path={'/new'} component={AddEventForm} />
          <ProtectedRoute path={'/events'} component={EventsPage} />
          <ProtectedRoute exact path={'/event/new-expand/:id'} component={AddExpenditureForm} />
          <ProtectedRoute exact path={'/event/:id'} component={ShowEvent} />
          <ProtectedRoute exact path={'/event/:id/:expenditureId'} component={ExpenditurePage} />
          <Route path="/">
            <Redirect to="/events" />
          </Route>
        </Switch>
      </EventContextProvider>
    </LoginContextProvider>
  );
}

export default App;
