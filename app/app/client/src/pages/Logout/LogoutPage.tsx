import React from "react";
import styles from "./LogoutPage.module.scss";
import Logout from "postAPI/oauth/Logout";


interface AllLibraries {}

const LogoutPage: React.FC<AllLibraries> = (props) => {

  const logout = async () => {
    let resp = await Logout(localStorage.getItem("user") || "user");
    localStorage.clear();
    window.location.href = "/";
  };

  logout();

  return (
    <div className={styles.main_div}>
      <text className={styles.title_box}>
        Выход...
      </text>
    </div>
  );
};

export default LogoutPage;
