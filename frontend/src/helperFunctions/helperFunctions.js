
export const displayName = (component) => {
  return `${component.firstName} ${component.lastName.substring(0,1)}.`;
}

export const formattedAmount = (component) => {
  return `${(component / 100).toFixed(2)} €` ;
}