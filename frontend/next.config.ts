import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // WSL上のマウント（/mnt/c）ではファイル変更の通知がコンテナに届かないため、
  // 一定間隔でファイルを監視（ポーリング）する
  watchOptions: {
    pollIntervalMs: 300,
  },
};

export default nextConfig;
