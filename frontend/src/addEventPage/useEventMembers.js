import axios from "axios";
import {useContext} from "react";
import LoginContext from "../contexts/LoginContext";

export default function useEventMembers() {
const { token } = useContext(LoginContext);

  const header = (token) => ({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  
  const addMember = (member) =>
    axios
      .get('/api/users/' + member, header(token))
      .then((response) => response.data);
  
  return [addMember]
}