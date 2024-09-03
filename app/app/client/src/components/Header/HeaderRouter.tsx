import React from "react";
import { Routes, Route } from "react-router";
import { BrowserRouter } from "react-router-dom";
import Header from ".";
// import CategoryHeader from "./Category";
// import RecipeHeader from "./Recipe";
// import SearchHeader from "./Search";
// import UserHeader from "./User";


export const HeaderRouter: React.FC<{}> = () => {
    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<Header title="Все библиотеки"/>}/>
            <Route path="/libraries" element={<Header title="Все библиотеки"/>}/>
            <Route path="/libraries/:uuid" element={<Header title="Книги в библиотеке"/>}/>
            <Route path="/rating" element={<Header title="Мой рейтинг"/>}/>
            <Route path="/reservations" element={<Header title="Мои бронирования" />}/>
            <Route path="/statistics" element={<Header title="Общая cтатистика" />}/>
            <Route path="/logout" element={<Header title="Выход" />}/>

            {/* <Route path="/accounts/:login/recipes" element={<UserHeader subtitle="Автор" title="" />}/>
            <Route path="/accounts/:login/likes" element={<UserHeader subtitle="Понравилось" title="" />}/> */}

            <Route path="/authorize" element={<Header title="Вход" undertitle="Добро пожаловать" />}/>
            {/* <Route path="/register" element={<Header title="Регистрация" undertitle="Чтобы получить доступ к книгам!" />}/> */}
            
            {/* <Route path="/recipes/:id" element={<RecipeHeader title=""/>}/>
            <Route path="/categories/:title" element={<CategoryHeader subtitle="Категория" title=""/>}/> */}

            <Route path="*" element={<Header title="Страница не найдена"/>}/>
        </Routes>
    </BrowserRouter>
}
