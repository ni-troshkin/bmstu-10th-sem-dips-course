import { Box, Checkbox } from "@chakra-ui/react";
import { useParams } from "react-router-dom"
import { SearchContext } from "context/Search";
import React, { useContext } from "react";
import { useState } from "react";

import styles from "./BooksInLibrary.module.scss";
import BookMap from "components/BookMap";
import GetBooksInLibrary from "postAPI/libraries/GetBooksInLibrary";

interface BooksInLibrary {}

const BooksInLibraryPage: React.FC<BooksInLibrary> = (props) => {
  const searchContext = useContext(SearchContext);
  const params = useParams();
  const libraryUid = params.uuid;
  console.log(libraryUid);
  // const [showAll, setShowAll] = useState(Boolean(localStorage.getItem("allBooks")));
  // localStorage.setItem("allBooks", String(showAll));

  if (localStorage.getItem("token") == null) {
    window.location.href = "/authorize";
    return (<Box></Box>);
  }

  const toggleMode = () => {
    // console.log(showAll);
    let temp = !(JSON.parse(localStorage.getItem("allBooks") || "false"));
    // setShowAll(temp);
    console.log(temp);
    // console.log(showAll);
    localStorage.setItem("allBooks", String(temp));
    window.location.reload();
  }

  return (
    <Box className={styles.main_box}>
      {/* <CategoryMap getCall={GetCategories}/> */}
      <Checkbox isChecked={JSON.parse(localStorage.getItem("allBooks") || "false")} onChange={toggleMode}>Показать недоступные</Checkbox>
      <BookMap searchQuery={searchContext.query} getCall={GetBooksInLibrary}
        libraryUid={libraryUid} />
    </Box>
  );
};

export default BooksInLibraryPage;
