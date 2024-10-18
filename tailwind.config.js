/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Poppins", "sans-serif"],
      },
      colors: {
        primary: {
          light: "#0A2C73",
          DEFAULT: "#0A2C73",
          dark: "#121640",
        },
        secondary: {
          light: "#ABD9FB",
          DEFAULT: "#ABD9FB",
          dark: "#258EDE",
        },
        accent: {
          DEFAULT: "#F79D8E",
          dark: "#C95E4D",
        },
      },
    },
  },
  plugins: [],
};
