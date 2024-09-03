import { Box } from "@chakra-ui/react";
import { SearchContext } from "context/Search";
import React, { useContext, useState } from "react";
import Input from "components/Input";
import RoundButton from "components/RoundButton";

import GetLibrariesByCity from "postAPI/libraries/GetLibrariesByCity";

import styles from "./AllLibrariesPage.module.scss";
import LibraryMap from "components/LibraryMap";

interface AllLibraries {}

const AllLibrariesPage: React.FC<AllLibraries> = () => {
  // const searchContext = useContext(SearchContext);

  const [city, setCity] = useState("Москва");
  const [tempCity, setTempCity] = useState(localStorage.getItem("city") || "Москва");
  // const [chosen, setChosen] = useState(false);

  const handleCityChange = (e) => {
    console.log(e.target.value);
    setTempCity(e.target.value);
  };

  const handleSubmit = (e) => {
    console.log(tempCity);
    e.preventDefault();
    setCity(tempCity);
    console.log(city);
    localStorage.setItem("city", tempCity);
    window.location.reload();
  };

  // const loadData = useCallback(async () => {
  //     console.log('Selected city:', city);
  //     let resp = await GetLibrariesByCity(city, 1, 10);
  //     setLibs(resp.content.items);
  // }, [city]);

  // useEffect(() => { loadData() }, [loadData]);

  return (
    <>
    <div className={styles.main_div}>
            <form className={styles.city_form} onSubmit={handleSubmit}>
                <div>
                    <input type="string" value={tempCity} onChange={handleCityChange} />
                </div>
                <RoundButton type="submit">Выбрать город</RoundButton>
            </form>
        </div>
      
    <Box className={styles.main_box}>
      {/* <CategoryMap getCall={GetCategories}/> */}
      <LibraryMap getCall={GetLibrariesByCity} city={city}/>
    </Box>
    
    </>
  );
};

export default AllLibrariesPage;
