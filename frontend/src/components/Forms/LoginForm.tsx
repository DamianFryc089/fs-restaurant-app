import React, { useState } from "react";
import type { AuthRequest } from "../../types/AuthRequest";

interface LoginFormProps {
    onSubmit: (data: AuthRequest) => void;
}

export const LoginForm: React.FC<LoginFormProps> = ({ onSubmit }) => {
    const [form, setForm] = useState<AuthRequest>({ username: "", password: "" });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(form);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="loginUsername">Username:</label>
                <input
                    id="loginUsername"
                    type="text"
                    name="username"
                    value={form.username}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="loginPassword">Password:</label>
                <input
                    id="loginPassword"
                    type="password"
                    name="password"
                    value={form.password}
                    onChange={handleChange}
                />
            </div>
            <button type="submit">Login</button>
        </form>
    );
};
