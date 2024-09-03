import { Box } from "@chakra-ui/react";
import React from "react";
import { AllReservationsResp } from "postAPI";

import styles from "./ReservationMap.module.scss";
import ReservationCard from "components/ReservationCard/ReservationCard";

interface ReservationBoxProps {
    searchQuery?: string
    getCall: (title?: string) => Promise<AllReservationsResp>
}

type State = AllReservationsResp

class ReservationMap extends React.Component<ReservationBoxProps, State> {
    constructor(props) {
        super(props);
    }

    async getAll() {
        var data = await this.props.getCall()
        if (data.status === 200)
            this.setState(data);
    }

    componentDidMount() {
        this.getAll()
    }

    componentDidUpdate(prevProps) {
        if (this.props.searchQuery !== prevProps.searchQuery) {
            this.getAll()
        }
    }

    render() {
        return (
            <Box className={styles.map_box}>
                {this.state?.content.map(item => <ReservationCard {...item} key={item.reservationUid}/>)}
            </Box>
        )
    }
}

export default React.memo(ReservationMap);
