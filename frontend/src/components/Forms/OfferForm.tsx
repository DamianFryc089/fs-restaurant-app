import React, { useState } from "react";
import type { Offer } from "../../types/Offer";

interface OfferFormProps {
    restaurantId: number;
    onSubmit: (data: Partial<Offer>) => void;
}

export const OfferForm: React.FC<OfferFormProps> = ({ restaurantId, onSubmit }) => {
    const [form, setForm] = useState<Partial<Offer>>({
        title: "",
        description: "",
        price: 1,
        availableQuantity: 1
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: type === "number" ? Number(value) : value
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit({ ...form, restaurantId });
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Title:</label>
                <input name="title" value={form.title} onChange={handleChange} />
            </div>
            <div>
                <label>Description:</label>
                <input name="description" value={form.description} onChange={handleChange} />
            </div>
            <div>
                <label>Price:</label>
                <input name="price" type="number" value={form.price} onChange={handleChange} />
            </div>
            <div>
                <label>Quantity:</label>
                <input name="availableQuantity" type="number" value={form.availableQuantity} onChange={handleChange} />
            </div>
            <button type="submit">Create Offer</button>
        </form>
    );
};
