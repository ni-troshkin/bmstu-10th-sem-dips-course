import { Box, Text, VStack } from "@chakra-ui/react";
import React from "react";
import { ServiceAvg } from "types/ServiceAvg";

import styles from "./StatisticsAvgServiceTimeCard.module.scss";


interface StatisticsAvgServiceTimeProps extends ServiceAvg {}


const StatisticsAvgServiceTimeCard: React.FC<ServiceAvg> = (props) => {
    return (
        <Box className={styles.main_box}>
            <Box className={styles.info_box}>
                <VStack>
                    <Box className={styles.title_box}>
                        <Text>Сервис: {props.service}</Text>
                    </Box>

                    <Box className={styles.description_box}>
                        <Text>Количество запросов: {props.num} шт</Text>
                    </Box>

                    <Box className={styles.description_box}>
                        <Text>Время работы: {props.avgTime} мс</Text>
                    </Box>
                </VStack>
            </Box>
        </Box>
    )
};

export default StatisticsAvgServiceTimeCard;