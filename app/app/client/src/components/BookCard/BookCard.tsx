import { Box, Text, VStack } from "@chakra-ui/react";
import React from "react";

import { LibraryBook as BookI} from "types/LibraryBook";

import styles from "./BookCard.module.scss";
import TakeBook from "postAPI/reservations/TakeBook";
import DateInput from "components/DateInput";


interface BookProps extends BookI {
  libraryUid: number
}

const BookCard: React.FC<BookProps> = (props) => {

  const takeBook = async (date: Date) => {
    console.log(props.libraryUid);
    console.log(props.bookUid);
    await TakeBook(props.libraryUid, props.bookUid, 
      new Date(date).toISOString().split(".")[0].replace("T", " ").substring(0, 10));
    window.location.reload();
  }

  return (
    <Box className={styles.main_box}>
      <Box className={styles.info_box}>
        <VStack>
          <Box className={styles.title_box}>
            <Text>{props.name}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Автор: {props.author}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Жанр: {props.genre}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Состояние: {props.condition}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Доступно: {props.availableCount}</Text>
          </Box>
        </VStack>
        <DateInput putCallback={(date: Date) => takeBook(date)}/>
      </Box>
    </Box>
  );
};

export default BookCard;
