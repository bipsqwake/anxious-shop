import type { ItemResponse } from "../../../types/ItemResponse";
import { useCart, type CartItem } from "../../cart/CartProvider";

export default function CartItemView({cartItem, card}: {cartItem: CartItem, card: ItemResponse}) {

    const {addItem, removeItem, getItemQuantity} = useCart()
    
    // function itemSum() {
    //     return (card.price ?? 0) * cartItem.quantity;
    // }

    function addAvailable() {
        return getItemQuantity(cartItem.id) < card.itemsLeft
    }

    return (
        <div className="cart-item">
            <img className="cart-item-img" src={card.imgUrl} alt={card.name} />
            <div className="cart-item-info">
                <div className="cart-item-title">{card.name}</div>
                <div className="cart-item-price">{card.price} ₽</div>
                {/* <div className="cart-item-sum">Сумма: <b>{itemSum()} ₽</b></div> */}
            </div>
            <div className="cart-item-counter">
                <div className="card-counter">
                    <button className="counter-btn" onClick={() => removeItem(cartItem.id)}>-</button>
                    <span className="counter-value">{cartItem.quantity}</span>
                    <button className="counter-btn" onClick={() => addItem(cartItem.id)} disabled={!addAvailable()}>+</button>
                </div>
            </div>
        </div>
    );
}