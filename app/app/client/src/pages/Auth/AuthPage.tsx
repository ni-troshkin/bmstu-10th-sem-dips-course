import React from "react";
import Authorize from "postAPI/oauth/Authorize";

import styles from "./AuthPage.module.scss";


interface AllLibraries {}

const AuthPage: React.FC<AllLibraries> = (props) => {

  const auth = async () => {
    localStorage.setItem("token", " ");
    window.location.href = 
    "http://146.185.210.83/realms/LibraryIdentityProvider/protocol/openid-connect/auth?response_type=code&client_id=library-client&redirect_uri=http%3A%2F%2Flocalhost%3A3000%2Ftoken&scope=openid&state=xyz"
      // await Authorize();
      // new Response("", {
      //   status: 302,
      //   headers: {
      //     Location: res.headers.Location,
      //   },
      // })
  };

  auth();

  return (
    <div className={styles.main_div}>
      <text className={styles.title_box}>
        Перенаправляем на страницу входа
      </text>
    </div>
  );
};

export default AuthPage;
