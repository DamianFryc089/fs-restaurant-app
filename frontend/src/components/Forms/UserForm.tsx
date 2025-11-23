import React, { useState, useEffect } from "react";
import type { User } from "../../types/User";

interface UserFormProps {
  user?: User | null;
  onSubmit: (userData: Partial<User>) => void;
}

export const UserForm: React.FC<UserFormProps> = ({ user, onSubmit }) => {
  const [form, setForm] = useState<Partial<User>>({
    id: 0,
    username: "",
    email: "",
    cityName: "",
    restaurantId: 0,
    role: "CLIENT",
    status: "ACTIVE",
  });

  useEffect(() => {
    setForm({
      id: user?.id ?? 0,
      username: user?.username ?? "",
      email: user?.email ?? "",
      cityName: user?.cityName ?? "",
      restaurantId: user?.restaurantId ?? 0,
      role: user?.role ?? "CLIENT",
      status: user?.status ?? "ACTIVE",
    });
  }, [user]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: name === "id" || name === "restaurantId" ? Number(value) : value
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.id) return;
    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Id:</label>
        <input name="id" value={form.id} onChange={handleChange} />
      </div>
      <div>
        <label>Username:</label>
        <input name="username" value={form.username} onChange={handleChange} />
      </div>
      <div>
        <label>Email:</label>
        <input name="email" value={form.email} onChange={handleChange} />
      </div>
      <div>
        <label>City name:</label>
        <input name="cityName" value={form.cityName} onChange={handleChange} />
      </div>
      <div>
        <label>Restaurant id:</label>
        <input name="restaurantId" value={form.restaurantId || 0} onChange={handleChange} />
      </div>
      <div>
        <label>Role:</label>
        <select name="role" value={form.role} onChange={handleChange}>
          <option value="CLIENT">CLIENT</option>
          <option value="EMPLOYEE">EMPLOYEE</option>
          <option value="ADMIN">ADMIN</option>
        </select>
      </div>
      <button type="submit">Update User</button>
    </form>
  );
};
