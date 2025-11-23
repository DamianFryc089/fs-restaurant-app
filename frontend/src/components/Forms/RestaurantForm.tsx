import React, { useState } from "react";
import type { Restaurant } from "../../types/Restaurant";

interface RestaurantFormProps {
    onSubmit: (data: Omit<Restaurant, "id">) => void;
}

export const RestaurantForm: React.FC<RestaurantFormProps> = ({ onSubmit }) => {
    const [form, setForm] = useState<Omit<Restaurant, "id">>({
        name: "",
        cityName: "",
        street: "",
        phone: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(form);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Name:</label>
                <input name="name" value={form.name} onChange={handleChange} />
            </div>
            <div>
                <label>City:</label>
                <input name="cityName" value={form.cityName} onChange={handleChange} />
            </div>
            <div>
                <label>Street:</label>
                <input name="street" value={form.street} onChange={handleChange} />
            </div>
            <div>
                <label>Phone:</label>
                <input name="phone" value={form.phone} onChange={handleChange} />
            </div>
            <button type="submit">Create Restaurant</button>
        </form>
    );
};
