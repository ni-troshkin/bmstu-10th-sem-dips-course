import { Box, Text, VStack } from "@chakra-ui/react";
import React, { useState } from "react";

import { Reservation as ReservationI } from "types/Reservation";

import styles from "./ReservationCard.module.scss";

import ReturnBook from "postAPI/reservations/ReturnBook";
import { statusItems } from "./items";
import { ReturnBookRequest } from "types/ReturnBookRequest";
import ConditionInput from "components/ConditionInput";


interface ReservationProps extends ReservationI {}

const ReservationCard: React.FC<ReservationProps> = (props) => {
  const [status, setStatus] = useState(statusItems[props.status]);

  async function return_book(condition: string) {
    let req: ReturnBookRequest = {
      condition: condition,
      date: new Date().toISOString().split(".")[0].replace("T", " ").substring(0, 10)
      // (
      //   "en-GB", 
      //   {
      //     year: "numeric", 
      //     month: "2-digit", 
      //     day: "2-digit"
      //   }
      // ).replaceAll("/", "-",)
    }
    await ReturnBook(props.reservationUid, req);
    window.location.reload();
  }

  return (
    <Box className={styles.main_box}>

    <Box className={styles.info_box}>
        <VStack>
        <Box className={styles.title_box}>
            <Text>{props.book.name}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Автор: {props.book.author}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Библиотека: {props.library.name}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Дата бронирования: {props.startDate}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Сдать до: {props.tillDate}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Статус бронирования: {status}</Text>
        </Box>
        <Box className={styles.description_box}>
            <Text>Адрес библиотеки: {props.library.address}</Text>
        </Box>
        </VStack>
        { props.status == 'RENTED' &&
        <ConditionInput putCallback={(condition: string) => return_book(condition)}/>
        }
    </Box>
    </Box>
  );
};

export default ReservationCard;
