import React from 'react';

import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Overview from './Overview';
import Expenditures from './Expenditures';

export default function TabPanel() {
  const [selectedTag, setSelectedTag] = React.useState(0);

  const handleChange = (event, newValue) => {
    setSelectedTag(newValue);
  };

  return (
    <div>
      <Tabs value={selectedTag} onChange={handleChange}>
        <Tab label="Übersicht" />
        <Tab label="Ausgaben" />
      </Tabs>
      {selectedTag === 0 && <Overview />}
      {selectedTag === 1 && <Expenditures />}
    </div>
  );
}
