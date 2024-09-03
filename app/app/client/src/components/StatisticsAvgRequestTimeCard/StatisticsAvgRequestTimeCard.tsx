import { Box, Text, VStack } from "@chakra-ui/react";
import React from "react";
import { RequestAvg } from "types/RequestAvg";

import styles from "./StatisticsAvgRequestTimeCard.module.scss";

interface StatisticsAvgRequestTimeProps extends RequestAvg {}


const StatisticsAvgRequestTimeCard: React.FC<StatisticsAvgRequestTimeProps> = (props) => {
    return (
        <Box className={styles.main_box}>
            <Box className={styles.info_box}>
                <VStack>
                    <Box className={styles.title_box}>
                        <Text>Запрос: {props.action}</Text>
                    </Box>

                    <Box className={styles.description_box}>
                        <Text>Сервис: {props.service}</Text>
                    </Box>

                    <Box className={styles.description_box}>
                        <Text>Количество запросов: {props.num} шт</Text>
                    </Box>

                    <Box className={styles.description_box}>
                        <Text>Среднее время выполнения: {props.avgTime} мс</Text>
                    </Box>
                </VStack>
            </Box>
        </Box>
    )
};

export default StatisticsAvgRequestTimeCard;