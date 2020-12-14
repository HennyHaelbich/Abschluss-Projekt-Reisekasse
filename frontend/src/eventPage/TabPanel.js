import React, { useState } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Overview from './overview/Overview';
import ExpenditureList from './expenditures/ExpenditureList';
import useEvent from '../hooks/useEvent';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import AddButton from '../commons/styling/AddButton';

export default function TabPanel(props) {
  const { event } = useEvent();
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

  const handleAddExpenditure = () => {
    history.push(`/event/expenditures/${event.id}`);
  };

  const handleBackClick = () => {
    history.push('/events');
  };

  const handleChange = (event, newValue) => {
    history.push(`${tabNameToIndex[newValue]}`);
    setSelectedTab(newValue);
  };

  return (
    <div>
      <Header title={event?.title} handleBackClick={handleBackClick} />
      <TabsFixed>
        <Tabs value={selectedTab} onChange={handleChange} centered={true}>
          <Tab label="Übersicht" />
          <Tab label="Ausgaben" />
        </Tabs>
      </TabsFixed>
      <TabsSpacer>
        {selectedTab === 0 && <Overview />}
        {selectedTab === 1 && <ExpenditureList />}
      </TabsSpacer>
      <AddButton handle={handleAddExpenditure} />
    </div>
  );
}

const TabsFixed = styled.div`
  position: fixed;
  width: 100%;
  background-color: whitesmoke;
  z-index: 10;
  box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.3);
`;

const TabsSpacer = styled.div`
  margin-top: 48px;
`;
