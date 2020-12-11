import React, { useState } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Overview from './Overview';
import ExpenditureList from './ExpenditureList';
import useEvent from '../hooks/useEvent';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import CompensationPage from '../compensationPage/CompensationPage';

export default function TabPanel(props) {
  const { event } = useEvent();
  const { match, history } = props;
  const { params } = match;
  const { page } = params;

  const tabNameToIndex = {
    0: 'overview',
    1: 'expenditures',
    2: 'compensation',
  };

  const indexToTabName = {
    overview: 0,
    expenditures: 1,
    compensation: 2,
  };

  const [selectedTab, setSelectedTab] = useState(indexToTabName[page]);

  const handleChange = (event, newValue) => {
    history.push(`${tabNameToIndex[newValue]}`);
    setSelectedTab(newValue);
  };

  return (
    <div>
      <Header title={event?.title} backbutton />
      <TabsFixed>
        <Tabs value={selectedTab} onChange={handleChange} centered={true}>
          <Tab label="Übersicht" />
          <Tab label="Ausgaben" />
          <Tab label="Ausgleich" />
        </Tabs>
      </TabsFixed>
      <TabsSpacer>
        {selectedTab === 0 && <Overview />}
        {selectedTab === 1 && <ExpenditureList />}
        {selectedTab === 2 && <CompensationPage />}
      </TabsSpacer>
    </div>
  );
}

const TabsFixed = styled.div`
  position: fixed;
  width: 100%;
  background-color: whitesmoke;
  z-index: 10;
  box-shadow: 0px 2px 10px 0px rgba(0, 0, 0, 0.3);
`;

const TabsSpacer = styled.div`
  margin-top: 48px;
`;
