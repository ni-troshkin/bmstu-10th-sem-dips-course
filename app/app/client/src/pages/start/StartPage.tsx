import React from "react";
import { Box } from "@chakra-ui/react";

interface AllLibraries {}

const StartPage: React.FC<AllLibraries> = (props) => {
    if (localStorage.getItem("token") == null) {
        window.location.href = "/authorize";
        return (<Box></Box>)
    }
    else {
        window.location.href = "/libraries";
        return (<Box></Box>)
    }
}

export default StartPage;
