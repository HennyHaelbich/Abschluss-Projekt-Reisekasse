export const displayName = (component) => {
  return `${component.firstName} ${component.lastName.substring(0, 1)}.`;
};

export const formattedAmount = (component) => {
  return `${(component / 100).toFixed(2)} €`;
};

export const formattedDate = (timestamp) => {
  return `${timestamp.substring(8, 10)}.${timestamp.substring(
    5,
    7
  )}.${timestamp.substring(0, 4)}`;
};
