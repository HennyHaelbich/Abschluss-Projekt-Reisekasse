import React from 'react';
import { Switch, Route } from 'react-router-dom';
import AddEventForm from './addEventPage/AddEventForm';
import EventContextProvider from './contexts/EventContextProvider';

function App() {
  return (
    <EventContextProvider>
      <Switch>
        <Route path={['/', '/new']}>
          <AddEventForm />
        </Route>
      </Switch>
    </EventContextProvider>
  );
}

export default App;
