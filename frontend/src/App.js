import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import ShowEvent from './eventPage/ShowEvent';
import EventsPage from './eventsListPage/EventsPage';

function App() {
  return (
    <EventContextProvider>
      <Switch>
        <Route path={'/new'}>
          <AddEventForm />
        </Route>
        <Route path={'/events'}>
          <EventsPage />
        </Route>
        <Route path={'/event/:id'}>
          <ShowEvent />
        </Route>
        <Route path="/">
          <Redirect to="/new" />
        </Route>
      </Switch>
    </EventContextProvider>
  );
}

export default App;
