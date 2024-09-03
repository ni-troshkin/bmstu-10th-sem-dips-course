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

import { statusItems } from './items';
import styles from "./ConditionInput.module.scss"
import ConditionInputBox from 'components/ConditionInputBox';


interface ConditionContextType {
  condition: string,
  setCondition: React.Dispatch<React.SetStateAction<string>>,
}

export const ConditionContext = React.createContext<ConditionContextType | undefined>(undefined);


export default function ConditionInput(props) {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const [ condition, setCondition] = useState("Отличное");

  async function put() { 
    await props.putCallback(statusItems[condition]);
    
    onClose()
  }

  return (
    <>
      <RoundButton onClick={onOpen}>
          Вернуть
      </RoundButton>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent className={styles.dark_bg}>
          <ModalHeader>Выберите текущее состояние книги</ModalHeader>
          <ModalCloseButton />
          <ModalBody className={styles.model_body}>
            <Box>
              <ConditionContext.Provider value={{ condition, setCondition }}>
                <ConditionInputBox />
              </ConditionContext.Provider>
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
