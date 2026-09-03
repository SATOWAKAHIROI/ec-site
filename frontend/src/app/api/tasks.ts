import { apiClient } from "./apiClient";

type Task = {
  id: number;
  title: string;
  completed: boolean;
};

export async function updateTask(
  id: number,
  title: string,
  completed: boolean,
) {
  const response = await apiClient(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/tasks/${id}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: title,
        completed: completed,
      }),
    },
  );

  if (!response.ok) {
    throw new Error("タスクの更新に失敗しました");
  }

  return response.json();
}

export async function getTasks(): Promise<Task[]> {
  const response = await apiClient(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/tasks`,
  );

  if (!response.ok) {
    throw new Error("タスクの取得に失敗しました");
  }

  return response.json();
}

export async function createTask(title: string, completed: boolean) {
  const response = await apiClient(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/tasks`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title,
        completed,
      }),
    },
  );

  if (!response.ok) {
    throw new Error("タスクの作成に失敗しました");
  }

  return response.json();
}
