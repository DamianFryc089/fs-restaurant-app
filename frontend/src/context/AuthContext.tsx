import React, { createContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import type { User, UserRole } from "../types/User";
import { fetchMe, logoutUser } from "../api/auth";

interface AuthContextType {
	id: number | null;
	username: string | null;
	role: UserRole | null;
	email: string | null;
	status: string | null;
	cityName: string | null;
	restaurantId: number | null;
	logout: () => Promise<void>;
	updateMe: () => void;
}

export const AuthContext = createContext<AuthContextType>({
	id: null,
	username: null,
	role: null,
	email: null,
	status: null,
	cityName: null,
	restaurantId: null,
	logout: async () => { },
	updateMe: async () => { },
});

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
	const [id, setId] = useState<number | null>(null);
	const [username, setUsername] = useState<string | null>(null);
	const [role, setRole] = useState<UserRole | null>(null);
	const [email, setEmail] = useState<string | null>(null);
	const [status, setStatus] = useState<string | null>(null);
	const [cityName, setCityName] = useState<string | null>(null);
	const [restaurantId, setRestaurantId] = useState<number | null>(null);

	const updateMe = async () => {
		try {
			const meData: User = await fetchMe();
			setId(meData.id);
			setUsername(meData.username);
			setRole(meData.role);
			setEmail(meData.email);
			setStatus(meData.status);
			setCityName(meData.cityName);
			setRestaurantId(meData.restaurantId);
		} catch {
			setId(null);
			setUsername(null);
			setRole(null);
			setEmail(null);
			setStatus(null);
			setCityName(null);
			setRestaurantId(null);
		}
	};

	useEffect(() => {
		updateMe();
	}, []);

	const logout = async () => {
		try {
			await logoutUser();
		} catch {} 
		finally {
			setId(null);
			setUsername(null);
			setRole(null);
			setEmail(null);
			setStatus(null);
			setCityName(null);
			setRestaurantId(null);
		}
	};

	return (
		<AuthContext.Provider value={{ id, username, role, email, status, cityName, restaurantId, logout, updateMe}}>
			{children}
		</AuthContext.Provider>
	);
};