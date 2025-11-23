const BASE_URL = import.meta.env.VITE_API_URL;

export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
	const res = await fetch(`${BASE_URL}${url}`, {
		...options,
		credentials: "include",
		headers: { "Content-Type": "application/json" }
	});
	const data = res ? await res.json() : null;
	if (!res.ok) throw data;
	return data;
};







