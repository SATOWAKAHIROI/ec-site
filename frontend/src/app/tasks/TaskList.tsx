"use client";

import { useEffect, useState } from "react";
import { createTask, getTasks, updateTask } from "../api/tasks";
import { logout } from "../api/auth";
import { useForm } from "react-hook-form";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";

const TaskSchema = z.object({
  title: z
    .string()
    .trim()
    .min(1, "タイトルは必須です。")
    .max(100, "タイトルは100文字以内で入力してください"),
});

type Task = {
  id: number;
  title: string;
  completed: boolean;
};

type FormData = z.infer<typeof TaskSchema>;

export default function TaskList() {
  const [taskList, setTaskList] = useState<Task[]>([]);
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);
  const [updating, setUpdating] = useState<boolean>(false);
  const [creating, setCreating] = useState<boolean>(false);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(TaskSchema),
  });
  const router = useRouter();

  useEffect(() => {
    const getTaskList = async () => {
      setLoading(true);
      try {
        const tasks = await getTasks();
        setTaskList(tasks);
      } catch (error) {
        if (error instanceof Error && error.message === "AUTH_REQUIRED") {
          router.push("/login");
        } else if (error instanceof Error) {
          setErrorMessage(error.message);
        }
      } finally {
        setLoading(false);
      }
    };
    getTaskList();
  }, []);

  const changeTask = async (id: number) => {
    setUpdating(true);
    setErrorMessage("");
    try {
      const task = taskList.find((task) => task.id === id);

      if (!task) {
        return;
      }

      await updateTask(id, task.title, !task.completed);

      setTaskList((taskList) =>
        taskList.map((task) =>
          task.id === id ? { ...task, completed: !task.completed } : task,
        ),
      );
    } catch (error) {
      if (error instanceof Error && error.message === "AUTH_REQUIRED") {
        router.push("/login");
      } else if (error instanceof Error) {
        setErrorMessage(error.message);
      }
    } finally {
      setUpdating(false);
    }
  };

  const onSubmit = async (data: FormData) => {
    setErrorMessage("");
    setCreating(true);
    try {
      const newTask = await createTask(data.title, false);

      setTaskList((taskList) => [...taskList, newTask]);

      reset();
    } catch (error) {
      if (error instanceof Error && error.message === "AUTH_REQUIRED") {
        router.push("/login");
      } else if (error instanceof Error) {
        setErrorMessage(error.message);
      }
    } finally {
      setCreating(false);
    }
  };

  const onLogout = async () => {
    await logout();
    router.push("/login");
  };

  return (
    <main>
      <h1>タスク一覧</h1>
      {loading ? (
        <p>読み込み中</p>
      ) : errorMessage ? (
        <p>{errorMessage}</p>
      ) : (
        <ul>
          {taskList.map((task) => (
            <li key={task.id}>
              {task.title}
              <input
                type="checkbox"
                onChange={() => changeTask(task.id)}
                checked={task.completed}
                disabled={updating}
              />
            </li>
          ))}
        </ul>
      )}

      <h1>タスク追加</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <input {...register("title")} disabled={creating} />
        <button type="submit" disabled={creating}>
          作成
        </button>
        {errors.title && <p>{errors.title.message}</p>}
      </form>
      <button type="button" onClick={onLogout}>
        ログアウト
      </button>
    </main>
  );
}
