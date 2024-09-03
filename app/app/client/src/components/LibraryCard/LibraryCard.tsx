import { Box, Link, Image, Text, VStack } from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";

import { Library as LibraryI} from "types/Library";

import styles from "./LibraryCard.module.scss";


interface LibraryProps extends LibraryI {}

const LibraryCard: React.FC<LibraryProps> = (props) => {
  const navigate = useNavigate();

  var path = "/libraries/" + props.libraryUid;

  return (
    <Link className={styles.link_div} href={path}>
    <Box className={styles.main_box}>
      <Box className={styles.info_box}>
        <VStack>
          <Box className={styles.title_box}>
            <Text>{props.name}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Город: {props.city}</Text>
          </Box>
          <Box className={styles.description_box}>
            <Text>Адрес: {props.address}</Text>
          </Box>
        </VStack>
      </Box>
    </Box>
    </Link>
  );
};

export default LibraryCard;
