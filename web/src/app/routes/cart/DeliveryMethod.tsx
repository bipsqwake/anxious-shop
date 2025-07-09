export default function DeliveryMethod({ value, text, selected, onChange }: { value: string, text: string,  selected: string, onChange: () => void }) {
    return (
        <label className="cart-radio">
            <input type="radio" name="delivery" value={value} checked={selected == value} onChange={onChange}/>
            {text}
        </label>
    );
}