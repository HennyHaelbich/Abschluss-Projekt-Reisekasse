import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';
import ShowEvents from './EventsOverviewPage/ShowEvents';

function App() {
  return (
    <EventContextProvider>
      <Switch>
        <Route path={'/new'}>
          <AddEventForm />
        </Route>
        <Route path={'/events'}>
          <ShowEvents />
        </Route>
        <Route path="/">
          <Redirect to="/new" />
        </Route>
      </Switch>
    </EventContextProvider>
  );
}

export default App;
