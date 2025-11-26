Tailwind Setup (Vite + React)

1. Install Tailwind. Requires Node 18 or higher
    Run this in your project root:

npm install -D @tailwindcss/postcss


2. Create postcss.config.js under the root folder. Add this:

export default {
  plugins: {
    "@tailwindcss/postcss": {},
    autoprefixer: {},
  },
};

3. In src/index.css (or App.css), add:

    @import "tailwindcss";

    (If you already have other styles, leave them below that line.)

3. Restart Vite

    npm run dev
