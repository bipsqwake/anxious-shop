import { createContext } from "react";
import { Cart } from "./Cart";

const CartContext = createContext<Cart>(new Cart())
export default CartContext;