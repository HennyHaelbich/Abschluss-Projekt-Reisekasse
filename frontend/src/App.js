import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import ShowEvent from './eventPage/ShowEvent';
import EventsPage from './eventsListPage/EventsPage';
import AddExpenditureForm from './addExpenditurePage/AddExpenditureForm';

function App() {
  return (
    <EventContextProvider>
      <Switch>
        <Route path={'/new'} component={AddEventForm} />
        <Route path={'/events'} component={EventsPage} />
        <Route exact path={'/event/:id'} component={ShowEvent} />
        <Route path={'/event/new-expand/:id'} component={AddExpenditureForm} />
        <Route path="/">
          <Redirect to="/new" />
        </Route>
      </Switch>
    </EventContextProvider>
  );
}

export default App;
