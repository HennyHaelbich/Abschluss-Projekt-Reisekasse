import React, { useState } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Overview from './Overview';
import Expenditures from './Expenditures';

export default function TabPanel({ props }) {
  const { match, history } = props;
  const { params } = match;
  const { page } = params;

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
    history.push(`home/${tabNameToIndex[newValue]}`);
    setSelectedTab(newValue);
  };

  return (
    <div>
      <Tabs value={selectedTab} onChange={handleChange} centered={true}>
        <Tab label="Übersicht" />
        <Tab label="Ausgaben" />
      </Tabs>
      {selectedTab === 0 && <Overview />}
      {selectedTab === 1 && <Expenditures />}
    </div>
  );
}
