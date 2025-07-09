import type { ReactNode } from 'react';
import { createContext, useContext, useEffect, useReducer } from 'react';

export type CartItem = {
    id: string;
    quantity: number;
};

type CartState = CartItem[];

type Action =
    | { type: 'ADD_ITEM'; id: string; }
    | { type: 'REMOVE_ITEM'; id: string }
    | { type: 'CLEAR_CART' }
    | { type: 'SET_CART'; cart: CartItem[] }
    | { type: 'UPDATE_QUANTITY'; id: string, quantity: number };

const CART_STORAGE_KEY = 'my_cart';

function cartReducer(state: CartState, action: Action): CartState {
    switch (action.type) {
        case 'ADD_ITEM': {
            const existing = state.find(item => item.id === action.id);
            if (existing) {
                return state.map(item =>
                    item.id === action.id
                        ? { ...item, quantity: item.quantity + 1 }
                        : item
                );
            }
            return [...state, { id: action.id, quantity: 1 }];
        }
        case 'REMOVE_ITEM': {
            return state
                .map(item =>
                    item.id === action.id
                        ? { ...item, quantity: item.quantity - 1 }
                        : item
                )
                .filter(item => item.quantity > 0);
        }
        case 'UPDATE_QUANTITY':
            const existing = state.find(item => item.id === action.id);
            if (existing) {
                return state.map(item =>
                    item.id === action.id
                        ? { ...item, quantity: action.quantity }
                        : item
                );
            }
            return [...state, { id: action.id, quantity: 1 }];
        case 'CLEAR_CART':
            return [];
        case 'SET_CART':
            return action.cart;
        default:
            return state;
    }
}

type CartContextType = {
    items: CartItem[];
    addItem: (id: string) => void;
    updateItemQuantity: (id: string, quantity: number) => void;
    removeItem: (id: string) => void;
    clearCart: () => void;
    getItemQuantity: (id: string) => number;
    getTotalQuantity: () => number;
    getCart: () => CartItem[];
};

function loadCartFromStorage(): CartItem[] {
    const raw = localStorage.getItem(CART_STORAGE_KEY);
    if (!raw) return [];
    try {
        const parsed = JSON.parse(raw);
        if (Array.isArray(parsed)) {
            return parsed;
        }
        return [];
    } catch {
        return [];
    }
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
    const ctx = useContext(CartContext);
    if (!ctx) throw new Error('useCart must be used within a CartProvider');
    return ctx;
};

export const CartProvider = ({ children }: { children: ReactNode }) => {
    const [items, dispatch] = useReducer(cartReducer, [], loadCartFromStorage);

    useEffect(() => {
        localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(items));
    }, [items]);

    const addItem = (id: string) => {
        dispatch({ type: 'ADD_ITEM', id });
    }

    const updateItemQuantity = (id: string, quantity: number) => {
        dispatch({ type: 'UPDATE_QUANTITY', id, quantity })
    }

    const removeItem = (id: string) =>
        dispatch({ type: 'REMOVE_ITEM', id });

    const clearCart = () => dispatch({ type: 'CLEAR_CART' });

    const getItemQuantity = (id: string) =>
        items.find(item => item.id === id)?.quantity ?? 0;

    const getTotalQuantity = () =>
        items.reduce((sum, item) => sum + item.quantity, 0);

    const getCart = () => items;



    return (
        <CartContext.Provider
            value={{
                items,
                addItem,
                updateItemQuantity,
                removeItem,
                clearCart,
                getItemQuantity,
                getTotalQuantity,
                getCart,
            }}
        >
            {children}
        </CartContext.Provider>
    );
};
