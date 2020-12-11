import React from 'react';
import RestaurantIcon from '@material-ui/icons/Restaurant';
import NaturePeopleIcon from '@material-ui/icons/NaturePeople';
import ConfirmationNumberIcon from '@material-ui/icons/ConfirmationNumber';
import CommuteIcon from '@material-ui/icons/Commute';
import LocalBarIcon from '@material-ui/icons/LocalBar';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import LocalHotelIcon from '@material-ui/icons/LocalHotel';
import EuroIcon from '@material-ui/icons/Euro';
import Avatar from '@material-ui/core/Avatar';

export default function ExpenditureCategoryIcon({ type }) {
  switch (type) {
    case 'excursion':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#A4BB67' }}>
          <NaturePeopleIcon />
        </Avatar>
      );
    case 'entry':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#5ea4c0' }}>
          <ConfirmationNumberIcon />
        </Avatar>
      );
    case 'transport':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#d19e6e' }}>
          <CommuteIcon />
        </Avatar>
      );
    case 'party':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#8d5ab1' }}>
          <LocalBarIcon />
        </Avatar>
      );
    case 'restaurant':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#c75553' }}>
          <RestaurantIcon />
        </Avatar>
      );
    case 'supermarkt':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#a4bb67' }}>
          <ShoppingCartIcon />
        </Avatar>
      );
    case 'sleeping':
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#4a8fb0' }}>
          <LocalHotelIcon />
        </Avatar>
      );
    default:
      return (
        <Avatar aria-label={type} style={{ backgroundColor: '#5e6368' }}>
          <EuroIcon />
        </Avatar>
      );
  }
}
