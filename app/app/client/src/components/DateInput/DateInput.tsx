import { Box, Button, useDisclosure } from '@chakra-ui/react'
import { useState } from 'react';
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
} from '@chakra-ui/react'
import React from 'react'

import AddIcon from 'components/Icons/Add'
import RoundButton from "components/RoundButton/RoundButton";

import styles from "./DateInput.module.scss"
import DateInputBox from 'components/DateInputBox/DateInputBox'


interface DateContextType {
  date: string,
  setDate: React.Dispatch<React.SetStateAction<string>>,
}

export const DateContext = React.createContext<DateContextType | undefined>(undefined);


export default function DateInput(props) {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const [ date, setDate] = useState('');

  async function put() { 
    let strdate = date.toLocaleString().replace("/", ".").substring(0, 10);
    await props.putCallback(strdate);
    
    onClose();
  }

  return (
    <>
      <RoundButton onClick={onOpen}>
          Забронировать
      </RoundButton>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent className={styles.dark_bg}>
          <ModalHeader>Выберите дату возвращения книги</ModalHeader>
          <ModalCloseButton />
          <ModalBody className={styles.model_body}>
            <Box>
              <DateContext.Provider value={{ date, setDate }}>
                <DateInputBox />
              </DateContext.Provider>
            </Box>

            <Button className={styles.ready_btn} onClick={put}>
              <AddIcon className={styles.img_btn} />
            </Button>
          </ModalBody>
          <ModalFooter/>
        </ModalContent>
      </Modal>
    </>
  )
}
