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
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-excursion)' }}
        >
          <NaturePeopleIcon />
        </Avatar>
      );
    case 'entry':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-entry)' }}
        >
          <ConfirmationNumberIcon />
        </Avatar>
      );
    case 'transport':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-transport)' }}
        >
          <CommuteIcon />
        </Avatar>
      );
    case 'party':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-party)' }}
        >
          <LocalBarIcon />
        </Avatar>
      );
    case 'restaurant':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-restaurant)' }}
        >
          <RestaurantIcon />
        </Avatar>
      );
    case 'supermarkt':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-supermarkt)' }}
        >
          <ShoppingCartIcon />
        </Avatar>
      );
    case 'sleeping':
      return (
        <Avatar
          aria-label={type}
          style={{ backgroundColor: 'var(--color-charts-sleeping)' }}
        >
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
