import React, { useState } from "react";
import type { Review } from "../../types/Review";

interface ReviewFormProps {
    orderId: number;
    onSubmit: (review: Partial<Review>) => void;
}

export const ReviewForm: React.FC<ReviewFormProps> = ({ orderId, onSubmit }) => {
    const [form, setForm] = useState<Partial<Review>>({ anonymous: false, comment: "", rating: 5 });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: type === "checkbox" ? checked : type === "number" ? Number(value) : value
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit({ ...form, orderId });
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Comment:</label>
                <input name="comment" value={form.comment} onChange={handleChange} />
            </div>
            <div>
                <label>Rating:</label>
                <input name="rating" type="number" value={form.rating} onChange={handleChange} min={1} max={5} />
            </div>
            <div>
                <label>Anonymous:</label>
                <input name="anonymous" type="checkbox" checked={form.anonymous} onChange={handleChange} />
            </div>
            <button type="submit">Create Review</button>
        </form>
    );
};
