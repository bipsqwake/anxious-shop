export type Order = {
    items: OrderElement[],
    deliveryType: string
}

export type OrderElement = {
    id: string,
    count: number
}