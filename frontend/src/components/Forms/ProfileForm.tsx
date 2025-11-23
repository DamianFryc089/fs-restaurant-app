import React, { useState, useEffect } from "react";
import type { User } from "../../types/User";

interface ProfileFormProps {
    initialData: Partial<User>;
    onSubmit: (data: Partial<User>) => void;
}

export const ProfileForm: React.FC<ProfileFormProps> = ({ initialData, onSubmit }) => {
    const [form, setForm] = useState<Partial<User>>(initialData);

    useEffect(() => {
        setForm(initialData);
    }, [initialData]);

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
                <label>Username:</label>
                <input name="username" value={form.username || ""} onChange={handleChange} />
            </div>
            <div>
                <label>Email:</label>
                <input name="email" value={form.email || ""} onChange={handleChange} />
            </div>
            <button type="submit">Update Profile</button>
        </form>
    );
};
