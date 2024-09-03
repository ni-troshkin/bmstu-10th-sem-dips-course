import * as React from "react";
import theme from "./styles/extendTheme";
import { ChakraProvider, Box, Container } from "@chakra-ui/react";
import { Routes, Route } from "react-router";
import { BrowserRouter } from "react-router-dom";

// import Login from "pages/Login";
// import SignUp from "pages/Signup";

import RatingInfoPage from "pages/Recipe/RatingInfo";
import AllLibrariesPage from "pages/Recipe/AllLibraries";
import BooksInLibraryPage from "pages/Recipe/BooksInLibrary";
import AllReservationsPage from "pages/Recipe/AllReservations";
import StartPage from "pages/start/StartPage";
import AuthPage from "pages/Auth/AuthPage";
import CallbackPage from "pages/Callback/CallbackPage";
import StatisticsPage from "pages/Recipe/AllStatistics";

import SearchContextProvider from "context/Search";
import { HeaderRouter } from "components/Header";
import LogoutPage from "pages/Logout/LogoutPage";


interface HomeProps {}
const Home: React.FC<HomeProps> = () => {
  return (
    <Box backgroundColor="bg" h="auto">
      <Container maxW="1000px" minH="95%"
        display="flex" 
        paddingX="0px" paddingY="30px"  
        alignSelf="center" justifyContent="center"
        textStyle="body"
      >
        <Routing />
      </Container>
    </Box>
  );
};

function Routing() {
  return <BrowserRouter>
    <Routes>
      <Route path="/" element={<StartPage/>}/>
      <Route path="/authorize" element={<AuthPage/>}/>
      <Route path="/token" element={<CallbackPage/>}/>
      <Route path="/libraries" element={<AllLibrariesPage/>}/>
      <Route path="/libraries/:uuid" element={<BooksInLibraryPage/>}/>
      <Route path="/reservations" element={<AllReservationsPage/>}/>
      <Route path="/statistics" element={<StatisticsPage/>}/>
      <Route path="/logout" element={<LogoutPage/>}/>

      {/* <Route path="/accounts/:login/stats" element={<StatisticsPage/>}/> */}
      {/* <Route path="/accounts/:login/likes" element={<LikedRecipesPage/>}/> */}

      {/* <Route path="/authorize" element={<Login/>}/> */}
      {/* <Route path="/register" element={<SignUp/>}/> */}

      <Route path="/rating" element={<RatingInfoPage />}/>

      {/* <Route path="/categories/:title" element={<CategoryPage />}/> */}
      {/* <Route path="/users" element={<UsersPage />}/> */}

      <Route path="*" element={<NotFound />}/>
    </Routes>
  </BrowserRouter>
}

function NotFound () {
  return <h1>Page not Found</h1>
}

export const App = () => {
  return (    
    <ChakraProvider theme={theme}>
    <SearchContextProvider>
      <HeaderRouter/>
      <Home />
    </SearchContextProvider>
    </ChakraProvider>
  )
};
