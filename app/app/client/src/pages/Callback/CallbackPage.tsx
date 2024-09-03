import React from "react";

import styles from "./CallbackPage.module.scss";
import Token from "postAPI/oauth/Token";
import { TokenRequest } from "types/TokenRequest";
import { TokenResponseResp } from "postAPI";
import { TokenResponse } from "types/TokenResponse";
import { Box } from "@chakra-ui/react";
import { useNavigate } from "react-router";
import { useSearchParams } from "react-router-dom";

interface AllLibraries {}

const CallbackPage: React.FC<AllLibraries> = (props) => {
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();
  const token = async () => {
    var code = searchParams.get("code");
    if (code != null) {
      let req: TokenRequest = {
        authentication_code: code,
        client_id: "library-client",
        client_secret: "F6H1kwYzUrSwOIM30Ri4dLrLO8aNpcmf",
        redirect_uri: "http%3A%2F%2Flocalhost%3A3000%2Ftoken"
      }
      let resp: TokenResponseResp = await Token(req);
      if (resp.status == 200) {
        console.log(resp.content.access_token);
        localStorage.setItem("token", resp.content.access_token);
        localStorage.setItem("user", resp.content.username);
        navigate('/libraries/');
        return <Box/>
      }
      else {
        console.log("Invalid token");
        navigate('/');
        return <Box/>
      }
    }
    else {
      console.log("Couldn't receive code");
      navigate('/');
      return <Box/>
    }
  };

  token();

  return (
    <div className={styles.main_div}>
      <text className={styles.title_box}>
        Авторизация...
      </text>
    </div>
  );
};

export default CallbackPage;
