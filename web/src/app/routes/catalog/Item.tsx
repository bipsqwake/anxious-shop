import type { ItemResponse } from "../../../types/ItemResponse";
import { useCart } from "../../cart/CartProvider";

export default function Item({ item }: { item: ItemResponse }) {

    const {addItem, removeItem, getItemQuantity} = useCart()

    function addToCart() {
        addItem(item.id)
    }

    function removeFromCart() {
        removeItem(item.id)
    }

    function addAvailable() {
        return getItemQuantity(item.id) < item.itemsLeft
    }

    function ToCartCounter() {
        if (getItemQuantity(item.id) <= 0) {
            return <button className="card-btn detail" onClick={addToCart}>в корзину</button>
        } else {
            return (
                <div className="card-counter">
                    <button className="counter-btn minus" aria-label="Уменьшить количество" onClick={removeFromCart}>−</button>
                    <span className="counter-value">{getItemQuantity(item.id)}</span>
                    <button className="counter-btn plus" aria-label="Увеличить количество" onClick={addToCart} disabled={!addAvailable()}>+</button>
                </div>
            );
        }
    }

    function Price() {
        if (item.oldPrice <= 0) {
            return <div className="card-price-container"><div className="card-price">{item.price} ₽</div></div>
        } else {
            return <div className="card-price-container"><div className="card-price striped">{item.oldPrice} ₽</div>&emsp;<div className="card-price">{item.price} ₽</div></div>
        }
    }

    return (
        <div className="card">
            <div className="card-img-wrap">
                <img src={item.imgUrl} alt={item.name} />
            </div>
            <div className="card-title">{item.name}</div>
            <div className="card-desc">{item.description}</div>
            {/* <div className="card-price-container"><div className="card-price striped">{item.oldPrice} ₽</div>&emsp;<div className="card-price">{item.price} ₽</div></div> */}
            <Price />
            <div className="card-actions">
                {/* <button className="card-btn detail" onClick={() => setPage("details" + item.id)}>Подробнее</button> */}
                <ToCartCounter />
            </div>
        </div>
    );
}