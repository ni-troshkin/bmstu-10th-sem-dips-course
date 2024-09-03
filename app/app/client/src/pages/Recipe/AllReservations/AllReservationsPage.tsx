import { Box } from "@chakra-ui/react";
import GetAllReservations from "postAPI/reservations/GetAllReservations";
import React from "react";

import styles from "./AllReservationsPage.module.scss";
import ReservationMap from "components/ReservationMap/ReservationMap";

interface AllReservations {}

const AllReservationsPage: React.FC<AllReservations> = () => {
  return (
    <Box className={styles.main_box}>
      <ReservationMap getCall={GetAllReservations} />
    </Box>
  );
};

export default AllReservationsPage;
