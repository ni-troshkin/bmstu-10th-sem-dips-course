import { Box } from "@chakra-ui/react";
import React from "react";

import styles from "./StatisticsPage.module.scss";
import GetAvgServiceTime from "postAPI/stats/GetAvgServiceTime";
import StatisticsMap from "components/StatisticsMap/StatisticsMap";
import GetAvgRequestTime from "postAPI/stats/GetAvgRequestTime";

interface AllStatistics {}

const StatisticsPage: React.FC<AllStatistics> = () => {
  return (
    <Box className={styles.main_box}>
      <StatisticsMap isAdmin={false} getCall={GetAvgServiceTime} getCallRequest={GetAvgRequestTime} />
    </Box>
  );
};

export default StatisticsPage;
