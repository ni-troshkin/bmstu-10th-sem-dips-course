import { Box } from "@chakra-ui/react";
import React from "react";
import LibraryCard from "../LibraryCard";
// import { AllHotelResp } from "postAPI"
import { GetLibrariesByCityResp } from "postAPI"

import styles from "./LibraryMap.module.scss";

interface LibraryBoxProps {
    searchQuery?: string
    getCall: (city: string, page: number, size: number) => Promise<GetLibrariesByCityResp>
    city: string
}

type State = GetLibrariesByCityResp

class LibraryMap extends React.Component<LibraryBoxProps, State> {
    constructor(props) {
        super(props);
        // this.state = {
        //     postContent: []
        // }
    }

    async getAll() {
        // console.log("JOPA");
        var data = await this.props.getCall(localStorage.getItem("city") || "Москва", 1, 50)
        // console.log(data.status);
        if (data.status === 200)
            this.setState(data);
    }

    componentDidMount() {
        this.getAll()
    }

    componentDidUpdate(prevProps) {
        // this.getAll()
        if (this.props.searchQuery !== prevProps.searchQuery) {
            this.getAll()
        }
    }

    render() {
        return (
            <Box className={styles.map_box}>
            { //this.state?.content.items.length != 0 &&
                this.state?.content.items.map(item => <LibraryCard {...item} key={item.libraryUid}/>)
            }
            </Box>
            
        )
    }
}

export default React.memo(LibraryMap);
