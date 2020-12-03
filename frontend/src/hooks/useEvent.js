import React, { useContext } from "react";
import { useParams } from "react-router-dom";
import EventContext from "../contexts/EventContext";

export default function useEvent() {
  const { events } = useContext(EventContext);
  const { eventId } = useParams();
  const event = events.find((event) => event.id === eventId);
  
  return { event, eventId };
}