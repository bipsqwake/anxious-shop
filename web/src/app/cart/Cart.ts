
type CartItem = {
    id: string;
    quantity: number;
};

const CART_STORAGE_KEY = 'my_cart';

export class Cart {
    private items: Map<string, CartItem> = new Map();

    constructor() {
        this.loadFromLocalStorage();
    }

    private saveToLocalStorage() {
        // Сохраняем корзину как строку JSON
        console.log("store")
        const itemsArray = Array.from(this.items.values());
        console.log("store " + itemsArray)
        localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(itemsArray));
    }

    private loadFromLocalStorage() {
        const data = localStorage.getItem(CART_STORAGE_KEY);
        console.log("load " + data)
        if (data) {
            try {
                const itemsArray: CartItem[] = JSON.parse(data);
                this.items = new Map(itemsArray.map(item => [item.id, item]));
            } catch (e) {
                // Если что-то пошло не так, очищаем localStorage
                localStorage.removeItem(CART_STORAGE_KEY);
                this.items = new Map();
            }
        }
    }

    // Добавить 1 товар в корзину по id и цене
    addItem(id: string): void {
        if (this.items.has(id)) {
            const item = this.items.get(id)!;
            item.quantity += 1;
        } else {
            this.items.set(id, { id, quantity: 1 });
        }
        this.saveToLocalStorage();
    }

    // Удалить 1 товар из корзины по id
    removeItem(id: string): void {
        if (this.items.has(id)) {
            const item = this.items.get(id)!;
            if (item.quantity > 1) {
                item.quantity -= 1;
            } else {
                this.items.delete(id);
            }
            this.saveToLocalStorage();
        }
    }

    // Получить количество товара по id
    getItemQuantity(id: string): number {
        return this.items.get(id)?.quantity ?? 0;
    }

    // Получить суммарное количество всех товаров в корзине
    getTotalQuantity(): number {
        let total = 0;
        for (const item of this.items.values()) {
            total += item.quantity;
        }
        return total;
    }

    // Очистить корзину
    clearCart(): void {
        this.items.clear();
        this.saveToLocalStorage();
    }
}