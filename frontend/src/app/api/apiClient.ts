export async function apiClient(url: string, option?: RequestInit) {
  const response = await fetch(url, {
    ...option,
    credentials: "include",
    headers: {
      ...option?.headers,
    },
  });

  if (response.status === 401) {
    localStorage.removeItem("token");
    throw new Error("AUTH_REQUIRED");
  }

  return response;
}
