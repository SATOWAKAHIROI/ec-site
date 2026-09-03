import { getTasks } from "../api/tasks";
import TaskList from "./TaskList";

type Task = {
  id: number;
  title: string;
  completed: boolean;
};

export default async function TasksPage() {
  return <TaskList />;
}
