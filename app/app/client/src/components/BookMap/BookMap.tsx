import { Box } from "@chakra-ui/react";
import React from "react";
import BookCard from "../BookCard";

import { GetBooksInLibraryResp } from "postAPI"

import styles from "./BookMap.module.scss";
import GetBooksInLibrary from "postAPI/libraries/GetBooksInLibrary";

interface BookBoxProps {
    searchQuery?: string
    getCall: (
        libraryUid: number, 
        page: number, 
        size: number,
        showAll: boolean
    ) => Promise<GetBooksInLibraryResp>
    libraryUid: number
    showAll?: boolean
}

type State = GetBooksInLibraryResp

class BookMap extends React.Component<BookBoxProps, State> {
    async getBooks() {
        var data = await this.props.getCall(
            this.props.libraryUid, 1, 50, JSON.parse(localStorage.getItem("allBooks") || "false")
        )
        if (data.status === 200)
            this.setState(data);
    }
    
    constructor(props) {
        super(props);
        // this.getBooks();
        // this.state = {
        //     postContent: []
        // }
    }

    

    componentDidMount() {
        this.getBooks()
    }

    componentDidUpdate(prevProps) {
        if (this.props.searchQuery !== prevProps.searchQuery) {
            this.getBooks()
        }
    }

    render() {
        return (
            <Box className={styles.map_box}>
                {this.state?.content.items.length > 0 &&
                    this.state?.content.items.map(
                    item => <BookCard 
                        {...item} 
                        key={item.bookUid} 
                        libraryUid={this.props.libraryUid}/>
                    )
                }
            </Box>
        )
    }
}

export default React.memo(BookMap);
