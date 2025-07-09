import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import CartButton from "../../components/CartButton";
import type { ItemResponse } from "../../../types/ItemResponse";
import Item from "./Item";
import Loading from "../../common/Loading";
import ErrorFallback from "../../common/Error";
import { useSyncCartWithStock } from "../../cart/CartStockSynk";

export default function Catalog({setPage}: {setPage: (arg0: string) => void}) {


    useSyncCartWithStock();

    const {data, isPending, isError} = useQuery({
        queryKey: ["catalog"],
        queryFn: () => {
            return axios.get<ItemResponse[]>(import.meta.env.VITE_API_URL + "/item?available=true")
        }
    })

    if (isPending) {
        return <Loading />
    }

    if (isError) {
        return <ErrorFallback />
    }

    return (
        <>
            <main>
                <div className="catalog-grid">
                    {data.data.map((item) => <Item key={item.id} item={item}/>)}
                </div>
            </main>
            <CartButton setPage={setPage}/>
        </>);

}