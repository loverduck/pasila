import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import crossOriginIsolation from "vite-plugin-cross-origin-isolation";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), crossOriginIsolation()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  optimizeDeps: {
    exclude: ["@ffmpeg/ffmpeg", "@ffmpeg/util"],
  },
});
