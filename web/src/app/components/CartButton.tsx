import { useCart } from "../cart/CartProvider";

export default function CartButton({setPage}: {setPage: (arg0: string) => void}) {

    const {getTotalQuantity} = useCart()

    return (
        <a className="fab-cart" title="Корзина" onClick={() => setPage("cart")}>
            <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="13" cy="29" r="2" /><circle cx="26" cy="29" r="2" />
                <path d="M2 2h5l4 20h15l3-13H7" />
            </svg>
            <span className="cart-count">{getTotalQuantity()}</span>
        </a>
    );
}