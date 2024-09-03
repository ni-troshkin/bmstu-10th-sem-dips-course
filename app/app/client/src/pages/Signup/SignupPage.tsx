import React from "react";
import theme from "styles/extendTheme";

import { Box, Link } from "@chakra-ui/react";
import { NavigateFunction } from "react-router-dom";

import Input from "components/Input";
import RoundButton from "components/RoundButton";

// import styles from "./SignupPage.module.scss";


type SignUpProps = {
    navigate: NavigateFunction
}


class SignUpPage extends React.Component<SignUpProps> {
    // registrationCard: RegistrationCard = {
    //     profile: {
    //         firstName: "",
    //         lastName: "",
    //         email: "",
    //         login: "",
    //         mobilePhone: ""
    //     },
    //     credentials: {
    //         password: {
    //             value: ""
    //         }
    //     }
    // }

    // repPassword: string = ""

    // setFirstName(val: string) {
    //     this.registrationCard.profile.firstName = val
    // }
    // setLastName(val: string) {
    //     this.registrationCard.profile.lastName = val
    // }
    // setEmail(val: string) {
    //     this.registrationCard.profile.email = val
    // }
    // setLogin(val: string) {
    //     this.registrationCard.profile.login = val
    // }
    // setMobilePhone(val: string) {
    //     this.registrationCard.profile.mobilePhone = val
    // }
    // setPassword(val: string) {
    //     this.registrationCard.credentials.password.value = val
    // }
    // setRepPassword(val: string) {
    //     this.repPassword = val
    // }

    // highlightNotMatch() {
    //     let node1 = document.getElementsByName("password")[0]
    //     let node2 = document.getElementsByName("rep-password")[0]

    //     if (node1.parentElement && node2.parentElement) {
    //         node1.parentElement.style.borderColor = theme.colors["title"]
    //         node2.parentElement.style.borderColor = theme.colors["title"]
    //     }

    //     var title = document.getElementById("undertitle")
    //     if (title)
    //         title.innerText = "Пароли не совпадают!"
    // }

    // async submit(e: React.MouseEvent<HTMLButtonElement, MouseEvent>) {
    //     if (this.registrationCard.credentials.password.value !== this.repPassword)
    //         return this.highlightNotMatch()

    //     //e.currentTarget.disabled = true
    //     var data = await CreateQuery(this.registrationCard)
    //     if (data.status === 200) {
    //         let acc: Account = {
    //             username: this.registrationCard.profile.login,
    //             password: this.registrationCard.credentials.password.value
    //         }

    //         await LoginQuery(acc);
    //         window.location.href = '/';
    //     } else {
    //         //e.currentTarget.disabled = false
    //         var title = document.getElementById("undertitle")
    //         if (title)
    //             title.innerText = "Ошибка создания аккаунта!"
    //     };
    // }

    // render() {
    //     return <Box className={styles.login_page}>
    //         <Box className={styles.input_div}>
    //             <Input name="login" placeholder="Введите имя" 
    //             onInput={event => this.setFirstName(event.currentTarget.value)}/>
    //             <Input name="login" placeholder="Введите фамилию" 
    //             onInput={event => this.setLastName(event.currentTarget.value)}/>
    //             <Input name="login" placeholder="Введите электронную почту" 
    //             onInput={event => this.setEmail(event.currentTarget.value)}/>
    //             <Input name="login" placeholder="Введите логин" 
    //             onInput={event => this.setLogin(event.currentTarget.value)}/>
    //             <Input name="login" placeholder="Введите телефон" 
    //             onInput={event => this.setMobilePhone(event.currentTarget.value)}/>
    //             <Input name="password" type="password" placeholder="Введите пароль"
    //             onInput={event => this.setPassword(event.currentTarget.value)}/>
    //             <Input name="rep-password" type="password" placeholder="Повторите пароль"
    //             onInput={event => this.setRepPassword(event.currentTarget.value)}/>
    //         </Box>

    //         <Box className={styles.input_div}>
    //             <RoundButton type="submit" onClick={event => this.submit(event)}>
    //                 Создать аккаунт
    //             </RoundButton>
    //             <Link href="/authorize">Войти</Link>
    //         </Box>
    //     </Box>
    // }
}

export default SignUpPage;