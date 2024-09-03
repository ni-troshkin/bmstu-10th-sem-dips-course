import { Box } from "@chakra-ui/react";
import { SearchContext } from "context/Search";
import React, { useContext, useState } from "react";

import styles from "./RatingInfo.module.scss";
import GetRating from "postAPI/rating/GetRating";

interface RatingInfo {}

const RatingInfoPage: React.FC<RatingInfo> = (props) => {
  const searchContext = useContext(SearchContext);
  const [rating, setRating] = useState(0);

//   if (localStorage.getItem("token") == null) {
//     window.location.href = "/authorize";
//     return (<Box></Box>);
//   }

  const loadData = async () => {
      let data = await GetRating();
      setRating(data.content.stars);
  };

  loadData();

  // useEffect(() => { loadData() }, [loadData]);

  return (
    <div className={styles.main_div}>
        <text className={styles.title_box}>
            Ваш рейтинг: {rating}
        </text>
    </div>
  );
};

export default RatingInfoPage;
