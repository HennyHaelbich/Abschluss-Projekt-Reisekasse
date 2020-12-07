import React, { useState } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Overview from './Overview';
import ExpenditureList from './ExpenditureList';
import useEvent from '../hooks/useEvent';
import Header from '../commons/Header';

export default function TabPanel(props) {
  const { event } = useEvent();
  const { match, history } = props;
  const { params } = match;
  const { page } = params;

  console.log(props);
  const tabNameToIndex = {
    0: 'overview',
    1: 'expenditures',
  };

  const indexToTabName = {
    overview: 0,
    expenditures: 1,
  };

  const [selectedTab, setSelectedTab] = useState(indexToTabName[page]);

  const handleChange = (event, newValue) => {
    history.push(`${tabNameToIndex[newValue]}`);
    setSelectedTab(newValue);
  };

  return (
    <div>
      <Header title={event?.title} backbutton />
      <Tabs value={selectedTab} onChange={handleChange} centered={true}>
        <Tab label="Übersicht" />
        <Tab label="Ausgaben" />
      </Tabs>
      {selectedTab === 0 && <Overview />}
      {selectedTab === 1 && <ExpenditureList />}
    </div>
  );
}
