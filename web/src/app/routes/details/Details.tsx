import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import type { ItemResponse } from "../../../types/ItemResponse";
import Loading from "../../common/Loading";
import ErrorFallback from "../../common/Error";
import { useCart } from "../../cart/CartProvider";

export default function Details({ id, setPage }: { id: string, setPage: (arg0: string) => void }) {

    const {addItem, removeItem, getItemQuantity} = useCart()
    
    const {data, isPending, isError} = useQuery({
        queryKey: [id],
        queryFn: () => {
            return axios.get<ItemResponse>(import.meta.env.VITE_API_URL + "/item/" + id)
        }
    })

    if (isPending) {
        return <Loading />
    }

    if (isError) {
        return <ErrorFallback />
    }

    function addAvailable() {
        return getItemQuantity(id) < (data?.data.itemsLeft ?? 0)
    }

    function ToCartCounter() {
        if (getItemQuantity(id) <= 0) {
            return <button className="card-btn" onClick={() => addItem(id)} disabled={!addAvailable()}>В корзину</button>
        } else {
            return (
                <div className="card-counter">
                    <button className="counter-btn minus" aria-label="Уменьшить количество" onClick={() => removeItem(id)}>−</button>
                    <span className="counter-value">{getItemQuantity(id)}</span>
                    <button className="counter-btn plus" aria-label="Увеличить количество" onClick={() => addItem(id)} disabled={!addAvailable()}>+</button>
                </div>
            );
        }
    }

    return (
        <main>
            <div className="product-layout">
                <div className="product-left">
                    <button className="back-btn" onClick={() => setPage("catalog")}>
                        <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6" /></svg>
                        Назад
                    </button>
                    <div className="product-img-wrap">
                        <img src={data.data.imgUrl} alt={data.data.name} />
                    </div>
                </div>
                <div className="product-info">
                    <h1 className="product-title">{data.data.name}</h1>
                    <div className="product-price">{data.data.price} ₽</div>
                    <div className="product-desc">
                        {data.data.description}
                    </div>
                    <div className="product-actions">
                        <ToCartCounter />
                    </div>
                </div>
            </div>
        </main>
    );
}