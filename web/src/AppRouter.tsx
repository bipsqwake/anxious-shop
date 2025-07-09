import { BrowserRouter, Route, Routes } from "react-router";
import Application from "./app/routes/Application";

export default function AppRouter() {


    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Application />} />
            </Routes>
        </BrowserRouter>
    );
}