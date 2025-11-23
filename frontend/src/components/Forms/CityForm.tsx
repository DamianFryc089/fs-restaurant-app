// CityForm.tsx - formularz reprezentacyjny
import React, { useState } from "react";
import type { City } from "../../types/City";

interface CityFormProps {
    onSubmit: (city: Omit<City, 'id'>) => void;
}

export const CityForm: React.FC<CityFormProps> = ({ onSubmit }) => {
    const [city, setCity] = useState({ name: "", postalCode: "" });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setCity(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(city);
        setCity({ name: "", postalCode: "" });
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <div>
                <label>Name:</label>
                <input name="name" value={city.name} onChange={handleChange} />
            </div>
            <div>
                <label>Postal Code:</label>
                <input name="postalCode" value={city.postalCode} onChange={handleChange} />
            </div>
            <button type="submit">Create City</button>
        </form>
    );
};
