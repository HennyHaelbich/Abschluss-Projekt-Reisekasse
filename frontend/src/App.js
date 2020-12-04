import React from 'react';
import { Switch, Route } from 'react-router-dom';
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
import CompensationPage from "./compensationPage/CompensationPage";

function App() {
  return (
    <LoginContextProvider>
      <EventContextProvider>
        <Switch>
          <Route path={'/signup'} component={SignUpForm} />
          <Route path={'/login'} component={LoginPage} />
          <ProtectedRoute path={'/new'} component={AddEventForm} />
          <ProtectedRoute path={'/events'} component={EventsPage} />
          <ProtectedRoute exact path={'/event/compensation/:eventId'} component={CompensationPage} />
          <ProtectedRoute exact path={'/event/new-expand/:eventId'} component={AddExpenditureForm} />
          <ProtectedRoute exact path={'/event/:eventId/:expenditureId'} component={ExpenditurePage} />
          <ProtectedRoute exact path={'/event/:eventId'} component={ShowEvent} />
        </Switch>
      </EventContextProvider>
    </LoginContextProvider>
  );
}

export default App;
