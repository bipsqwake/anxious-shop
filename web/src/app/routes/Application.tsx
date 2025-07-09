import { useState } from "react";
import { CartProvider } from "../cart/CartProvider";
import Header from "../components/Header";
import Cart from "./cart/Cart";
import Catalog from "./catalog/Catalog";
import Details from "./details/Details";

export default function Application() {

    const [page, setPage] = useState("catalog")

    function Page() {
        console.log(page)
        if (page.startsWith("details")) {
            return <Details id={page.replaceAll("details", "")} setPage={setPage} />
        }
        switch (page) {
            case "cart": return <Cart setPage={setPage}/>
            default: return <Catalog setPage={setPage}/>
        }
    }

    return (
        <CartProvider>
            <Header />
            <Page />
        </CartProvider>
    );
}