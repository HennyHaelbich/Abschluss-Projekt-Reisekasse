/*
import {useEffect, useState} from 'react';
import axios from "axios";

export default function useUsers() {
  
  const [users, setUsers] = useState([])
  
  useEffect(() => {
    axios.get('/api/user')
      .then((response) => response.data)
      .then((users) => {
        setUsers(users)
      })
  }, []);
  
  console.log('Initial users', users)
  return users;
  
}*/
