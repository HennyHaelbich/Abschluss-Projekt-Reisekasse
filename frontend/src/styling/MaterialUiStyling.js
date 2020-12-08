import { makeStyles } from '@material-ui/core/styles';

export const useTextFieldStyle = makeStyles({
  root: {
    '& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline': {
      borderColor: '#777777',
    },
    '& .MuiInputLabel-outlined.Mui-focused': {
      color: '#555555',
    },
  },
});
