"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import z from "zod";
import { login } from "../api/auth";
import { useState } from "react";
import { useRouter } from "next/navigation";

const loginSchema = z.object({
  email: z
    .email("メールアドレスの形式で入力してください")
    .trim()
    .min(1, "メールアドレスの入力は必須です。"),

  password: z.string().trim().min(1, "パスワードは必須です。"),
});

type FormData = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(loginSchema),
  });

  const [errorMessage, setErrorMessage] = useState<string>("");
  const router = useRouter();

  const onSubmit = async (data: FormData) => {
    setErrorMessage("");
    try {
      await login(data.email, data.password);
      reset();
      router.push("/tasks");
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      }
    }
  };

  return (
    <div>
      <h1>ログインページ</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <input {...register("email")} />
        <input {...register("password")} />
        <button type="submit">ログイン</button>
        {errors.email?.message && <p>{errors.email.message}</p>}
        {errors.password?.message && <p>{errors.password.message}</p>}
        {errorMessage && <p>{errorMessage}</p>}
      </form>
    </div>
  );
}
