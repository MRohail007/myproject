import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { resolve } from 'path'

import wasm from 'vite-plugin-wasm'
import topLevelAwait from 'vite-plugin-top-level-await'

export default defineConfig({
  plugins: [react(), wasm(), topLevelAwait()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'), // This sets up the alias
    },
    define: {
      global: 'window', // Maps `global` to `window` in the browser
    },
    proxy: {
      '/api': 'http://localhost:8080'
    }
  },
})
