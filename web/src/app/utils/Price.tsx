export default function Price({price, oldPrice}: {price: number, oldPrice: number}) {
    if (oldPrice == 0) {
        return price + " "
    }
}