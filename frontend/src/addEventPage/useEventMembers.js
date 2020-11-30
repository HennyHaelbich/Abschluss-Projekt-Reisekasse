import axios from "axios";
import {useContext, useState} from "react";
import LoginContext from "../contexts/LoginContext";

export default function useEventMembers() {
  const { token } = useContext(LoginContext);
  const [members, setMembers] = useState([]);

  const header = (token) => ({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  
  const addMember = (member) =>
    axios
      .get('/api/users/' + member, header(token))
      .then((response) => response.data)
    //  .then((data) => console.log("Daten aus dem Backend", data))
      .then((newMember) => setMembers([...members, newMember]));
  return { addMember, members }
}