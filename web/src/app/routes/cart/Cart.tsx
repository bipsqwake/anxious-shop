import { useQuery, useQueryClient } from "@tanstack/react-query";
import axios, { type AxiosResponse } from "axios";
import { useState } from "react";
import { deliveryTypes } from "../../../types/DeliveryMethodType";
import type { ItemResponse } from "../../../types/ItemResponse";
import type { ItemSearchResponse } from "../../../types/ItemSearchResponse";
import type { Order } from "../../../types/Order";
import type { OrderStatus } from "../../../types/OrderStatus";
import { orderTemplate } from "../../../types/OrderTemplate";
import { useCart } from "../../cart/CartProvider";
import { useSyncCartWithStock } from "../../cart/CartStockSynk";
import CartItemView from "./CartItemView";
import DeliveryMethod from "./DeliveryMethod";

export default function Cart({setPage}: {setPage: (arg0: string) => void}) {
    const {getCart, clearCart} = useCart()

    var queryClient = useQueryClient();

    useSyncCartWithStock();

    const [deliveryMethod, setDeliveryMethod] = useState(deliveryTypes[0])

    const {data, isPending, isError} = useQuery({
        queryKey: ["checkCart", getCart().map(item => item.id)],
        queryFn: () => {
            return axios.get<ItemSearchResponse>(import.meta.env.VITE_API_URL + "/item/find?ids=" + getCart().map(item => item.id).join(","));
        }
    })

    if (isPending) {
        return <></>
    }

    if (isError) {
        return <></>
    }

    console.log(data)

    function CartItems() {
        if (getCart().length == 0) {
            return <div className="cart-empty">тут пустовато...</div>
        } else {
            return getCart().map(item => <CartItemView key={item.id} cartItem={item} card={data?.data[item.id] ?? {} as ItemResponse} />)
        }
    }

    function sum() {
        return getCart().reduce((sum, item) => sum + (data?.data[item.id].price ?? 0) * item.quantity, 0) + deliveryMethod.price;
    }

    function placeOrder() {
        var order = {
            items: getCart().map(obj => ({id: obj.id, count: obj.quantity})),
            deliveryType: deliveryMethod.value
        } as Order
        axios.post<Order, AxiosResponse<OrderStatus>>(import.meta.env.VITE_API_URL + "/order", order)
            .then(resp => {
                console.log(resp.status)
                if (resp.data.status == "SUCCESS") {
                    successOrder()
                } else if (resp.data.status == "CONFLICT") {
                    queryClient.invalidateQueries({queryKey: ["cart-stock"]})
                } else {

                }
            }).catch(err => {
                console.log(err)
            })
    }

    function successOrder() {
        var orderMessage = orderTemplate.replaceAll("{order}", getOrder()).replaceAll("{delivery}", deliveryMethod.value).replaceAll("{amount}", sum() + "");
        var orderLink = "https://t.me/dianakho?text=" + orderMessage
        console.log(orderLink)
        clearCart();
        window.location.href = orderLink;
        setPage("catalog")
    }

    function getOrder() {
        return getCart().map((cartItem) => data?.data[cartItem.id].intName).join(";")
    }

    return (
        <main>
            <div className="cart-container">
                <button className="cart-back-btn" onClick={() => setPage("catalog")}>
                    <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6" /></svg>
                    назад
                </button>
                <div className="disclaimer">
                    дорогуши, обращаю ваше внимание, что цвет брелоков в жизни может отличаться от картинки на сайте из-за разницы цветопередачи экранов. учитывайте этот момент при покупке!
                </div>
                <div className="cart-title">корзина</div>
                <div className="cart-list">
                    <CartItems />
                </div>
                <div className="cart-delivery">
                    <div className="cart-delivery-label">способ доставки:</div>
                    <div className="cart-delivery-options">
                        {deliveryTypes.map(value => <DeliveryMethod key={value.value} text={value.text} value={value.value} selected={deliveryMethod.value} onChange={() => setDeliveryMethod(value)}/>)}
                    </div>
                </div>
                <div className="cart-total">
                    итого: <span style={{marginLeft: "0.6em"}}><b>{sum()} ₽</b></span>
                </div>
                <div className="cart-actions">
                    <button className="cart-action-btn primary" onClick={placeOrder}>оформить заказ</button>
                    <button className="cart-action-btn secondary" onClick={clearCart}>очистить корзину</button>
                </div>
            </div>
        </main>
    );
}