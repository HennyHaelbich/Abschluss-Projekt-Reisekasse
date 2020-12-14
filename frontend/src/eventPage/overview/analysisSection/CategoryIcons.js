import NaturePeopleIcon from '@material-ui/icons/NaturePeople';
import ConfirmationNumberIcon from '@material-ui/icons/ConfirmationNumber';
import CommuteIcon from '@material-ui/icons/Commute';
import LocalBarIcon from '@material-ui/icons/LocalBar';
import RestaurantIcon from '@material-ui/icons/Restaurant';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import LocalHotelIcon from '@material-ui/icons/LocalHotel';
import EuroIcon from '@material-ui/icons/Euro';
import React from 'react';

export default function CategoryIcons({ type }) {
  switch (type) {
    case 'excursion':
      return <NaturePeopleIcon />;
    case 'entry':
      return <ConfirmationNumberIcon />;
    case 'transport':
      return <CommuteIcon />;
    case 'party':
      return <LocalBarIcon />;
    case 'restaurant':
      return <RestaurantIcon />;
    case 'supermarkt':
      return <ShoppingCartIcon />;
    case 'sleeping':
      return <LocalHotelIcon />;
    default:
      return <EuroIcon />;
  }
}
