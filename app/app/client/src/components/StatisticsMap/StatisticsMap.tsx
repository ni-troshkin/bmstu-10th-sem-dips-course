import { Box, Text } from "@chakra-ui/react";
import React from "react";

import styles from "./StatisticsMap.module.scss";
import { AllAvgRequestTimeResp, AllAvgServiceTimeResp } from "postAPI";
import StatisticsAvgServiceTimeCard from "components/StatisticsAvgServiceTimeCard/StatisticsAvgServiceTimeCard";
import StatisticsAvgRequestTimeCard from "components/StatisticsAvgRequestTimeCard/StatisticsAvgRequestTimeCard";


interface StatisticsBoxProps {
    searchQuery?: string
    getCall: () => Promise<AllAvgServiceTimeResp>
    getCallRequest: () => Promise<AllAvgRequestTimeResp>
}

type State = {
    avgServiceTime?: any
    avgRequestTime?: any
    isAdmin?: any
}


class StatisticsMap extends React.Component<StatisticsBoxProps, State> {
    constructor(props) {
        super(props);
        this.state = {
            avgServiceTime: [],
            avgRequestTime: [],
            isAdmin: false
        }
    }

    async getAllAvgServiceTime() {
        var data = await this.props.getCall();
        if (data.status === 200)
            this.setState({avgServiceTime: data.content, isAdmin: true});

    }

    async getAllAvgRequestTime() {
        var data = await this.props.getCallRequest();
        console.log(data.status);
        if (data.status === 200)
            this.setState({avgRequestTime: data.content, isAdmin: true})
    }

    componentDidMount() {
        this.getAllAvgServiceTime();
        this.getAllAvgRequestTime();
    }

    render() {
        return <>
            {this.state.isAdmin &&
                <Box className={styles.map_box}>
                    <Text className={styles.title_box}>
                        Общие характеристики по сервисам
                    </Text>
                    {this.state.avgServiceTime.map(item => <StatisticsAvgServiceTimeCard {...item} key={item.service}/>)}
                    
                    <Text className={styles.title_box}>
                        Общие характеристики по запросам
                    </Text>
                    {this.state.avgRequestTime.map(item => <StatisticsAvgRequestTimeCard {...item} key={item.action + item.service}/>)}
                </Box>
            }
            {
                !this.state.isAdmin &&
                <Box className={styles.map_box}>
                    <Text className={styles.title_box}>
                        Данный модуль недоступен для пользователя
                    </Text>
                </Box>
            }
            </>
    }
}

export default React.memo(StatisticsMap);