import type { AuthRequest } from "../types/AuthRequest";
import type { User } from "../types/User";
import { fetchWithAuth } from "./client";

export const fetchMe = async (): Promise<User> => {
    return fetchWithAuth("/auth/me");
};

export const logoutUser = async () => {
    return fetchWithAuth("/auth/logout", {
        method: "POST",
    });
};

export const loginUser = async (authRequest: AuthRequest) => {
    return fetchWithAuth("/auth/login", {
        method: "POST",
        body: JSON.stringify({
            username: authRequest.username,
            password: authRequest.password,
        })
    });
};