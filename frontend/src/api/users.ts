import type { CreateUser, User, UserFilters } from "../types/User";
import { fetchWithAuth } from "./client";

export const fetchUsersFiltered = async (filters: UserFilters = {}) => {
    const params = new URLSearchParams();
    if (filters.restaurantId) params.append("restaurantId", filters.restaurantId.toString());
    if (filters.role) params.append("role", filters.role);
    if (filters.status) params.append("status", filters.status);
    if (filters.cityName) params.append("cityName", filters.cityName);
    if (filters.username) params.append("username", filters.username);
    if (filters.email) params.append("email", filters.email);
    return fetchWithAuth(`/users?${params.toString()}`);
};

export const createUser = async (user: CreateUser) => {
    return fetchWithAuth("/users", {
        method: "POST",
        body: JSON.stringify(user),
    });
};

export const updateUser = async (userData: Partial<User>): Promise<User> => {
    return fetchWithAuth(`/users/${userData.id}`, {
        method: "PUT",
        body: JSON.stringify(userData),
    });
};

export const updateUserAdmin = async (userData: Partial<User>): Promise<User> => {
    return fetchWithAuth(`/users/admin/${userData.id}`, {
        method: "PUT",
        body: JSON.stringify(userData),
    });
};


export const changeStatusOfUser = async (user: User) => {
    return fetchWithAuth(`/users/changeStatus/${user.id}`, {
        method: "PUT",
        body: JSON.stringify(user)
    });
}

export const changePassword = async (userId: number, data: CreateUser) => {
    return fetchWithAuth(`/users/changePassword/${userId}`, {
        method: "PUT",
        body: JSON.stringify(data)
    });
}
