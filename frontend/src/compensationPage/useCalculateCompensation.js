import axios from "axios";
import {useContext} from "react";
import LoginContext from "../contexts/LoginContext";

export default function useCalculateCompensation() {
  const {token} = useContext(LoginContext);
  const [compensationsPayments, setCompensationPayments] = useState([]);
  
  const header = (token) => ({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  
  const calculateCompensations = (members) =>
    axios
      .put('api/events/calcualte-compensation', {members}, header(token))
      .then((response) => response.data)
      .then((compensations) => setCompensationPayments(compensations))
  
  return { calculateCompensations, compensationsPayments }
}

