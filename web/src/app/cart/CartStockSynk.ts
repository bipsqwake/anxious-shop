import { useEffect } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useCart } from './CartProvider';
import axios from 'axios';
import type { ItemSearchResponse } from '../../types/ItemSearchResponse';
import toast from 'react-hot-toast';

export function useSyncCartWithStock() {
    const { items, updateItemQuantity, removeItem } = useCart();
    const ids = items.map(item => item.id);
    const queryClient = useQueryClient();

    const { data: actualProducts, isSuccess } = useQuery({
        queryKey: ['cart-stock', ids],
        queryFn: () => {
            return axios.get<ItemSearchResponse>(import.meta.env.VITE_API_URL + "/item/find?ids=" + ids.join(','));
        },
        enabled: ids.length > 0,
        refetchOnWindowFocus: true,
    });

    useEffect(() => {
        if (!isSuccess || !actualProducts) return;
        let changed = false;
        Object.keys(actualProducts.data).forEach((key) => {
            const cartItem = items.find(i => i.id === actualProducts.data[key].id);
            if (!cartItem) return;
            if (actualProducts.data[key].itemsLeft === 0) {
                removeItem(actualProducts.data[key].id);
                changed = true;
            } else if (cartItem.quantity > actualProducts.data[key].itemsLeft) {
                updateItemQuantity(actualProducts.data[key].id, actualProducts.data[key].itemsLeft);
                changed = true;
            }
        });

        if (changed) {
            toast('упс! кто-то увёл товар из корзины прямо у вас из под носа. очень жаль!');
        }
        queryClient.invalidateQueries({ queryKey: ['catalog'] });
    }, [isSuccess, actualProducts, items, updateItemQuantity, removeItem]);
}