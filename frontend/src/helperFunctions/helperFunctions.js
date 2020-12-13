export const displayName = (component) => {
  return `${component.firstName} ${component.lastName.substring(0, 1)}.`;
};

export const formattedAmount = (component) => {
  return `${(component / 100).toFixed(2)} €`;
};

export const formattedDate = (date) => {
  return `${date.substring(8, 10)}.${date.substring(5, 7)}.${date.substring(
    0,
    4
  )}`;
};
