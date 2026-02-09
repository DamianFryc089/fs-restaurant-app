import React, { useState } from "react";
import type { CreateUser } from "../../types/User";

interface RegisterFormProps {
    onSubmit: (data: CreateUser) => void;
    initialData: CreateUser | null;
}

export const RegisterForm: React.FC<RegisterFormProps> = ({ onSubmit, initialData }) => {
    const [form, setForm] = useState<CreateUser>({
        username: initialData?.username || "",
        email: initialData?.email || "",
        password: initialData?.password || "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(form);
        form.password = "";
    };

    return (
        <form onSubmit={handleSubmit}>
            {!initialData &&
                <>
                    <div>
                        <label htmlFor="registerUsername">Username:</label>
                        <input id="registerUsername" type="text" name="username" value={form.username} onChange={handleChange} required />
                    </div>
                    <div>
                        <label htmlFor="registerEmail">Email:</label>
                        <input id="registerEmail" type="email" name="email" value={form.email} onChange={handleChange} required />
                    </div>
                </>
            }
            <div>
                <label htmlFor="registerPassword">Password:</label>
                <input id="registerPassword" type="password" name="password" value={form.password} onChange={handleChange} required minLength={8} />
            </div>
            { !initialData && <button type="submit">Register</button>}
            { initialData && <button type="submit">Change Password</button>}
        </form>
    );
};
